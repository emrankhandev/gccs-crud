package com.gccws.sysadmin.control.service;

import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.NotificationDetails;
import com.gccws.common.entity.NotificationMaster;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.NotificationDetailsRepository;
import com.gccws.common.repository.NotificationMasterRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.NotificationDetailsDto;
import com.gccws.sysadmin.control.dto.NotificationMasterDto;
import com.gccws.sysadmin.control.model.NotificationModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     July 31, 2023
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{
	
	private ModelMapper modelMapper;
	private final NotificationMasterRepository masterRepo;
	private final NotificationDetailsRepository detailsRepo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = NotificationModel.class.getSimpleName();
	
	@Transactional
	@Override
	public NotificationModel save(NotificationModel obj, int userId) {

		NotificationMasterDto master = obj.getMaster();
		List<NotificationDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		NotificationMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, true));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			List<NotificationDetails> listForSave = new ArrayList<>();
			for(NotificationDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<NotificationMaster> masterList = new ArrayList<>();
		masterList.add(masterEntity);
		NotificationModel saveAuditModel = convertMasterToDetails(masterList).get(0);
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, saveAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	
	@Transactional
	@Override
	public NotificationModel update(NotificationModel obj, int userId) {

		// for audit log start
		NotificationMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<NotificationMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		NotificationModel oldAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		NotificationMasterDto master = obj.getMaster();
		List<NotificationDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		NotificationMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, false));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			
			/* delete previous data if needed */
			deleteDetilsData(masterEntity, detailsList);
			
			/* now save data*/
			List<NotificationDetails> listForSave = new ArrayList<>();
			for(NotificationDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<NotificationMaster> newMasterList = new ArrayList<>();
		newMasterList.add(masterEntity);
		NotificationModel newAuditModel = convertMasterToDetails(newMasterList).get(0);
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, newAuditModel, oldAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	@Transactional
	@Override
	public Boolean delete(NotificationModel obj,int userId) {
		// for audit log start
		NotificationMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<NotificationMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		NotificationModel deleteAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		if(!ObjectUtils.isEmpty(obj.getMaster().getId())) {
			NotificationMaster entity = new NotificationMaster();
    		entity.setId(obj.getMaster().getId());
    		masterRepo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditModel);
    		return true;
    	}else {
    		return false;
    	}
	}
	
	@Override
	public NotificationModel getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<NotificationMaster> dataList = masterRepo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		List<NotificationMaster> masterList = new ArrayList<>();
    		masterList.add(dataList.get());
			return convertMasterToDetails(masterList).get(0);
    	}
	}
	
	@Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return masterRepo.findDropdownModel();
    }

	@Override
	public Page<NotificationModel> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<NotificationMaster> pageresult = masterRepo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<NotificationModel> objlist = convertMasterToDetails(pageresult.getContent());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}
	
	//..................... Generate Model....................//
	
	/* master part */
	private NotificationMaster generateMasterEntity(NotificationMasterDto dto, int userId, Boolean isSaved) {
		NotificationMaster entity = new NotificationMaster();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
			NotificationMaster dbEntity = masterRepo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}

    	return entity;
    }
	
	private NotificationMasterDto generateMasterDto(NotificationMaster entity) {
		NotificationMasterDto dto = modelMapper.map(entity, NotificationMasterDto.class);
		return dto;
    }
	
	/* details part */
	private NotificationDetails generateDetailsEntity(NotificationDetailsDto dto, NotificationMaster masterEntity, int userId) {
		NotificationDetails entity = new NotificationDetails();
    	BeanUtils.copyProperties(dto, entity);
    	entity.setMaster(masterEntity);
    	entity.setEntryUser(userId);
    	commonUtils.setEntryUserInfo(entity);
		if(!ObjectUtils.isEmpty(dto.getAppUserId())) {
			AppUser appUser = new AppUser();
			appUser.setId(dto.getAppUserId());
			entity.setAppUser(appUser);
		}
    	return entity;
    }
	
	private List<NotificationDetailsDto> convertDetailsEntityListToDtoList(Stream<NotificationDetails> entityList) {
    	return entityList.map(entity -> {
    		return generateDetailsDto(entity);
		}).collect(Collectors.toList());
    }
	
	private NotificationDetailsDto generateDetailsDto(NotificationDetails entity) {
		NotificationDetailsDto dto = modelMapper.map(entity, NotificationDetailsDto.class);
		if(!ObjectUtils.isEmpty(entity.getAppUser())) {
			dto.setAppUserId(entity.getAppUser().getId());
			dto.setAppUserName(entity.getAppUser().getUsername() + " - " +entity.getAppUser().getDisplayName());
		}
		return dto;
    }
	
	//..................... Helper ....................//
	
	private void deleteDetilsData(NotificationMaster savedEntity, List<NotificationDetailsDto> details) {
        List<NotificationDetails> listForCheckDelete = detailsRepo.findByMasterId(savedEntity.getId()); // 1,2,3
        List<NotificationDetails> listForDelete = new ArrayList<>();
        for(NotificationDetails obj: listForCheckDelete) {
        	boolean needToDelete = true;
            for(NotificationDetailsDto detailsEntity: details) {
                if(obj.getId().equals(detailsEntity.getId())) {
                	needToDelete = false;
                    break;
                }
            }
            if (needToDelete) {
            	listForDelete.add(obj);
            }
        }
        detailsRepo.deleteAll(listForDelete);
	 }
	 
	 private List<NotificationModel> convertMasterToDetails(List<NotificationMaster> list) {
	        List<NotificationModel> returnList = new ArrayList<>();
	        for (NotificationMaster master: list) {
				NotificationModel tmp = new NotificationModel();
	            /*set master*/
	        	tmp.setMaster(generateMasterDto(master));
	            /*set details*/
	            tmp.setDetailsList(convertDetailsEntityListToDtoList(detailsRepo.findByMasterId(master.getId()).stream()));
	            returnList.add(tmp);
	        }
	        return  returnList;
	    }

}
