package com.gccws.sysadmin.approve.service;

import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.approve.model.ApproveUserModel;
import com.gccws.sysadmin.approve.dto.ApprovalConfigurationDto;

/**
 * @Author    Rima
 * @Since     February 23, 20223
 * @version   1.0.0
 */
public interface ApprovalConfigurationService extends BaseService<ApprovalConfigurationDto, CommonDropdownModel, CommonPageableData> {
	ApproveUserModel getSubmitUserByDepartmentAndAppUserIdAndTransactionTypeId(Integer departmentId, Integer appUserId, Integer transactionTypeId, int userId);
	ApproveUserModel getApproveAndForwardUserByDepartmentAndAppUserId(Integer departmentId, Integer appUserId, Integer transactionTypeId, int userId);
	ApprovalConfigurationDto getByToTeamAndDepartmentId(Integer toTeamId, Integer departmentId, Integer transactionTypeId, int userId);

}
