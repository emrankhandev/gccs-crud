package com.gccws.sysadmin.control.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.UserRoleDetails;
import com.gccws.common.entity.UserRoleMaster;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.UserRoleDetailsRepository;
import com.gccws.common.repository.UserRoleMasterRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.UserRoleDetailsDto;
import com.gccws.sysadmin.control.dto.UserRoleMasterDto;
import com.gccws.sysadmin.control.model.UserRoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;

/**
 * @Author    Md Chabed Alam
 * @Since     August 28, 2022
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService{
	
	private ModelMapper modelMapper;
	private final UserRoleMasterRepository masterRepo;
	private final UserRoleDetailsRepository detailsRepo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = UserRoleMaster.class.getSimpleName();
	
	@Transactional
	@Override
	public UserRoleModel save(UserRoleModel obj, int userId) {
		UserRoleMasterDto master = obj.getMaster();
		List<UserRoleDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		UserRoleMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, true));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			List<UserRoleDetails> listForSave = new ArrayList<>();
			for(UserRoleDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		// for audit log start
		List<UserRoleMaster> masterList = new ArrayList<>();
		masterList.add(masterEntity);
		UserRoleModel saveAuditModel = convertMasterToDetails(masterList).get(0);
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, saveAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	
	@Transactional
	@Override
	public UserRoleModel update(UserRoleModel obj, int userId) {
		// for audit log start
		UserRoleMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<UserRoleMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		UserRoleModel oldAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		UserRoleMasterDto master = obj.getMaster();
		List<UserRoleDetailsDto> detailsList = obj.getDetailsList();
		
		/*save master*/
		UserRoleMaster masterEntity = masterRepo.save(generateMasterEntity(master, userId, false));

		/*save details*/
		if(!ObjectUtils.isEmpty(master)){
			
			/* delete previous data if needed */
			deleteDetilsData(masterEntity, detailsList);
			
			/* now save data*/
			List<UserRoleDetails> listForSave = new ArrayList<>();
			for(UserRoleDetailsDto detail: detailsList){
				listForSave.add(generateDetailsEntity(detail, masterEntity, userId));
            }
			detailsRepo.saveAll(listForSave);
		}

		/*// for audit log start
		List<UserRoleMaster> newMasterList = new ArrayList<>();
		newMasterList.add(masterEntity);
		UserRoleModel newAuditModel = convertMasterToDetails(newMasterList).get(0);*/
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, masterEntity, oldAuditModel);
		// for audit log end
		
		/* create model */
		obj.setMaster(generateMasterDto(masterEntity));
		return obj;
	}
	
	@Transactional
	@Override
	public Boolean delete(UserRoleModel obj,int userId) {
		// for audit log start
		UserRoleMaster oldEntity = masterRepo.findById(obj.getMaster().getId()).get();
		List<UserRoleMaster> oldMasterList = new ArrayList<>();
		oldMasterList.add(oldEntity);
		UserRoleModel deleteAuditModel = convertMasterToDetails(oldMasterList).get(0);
		// for audit log end

		if(!ObjectUtils.isEmpty(obj.getMaster().getId())) {
    		UserRoleMaster entity = new UserRoleMaster();
    		entity.setId(obj.getMaster().getId());
    		masterRepo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditModel);
    		return true;
    	}else {
    		return false;
    	}
	}
	
	@Override
	public UserRoleModel getById(int id,int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<UserRoleMaster> dataList = masterRepo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		List<UserRoleMaster> masterList = new ArrayList<>();
    		masterList.add(dataList.get());
			return convertMasterToDetails(masterList).get(0);
    	}
	}

	@Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return masterRepo.findDropdownModel();
    }

	@Override
	public Page<UserRoleModel> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<UserRoleMaster> pageresult = masterRepo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<UserRoleModel> objlist = convertMasterToDetails(pageresult.getContent());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}


//	@Override
//	public List<UserRoleMaster> getRoleListByUser(Integer appUserId) {
//		commonUtils.auditLoggingForGet(appUserId, ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
//		return masterRepo.findUserRoleQuery(appUserId);
//	}

	@Override
	public List<UserRoleMasterDto> getRoleListByUser(Integer appUserId, int userId) {
		commonUtils.auditLoggingForGet(appUserId, ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertMasterToDtoList(masterRepo.findUserRoleQuery(appUserId).stream());
	}
	//..................... Generate Model....................//
	
	/* master part */
	private UserRoleMaster generateMasterEntity(UserRoleMasterDto dto, int userId, Boolean isSaved) {
    	UserRoleMaster entity = new UserRoleMaster();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		UserRoleMaster dbEntity = masterRepo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
    	return entity;
    }
	
	private UserRoleMasterDto generateMasterDto(UserRoleMaster entity) {
		UserRoleMasterDto dto = modelMapper.map(entity, UserRoleMasterDto.class);
		return dto;
    }
	
	/* details part */
	private UserRoleDetails generateDetailsEntity(UserRoleDetailsDto dto, UserRoleMaster masterEntity, int userId) {
		UserRoleDetails entity = new UserRoleDetails();
    	BeanUtils.copyProperties(dto, entity);
    	entity.setMaster(masterEntity);
    	entity.setEntryUser(userId);
    	commonUtils.setEntryUserInfo(entity);
		if(!ObjectUtils.isEmpty(dto.getMenuItemId())) {
			MenuItem menuItem = new MenuItem();
			menuItem.setId(dto.getMenuItemId());
			entity.setMenuItem(menuItem);
		}else {
			entity.setMenuItem(null);
		}
    	return entity;
    }
	
	private List<UserRoleDetailsDto> convertDetailsEntityListToDtoList(Stream<UserRoleDetails> entityList) {
    	return entityList.map(entity -> {
    		return generateDetailsDto(entity);
		}).collect(Collectors.toList());
    }
	
	private UserRoleDetailsDto generateDetailsDto(UserRoleDetails entity) {
		UserRoleDetailsDto dto = modelMapper.map(entity, UserRoleDetailsDto.class);
		if(!ObjectUtils.isEmpty(entity.getMenuItem())) {
			dto.setMenuItemId(entity.getMenuItem().getId());
			dto.setMenuItemName(entity.getMenuItem().getName() + " (" + entity.getMenuItem().getMenuTypeName() + ")");
		}
		return dto;
    }
	
	//..................... Helper ....................//
	
	private void deleteDetilsData(UserRoleMaster savedEntity, List<UserRoleDetailsDto> details) {
        List<UserRoleDetails> listForCheckDelete = detailsRepo.findByMasterId(savedEntity.getId()); // 1,2,3
        List<UserRoleDetails> listForDelete = new ArrayList<>();
        for(UserRoleDetails obj: listForCheckDelete) {
        	boolean needToDelete = true;
            for(UserRoleDetailsDto detailsEntity: details) { // 1,3
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
	 
	 private List<UserRoleModel> convertMasterToDetails(List<UserRoleMaster> list) {
	        List<UserRoleModel> returnList = new ArrayList<>();
	        for (UserRoleMaster master: list) {

	        	UserRoleModel tmp = new UserRoleModel();
	            /*set master*/
	        	tmp.setMaster(generateMasterDto(master));
	            /*set details*/
	            tmp.setDetailsList(convertDetailsEntityListToDtoList(detailsRepo.findByMasterId(master.getId()).stream()));
				returnList.add(tmp);
	        }
	        return  returnList;
	    }

	private List<UserRoleMasterDto> convertMasterToDtoList(Stream<UserRoleMaster> list) {
		return list.map(entity -> {
			return generateMasterDto(entity);
		}).collect(Collectors.toList());
	}


}
