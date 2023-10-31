package com.gccws.sysadmin.control.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;

import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.UserRoleAssignDetails;
import com.gccws.common.entity.UserRoleAssignMaster;
import com.gccws.common.entity.UserRoleMaster;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.repository.UserRoleAssignDetailsRepository;
import com.gccws.common.repository.UserRoleAssignMasterRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.UserRoleAssignDetailsDto;
import com.gccws.sysadmin.control.dto.UserRoleAssignMasterDto;
import com.gccws.sysadmin.control.model.UserRoleAssignModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;

/**
 * @Author    Rima
 * @Since     September 1, 2022
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class UserRoleAssignServiceImpl implements UserRoleAssignService{
	
	private ModelMapper modelMapper;
	private final UserRoleAssignMasterRepository masterRepo;
	private final UserRoleAssignDetailsRepository detailsRepo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = UserRoleAssignMaster.class.getSimpleName();
	
	@Transactional
	@Override
	public UserRoleAssignModel save(UserRoleAssignModel obj, int userId) {

		UserRoleAssignMasterDto master = obj.getMaster();
		List<UserRoleAssignDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		UserRoleAssignMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, true));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			List<UserRoleAssignDetails> listForSave = new ArrayList<>();
			for(UserRoleAssignDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<UserRoleAssignMaster> masterList = new ArrayList<>();
		masterList.add(masterEntity);
		UserRoleAssignModel saveAuditModel = convertMasterToDetails(masterList).get(0);
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, saveAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	
	@Transactional
	@Override
	public UserRoleAssignModel update(UserRoleAssignModel obj, int userId) {

		// for audit log start
		UserRoleAssignMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<UserRoleAssignMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		UserRoleAssignModel oldAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		UserRoleAssignMasterDto master = obj.getMaster();
		List<UserRoleAssignDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		UserRoleAssignMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, false));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			
			/* delete previous data if needed */
			deleteDetilsData(masterEntity, detailsList);
			
			/* now save data*/
			List<UserRoleAssignDetails> listForSave = new ArrayList<>();
			for(UserRoleAssignDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<UserRoleAssignMaster> newMasterList = new ArrayList<>();
		newMasterList.add(masterEntity);
		UserRoleAssignModel newAuditModel = convertMasterToDetails(newMasterList).get(0);
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, newAuditModel, oldAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	@Transactional
	@Override
	public Boolean delete(UserRoleAssignModel obj,int userId) {
		// for audit log start
		UserRoleAssignMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<UserRoleAssignMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		UserRoleAssignModel deleteAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		if(!ObjectUtils.isEmpty(obj.getMaster().getId())) {
    		UserRoleAssignMaster entity = new UserRoleAssignMaster();
    		entity.setId(obj.getMaster().getId());
    		masterRepo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditModel);
    		return true;
    	}else {
    		return false;
    	}
	}
	
	@Override
	public UserRoleAssignModel getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<UserRoleAssignMaster> dataList = masterRepo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		List<UserRoleAssignMaster> masterList = new ArrayList<>();
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
	public Page<UserRoleAssignModel> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<UserRoleAssignMaster> pageresult = masterRepo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<UserRoleAssignModel> objlist = convertMasterToDetails(pageresult.getContent());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}
	
	//..................... Generate Model....................//
	
	/* master part */
	private UserRoleAssignMaster generateMasterEntity(UserRoleAssignMasterDto dto, int userId, Boolean isSaved) {
    	UserRoleAssignMaster entity = new UserRoleAssignMaster();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		UserRoleAssignMaster dbEntity = masterRepo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
    	if(!ObjectUtils.isEmpty(dto.getAppUserId())) {
    		AppUser appUser = new AppUser();
    		appUser.setId(dto.getAppUserId());
        	entity.setAppUser(appUser);
    	}
    	return entity;
    }
	
	private UserRoleAssignMasterDto generateMasterDto(UserRoleAssignMaster entity) {
		UserRoleAssignMasterDto dto = modelMapper.map(entity, UserRoleAssignMasterDto.class);
		if(!ObjectUtils.isEmpty(entity.getAppUser())) {
    		dto.setAppUserId(entity.getAppUser().getId());
	    	dto.setAppUserName(entity.getAppUser().getUsername() + " - " +entity.getAppUser().getDisplayName());
    	}
		return dto;
    }
	
	/* details part */
	private UserRoleAssignDetails generateDetailsEntity(UserRoleAssignDetailsDto dto, UserRoleAssignMaster masterEntity, int userId) {
		UserRoleAssignDetails entity = new UserRoleAssignDetails();
    	BeanUtils.copyProperties(dto, entity);
    	entity.setMaster(masterEntity);
    	entity.setEntryUser(userId);
    	commonUtils.setEntryUserInfo(entity);
    	if(!ObjectUtils.isEmpty(dto.getUserRoleId())) {
    		UserRoleMaster obj = new UserRoleMaster();
    		obj.setId(dto.getUserRoleId());
    		entity.setUserRole(obj);
    	}
    	return entity;
    }
	
	private List<UserRoleAssignDetailsDto> convertDetailsEntityListToDtoList(Stream<UserRoleAssignDetails> entityList) {
    	return entityList.map(entity -> {
    		return generateDetailsDto(entity);
		}).collect(Collectors.toList());
    }
	
	private UserRoleAssignDetailsDto generateDetailsDto(UserRoleAssignDetails entity) {
		UserRoleAssignDetailsDto dto = modelMapper.map(entity, UserRoleAssignDetailsDto.class);
    	if(!ObjectUtils.isEmpty(entity.getUserRole())) {
    		dto.setUserRoleId(entity.getUserRole().getId());
	    	dto.setUserRoleName(entity.getUserRole().getName());
    	}
		return dto;
    }
	
	//..................... Helper ....................//
	
	private void deleteDetilsData(UserRoleAssignMaster savedEntity, List<UserRoleAssignDetailsDto> details) {
        List<UserRoleAssignDetails> listForCheckDelete = detailsRepo.findByMasterId(savedEntity.getId()); // 1,2,3
        List<UserRoleAssignDetails> listForDelete = new ArrayList<>();
        for(UserRoleAssignDetails obj: listForCheckDelete) {
        	boolean needToDelete = true;
            for(UserRoleAssignDetailsDto detailsEntity: details) {
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
	 
	 private List<UserRoleAssignModel> convertMasterToDetails(List<UserRoleAssignMaster> list) {
	        List<UserRoleAssignModel> returnList = new ArrayList<>();
	        for (UserRoleAssignMaster master: list) {
	        	UserRoleAssignModel tmp = new UserRoleAssignModel();
	            /*set master*/
	        	tmp.setMaster(generateMasterDto(master));
	            /*set details*/
	            tmp.setDetailsList(convertDetailsEntityListToDtoList(detailsRepo.findByMasterId(master.getId()).stream()));
	            returnList.add(tmp);
	        }
	        return  returnList;
	    }

}
