package com.gccws.sysadmin.control.service;

import com.gccws.base.service.BaseService;
import com.gccws.common.entity.AppUser;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.model.CommonPageableData;
import com.gccws.sysadmin.control.model.ChangePasswordModel;
import com.gccws.sysadmin.control.dto.AppUserDto;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

/**
 * @Author    Rima
 * @Since     August 30, 2022
 * @version   1.0.0
 */

public interface AppUserService extends BaseService<AppUserDto, CommonDropdownModel, CommonPageableData> {
	Optional<AppUserDto> getByUsernameIgnoreCase(String username,int userId);
	List<AppUserDto> getByActive(boolean active,int userId);
	List<AppUserDto> getByEmployeeCodeAndActive(String code, boolean active,int userId);

	//Page<AppUserDto> getPageableListByUserType(int page, int size, String searchValue, int userType, int userId);


	/*public method start*/

	String sendEmailForOTP(String emailAddress, String userName) throws MessagingException;


	AppUserDto changePassword(ChangePasswordModel body);
	AppUserDto getByUserName(String username);

	AppUserDto signUp(AppUserDto appUserDto);

	AppUser getByEmail(String email);

	Page<AppUserDto> getPageableListDataForAgentProfile(CommonPageableData pageableBody, int userId);

	AppUserDto getByEmpPersonalInfoId(Integer empId, int userId);

}
