package com.gccws.sysadmin.approve.service;

import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.approve.dto.ApprovalHistoryDto;
import com.gccws.common.model.CommonDropdownModel;

import java.util.Date;
import java.util.List;

/**
 * @Author    Rima
 * @Since     February 23, 20223
 * @version   1.0.0
 */
public interface ApprovalHistoryService extends BaseService<ApprovalHistoryDto, CommonDropdownModel, CommonPageableData> {
    List<ApprovalHistoryDto> getNotificationListByNotifyUserId(Integer userId);
    List<ApprovalHistoryDto> getApprovalPendingList(Integer userId, Date fromDate, Date toDate, Integer transactionTypeId);
    List<ApprovalHistoryDto> getByApprovalHistoryId(Integer approvalHistoryId, Integer userId);

    List<ApprovalHistoryDto> getApprovalPendingListByDate(int userId, Date formDate, Date toDate );

}
