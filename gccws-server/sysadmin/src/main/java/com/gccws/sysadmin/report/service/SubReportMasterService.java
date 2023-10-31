package com.gccws.sysadmin.report.service;

import java.util.List;


import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.report.dto.SubReportMasterDto;

/**
 * @Author		Rima
 * @Since		July 10, 2023
 * @version		1.0.0
 */
public interface SubReportMasterService extends BaseService<SubReportMasterDto, CommonDropdownModel, CommonPageableData> {

    List<SubReportMasterDto> getByMenuItemDevCodeAndActive(int devCode, boolean active);
}

