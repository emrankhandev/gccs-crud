/**
 * 
 */
package com.gccws.sysadmin.control.dto;

import java.io.Serializable;


import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author    Rima
 * @Since     August 30, 2022
 * @version   1.0.0
 */  
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer passwordPolicyId;
    private String passwordPolicyName;
    private String username;
    private String password;
    private String displayName;
    private String email;
    private String mobile;

    private Integer userTypeId;
    private String userTypeName;

    private String otp;

    private Integer empPersonalInfoId;
    private String empPersonalInfoName;

    private Integer departmentId;

    private Integer customerInfoId;
    private String customerInfoName;

}
