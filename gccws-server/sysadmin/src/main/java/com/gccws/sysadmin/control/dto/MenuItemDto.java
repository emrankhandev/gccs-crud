package com.gccws.sysadmin.control.dto;

import java.io.Serializable;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author    Rima
 * @Since     July 10, 2023
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer menuType;
	private String menuTypeName;
	private Integer parentId;
	private String parentName;
	private Integer reportUploadId;
	private String reportUploadCode;
	private String reportUploadName;
	private String reportUploadNameJasper;
	private String reportUploadNameParams;
	private Boolean reportUploadIsSubreport;
	private Integer serialNo;
	private String url;
	private Integer devCode;
	private String icon;
	private Boolean insert;
	private Boolean edit;
	private Boolean delete;
	private Boolean approve;
	private Boolean view;
	
}
