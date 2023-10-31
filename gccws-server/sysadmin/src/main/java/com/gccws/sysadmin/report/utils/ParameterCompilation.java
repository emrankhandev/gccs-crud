package com.gccws.sysadmin.report.utils;

import com.gccws.sysadmin.report.dto.ParameterMasterDto;
import com.gccws.sysadmin.report.service.ParameterAssignService;
import com.gccws.sysadmin.report.service.ParameterMasterService;
import com.gccws.sysadmin.report.service.ReportService;
import com.gccws.sysadmin.report.service.SubReportMasterService;
import com.gccws.sysadmin.report.dto.ParameterAssignDetailDto;
import com.gccws.sysadmin.report.dto.ReportDropdownListDataDto;
import com.gccws.sysadmin.report.dto.SubReportMasterDto;
import com.gccws.sysadmin.report.model.ParameterAssignModel;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author		Md. Mizanur Rahman
 * @Since		August 28, 2022
 * @version		1.0.0
 */

@Component
public class ParameterCompilation {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ParameterCompilation.class);

	@Autowired
	ReportSources reportSources;

	@Autowired
    ReportService reportService;

	@Autowired
    SubReportMasterService subReportMasterService;

	@Autowired
    ParameterAssignService reportWithParameterService;

	@Autowired
    ParameterMasterService parameterMasterService;


	public Map<String, Object> getParamsValue(Integer devCode, Map<String, Object> params) throws FileNotFoundException, JRException, ParseException {

		Map<String, Object> parameters = new HashMap<>();

		List<ParameterAssignModel> modelList = reportWithParameterService.getByMenuItemDevCode(devCode);

		List<ParameterAssignDetailDto> list = modelList.size() > 0 ? modelList.get(0).getDetailsList() : null;

		if(!CollectionUtils.isEmpty(list)) {
			for(ParameterAssignDetailDto data: list) {

				if(data.getParameterMasterDataType().equals("Integer")) {
					if(!ObjectUtils.isEmpty(params.get(data.getParameterMasterName()))) {
						parameters.put(data.getParameterMasterName(), Integer.valueOf(params.get(data.getParameterMasterName()).toString()));
					}else {
						parameters.put(data.getParameterMasterName(), null);
					}
				}else if(data.getParameterMasterDataType().equals("Date")) {
					if(!ObjectUtils.isEmpty(params.get(data.getParameterMasterName()))) {
						String requestDateString= params.get(data.getParameterMasterName()).toString();
					    Date requestDate=new SimpleDateFormat("yyyy-MM-dd").parse(requestDateString);

						parameters.put(data.getParameterMasterName(), requestDate);
					}else {
						parameters.put(data.getParameterMasterName(), null);
					}
				}else if(data.getParameterMasterDataType().equals("List")) {
					if(!params.get(data.getParameterMasterName()).equals(0)) {
						parameters.put(data.getParameterMasterName(), Integer.valueOf(params.get(data.getParameterMasterName()).toString()));
					}else {
						parameters.put(data.getParameterMasterName(), null);
					}
				}else {
					if(!ObjectUtils.isEmpty(params.get(data.getParameterMasterName()))) {
						parameters.put(data.getParameterMasterName(), params.get(data.getParameterMasterName()));
					}else {
						parameters.put(data.getParameterMasterName(), null);
					}
				}

			}
		}

		//parameters.put("LOGO", reportSources.getImage(reportSources.LOGO));
		List<SubReportMasterDto> subreportDtoList = subReportMasterService.getByMenuItemDevCodeAndActive(devCode, true);
		if(!CollectionUtils.isEmpty(subreportDtoList)) {
			for(SubReportMasterDto subreport: subreportDtoList) {
				parameters.put(subreport.getReportUploadFileNameParams(), reportSources.getCompiledReport(subreport.getReportUploadFileNameJasper()));
			}
		}

		return parameters;
	}



	public List<ParameterAssignDetailDto> getParamsView(Integer reportDevCode) throws FileNotFoundException, JRException, ParseException {

		List<ParameterAssignDetailDto> res = new ArrayList<>();

		List<ParameterAssignModel> modelList = reportWithParameterService.getByMenuItemDevCode(reportDevCode);

		List<ParameterAssignDetailDto> list = modelList.size() > 0 ? modelList.get(0).getDetailsList() : null;

		if(!CollectionUtils.isEmpty(list)) {
			for(ParameterAssignDetailDto data : list) {
				if(data.getParameterMasterDataType().equals("List")) {
					String dropdownValue = "";
    				String sql = data.getParameterMasterSql();
    				List<ReportDropdownListDataDto> listdata = reportService.getDropdownListData(sql);
    				if(!CollectionUtils.isEmpty(listdata)) {

    					for(ReportDropdownListDataDto a : listdata) {
    						Integer id = a.getReport_dropdown_id();
    						String name = a.getReport_dropdown_name();
    						dropdownValue += "<option value="+id+">"+name+"</option>";
    					}
    					data.setDropdownListData(dropdownValue);
    				}
				}

				res.add(data);
			}
		}

		return res;
	}


	public String getChildListParamsView(Integer parentId, Integer paramId, Integer userId){

	    ParameterMasterDto parentParameterMaster = parameterMasterService.getById(parentId, userId);
		if(!ObjectUtils.isEmpty(parentParameterMaster)){
			if(!ObjectUtils.isEmpty(parentParameterMaster.getChildRelationSql())){
				List<ReportDropdownListDataDto> listdata = reportService.getChildDropdownListData(parentParameterMaster.getChildRelationSql(), paramId);

				if(!CollectionUtils.isEmpty(listdata)) {
					String dropdownValue = "";
					for(ReportDropdownListDataDto a : listdata) {
						Integer id = a.getReport_dropdown_id();
						String name = a.getReport_dropdown_name();
						dropdownValue += "<option value="+id+">"+name+"</option>";
					}
					return dropdownValue;
				}
			}
		}

		return null;
	}

}
