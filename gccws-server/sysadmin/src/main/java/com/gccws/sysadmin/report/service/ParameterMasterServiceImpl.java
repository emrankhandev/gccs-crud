package com.gccws.sysadmin.report.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.report.entity.ParameterMaster;
import com.gccws.sysadmin.report.repository.ParameterMasterRepository;
import com.gccws.sysadmin.report.dto.ParameterMasterDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;

import javax.transaction.Transactional;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Service
@AllArgsConstructor
public class ParameterMasterServiceImpl implements ParameterMasterService{

	@SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ParameterMasterServiceImpl.class);

	private ModelMapper modelMapper;
	private ParameterMasterRepository repo;
    private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = ParameterMaster.class.getSimpleName();

    @Transactional
	@Override
	public ParameterMasterDto save(ParameterMasterDto obj, int userId) {
		ParameterMaster savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}


	@Transactional
	@Override
	public ParameterMasterDto update(ParameterMasterDto obj, int userId) {
		ParameterMasterDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		ParameterMaster savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}


	@Transactional
	@Override
	public Boolean delete(ParameterMasterDto obj,int userId) {
		ParameterMasterDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
			ParameterMaster entity = new ParameterMaster();
    		entity.setId(obj.getId());
    		repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditDto);
    		return true;
    	}else {
    		return false;
    	}
	}


	@Override
	public ParameterMasterDto getById(int id,int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<ParameterMaster> dataList = repo.findById(id);
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
	public Page<ParameterMasterDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<ParameterMaster> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<ParameterMasterDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}



//..................... Generate Model....................//

    private ParameterMaster generateEntity(ParameterMasterDto dto, int userId, Boolean isSaved) {
    	ParameterMaster entity = new ParameterMaster();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		ParameterMaster dbEntity = repo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
		if(!ObjectUtils.isEmpty(dto.getChildId())){
			ParameterMaster child = new ParameterMaster();
			child.setId(dto.getChildId());
			entity.setChild(child);
		}

    	return entity;
    }



    private List<ParameterMasterDto> convertEntityListToDtoList(Stream<ParameterMaster> entityList) {
    	return entityList.map(entity -> {
    		return generateDto(entity);
		}).collect(Collectors.toList());
    }


    private ParameterMasterDto generateDto(ParameterMaster entity) {
    	ParameterMasterDto dto = modelMapper.map(entity, ParameterMasterDto.class);
		if(!ObjectUtils.isEmpty(entity.getChild())){
			dto.setChildId(entity.getChild().getId());
			dto.setChildName(entity.getChild().getName());
		}
		return dto;
    }
}
