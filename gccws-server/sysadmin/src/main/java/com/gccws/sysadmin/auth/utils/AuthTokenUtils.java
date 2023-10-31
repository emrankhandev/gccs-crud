package com.gccws.sysadmin.auth.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.gccws.common.entity.AppUser;
import com.gccws.sysadmin.control.dto.AppUserDto;
import com.gccws.common.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */
@Component
public class AuthTokenUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthTokenUtils.class);

	private static String JWT_SECRET = "gccws-jwt-token";
	private static int JWT_EXPIRATION_MS = 86400000;
	
	@Autowired
	AppUserRepository userRepository;

//	@Autowired
//	EmpOfficialInfoRepository empOfficialInfoRepo;


	public String generateJwtToken(Authentication authentication) {

		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

		/*System.out.println("ex : ");
		System.out.println(new Date((new Date()).getTime() + JWT_EXPIRATION_MS));*/

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername())) //unable to get subject from UI.
				.claim("userInfo", customizeUser(userRepository.findById(userPrincipal.getId()).get()))
//				.claim("username", userPrincipal.getUsername())
//				.claim("appAccess", userRoleAssignRepository.findByAppUserId(userPrincipal.getId()))
				.claim("id", userPrincipal.getId())
//				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
	}
	
	private AppUserDto customizeUser(AppUser mainUser) {
//		CommonLookupDetails officeInfo = mainUser.getOffice();
//		if(officeInfo != null) {
//			CommonLookupDetails customOffice = new CommonLookupDetails();
//			customOffice.setId(officeInfo.getId());
//			customOffice.setName(officeInfo.getName());
//			mainUser.setOffice(customOffice);
//		}
//		CommonLookupDetails billingOffice = mainUser.getBillingOffice();
//		if(billingOffice != null) {
//			CommonLookupDetails customBillingOffice = new CommonLookupDetails();
//			customBillingOffice.setId(billingOffice.getId());
//			customBillingOffice.setName(billingOffice.getName());
//			mainUser.setBillingOffice(customBillingOffice);
//		}

		AppUserDto shortUser = new AppUserDto();
		shortUser.setId(mainUser.getId());
		shortUser.setUsername(mainUser.getUsername());
		shortUser.setDisplayName(mainUser.getDisplayName());
		shortUser.setUserTypeId(mainUser.getUserTypeId());
		shortUser.setEmail(mainUser.getEmail());
//		if(!ObjectUtils.isEmpty(mainUser.getEmpPersonalInfo())){
//			shortUser.setEmpPersonalInfoId(mainUser.getEmpPersonalInfo().getId());
//			EmpOfficialInfo empOfficialInfo = empOfficialInfoRepo.findByEmpPersonalInfoId(mainUser.getEmpPersonalInfo().getId());
//			if(!ObjectUtils.isEmpty(empOfficialInfo) && !ObjectUtils.isEmpty(empOfficialInfo.getDepartment())){
//				shortUser.setDepartmentId(empOfficialInfo.getDepartment().getId());
//			}
//		}
//		if(!ObjectUtils.isEmpty(mainUser.getCustomerInfo())){
//			shortUser.setCustomerInfoId(mainUser.getCustomerInfo().getId());
//		}
		return shortUser;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String getUserIdFromJwtToken(String token) {
		return String.valueOf(Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().get("id"));
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			LOG.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			LOG.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			LOG.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOG.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOG.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	
	
	public String getTokenFromRequest(HttpServletRequest request) {
		String token = null;
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			token = headerAuth.substring(7);
		}
		return token;
	}
	
	public Integer getUserIdFromRequest(HttpServletRequest request) {
		return Integer.valueOf(getUserIdFromJwtToken(getTokenFromRequest(request)));
	}
}
