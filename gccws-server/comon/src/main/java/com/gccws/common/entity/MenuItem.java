package com.gccws.common.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.gccws.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author    Md. Chabed Alam
 * @Since     July 10, 2023
 * @version   1.0.0
 */


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_MENU_ITEM")
public class MenuItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", length=100)
	private String name;

	@Column(name = "BANGLA_NAME", length=100)
	private String banglaName;

	@Column(name = "MENU_TYPE")
	private Integer menuType;

	@Column(name = "MENU_TYPE_NAME", length=100)
	private String menuTypeName;

	@Column(name = "SERIAL_NO")
	private Integer serialNo;

	@Column(name = "URL")
	private String url;

	@Column(name = "DEV_CODE")
	private Integer devCode;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private MenuItem parent;

	@ManyToOne
	@JoinColumn(name = "REPORT_UPLOAD_ID")
	private ReportUpload reportUpload;

	@Column(name = "ICON", length = 100)
	private String icon;

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
