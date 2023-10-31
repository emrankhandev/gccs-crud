package com.gccws.sysadmin.report.controller;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.report.dto.ReportUploadDto;
import com.gccws.sysadmin.report.service.ReportUploadService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.REPORT_UPLOAD_VALIDATOR_CODE;
import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT+"report-upload")
public class ReportUploadController implements BaseController<BaseResponse, ReportUploadDto, CommonPageableData, HttpServletRequest> {
	
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ReportUploadController.class);

    private final ReportUploadService service;
    /* utils */
    private final CommonUtils commonUtils;
    private final UserTokenRequestUtils authTokenUtils;
    private final ResourceLoader resourceLoader;
    private final Environment env;

    
    @Override
	public BaseResponse save(ReportUploadDto obj, HttpServletRequest request) {
		return null;
	}
    
    @PostMapping()
	public BaseResponse saveWithFile(@Valid @RequestPart(value="reportObj", required=true) ReportUploadDto obj,
									 @RequestPart(value="file", required = true) final MultipartFile file,
									 HttpServletRequest request, HttpServletResponse response){
    	try {
			if(commonUtils.isFileValid(file, REPORT_UPLOAD_VALIDATOR_CODE)){
				return commonUtils.generateSuccessResponse(service.saveWithFile(file, obj, authTokenUtils.getUserIdFromRequest(request)), SAVE_MESSAGE, SAVE_MESSAGE_BN);
			}else{
				return commonUtils.generateValidationResponse(response, null, INPUT_VALIDATION_MESSAGE, INPUT_VALIDATION_MESSAGE_BN);
			}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }

    @Override
	public BaseResponse update(ReportUploadDto obj, HttpServletRequest request) {
		return null;
	}
    
    @PutMapping
    public BaseResponse updateWithFile(@Valid @RequestPart(value="reportObj", required=true) ReportUploadDto obj,
    		@RequestPart(value="file", required = true) final MultipartFile file,
									   HttpServletRequest request, HttpServletResponse response){
    	try {
			if(commonUtils.isFileValid(file, REPORT_UPLOAD_VALIDATOR_CODE)){
				return commonUtils.generateSuccessResponse(service.updateWithFile(file, obj, authTokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
			}else{
				return commonUtils.generateValidationResponse(response, null, INPUT_VALIDATION_MESSAGE, INPUT_VALIDATION_MESSAGE_BN);
			}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }

	@Override
	@DeleteMapping
    public BaseResponse delete(@Valid @RequestBody ReportUploadDto body, HttpServletRequest request){
    	try {
    		if(service.deleteWithFile(body, authTokenUtils.getUserIdFromRequest(request))) {
    			return commonUtils.generateSuccessResponse(null, DELETE_MESSAGE, DELETE_MESSAGE_BN);
    		}else {
    			return commonUtils.generateSuccessResponse(null, DELETE_MESSAGE_FAILED, DELETE_MESSAGE_FAILED_BN);
    		}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }


    
    @GetMapping( value = GET_OBJECT_BY_ID, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(OBJECT_ID) int id, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.getById(id, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}
    
    @GetMapping( value = "/subreport", produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getAllActiveSubreport(HttpServletRequest request){
    	try {
    		return commonUtils.generateSuccessResponse(service.getAllActiveSubreport( authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }
    
    
    @GetMapping( value = "/master-report", produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getAllActiveMasterReport(HttpServletRequest request){
    	try {
    		return commonUtils.generateSuccessResponse(service.getAllActiveMasterReport(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }
    
    @GetMapping(value = DROPDOWN_LIST_PATH, produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getDropdownList(HttpServletRequest request){
    	try {
    		return commonUtils.generateSuccessResponse(service.getDropdownList( authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }

	@Override
	@PutMapping( value = PAGEABLE_DATA_PATH, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = ("/subreport-dropdown-list"), produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getSubreportDropdownList(HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getSubReportList(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = ("/master-report-dropdown-list"), produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getMasterReportDropdownList(HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getMasterReportList(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping("/download/{filename}")
	public ResponseEntity<?> downloadFile(@PathVariable("filename") String fileName){
		Resource resource = resourceLoader.getResource("file:" +env.getProperty("report.source.dir") + fileName);
		try {
			System.out.println(fileName);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (Exception e1) {
			e1.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
    
}
