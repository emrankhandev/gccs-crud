package com.gccws.sysadmin.auth.response;

import com.gccws.common.dto.PasswordHistoryDto;
import com.gccws.sysadmin.control.dto.PasswordPolicyDto;
import lombok.Data;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Data
public class LoginResponse {

	private String token;
	private PasswordHistoryDto passwordHistory;
	private PasswordPolicyDto passwordPolicy;

	public LoginResponse(String token, PasswordHistoryDto passwordHistory, PasswordPolicyDto passwordPolicy) {
		this.token = token;
		this.passwordHistory = passwordHistory;
		this.passwordPolicy = passwordPolicy;
	}
}
