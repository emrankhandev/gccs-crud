package com.gccws.sysadmin.report.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterAssignMasterDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer menuItemId;
    private String menuItemName;

}
