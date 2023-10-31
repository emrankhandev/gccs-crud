package com.gccws.sysadmin.report.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.ReportUpload;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.report.repository.SubReportMasterRepository;
import com.gccws.sysadmin.report.dto.SubReportMasterDto;
import com.gccws.sysadmin.report.entity.SubReportMaster;
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

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Service
@AllArgsConstructor
public class SubReportMasterServiceImpl implements SubReportMasterService{
	
	@SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(SubReportMasterServiceImpl.class);
	
	private ModelMapper modelMapper;
	private final SubReportMasterRepository repo;
	private final CommonUtils commonUtils;

	/*extra code*/
	private final String ENTITY_NAME = SubReportMaster.class.getSimpleName();

	@Transactional
    @Override
    public SubReportMasterDto save(SubReportMasterDto obj, int userId){
		SubReportMaster savedEntity = repo.save(generateEntity(obj, userId, true));
		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	
	@Transactional
    @Override
	public SubReportMasterDto update(SubReportMasterDto obj, int userId) {
		SubReportMasterDto oldAuditDto =generateDto(repo.findById(obj.getId()).get());
		SubReportMaster savedEntity = repo.save(generateEntity(obj,userId, false));
		commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Transactional
    @Override
    public Boolean delete(SubReportMasterDto obj,int userId) {
		SubReportMasterDto deleteAuditDto =generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
			SubReportMaster entity = new SubReportMaster();
    		entity.setId(obj.getId());
    		repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditDto);
    		return true;
    	}else {
    		return false;
    	}
    }

	@Override
	public SubReportMasterDto getById(int id,int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<SubReportMaster> dataList = repo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		return generateDto(dataList.get());
    	}
	}

	@Override
	public List<SubReportMasterDto> getByMenuItemDevCodeAndActive(int devCode, boolean active) {
		return convertEntityListToDtoList(repo.findByMenuItemDevCodeAndActive(devCode, active).stream());
	}

    @Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME,Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findDropdownModel();
    }

	@Override
	public Page<SubReportMasterDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<SubReportMaster> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<SubReportMasterDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}




//..................... Generate Model....................//
    
    private SubReportMaster generateEntity(SubReportMasterDto dto, int userId, Boolean isSaved) {
    	SubReportMaster entity = new SubReportMaster();
    	BeanUtils.copyProperties(dto, entity);
    	if(isSaved) {
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		SubReportMaster dbEntity = repo.findById(dto.getId()).get();
        	entity.setUpdateUser(userId);
        	commonUtils.setUpdateUserInfo(entity, dbEntity);
    	}
		if(!ObjectUtils.isEmpty(dto.getMenuItemId())) {
			MenuItem menuItem = new MenuItem();
			menuItem.setId(dto.getMenuItemId());
			entity.setMenuItem(menuItem);
		}else {
    		entity.setMenuItem(null);
    	}
    	
    	if(!ObjectUtils.isEmpty(dto.getReportUploadId())) {
    		ReportUpload reportUpload = new ReportUpload();
    		reportUpload.setId(dto.getReportUploadId());
        	entity.setReportUpload(reportUpload);
    	}else {
    		entity.setReportUpload(null);
    	}
    	
    	return entity;
    }
    
    
    
    private List<SubReportMasterDto> convertEntityListToDtoList(Stream<SubReportMaster> entityList) {
    	return entityList.map(entity -> {
    		return generateDto(entity);
		}).collect(Collectors.toList());
    }
    
    
    private SubReportMasterDto generateDto(SubReportMaster entity) {
    	SubReportMasterDto dto = modelMapper.map(entity, SubReportMasterDto.class);
		if(!ObjectUtils.isEmpty(entity.getMenuItem())) {
			dto.setMenuItemId(entity.getMenuItem().getId());
			dto.setMenuItemName(entity.getMenuItem().getName());
		}
		if(!ObjectUtils.isEmpty(entity.getReportUpload())) {
			dto.setReportUploadId(entity.getReportUpload().getId());
			dto.setReportUploadCode(entity.getReportUpload().getCode());
			dto.setReportUploadFileName(entity.getReportUpload().getFileName());
			dto.setReportUploadFileNameJasper(entity.getReportUpload().getFileNameJasper());
			dto.setReportUploadFileNameParams(entity.getReportUpload().getFileNameParams());
			dto.setReportUploadIsSubreport(entity.getReportUpload().getIsSubreport());
		}
		return dto;
    }
    
}
