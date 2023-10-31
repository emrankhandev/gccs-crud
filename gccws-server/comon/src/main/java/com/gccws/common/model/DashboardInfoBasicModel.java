package com.gccws.common.model;

/**
 * @Author    Rima
 * @Since     April 10, 2023
 * @version   1.0.0
 */

public interface DashboardInfoBasicModel {
	Integer getTotalEmployee();
    Integer getTotalLate();
    Integer getTotalAbsent();
    Integer getTotalNotice();
    Integer getTotalProbationEmp();

}
