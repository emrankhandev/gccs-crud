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
public class ParameterMasterDto extends BaseDto implements Serializable {

    private String title;
    private String name;
    private String banglaName;
    private String dataType;
    private String sql;

    private Integer childId;
    private String childName;
    private String childRelationSql;
}
