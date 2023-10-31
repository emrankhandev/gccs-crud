package com.gccws.sysadmin.control.model;

import java.util.List;

import com.gccws.sysadmin.control.dto.UserRoleAssignDetailsDto;
import com.gccws.sysadmin.control.dto.UserRoleAssignMasterDto;

import lombok.Data;

@Data
public class UserRoleAssignModel {
	private UserRoleAssignMasterDto master;
    private List<UserRoleAssignDetailsDto> detailsList;

}
