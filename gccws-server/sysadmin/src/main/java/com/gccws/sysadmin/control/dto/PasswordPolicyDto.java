package com.gccws.sysadmin.control.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author    Md. Chabed Alam
 * @Since     January 11, 2023
 * @version   1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordPolicyDto extends BaseDto {

	private String name;

	private Integer minLength;

	private Boolean sequential;

	private Boolean specialChar;

	private Boolean alphanumeric;

	private Boolean upperLower;

	private Boolean matchUsername;

	private Integer passwordRemember;

	private Integer passwordAge;

	private Integer devCode;

	
}
