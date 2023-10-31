package com.gccws.sysadmin.report.model;

import com.gccws.sysadmin.report.dto.ParameterAssignDetailDto;
import com.gccws.sysadmin.report.dto.ParameterAssignMasterDto;
import lombok.Data;

import java.util.List;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

@Data
public class ParameterAssignModel {

    private ParameterAssignMasterDto master;
    private List<ParameterAssignDetailDto> detailsList;

}
