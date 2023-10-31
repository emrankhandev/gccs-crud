package com.gccws.sysadmin.control.service;

import com.gccws.base.service.BaseService;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.control.dto.UserRoleMasterDto;
import com.gccws.sysadmin.control.model.UserRoleModel;

import java.util.List;

/**
 * @Author    MD. Chabed Alam
 * @Since     August 29, 2022
 * @version   1.0.0
 */

public interface UserRoleService extends BaseService<UserRoleModel, CommonDropdownModel, CommonPageableData> {
    List<UserRoleMasterDto> getRoleListByUser(Integer appUserId, int userId);

}
