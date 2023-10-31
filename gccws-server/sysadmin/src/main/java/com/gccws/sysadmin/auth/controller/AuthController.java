package com.gccws.sysadmin.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gccws.base.model.BaseResponse;
import com.gccws.common.dto.PasswordHistoryDto;
import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.PasswordHistory;
import com.gccws.common.repository.AppUserRepository;
import com.gccws.common.repository.PasswordHistoryRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.PasswordPolicyDto;
import com.gccws.sysadmin.control.service.PasswordPolicyService;
import com.gccws.sysadmin.auth.response.LoginResponse;
import com.gccws.sysadmin.auth.request.LoginRequest;
import com.gccws.sysadmin.auth.utils.AuthTokenUtils;
import com.gccws.sysadmin.auth.utils.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import static com.gccws.common.utils.CommonConstants.AUTH_END_POINT;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(AUTH_END_POINT)
public class AuthController {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthenticationManager authenticationManager;
	private final PasswordHistoryRepository historyRepository;
	private final AppUserRepository appUserRepository;
	private final PasswordPolicyService passwordPolicyService;

	private final AuthTokenUtils authTokenUtils;
	private final CommonUtils commonUtils;
	private PasswordEncoder encoder;

	@PostMapping("signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwtToken = authTokenUtils.generateJwtToken(authentication);

			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

			/* get password history */
			PasswordHistory passwordHistoy = historyRepository.getMaxEntryDateByUserId(userDetails.getId());
			PasswordHistoryDto passwordHistoryDto = new PasswordHistoryDto();
			passwordHistoryDto.setEntryUser(passwordHistoy.getEntryUser());
			passwordHistoryDto.setPassword(passwordHistoy.getPassword());
			passwordHistoryDto.setAppUserId(passwordHistoy.getAppUser().getId());
			passwordHistoryDto.setAppUserName(passwordHistoy.getAppUser().getUsername());

			/* get password policy */
			AppUser appUser = appUserRepository.findById(userDetails.getId()).get();
			PasswordPolicyDto passwordPolicy = passwordPolicyService.generateDto(appUser.getPasswordPolicy());

			/* create response */
			BaseResponse res  = new BaseResponse();
			res.setStatus(true);
			res.setData(new LoginResponse(jwtToken, passwordHistoryDto, passwordPolicy));
			commonUtils.auditLoggingForSignIn(userDetails.getId(),  AppUser.class.getSimpleName());
			return ResponseEntity.ok(res);
			
		}catch (InternalAuthenticationServiceException bce) {
			BaseResponse res = new BaseResponse();
			res.setStatus(false);
			res.setMessage(bce.getMessage());
			bce.printStackTrace();
			return ResponseEntity.ok(res);
		}catch (Exception e) {
			BaseResponse res  = new BaseResponse();
			res.setStatus(false);
			res.setMessage("Wrong username or password");
			return ResponseEntity.ok(res);
		}
		
	}
	
	@PostMapping("reset-password")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordHistoryDto historyDto, HttpServletRequest request) {
		System.out.println("hit method");
		try {
			BaseResponse res  = new BaseResponse();
			AppUser appUser = appUserRepository.findById(historyDto.getAppUserId()).get();
			appUser.setUpdateUser(appUser.getId());
			appUser.setPassword(encoder.encode(historyDto.getPassword()));
			AppUser dbEntity = appUserRepository.findById(appUser.getId()).get();
	    	commonUtils.setUpdateUserInfo(appUser, dbEntity);
			
	    	AppUser savedEntity = appUserRepository.save(appUser);
	    	if (!ObjectUtils.isEmpty(savedEntity)) {
				PasswordHistory passwordHistory = new PasswordHistory();
				passwordHistory.setPassword(historyDto.getPassword());
				passwordHistory.setAppUser(appUser);
				passwordHistory.setActive(true);
				passwordHistory.setEntryUser(appUser.getId());
		    	commonUtils.setEntryUserInfo(passwordHistory);
		    	historyRepository.save(passwordHistory);
		    	res.setStatus(true);
		    	res.setMessage("password reset successful please login with new password.");
	    	}else {
	    		res.setStatus(false);
				res.setMessage("unable to save data");
	    	}
			
			return ResponseEntity.ok(res);
			
		}catch (Exception e) {
			BaseResponse res  = new BaseResponse();
			e.printStackTrace();
			res.setStatus(false);
			res.setMessage("Server Error");
			return ResponseEntity.ok(res);
		}
		
		
	}
	
//
//	// Temp function for initial login
//	@PostMapping("signup")
//	public ResponseEntity<?> signUp(@Valid @RequestBody LoginRequest loginRequest) {
//
//		List<AppUser> userList = appUserRepository.findAll();
//		if(CollectionUtils.isEmpty(userList)) {
//
//			PasswordPolicy pw = new PasswordPolicy();
//			pw.setId(0);
//			pw.setName("Simple");
//			pw.setEntryUser(1);
//			pw.setEntryDate(new Date());
//			pw.setMinLength(5);
//			pw.setPasswordAge(6);
////			pw.setPasswordRemember(3);
//			PasswordPolicy pp = passwordPolicyRepo.save(pw);
//
//			AppUser user = new AppUser();
//			user.setEntryUser(1);
//			user.setEntryDate(new Date());
//			user.setUsername(loginRequest.getUsername());
//			user.setPassword(encoder.encode(loginRequest.getPassword()));
//			//user.setPasswordPolicy(pp);
//			//user.setEmployeeCode("EMP-01");
//			AppUser appUser = appUserRepository.save(user);
//			BaseResponse res  = new BaseResponse();
//			res.setStatus(true);
//			res.setMessage("SignUp Success");
//			res.setData(appUser);
//			return ResponseEntity.ok(res);
//		}else {
//			BaseResponse res  = new BaseResponse();
//			res.setStatus(false);
//			res.setMessage("SignUp Fail");
//			res.setData(null);
//			return ResponseEntity.ok(res);
//		}
//	}

	
}
