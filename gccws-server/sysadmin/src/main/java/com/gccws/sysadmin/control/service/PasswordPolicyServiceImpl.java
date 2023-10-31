package com.gccws.sysadmin.control.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.gccws.common.entity.PasswordPolicy;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.PasswordPolicyRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.PasswordPolicyDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;

/**
 * @Author    Md. Chabed Alam
 * @Since     January 11, 2023
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class PasswordPolicyServiceImpl implements PasswordPolicyService{
	
	private ModelMapper modelMapper;
	private final PasswordPolicyRepository repo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = PasswordPolicy.class.getSimpleName();
	
	@Transactional
	@Override
	public PasswordPolicyDto save(PasswordPolicyDto obj, int userId) {
		PasswordPolicy savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public PasswordPolicyDto update(PasswordPolicyDto obj, int userId) {
		PasswordPolicyDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		PasswordPolicy savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Override
	public Boolean delete(PasswordPolicyDto obj,int userId) {
		PasswordPolicyDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
			PasswordPolicy entity = new PasswordPolicy();
    		entity.setId(obj.getId());
    		repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditDto);
    		return true;
    	}else {
    		return false;
    	}
	}

	@Override
	public PasswordPolicyDto getById(int id,int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<PasswordPolicy> dataList = repo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		return generateDto(dataList.get());
    	}
	}
	
	@Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findDropdownModel();
    }

	@Override
	public Page<PasswordPolicyDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<PasswordPolicy> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<PasswordPolicyDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}

	@Override
	public PasswordPolicyDto getAgentPolicy() {
		PasswordPolicy data = repo.findAgentPolicy();
		if(ObjectUtils.isEmpty(data)) {
			return null;
		}else {
			return generateDto(data);
		}
	}
	
	
	//..................... Generate Model....................//
	
	private PasswordPolicy generateEntity(PasswordPolicyDto dto, int userId, Boolean isSaved) {
		
		PasswordPolicy entity = new PasswordPolicy();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		PasswordPolicy dbEntity = repo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
    	return entity;
    }
 
	 private List<PasswordPolicyDto> convertEntityListToDtoList(Stream<PasswordPolicy> entityList) {
	    	return entityList.map(entity -> {
	    		return generateDto(entity);
			}).collect(Collectors.toList());
	    }
 
  
    public PasswordPolicyDto generateDto(PasswordPolicy entity) {
    	PasswordPolicyDto dto = modelMapper.map(entity, PasswordPolicyDto.class);
		return dto;
    }


}
