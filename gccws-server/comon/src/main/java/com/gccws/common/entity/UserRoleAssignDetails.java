package com.gccws.common.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.gccws.base.entity.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author    Md. Chabed Alam
 * @Since     January 09, 2023
 * @version   1.0.0
 */


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_USER_ROLE_ASSIGN_DETAILS") 
//@Table(name = "SYA_USER_ROLE_ASSIGN_DETAILS", uniqueConstraints={
//	    @UniqueConstraint(columnNames = {"APP_USER_ID", "USER_ROLE_ID"})
//	}) 
public class UserRoleAssignDetails extends BaseEntity {

	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(UserRoleAssignDetails.class);

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
	@JoinColumn(name = "MASTER_ID")
	private UserRoleAssignMaster master;

	@OneToOne
	@JoinColumn(name = "USER_ROLE_ID", nullable = false)
    private UserRoleMaster userRole;
	
	
	
}
