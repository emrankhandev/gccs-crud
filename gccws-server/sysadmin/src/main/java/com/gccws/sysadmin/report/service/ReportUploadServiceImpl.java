package com.gccws.sysadmin.report.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.gccws.common.entity.ReportUpload;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.report.repository.ReportUploadRepository;
import com.gccws.sysadmin.report.dto.ReportUploadDto;
import com.gccws.sysadmin.report.utils.ReportSources;
import com.gccws.sysadmin.report.utils.ReportUploader;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Service
@AllArgsConstructor
public class ReportUploadServiceImpl implements ReportUploadService{
	
	@SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ReportUploadServiceImpl.class);
	
	private ModelMapper modelMapper;
	private final ReportUploadRepository repo;
	private final CommonUtils commonUtils;
	private final ReportUploader reportUploader;
	private final ReportSources reportSources;

	/*extra code*/
	private final String ENTITY_NAME = ReportUpload.class.getSimpleName();
	
	@PostConstruct
    public void init() {
    }
	
	
	@Override
	public ReportUploadDto save(ReportUploadDto obj, int userId) {
		return null;
	}

	@Transactional
	@Override
	public ReportUploadDto saveWithFile(MultipartFile file, ReportUploadDto obj, int userId) throws IOException {
		ReportUpload savedEntity = repo.save(repo.save(generateEntity(obj, file, userId, true)));
		commonUtils.auditLoggingForSave(userId, ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}
	
	
	@Override
	public ReportUploadDto update(ReportUploadDto obj, int userId) {
		return null;
	}
	@Override
	public ReportUploadDto updateWithFile(MultipartFile file, ReportUploadDto obj, int userId) throws IOException {
		ReportUploadDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		ReportUpload savedEntity = repo.save(repo.save(generateEntity(obj, file, userId, false)));
		commonUtils.auditLoggingForUpdate(userId, ENTITY_NAME,generateDto(savedEntity),oldAuditDto);
		return generateDto(savedEntity);
	}

	
	@Override
	public Boolean delete(ReportUploadDto obj,int userId) {
		return null;
	}
	@Override
	public Boolean deleteWithFile(ReportUploadDto obj,int userId) throws IOException {
		ReportUploadDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
			ReportUpload entity = new ReportUpload();
    		entity.setId(obj.getId());
    		entity.setFileName(obj.getFileName());
    		
    		Path path = Paths.get(reportSources.getSourceReport(entity.getFileName()));
    		if(Files.exists(path)) {
    			Files.delete(path);
    		}
    		
    		repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId, ENTITY_NAME,deleteAuditDto);
    		return true;
    	}else {
    		return false;
    	}
	}

	@Override
	public ReportUploadDto getById(int id,int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<ReportUpload> dataList = repo.findById(id);
    	if(dataList.isEmpty()) {
    		return null;
    	}else {
    		return generateDto(dataList.get());
    	}
	}

	@Override
	public List<ReportUploadDto> getAllActiveSubreport(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertEntityListToDtoList(repo.findAllActiveSubreport().stream());
	}


	@Override
	public List<ReportUploadDto> getAllActiveMasterReport(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return convertEntityListToDtoList(repo.findAllActiveMasterReport().stream());
	}

	@Override
	public List<CommonDropdownModel> getSubReportList(int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findSubReportDropdownModel();
	}

	@Override
	public List<CommonDropdownModel> getMasterReportList(int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findMasterReportDropdownModel();

	}

	@Override
    public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
    	return repo.findDropdownModel();
    }

	@Override
	public Page<ReportUploadDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<ReportUpload> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<ReportUploadDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}




	//..................... Generate Model start....................//
    
    private ReportUpload generateEntity(ReportUploadDto dto, MultipartFile file, int userId, Boolean isSaved) throws IOException {
    	ReportUpload entity = new ReportUpload();
    	BeanUtils.copyProperties(dto, entity);
		String fileNameWithoutExt = file.getOriginalFilename().split("\\.")[0];
    	if(isSaved) {
    		//save report to server
    		Map<String, String> reportInfo = reportUploader.uploadMasterReportToServer(file, fileNameWithoutExt);

			if(ObjectUtils.isEmpty(entity.getIsSubreport())) {
    			entity.setIsSubreport(false);
    		}
            entity.setFileName(reportInfo.get("fileNameEng"));
            entity.setFileLocation(reportInfo.get("fileLocation"));
            entity.setFileNameJasper(reportInfo.get("fileNameJasperEng"));
			entity.setFileNameParams(fileNameWithoutExt);
    		entity.setEntryUser(userId);
        	commonUtils.setEntryUserInfo(entity);
    	}else {
    		
    		ReportUpload data = repo.findById(entity.getId()).get();
    		if(ObjectUtils.isEmpty(data)) {
    			return null;
    		}
    		System.out.println("callling update......");
    		Path path = Paths.get(reportSources.getSourceReport(data.getFileName()));
    		if(Files.exists(path)) {
    			Files.delete(path);
    		}
    		
    		//save report to server
    		Map<String, String> reportInfo = reportUploader.uploadMasterReportToServer(file, fileNameWithoutExt);
        	
    		if(ObjectUtils.isEmpty(entity.getIsSubreport())) {
    			entity.setIsSubreport(false);
    		}
    		entity.setFileName(reportInfo.get("fileNameEng"));
            entity.setFileLocation(reportInfo.get("fileLocation"));
            entity.setFileNameJasper(reportInfo.get("fileNameJasperEng"));
			entity.setFileNameParams(fileNameWithoutExt);
            entity.setUpdateUser(userId);
            commonUtils.setUpdateUserInfo(entity, data);
    	}
    	
    	return entity;
    }
    
    
    private ReportUploadDto generateDto(ReportUpload entity) {
    	ReportUploadDto dto = modelMapper.map(entity, ReportUploadDto.class);
		return dto;
    }
    
    
    private List<ReportUploadDto> convertEntityListToDtoList(Stream<ReportUpload> entityList) {
    	return entityList.map(entity -> {
    		return generateDto(entity);
		}).collect(Collectors.toList());
    }
    
  //..................... Generate Model end....................//
    
}
