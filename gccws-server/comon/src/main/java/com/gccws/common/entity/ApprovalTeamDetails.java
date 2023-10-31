package com.gccws.common.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_APPROVAL_TEAM_DETAILS")
public class ApprovalTeamDetails extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ApprovalTeamDetails.class);

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne
	@JoinColumn(name = "MASTER_ID")
	private ApprovalTeamMaster master;

	@ManyToOne
	@JoinColumn(name = "APP_USER_ID")
	private AppUser appUser;

}
