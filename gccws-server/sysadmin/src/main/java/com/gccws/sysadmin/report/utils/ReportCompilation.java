package com.gccws.sysadmin.report.utils;


import com.gccws.sysadmin.control.dto.MenuItemDto;
import com.gccws.sysadmin.control.service.MenuItemService;
import com.gccws.sysadmin.report.service.SubReportMasterService;
import com.gccws.sysadmin.report.dto.SubReportMasterDto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author		Md. Mizanur Rahman
 * @Since		August 28, 2022
 * @version		1.0.0
 */
@Component
public class ReportCompilation {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ReportCompilation.class);

	@Autowired
	ReportSources reportSources;
	
	@Autowired
    MenuItemService reportMasterservice;
	
	@Autowired
    SubReportMasterService subReportMasterService;
	
	@Autowired
    Environment env;
	

	public JasperReport compileReports(Integer devCode) throws JRException, IOException {
		MenuItemDto masterReportDto = reportMasterservice.getByDevCode(devCode);
		if(ObjectUtils.isEmpty(masterReportDto)) {
			return null;
		}

		String compileDir = env.getProperty("report.compile.dir");
		Path compilePath = Paths.get(compileDir);
        if (!Files.exists(compilePath)) {
        	Files.createDirectories(Paths.get(compileDir));
        }

		//compile master report
		File reportFile = ResourceUtils.getFile(reportSources.getSourceReport(masterReportDto.getReportUploadName()));
		JasperReport report = JasperCompileManager.compileReport(reportFile.getAbsolutePath());
		JRSaver.saveObject(report, reportSources.getCompiledReport(masterReportDto.getReportUploadNameJasper()));

		//compile subreport
		List<SubReportMasterDto> subreportMasterDtoList = subReportMasterService.getByMenuItemDevCodeAndActive(masterReportDto.getDevCode(),
				true);
		if (!CollectionUtils.isEmpty(subreportMasterDtoList)) {
			for (SubReportMasterDto subreport : subreportMasterDtoList) {
				File subReportFile = ResourceUtils.getFile(reportSources.getSourceReport(subreport.getReportUploadFileName()));
				JasperReport subReport = JasperCompileManager.compileReport(subReportFile.getAbsolutePath());
				JRSaver.saveObject(subReport, reportSources.getCompiledReport(subreport.getReportUploadFileNameJasper()));
			}
		}

		return report;
	}

}
