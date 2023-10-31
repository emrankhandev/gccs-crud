package com.gccws.sysadmin.control.dto;

import java.io.Serializable;


import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author    Rima
 * @Since     September 1, 2022
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleAssignMasterDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer appUserId;
	private String appUserName;
	
}
