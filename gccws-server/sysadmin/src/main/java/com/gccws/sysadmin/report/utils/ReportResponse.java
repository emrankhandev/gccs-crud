package com.gccws.sysadmin.report.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @Author		Md. Mizanur Rahman
 * @Since		August 28, 2022
 * @version		1.0.0
 */
@Component
public class ReportResponse {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ReportResponse.class);
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	ReportSources reportSources;
	
	public ResponseEntity<Resource> getPDFResponse(String filename) throws IOException {
		Resource file = resourceLoader.getResource("file:" +reportSources.getOutputReport(filename));
        Path path = file.getFile()
                        .toPath();
        
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                             .body(file);
	}
	
	
//	public void getPDFReportResponse(HttpServletRequest request, HttpServletResponse response, Integer viewId) {
//		response.setContentType("application/pdf");
//		response.setHeader("Content-disposition", "inline; filename=" + viewId + ".pdf");
//		response.setHeader("responseURL", "" + request.getRequestURL());
//	}
	
	/*public void getXLSReportResponse(HttpServletRequest request, HttpServletResponse response, String viewId) {
		response.setContentType("application/xls");
		response.setHeader("Content-disposition", "inline; filename=" +viewId+ ".xls");
		response.setHeader("responseURL", ""+request.getRequestURL());
	}*/
	
	/*public void getCSVReportResponse(HttpServletRequest request, HttpServletResponse response, String viewId) {
		response.setContentType("application/csv");
		response.setHeader("Content-disposition", "inline; filename=" +viewId+ ".csv");
		response.setHeader("responseURL", ""+request.getRequestURL());
	}*/
	
	
	/*public void getHTMLReportResponse(HttpServletRequest request, HttpServletResponse response, String viewId) {
		response.setContentType("application/html");
		response.setHeader("Content-disposition", "inline; filename=" +viewId+ ".html");
		response.setHeader("responseURL", ""+request.getRequestURL());
	}*/
}
