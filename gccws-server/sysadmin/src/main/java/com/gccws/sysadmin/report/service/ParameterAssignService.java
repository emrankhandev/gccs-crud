package com.gccws.sysadmin.report.service;


import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.report.model.ParameterAssignModel;

import java.util.List;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

public interface ParameterAssignService extends BaseService<ParameterAssignModel, CommonDropdownModel, CommonPageableData> {

    List<ParameterAssignModel> getByMenuItemId(int id);

    List<ParameterAssignModel> getByMenuItemDevCode(int devCode);
}
