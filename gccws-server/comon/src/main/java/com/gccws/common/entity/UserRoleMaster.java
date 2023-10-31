package com.gccws.common.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.gccws.base.entity.BaseEntity;

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
@Table(name = "SYA_USER_ROLE_MASTER")
public class UserRoleMaster extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "NAME", length=100, nullable = false, unique = true)
	private String name;
	
	@Column(name = "BANGLA_NAME", length=100, nullable = false, unique = true)
	private String banglaName;

	@Column(name = "DEV_CODE")
	private Integer devCode;
	
}
