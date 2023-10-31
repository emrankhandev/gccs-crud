package com.gccws.sysadmin.approve.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author    Rima
 * @Since     February 23, 2023
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalHistoryDto  extends BaseDto implements Serializable {

private static final long serialVersionUID = 1L;

    private Integer departmentId;
    private String  departmentName;
    private Integer approvalTransactionTypeId;
    private String approvalTransactionTypeName;
    private Integer transactionId;
    private Integer transactionTypeId;
    private String 	transactionTypeName;
    private String 	transactionTableName;
    private String 	transactionDescription;
    private String 	routerUrl;
    private Integer fromTeamId;
    private String  fromTeamName;
    private Integer toTeamId;
    private String  toTeamName;
    private Integer fromAppUserId;
    private String  fromAppUserName;
    private Integer defaultAppUserId;
    private String  defaultAppUserName;
    private Integer approvalStatusId;
    private String  approvalStatusName;
    private Double  totalAmount;
    private String  approvalComment;
    private Boolean isSeen;
    private Date seenDate;
    private Boolean isClose;
    private Boolean isCross;
    private Date entryDate;

}
