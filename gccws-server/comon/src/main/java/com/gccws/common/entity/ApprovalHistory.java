package com.gccws.common.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author    Rima
 * @Since     February 23, 2023
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_APPROVAL_HISTORY")
public class ApprovalHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ApprovalHistory.class);

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private SetupDetails department;

    @Column(name = "APPROVAL_TRANSACTION_TYPE_ID")
    private Integer approvalTransactionTypeId;

    @Column(name = "APPROVAL_TRANSACTION_TYPE_NAME", length=50)
    private String approvalTransactionTypeName;

    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;

    @Column(name = "TRANSACTION_TYPE_ID")
    private Integer transactionTypeId;

    @Column(name = "TRANSACTION_TYPE_NAME", length=30)
    private String transactionTypeName;

    @Column(name = "TRANSACTION_TABLE_NAME", length=30)
    private String transactionTableName;

    @Column(name = "TRANSACTION_DESCRIPTION")
    private String transactionDescription;

    @Column(name = "router_url", length=200)
    private String routerUrl;

    @ManyToOne
    @JoinColumn(name = "FROM_TEAM_ID")
    private ApprovalTeamMaster fromTeam;

    @ManyToOne
    @JoinColumn(name = "TO_TEAM_ID")
    private ApprovalTeamMaster toTeam;

    @ManyToOne
    @JoinColumn(name = "FROM_APP_USER_ID")
    private AppUser fromAppUser;

    @ManyToOne
    @JoinColumn(name = "DEFAULT_APP_USER_ID")
    private AppUser defaultAppUser;

    @Column(name = "approval_status_id")
    private Integer approvalStatusId;

    @Column(name = "approval_status_name", length=20)
    private String approvalStatusName;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @Column(name = "approval_comment", length = 4000)
    private String approvalComment;

    @Column(name = "IS_SEEN", columnDefinition = "boolean default false")
    private Boolean isSeen = false;

    @Column(name = "SEEN_DATE")
    private Date seenDate;

    @Column(name = "IS_CLOSE", columnDefinition = "boolean default false")
    private Boolean isClose = false;

    @Column(name = "IS_CROSS", columnDefinition = "boolean default false")
    private Boolean isCross = false;

}
