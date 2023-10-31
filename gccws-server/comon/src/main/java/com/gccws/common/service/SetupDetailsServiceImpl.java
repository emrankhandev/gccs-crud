package com.gccws.common.service;
import com.gccws.common.dto.SetupDetailsDto;
import com.gccws.common.entity.SetupDetails;
import com.gccws.common.entity.SetupMaster;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.repository.SetupDetailsRepository;
import com.gccws.common.repository.UserRoleAssignDetailsRepository;
import com.gccws.common.repository.UserRoleDetailsRepository;
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
 * @Author    Rima
 * @Since     January 09, 2023
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class SetupDetailsServiceImpl implements SetupDetailsService{

	private ModelMapper modelMapper;
	private final SetupDetailsRepository repo;
	private final UserRoleAssignDetailsRepository roleAssignDetailsRepository;
	private final UserRoleDetailsRepository roleDetailsRepository;
	private final CommonUtils commonUtils;

	/*exta code*/
	private final String ENTITY_NAME = SetupDetails.class.getSimpleName();

	@Transactional
	@Override
	public SetupDetailsDto save(SetupDetailsDto obj, int userId) {
		SetupDetails savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public SetupDetailsDto update(SetupDetailsDto obj, int userId) {
		SetupDetailsDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		SetupDetails savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public Boolean delete(SetupDetailsDto obj, int userId) {
		SetupDetailsDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if (!ObjectUtils.isEmpty(obj.getId())){
			SetupDetails entity = new SetupDetails();
			entity.setId(obj.getId());
			repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME, deleteAuditDto);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public SetupDetailsDto getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<SetupDetails> dataList = repo.findById(id);
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
	public Page<SetupDetailsDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<SetupDetails> pageResult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<SetupDetailsDto> objList = convertEntityListToDtoList(pageResult.stream());
		return new PageImpl<>(objList,pageRequest, pageResult.getTotalElements());
	}

	@Override
	public Page<SetupDetailsDto> getPageableListByModuleId(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<SetupDetails> pageResult = repo.searchPageableListByModuleId(pageableData.getSearchValue(),pageableData.getIntParam1(), pageRequest);
		List<SetupDetailsDto> objList = convertEntityListToDtoList(pageResult.stream());
		return new PageImpl<>(objList, pageRequest, pageResult.getTotalElements());
	}

	@Override
	public List<SetupDetailsDto> getByDevCodeAndParentId(Integer devCode, Integer parentId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertEntityListToDtoList(repo.findByDevCodeAndParentIdAndActive(devCode, parentId).stream());
	}

	@Override
	public List<SetupDetailsDto> getByDevCodeAndActive(Integer masterId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertEntityListToDtoList(repo.findByDevCodeAndActive(masterId).stream());
	}

	//..................... Generate Model....................//

	private SetupDetails generateEntity(SetupDetailsDto dto, int userId, Boolean isSaved) {
		SetupDetails entity = new SetupDetails();
		BeanUtils.copyProperties(dto, entity);
		if(isSaved) {
			entity.setEntryUser(userId);
			commonUtils.setEntryUserInfo(entity);
		}else {
			SetupDetails dbEntity = repo.findById(dto.getId()).get();
			entity.setUpdateUser(userId);
			commonUtils.setUpdateUserInfo(entity, dbEntity);
		}
		if(!ObjectUtils.isEmpty(dto.getMasterId())) {
			SetupMaster obj = new SetupMaster();
			obj.setId(dto.getMasterId());
			entity.setMaster(obj);
		}

		if(!ObjectUtils.isEmpty(dto.getParentId())) {
			SetupDetails parent = new SetupDetails();
			parent.setId(dto.getParentId());
			entity.setParent(parent);
		}

		return entity;
	}

	private List<SetupDetailsDto> convertEntityListToDtoList(Stream<SetupDetails> entityList) {
		return entityList.map(entity -> {
			return generateDto(entity);
		}).collect(Collectors.toList());
	}

	private SetupDetailsDto generateDto(SetupDetails entity) {
		SetupDetailsDto dto = modelMapper.map(entity, SetupDetailsDto.class);
		if(!ObjectUtils.isEmpty(entity.getMaster())) {
			dto.setMasterId(entity.getMaster().getId());
			dto.setMasterName(entity.getMaster().getName());
		}
		if(!ObjectUtils.isEmpty(entity.getParent())) {
			dto.setParentId(entity.getParent().getId());
			dto.setParentName(entity.getParent().getName());
		}
		return dto;
	}
}
