package com.gccws.sysadmin.report.dto;



import com.gccws.base.dto.BaseDto;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */


@Data
public class SubReportMasterDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer menuItemId;
    private String menuItemName;
    private Integer reportUploadId;
    private String reportUploadCode;
    private String reportUploadFileName;
    private String reportUploadFileNameJasper;
    private String reportUploadFileNameParams;
    private Boolean reportUploadIsSubreport;
    private Integer serialNo;
    
}
