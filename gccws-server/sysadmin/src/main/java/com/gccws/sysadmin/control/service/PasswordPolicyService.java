package com.gccws.sysadmin.control.service;

import com.gccws.base.service.BaseService;
import com.gccws.common.entity.PasswordPolicy;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.control.dto.PasswordPolicyDto;

/**
 * @Author    Md. Chabed Alam
 * @Since     January 11, 2023
 * @version   1.0.0
 */

public interface PasswordPolicyService extends BaseService<PasswordPolicyDto, CommonDropdownModel, CommonPageableData> {
    public PasswordPolicyDto generateDto(PasswordPolicy entity);
    PasswordPolicyDto getAgentPolicy();
}
