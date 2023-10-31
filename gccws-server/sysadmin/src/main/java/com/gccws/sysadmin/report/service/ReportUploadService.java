package com.gccws.sysadmin.report.service;

import java.io.IOException;
import java.util.List;

import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.report.dto.ReportUploadDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
public interface ReportUploadService extends BaseService<ReportUploadDto, CommonDropdownModel, CommonPageableData> {
	
	//CRUD
	ReportUploadDto saveWithFile(MultipartFile file, ReportUploadDto obj, int userId) throws IOException;
	ReportUploadDto updateWithFile(MultipartFile file, ReportUploadDto obj, int userId) throws IOException;
	Boolean deleteWithFile(ReportUploadDto obj,int userId) throws IOException;
	List<ReportUploadDto> getAllActiveSubreport(int userId);
	List<ReportUploadDto> getAllActiveMasterReport(int userId);
	List<CommonDropdownModel> getSubReportList (int userId);
	List<CommonDropdownModel> getMasterReportList (int userId);

	//Business
	
}
