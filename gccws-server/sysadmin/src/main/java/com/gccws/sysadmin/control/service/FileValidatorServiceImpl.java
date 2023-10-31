package com.gccws.sysadmin.control.service;

import com.gccws.common.entity.FileValidator;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.FileValidatorRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.FileValidatorDto;
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
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class FileValidatorServiceImpl implements FileValidatorService{
	
	private ModelMapper modelMapper;
	private final FileValidatorRepository repo;
	private final CommonUtils commonUtils;

	private final String ENTITY_NAME = FileValidator.class.getSimpleName();

	@Transactional
	@Override
	public FileValidatorDto save(FileValidatorDto obj, int userId) {
		FileValidator savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public FileValidatorDto update(FileValidatorDto obj, int userId) {
		FileValidatorDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		FileValidator savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Override
	public Boolean delete(FileValidatorDto obj, int userId) {
		FileValidatorDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
			FileValidator entity = new FileValidator();
			entity.setId(obj.getId());
			repo.delete(entity);
			commonUtils.auditLoggingForDelete(obj.getId(),  ENTITY_NAME, deleteAuditDto);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public FileValidatorDto getById(int id, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<FileValidator> dataList = repo.findById(id);
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
	public Page<FileValidatorDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<FileValidator> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<FileValidatorDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}

	@Override
	public FileValidatorDto getByDevCode(Integer devCode, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		FileValidator obj = repo.findByDevCode(devCode);
		if(ObjectUtils.isEmpty(obj)) {
			return null;
		}else {
			return generateDto(obj);
		}
	}
	/*for public api*/
	@Override
	public FileValidatorDto getByDevCode(Integer devCode) {
		FileValidator obj = repo.findByDevCode(devCode);
		if(ObjectUtils.isEmpty(obj)) {
			return null;
		}else {
			return generateDto(obj);
		}
	}

	// for public start

	@Transactional
	@Override
	public FileValidatorDto save(FileValidatorDto obj) {
		FileValidator savedEntity = repo.save(generateEntity(obj, 1, true));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public FileValidatorDto update(FileValidatorDto obj) {
		FileValidator savedEntity = repo.save(generateEntity(obj,1, false));
		return generateDto(savedEntity);
	}

	@Override
	public Boolean delete(FileValidatorDto obj) {
		if(!ObjectUtils.isEmpty(obj.getId())) {
			FileValidator entity = new FileValidator();
			entity.setId(obj.getId());
			repo.delete(entity);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public FileValidatorDto getById(int id) {
		Optional<FileValidator> dataList = repo.findById(id);
		if(dataList.isEmpty()) {
			return null;
		}else {
			return generateDto(dataList.get());
		}
	}

	@Override
	public List<FileValidatorDto> getAll() {
		return convertEntityListToDtoList(repo.findAll().stream());
	}

	@Override
	public List<FileValidatorDto> getAllActive() {
		return convertEntityListToDtoList(repo.findByActive(true).stream());
	}


	// for public end

	//..................... Generate Model....................//

	private FileValidator generateEntity(FileValidatorDto dto, int userId, Boolean isSaved) {
		FileValidator entity = new FileValidator();
		BeanUtils.copyProperties(dto, entity);
		if(isSaved) {
			entity.setEntryUser(userId);
			commonUtils.setEntryUserInfo(entity);
		}else {
			FileValidator dbEntity = repo.findById(dto.getId()).get();
			entity.setUpdateUser(userId);
			commonUtils.setUpdateUserInfo(entity, dbEntity);
		}
		return entity;
	}

	private List<FileValidatorDto> convertEntityListToDtoList(Stream<FileValidator> entityList) {
		return entityList.map(entity -> {
			return generateDto(entity);
		}).collect(Collectors.toList());
	}

	private FileValidatorDto generateDto(FileValidator entity) {
		FileValidatorDto dto = modelMapper.map(entity, FileValidatorDto.class);
		return dto;
	}

}
