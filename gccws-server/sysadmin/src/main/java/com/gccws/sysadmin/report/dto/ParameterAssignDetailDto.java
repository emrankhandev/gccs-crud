package com.gccws.sysadmin.report.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParameterAssignDetailDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer parameterAssignMasterId;
    private String parameterAssignMasterName;
    private Integer parameterMasterId;
    private String parameterMasterTitle;
    private String parameterMasterName;
    private String parameterMasterDataType;
    private String parameterMasterSql;
    private Integer parameterMasterChildId;
    private String parameterMasterChildName;
    private String dropdownListData;
    private Boolean isRequired;
    private Integer serialNo;
    private Boolean isDependent;

}
