package com.gccws.common.entity;


import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_FILE_VALIDATOR")
public class FileValidator extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", length=100, nullable = false)
	private String name;

	@Column(name = "FILE_EXTENSIONS")
	private String fileExtensions;
	
	@Column(name = "FILE_SIZE") // KB
	private Integer fileSize;
	
	@Column(name = "FILE_HEIGHT")
	private Integer fileHeight;
	
	@Column(name = "FILE_WIDTH")
	private Integer fileWidth;

	@Column(name = "IS_FIXED")
	private Boolean isFixed;

	@Column(name = "DEV_CODE", unique = true)
	private Integer devCode;

}
