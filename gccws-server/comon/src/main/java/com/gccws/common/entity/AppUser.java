package com.gccws.common.entity;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gccws.base.entity.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author    Md. Chabed Alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_APP_USER")
public class AppUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AppUser.class);

	@Column(name = "USERNAME", length=50, nullable = false, unique = true)
	private String username;

	@JsonIgnore
	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "DISPLAY_NAME", length=100)
	private String displayName;
	
	@Column(name = "EMAIL", length=100)
	private String email;
	
	@Column(name = "MOBILE", length=20)
	private String mobile;

	@OneToOne
	@JoinColumn(name = "PASSWORD_POLICY_ID", nullable = false)
	private PasswordPolicy passwordPolicy;

	@Column(name = "USER_TYPE_ID")
	private Integer userTypeId;

	@Column(name = "USER_TYPE_NAME", length = 20)
	private String userTypeName;

	@Column(name = "IS_ACCOUNT_EXPIRED", columnDefinition = "boolean default false")
	private Boolean accountExpired= false;

	@Column(name = "IS_CREDENTIALS_EXPIRED", columnDefinition = "boolean default false")
	private Boolean credentialsExpired = false;

	@Column(name = "IS_ACCOUNT_LOCKED", columnDefinition = "boolean default false")
	private Boolean accountLocked= false;


	@Column(name = "OTP")
	private String otp;

//	@OnDelete(action = OnDeleteAction.CASCADE)
//	@ManyToOne
//	@JoinColumn(name = "EMP_PERSONAL_INFO_ID")
//	private EmpPersonalInfo empPersonalInfo;
//
//	@ManyToOne
//	@JoinColumn(name = "CUSTOMER_INFO_ID")
//	private CustomerInfo customerInfo;

}
