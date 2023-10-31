package com.gccws.sysadmin.control.model;

import java.util.List;

import com.gccws.sysadmin.control.dto.UserRoleDetailsDto;
import com.gccws.sysadmin.control.dto.UserRoleMasterDto;

import lombok.Data;

@Data
public class UserRoleModel {
	private UserRoleMasterDto master;
    private List<UserRoleDetailsDto> detailsList;

}
