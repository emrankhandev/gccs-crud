package com.gccws.sysadmin.approve.service;

import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.ApprovalTeamDetails;
import com.gccws.common.entity.ApprovalTeamMaster;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.ApprovalTeamDetailsRepository;
import com.gccws.common.repository.ApprovalTeamMasterRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.approve.dto.ApprovalTeamDetailsDto;
import com.gccws.sysadmin.approve.dto.ApprovalTeamMasterDto;
import com.gccws.sysadmin.approve.model.ApprovalTeamModel;
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
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class ApprovalTeamServiceImpl implements ApprovalTeamService {
	
	private ModelMapper modelMapper;
	private final ApprovalTeamMasterRepository masterRepo;
	private final ApprovalTeamDetailsRepository detailsRepo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = ApprovalTeamMaster.class.getSimpleName();
	
	@Transactional
	@Override
	public ApprovalTeamModel save(ApprovalTeamModel obj, int userId) {
//		// for audit log start
//		ApprovalTeamMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
//		List<ApprovalTeamMaster> oldMasterList = new ArrayList<>();
//		oldMasterList.add(oldEntity);
//		ApprovalTeamModel oldAuditModel = convertMasterToDetails(oldMasterList).get(0);
//		// for audit log end

		ApprovalTeamMasterDto master = obj.getMaster();
		List<ApprovalTeamDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		ApprovalTeamMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, true));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			List<ApprovalTeamDetails> listForSave = new ArrayList<>();
			for(ApprovalTeamDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<ApprovalTeamMaster> masterList = new ArrayList<>();
		masterList.add(masterEntity);
		ApprovalTeamModel saveAuditModel = convertMasterToDetails(masterList).get(0);
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, saveAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	
	@Transactional
	@Override
	public ApprovalTeamModel update(ApprovalTeamModel obj, int userId) {

		// for audit log start
		ApprovalTeamMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<ApprovalTeamMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		ApprovalTeamModel oldAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		ApprovalTeamMasterDto master = obj.getMaster();
		List<ApprovalTeamDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		ApprovalTeamMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, false));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			
			/* delete previous data if needed */
			deleteDetilsData(masterEntity, detailsList);
			
			/* now save data*/
			List<ApprovalTeamDetails> listForSave = new ArrayList<>();
			for(ApprovalTeamDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<ApprovalTeamMaster> newMasterList = new ArrayList<>();
		newMasterList.add(masterEntity);
		ApprovalTeamModel newAuditModel = convertMasterToDetails(newMasterList).get(0);
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, newAuditModel, oldAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	@Transactional
	@Override
	public Boolean delete(ApprovalTeamModel obj,int userId) {
		// for audit log start
		ApprovalTeamMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<ApprovalTeamMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		ApprovalTeamModel deleteAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		if(!ObjectUtils.isEmpty(obj.getMaster().getId())) {
    		ApprovalTeamMaster entity = new ApprovalTeamMaster();
    		entity.setId(obj.getMaster().getId());
    		masterRepo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditModel);
    		return true;
    	}else {
    		return false;
    	}
	}
	
	@Override
	public ApprovalTeamModel getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<ApprovalTeamMaster> dataList = masterRepo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		List<ApprovalTeamMaster> masterList = new ArrayList<>();
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
	public Page<ApprovalTeamModel> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<ApprovalTeamMaster> pageresult = masterRepo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<ApprovalTeamModel> objlist = convertMasterToDetails(pageresult.getContent());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}

	@Override
	public List<CommonDropdownModel> getNextTeamByDepartmentAndCurrentTeamId(Integer departmentId, Integer transactionTypeId, Integer currentTeamId, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return masterRepo.findNextTeamByDepartmentAndCurrentTeamId(departmentId, transactionTypeId, currentTeamId);
	}

	@Override
	public List<CommonDropdownModel> getPreviousTeamByDepartmentAndCurrentTeamId(Integer departmentId, Integer transactionTypeId, Integer currentTeamId, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return masterRepo.findPreviousTeamByDepartmentAndCurrentTeamId(departmentId, transactionTypeId, currentTeamId);
	}


	@Override
	public List<ApprovalTeamModel> getTeamByDepartmentId(Integer departmentId, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertMasterToDetails(masterRepo.findTeamByDepartmentId(departmentId));
	}

	//..................... Generate Model....................//
	
	/* master part */
	private ApprovalTeamMaster generateMasterEntity(ApprovalTeamMasterDto dto, int userId, Boolean isSaved) {
    	ApprovalTeamMaster entity = new ApprovalTeamMaster();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		ApprovalTeamMaster dbEntity = masterRepo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
    	return entity;
    }
	
	private ApprovalTeamMasterDto generateMasterDto(ApprovalTeamMaster entity) {
		ApprovalTeamMasterDto dto = modelMapper.map(entity, ApprovalTeamMasterDto.class);
		return dto;
    }
	
	/* details part */
	private ApprovalTeamDetails generateDetailsEntity(ApprovalTeamDetailsDto dto, ApprovalTeamMaster masterEntity, int userId) {
		ApprovalTeamDetails entity = new ApprovalTeamDetails();
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
	
	private List<ApprovalTeamDetailsDto> convertDetailsEntityListToDtoList(Stream<ApprovalTeamDetails> entityList) {
    	return entityList.map(entity -> {
    		return generateDetailsDto(entity);
		}).collect(Collectors.toList());
    }
	
	private ApprovalTeamDetailsDto generateDetailsDto(ApprovalTeamDetails entity) {
		ApprovalTeamDetailsDto dto = modelMapper.map(entity, ApprovalTeamDetailsDto.class);

    	if(!ObjectUtils.isEmpty(entity.getAppUser())) {
    		dto.setAppUserId(entity.getAppUser().getId());
	    	dto.setAppUserName(entity.getAppUser().getUsername());
	    	dto.setDisplayName(entity.getAppUser().getDisplayName());
    	}
		return dto;
    }
	
	//..................... Helper ....................//
	
	private void deleteDetilsData(ApprovalTeamMaster savedEntity, List<ApprovalTeamDetailsDto> details) {
        List<ApprovalTeamDetails> listForCheckDelete = detailsRepo.findByMasterId(savedEntity.getId()); // 1,2,3
        List<ApprovalTeamDetails> listForDelete = new ArrayList<>();
        for(ApprovalTeamDetails obj: listForCheckDelete) {
        	boolean needToDelete = true;
            for(ApprovalTeamDetailsDto detailsEntity: details) {
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
	 
	 private List<ApprovalTeamModel> convertMasterToDetails(List<ApprovalTeamMaster> list) {
	        List<ApprovalTeamModel> returnList = new ArrayList<>();
	        for (ApprovalTeamMaster master: list) {
	        	ApprovalTeamModel tmp = new ApprovalTeamModel();
	            /*set master*/
	        	tmp.setMaster(generateMasterDto(master));
	            /*set details*/
	            tmp.setDetailsList(convertDetailsEntityListToDtoList(detailsRepo.findByMasterId(master.getId()).stream()));
	            returnList.add(tmp);
	        }
	        return  returnList;
	    }


}
