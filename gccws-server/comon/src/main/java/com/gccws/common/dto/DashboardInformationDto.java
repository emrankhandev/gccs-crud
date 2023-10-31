package com.gccws.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author    Rima
 * @Since     April 10, 2023
 * @version   1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardInformationDto {

    private Integer empPersonalInfoId;
    private Integer empId;
    private String empName;
    private String result;
    private Integer statusId;
    private String designationName;
    private String departmentName;
    private String projectName;
    private String profileImg;
    private String imgLocation;
    private Date joiningDate;
    private Date probationDate;

}
