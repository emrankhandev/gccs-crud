package com.gccws.sysadmin.report.entity;

import javax.persistence.*;

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
@Table(name = "SYA_PARAMETER_MASTER")
public class ParameterMaster extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "TITLE", length = 100)
	private String title;

	@Column(name = "NAME", length = 100)
	private String name;

	@Column(name = "BANGLA_NAME", length = 100)
	private String banglaName;

	@Column(name = "DATA_TYPE", length = 50)
	private String dataType;

	@Column(name = "SQL", length = 6000)
	private String sql;

	@ManyToOne
	@JoinColumn(name = "CHILD_ID")
	private ParameterMaster child;

	@Column(name = "CHILD_RELATION_SQL", length = 6000)
	private String childRelationSql;
	
}
