package com.gccws.common.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.gccws.base.entity.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "SYA_USER_ROLE_DETAILS")
public class UserRoleDetails extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "MASTER_ID")
    private UserRoleMaster master;
	
	@OneToOne
	@JoinColumn(name = "MENU_ITEM_ID")
	private MenuItem menuItem;

	@Column(name = "IS_INSERT", columnDefinition = "boolean default false")
	private Boolean insert = false;
    
	@Column(name = "IS_EDIT", columnDefinition = "boolean default false")
	private Boolean edit = false;
	
	@Column(name = "IS_DELETE", columnDefinition = "boolean default false")
	private Boolean delete = false;

	@Column(name = "IS_APPROVE", columnDefinition = "boolean default false")
	private Boolean approve = false;
	
	@Column(name = "IS_VIEW", columnDefinition = "boolean default false")
	private Boolean view = false;
	
	
}
