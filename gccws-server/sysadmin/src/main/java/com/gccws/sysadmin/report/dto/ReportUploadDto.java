package com.gccws.sysadmin.report.dto;



import com.gccws.base.dto.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */


@Data
public class ReportUploadDto extends BaseDto {

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9_-]*")
    private String code;

    private String fileLocation;

    private String fileName;

    private String fileNameJasper;

    private String fileNameParams;

    private Boolean isSubreport;

    private String remarks;
}
