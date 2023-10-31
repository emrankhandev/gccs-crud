package com.gccws.sysadmin.approve.service;

import com.gccws.base.model.BaseDropdownModel;
import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.approve.model.ApprovalTeamModel;

import java.util.List;

/**
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */

public interface ApprovalTeamService extends BaseService<ApprovalTeamModel, CommonDropdownModel, CommonPageableData> {
    List<CommonDropdownModel> getNextTeamByDepartmentAndCurrentTeamId(Integer moduleId, Integer transactionTypeId, Integer currentTeamId, int userId);
    List<CommonDropdownModel> getPreviousTeamByDepartmentAndCurrentTeamId(Integer moduleId, Integer transactionTypeId, Integer currentTeamId, int userId);
    List<ApprovalTeamModel> getTeamByDepartmentId(Integer departmentId, int userId);

}
