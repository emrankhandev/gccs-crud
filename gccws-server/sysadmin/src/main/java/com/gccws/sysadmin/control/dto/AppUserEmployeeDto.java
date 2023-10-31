/**
 * 
 */
package com.gccws.sysadmin.control.dto;

import java.io.Serializable;
import java.util.Date;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author    Rima
 * @Since     August 31, 2022
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppUserEmployeeDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer appUserId;
	private String appUserName;
	private Integer empId;
	private String empName;
    private String employeeCode;
    private String designation;
    private String displayName;
    private Date activeDate;
    private String mobile;
    private String email;
    private Date inactiveDate;
    private String password;

}
