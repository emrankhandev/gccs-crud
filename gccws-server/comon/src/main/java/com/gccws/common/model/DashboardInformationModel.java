package com.gccws.common.model;

import java.util.Date;

/**
 * @Author    Rima
 * @Since     April 10, 2023
 * @version   1.0.0
 */

public interface DashboardInformationModel {
	Integer getEmpPersonalInfoId();
	Integer getEmpId();

    String getEmpName();
    String getResult();
    Integer getStatusId();
    String getDesignationName();
    String getDepartmentName();
    String getProfileImg();
    String getImgLocation();
    Date getJoiningDate();
    Date getProbationDate();
    String getProjectName();

}
