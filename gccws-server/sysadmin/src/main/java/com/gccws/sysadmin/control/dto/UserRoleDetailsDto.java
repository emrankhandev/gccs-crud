 package com.gccws.sysadmin.control.dto;

import java.io.Serializable;


import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author    Md. Chabed Alam
 * @Since     August 29, 2022
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDetailsDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer menuItemId;
    private String menuItemName;
	private Boolean insert;
	private Boolean edit;
	private Boolean delete;
	private Boolean approve;
	private Boolean view;
	
}
