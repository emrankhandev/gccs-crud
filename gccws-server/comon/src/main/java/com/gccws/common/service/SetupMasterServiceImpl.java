package com.gccws.common.service;

import com.gccws.common.dto.SetupMasterDto;
import com.gccws.common.entity.SetupMaster;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.repository.SetupMasterRepository;
import com.gccws.common.utils.CommonUtils;
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
 * @author Emran Khan
 * * @since July 11,2023
 * @version 1.0
 */

@Service
@AllArgsConstructor
public class SetupMasterServiceImpl implements SetupMasterService{

	private ModelMapper modelMapper;
	private final SetupMasterRepository repo;
	private final CommonUtils commonUtils;

	/*exta code*/
	private final String ENTITY_NAME = SetupMaster.class.getSimpleName();

	@Transactional
	@Override
	public SetupMasterDto save(SetupMasterDto obj, int userId) {
		SetupMaster savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public SetupMasterDto update(SetupMasterDto obj, int userId) {
		SetupMasterDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		SetupMaster savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public Boolean delete(SetupMasterDto obj, int userId) {
		SetupMasterDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if (!ObjectUtils.isEmpty(obj.getId())){
			SetupMaster entity = new SetupMaster();
			entity.setId(obj.getId());
			repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditDto);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public SetupMasterDto getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<SetupMaster> dataList = repo.findById(id);
		if(dataList.isEmpty()) {
			return null;
		}else {
			return generateDto(dataList.get());
		}
	}

	@Override
	public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findDropdownModel();
	}

	@Override
	public List<CommonDropdownModel> getDropdownListByModuleId(int moduleId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findDropdownModelByModuleId(moduleId);
	}


	@Override
	public Page<SetupMasterDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<SetupMaster> pageResult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<SetupMasterDto> objList = convertEntityListToDtoList(pageResult.stream());
		return new PageImpl<>(objList,pageRequest, pageResult.getTotalElements());
	}


	@Override
	public Page<SetupMasterDto> getPageableListByModuleId(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<SetupMaster> pageResult = repo.searchPageableListByModuleId(pageableData.getSearchValue(),pageableData.getIntParam1(), pageRequest);
		List<SetupMasterDto> objList = convertEntityListToDtoList(pageResult.stream());
		return new PageImpl<>(objList, pageRequest, pageResult.getTotalElements());
	}


	//..................... Generate Model....................//

	private SetupMaster generateEntity(SetupMasterDto dto, int userId, Boolean isSaved) {
		SetupMaster entity = new SetupMaster();
		BeanUtils.copyProperties(dto, entity);
		if(isSaved) {
			entity.setEntryUser(userId);
			commonUtils.setEntryUserInfo(entity);
		}else {
			SetupMaster dbEntity = repo.findById(dto.getId()).get();
			entity.setUpdateUser(userId);
			commonUtils.setUpdateUserInfo(entity, dbEntity);
		}
		if(!ObjectUtils.isEmpty(dto.getParentId())) {
			SetupMaster parent = new SetupMaster();
			parent.setId(dto.getParentId());
			entity.setParent(parent);
		}else {
			entity.setParent(null);
		}

		return entity;
	}

	private List<SetupMasterDto> convertEntityListToDtoList(Stream<SetupMaster> entityList) {
		return entityList.map(entity -> {
			return generateDto(entity);
		}).collect(Collectors.toList());
	}

	private SetupMasterDto generateDto(SetupMaster entity) {
		SetupMasterDto dto = modelMapper.map(entity, SetupMasterDto.class);
		if(!ObjectUtils.isEmpty(entity.getParent())) {
			dto.setParentId(entity.getParent().getId());
			dto.setParentName(entity.getParent().getName());

		}
		return dto;
	}

}
