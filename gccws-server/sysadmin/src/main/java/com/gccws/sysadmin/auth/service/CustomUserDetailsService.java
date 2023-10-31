package com.gccws.sysadmin.auth.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gccws.common.entity.AppUser;
import com.gccws.common.repository.AppUserRepository;
import com.gccws.sysadmin.auth.utils.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	private AppUserRepository userRepository;


	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AppUser user = userRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		
		if(!user.getActive()) {
			LOG.error("User is not active with username: " + username);
			throw new InternalAuthenticationServiceException("User is not active with username: " + username);
		}
		
		if(user.getAccountExpired()) {
			LOG.error("User is expired with username: " + username);
			throw new InternalAuthenticationServiceException("User is expired with username: " + username);
		}
		
		if(user.getAccountLocked()) {
			LOG.error("User is locked with username: " + username);
			throw new InternalAuthenticationServiceException("User is locked with username: " + username);
		}
		
		if(user.getCredentialsExpired()) {
			LOG.error("User credentials expired with username: " + username);
			throw new InternalAuthenticationServiceException("User credentials expired with username: " + username);
		}
//
//		AppUserEmployee appUserEmp = appUserEmpRepo.findByActiveAndAppUserUsernameIgnoreCase(true, username)
//				.orElseThrow(() -> new UsernameNotFoundException("App User Emp Not Found with username: " + username));
//
////		LOG.info("----> active date <--------" + appUserEmp.getActiveDate());
//		if(appUserEmp.getActiveDate() != null && isDateExpire(appUserEmp.getActiveDate(), true)) {
//			LOG.error("active date not come for username: " + username);
//			throw new UsernameNotFoundException("active date not come for username: " + username);
//		}
//
//
//		if(appUserEmp.getInactiveDate() != null && isDateExpire(appUserEmp.getInactiveDate(), false)) {
//			LOG.error("inactive date pass for username: " + username);
//			throw new UsernameNotFoundException("inactive date pass for username: " + username);
//		}
//

		return CustomUserDetails.build(user);
	}
	
	
	
	private boolean isDateExpire(Date expDate, boolean isActiveDate) {
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			
			Date dbDate = df.parse(df.format(expDate));
			Date currentDate = df.parse(df.format(removeTimeFromDate(new Date())));
			
			int dateCompare = dbDate.compareTo(currentDate);

//			System.out.println("date compare ---> " + dateCompare);
			if(isActiveDate) {
				if ( dateCompare > 0) {
					return true;
				}
			}else {
				if (dateCompare != 0 && dateCompare < 0) {
					return true;
				}
			}
			
		}catch (ParseException ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public static Date removeTimeFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
