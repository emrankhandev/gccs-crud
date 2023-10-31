package com.gccws.sysadmin.report.controller;


import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.report.utils.ParameterCompilation;
import com.gccws.sysadmin.report.utils.ReportCompilation;
import com.gccws.sysadmin.report.utils.ReportExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;


/**
 * @Author		Md. Mizanur Rahman
 * @Since		August 28, 2022
 * @version		1.0.0
 */
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT+"report-configure")
public class ReportController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

	@Autowired private ReportCompilation reportCompilation;
	@Autowired private ParameterCompilation parameterCompilation;
	@Autowired private ReportExporter reportExporter;
	@Autowired private CommonUtils commonUtils;
	@Autowired private UserTokenRequestUtils tokenUtils;


	@GetMapping( value = "/params/{reportId}")
    public BaseResponse getAllParamsById(@PathVariable("reportId") Integer reportDevCode, HttpServletRequest request, HttpServletResponse response){
    	try {
			BaseResponse res = commonUtils.generateSuccessResponse(parameterCompilation.getParamsView(reportDevCode));
    		commonUtils.auditLoggingForGet(tokenUtils.getUserIdFromRequest(request), "ParameterAssignMaster", "getAllParamsById");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
    }

	@GetMapping( value = "/child-list-params/{parentId}/{paramId}")
	public BaseResponse getParamChildListData(@PathVariable("parentId") Integer parentId, @PathVariable("paramId") Integer paramId, HttpServletRequest request, HttpServletResponse response){
		try {
			BaseResponse res = commonUtils.generateSuccessResponse(parameterCompilation.getChildListParamsView(parentId, paramId, tokenUtils.getUserIdFromRequest(request)));
			commonUtils.auditLoggingForGet(tokenUtils.getUserIdFromRequest(request), "ParameterAssignMaster", "getChildListParams");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@PostMapping("/generate-report/print")
	public ResponseEntity<byte[]> printReport(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException, ParseException, SQLException {
		Integer id = Integer.valueOf(params.get("id").toString());
		JasperReport report = reportCompilation.compileReports(id);
		Map<String, Object> parameters = parameterCompilation.getParamsValue(id, params);
		parameters.put("P_USER_ID_PREDEFINE", tokenUtils.getUserIdFromRequest(request));
		byte[] bytes = reportExporter.uploadPDFReport(id, report, parameters, response);
		commonUtils.auditLoggingForReportPrint(tokenUtils.getUserIdFromRequest(request), "ParameterAssignMaster", "getAllParamsById");

		var contentDisposition = ContentDisposition.builder("attachment").filename(id + ".pdf").build();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentDisposition(contentDisposition);
	    return ResponseEntity.ok()
	      .header("Content-Type", "application/pdf; charset=UTF-8")
	      .headers(headers)
	      .body(bytes);
	}

	@PostMapping("/generate-report/download")
	public ResponseEntity<byte[]> downloadReport(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws IOException, JRException, SQLException, ParseException {
		Integer id = Integer.valueOf(params.get("id").toString());
		JasperReport report = reportCompilation.compileReports(id);
		Map<String, Object> parameters = parameterCompilation.getParamsValue(id, params);
		parameters.put("P_USER_ID_PREDEFINE", tokenUtils.getUserIdFromRequest(request));
		byte[] bytes = reportExporter.uploadXlsxReport(id, report, parameters, response);
		var contentDisposition = ContentDisposition.builder("attachment").filename(id + ".xlsx").build();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(contentDisposition);
		return ResponseEntity.ok()
				.header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8")
				.headers(headers)
				.body(bytes);

	}

}
