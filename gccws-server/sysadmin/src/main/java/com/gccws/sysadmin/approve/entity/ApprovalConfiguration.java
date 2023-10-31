package com.gccws.sysadmin.approve.entity;

import com.gccws.base.entity.BaseEntity;
import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.ApprovalTeamMaster;
import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.SetupDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
/**
 * @Author    Rima
 * @Since     February 23, 2023
 * @version   1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_APPROVAL_CONFIGURATION")
public class ApprovalConfiguration extends BaseEntity {


	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ApprovalConfiguration.class);

	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	private SetupDetails department;

	@Column(name = "TRANSACTION_TYPE_ID")
	private Integer transactionTypeId;

	@Column(name = "TRANSACTION_TYPE_NAME", length=100)
	private String transactionTypeName;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	@JoinColumn(name = "MODULE_ID")
	private MenuItem module;

	@Column(name = "SERIAL_NO")
	private Integer serialNo;

	@ManyToOne
	@JoinColumn(name = "FROM_TEAM_ID")
	private ApprovalTeamMaster fromTeam;

	@ManyToOne
	@JoinColumn(name = "TO_TEAM_ID")
	private ApprovalTeamMaster toTeam;

	@ManyToOne
	@JoinColumn(name = "NOTIFY_APP_USER_ID", nullable = false)
	private AppUser notifyAppUser;

	@Column(name = "FROM_AMOUNT")
	private Double fromAmount;

	@Column(name = "TO_AMOUNT")
	private Double toAmount;

	@Column(name = "HAS_APPROVAL_PERMISSION", columnDefinition = "boolean default true")
	private Boolean approvalPermission= true;

	@Column(name = "HAS_BACK_PERMISSION", columnDefinition = "boolean default true")
	private Boolean backPermission= true;

	@Column(name = "HAS_CHANGE_PERMISSION", columnDefinition = "boolean default true")
	private Boolean changePermission= true;

}
