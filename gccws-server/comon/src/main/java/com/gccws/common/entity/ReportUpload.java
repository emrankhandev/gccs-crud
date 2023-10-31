package com.gccws.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.gccws.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_REPORT_UPLOAD")
public class ReportUpload extends BaseEntity {

	private static final long serialVersionUID = 5824148595416692347L;

	@Column(name = "CODE", length = 100)
	private String code;

	@Column(name = "FILE_LOCATION")
	private String fileLocation;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_NAME_JASPER")
	private String fileNameJasper;

	@Column(name = "FILE_NAME_PARAMS")
	private String fileNameParams;

	@Column(name = "IS_SUBREPORT", columnDefinition = "boolean default true")
	private Boolean isSubreport = true;

	@Column(name = "REMARKS")
	private String remarks;


}
