package com.gccws.sysadmin.approve.service;

import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.ApprovalTeamMaster;
import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.SetupDetails;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.approve.repository.ApprovalConfigurationRepository;
import com.gccws.sysadmin.approve.dto.ApprovalConfigurationDto;
import com.gccws.sysadmin.approve.entity.ApprovalConfiguration;
import com.gccws.sysadmin.approve.model.ApproveUserModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * @Author    Rima
 * @Since     February 23, 20223
 * @version   1.0.0
 */
@Service
@AllArgsConstructor
public class ApprovalConfigurationServiceImpl implements ApprovalConfigurationService {
	
	private ModelMapper modelMapper;
	private final ApprovalConfigurationRepository repo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = ApprovalConfiguration.class.getSimpleName();
	
	@Transactional
	@Override
	public ApprovalConfigurationDto save(ApprovalConfigurationDto obj, int userId) {
		ApprovalConfiguration savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}
	
	@Transactional
	@Override
	public ApprovalConfigurationDto update(ApprovalConfigurationDto obj, int userId) {
		ApprovalConfigurationDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		ApprovalConfiguration savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME,generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}
	
	@Transactional
	@Override
	public Boolean delete(ApprovalConfigurationDto obj, int userId) {
		ApprovalConfigurationDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
    		ApprovalConfiguration entity = new ApprovalConfiguration();
    		entity.setId(obj.getId());
    		repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditDto);
    		return true;
    	}else {
    		return false;
    	}
	}



	@Override
	public ApprovalConfigurationDto getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<ApprovalConfiguration> dataList = repo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		return generateDto(dataList.get());
    	}
	}
	
	@Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
    	return repo.findDropdownModel();
    }

	@Override
	public Page<ApprovalConfigurationDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<ApprovalConfiguration> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<ApprovalConfigurationDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}
	
	@Override
	public ApproveUserModel getSubmitUserByDepartmentAndAppUserIdAndTransactionTypeId(Integer departmentId, Integer appUserId, Integer transactionTypeId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findSubmitUserByDepartmentAndAppUserIdAndTransactionTypeId(departmentId, appUserId, transactionTypeId);
	}

	@Override
	public ApproveUserModel getApproveAndForwardUserByDepartmentAndAppUserId(Integer departmentId, Integer appUserId, Integer transactionTypeId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findApproveAndForwardUserByDepartmentAndAppUserId(departmentId, appUserId, transactionTypeId);
	}
	@Override
	public ApprovalConfigurationDto getByToTeamAndDepartmentId(Integer toTeamId, Integer departmentId, Integer transactionTypeId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		//return repo.findToTeamAndModuleId( toTeamId,moduleId);
		return  generateDto(repo.findToTeamAndDepartmentId( toTeamId,departmentId, transactionTypeId));
	}


	//..................... Generate Model....................//
	
	private ApprovalConfiguration generateEntity(ApprovalConfigurationDto dto, int userId, Boolean isSaved) {
    	ApprovalConfiguration entity = new ApprovalConfiguration();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		ApprovalConfiguration dbEntity = repo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
		if(!ObjectUtils.isEmpty(dto.getDepartmentId())) {
			SetupDetails obj = new SetupDetails();
			obj.setId(dto.getDepartmentId());
			entity.setDepartment(obj);
		}
		if(!ObjectUtils.isEmpty(dto.getModuleId())) {
			MenuItem obj = new MenuItem();
			obj.setId(dto.getModuleId());
			entity.setModule(obj);
		}else {
			entity.setModule(null);
		}

		if(!ObjectUtils.isEmpty(dto.getFromTeamId())) {
			ApprovalTeamMaster fromTeamObj = new ApprovalTeamMaster();
			fromTeamObj.setId(dto.getFromTeamId());
			entity.setFromTeam(fromTeamObj);
		}else {
			entity.setFromTeam(null);
		}

		if(!ObjectUtils.isEmpty(dto.getToTeamId())) {
			ApprovalTeamMaster toTeamObj = new ApprovalTeamMaster();
			toTeamObj.setId(dto.getToTeamId());
			entity.setToTeam(toTeamObj);
		}else {
			entity.setToTeam(null);
		}

    	if(!ObjectUtils.isEmpty(dto.getNotifyAppUserId())) {
    		AppUser notifyAppUserObj = new AppUser();
			notifyAppUserObj.setId(dto.getNotifyAppUserId());
        	entity.setNotifyAppUser(notifyAppUserObj);
    	}else {
    		entity.setNotifyAppUser(null);
    	}
    	return entity;
    }
	
	private List<ApprovalConfigurationDto> convertEntityListToDtoList(Stream<ApprovalConfiguration> entityList) {
    	return entityList.map(entity -> {
    		return generateDto(entity);
		}).collect(Collectors.toList());
    }
	
	private ApprovalConfigurationDto generateDto(ApprovalConfiguration entity) {
		ApprovalConfigurationDto dto = modelMapper.map(entity, ApprovalConfigurationDto.class);

		if(!ObjectUtils.isEmpty(entity.getDepartment())) {
			dto.setDepartmentId(entity.getDepartment().getId());
			dto.setDepartmentName(entity.getDepartment().getName());
		}

		if(!ObjectUtils.isEmpty(entity.getModule())){
			dto.setModuleId(entity.getModule().getId());
			dto.setModuleName(entity.getModule().getName());
		}

		if(!ObjectUtils.isEmpty(entity.getFromTeam())) {
    		dto.setFromTeamId(entity.getFromTeam().getId());
	    	dto.setFromTeamName(entity.getFromTeam().getName());
    	}

		if(!ObjectUtils.isEmpty(entity.getToTeam())) {
			dto.setToTeamId(entity.getToTeam().getId());
			dto.setToTeamName(entity.getToTeam().getName());
		}

		if(!ObjectUtils.isEmpty(entity.getNotifyAppUser())) {
			dto.setNotifyAppUserId(entity.getNotifyAppUser().getId());
			dto.setNotifyAppUserName("("+ entity.getNotifyAppUser().getDisplayName()+ ") " + entity.getNotifyAppUser().getUsername() );
		}

		return dto;
    }

	

}
