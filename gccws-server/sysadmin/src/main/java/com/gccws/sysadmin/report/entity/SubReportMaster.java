package com.gccws.sysadmin.report.entity;

import javax.persistence.*;

import com.gccws.base.entity.BaseEntity;
import com.gccws.common.entity.MenuItem;
import com.gccws.common.entity.ReportUpload;
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
@Table(name = "SYA_SUB_REPORT_MASTER",
		uniqueConstraints = { @UniqueConstraint(name = "UniqueMenuItemAndReportUpload", columnNames = { "MENU_ITEM_ID", "REPORT_UPLOAD_ID" }) })
public class SubReportMaster extends BaseEntity {

	private static final long serialVersionUID = 5824148595416692347L;

	@ManyToOne
	@JoinColumn(name = "MENU_ITEM_ID")
	private MenuItem menuItem;

	@ManyToOne
	@JoinColumn(name = "REPORT_UPLOAD_ID")
	private ReportUpload reportUpload;
	
    
}
