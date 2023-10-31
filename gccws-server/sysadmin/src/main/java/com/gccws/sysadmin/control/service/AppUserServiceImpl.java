package com.gccws.sysadmin.control.service;

import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.PasswordHistory;
import com.gccws.common.entity.PasswordPolicy;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.repository.AppUserRepository;
import com.gccws.common.repository.PasswordHistoryRepository;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.AppUserDto;
import com.gccws.sysadmin.control.model.ChangePasswordModel;
import com.gccws.sysadmin.control.util.SysAdminConstants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author    Rima
 * @Since     August 30, 2022
 * @version   1.0.0
 */

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService,SysAdminConstants{

	private ModelMapper modelMapper;
	private final AppUserRepository repo;
	private final PasswordHistoryRepository passwordHistoryRepository;
	private final CommonUtils commonUtils;

	private PasswordEncoder encoder;

	private Environment env;

	private final   Integer clientRollId = 5;

	/*extra code*/
	private final String ENTITY_NAME = AppUser.class.getSimpleName();


	@Transactional
	@Override
	public AppUserDto save(AppUserDto obj, int userId) {
		AppUser savedEntity = repo.save(generateEntity(obj, userId, true));

		/*assign role on user create*/
		/*if (obj.getUserTypeId() != 1){
			UserRoleAssignModel userRoleAssignModel = new UserRoleAssignModel();
			UserRoleAssignMasterDto masterDto = new UserRoleAssignMasterDto();
			masterDto.setAppUserId(savedEntity.getId());
			userRoleAssignModel.setMaster(masterDto);

			UserRoleAssignDetailsDto detailsDto = new UserRoleAssignDetailsDto();
			detailsDto.setUserRoleId(clientRollId);
			List<UserRoleAssignDetailsDto> detailList =  new ArrayList();
			detailList.add(detailsDto);
			userRoleAssignModel.setDetailsList(detailList);

			userRoleAssignService.save(userRoleAssignModel, userId);
		}*/


		/*save password history*/
		PasswordHistory passwordHistory = new PasswordHistory();
		passwordHistory.setAppUser(savedEntity);
		passwordHistory.setPassword(obj.getPassword());
		passwordHistory.setEntryUser(userId);
		commonUtils.setEntryUserInfo(passwordHistory);
		passwordHistoryRepository.save(passwordHistory);


		commonUtils.auditLoggingForSave(userId,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);

	}


	@Transactional
	@Override
	public AppUserDto signUp(AppUserDto obj) {
		AppUser savedEntity = repo.save(generateEntity(obj, 1, true));

		/*save password history*/
		PasswordHistory passwordHistory = new PasswordHistory();
		passwordHistory.setAppUser(savedEntity);
		passwordHistory.setPassword(obj.getPassword());
		passwordHistory.setEntryUser(1);
		commonUtils.setEntryUserInfo(passwordHistory);
		passwordHistoryRepository.save(passwordHistory);

		commonUtils.auditLoggingForSave(1,  ENTITY_NAME, generateDto(savedEntity));
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public AppUserDto update(AppUserDto obj, int userId) {
		AppUserDto oldAuditDto = generateDto(repo.findById(obj.getId()).get());
		AppUser savedEntity = repo.save(generateEntity(obj,userId, false));

		/*save password history*/
		if(!ObjectUtils.isEmpty(obj.getPassword())){
			PasswordHistory passwordHistory = new PasswordHistory();
			passwordHistory.setAppUser(savedEntity);
			passwordHistory.setPassword(obj.getPassword());
			passwordHistory.setEntryUser(userId);
			commonUtils.setEntryUserInfo(passwordHistory);
			passwordHistoryRepository.save(passwordHistory);
		}

		commonUtils.auditLoggingForUpdate(userId,  ENTITY_NAME, generateDto(savedEntity), oldAuditDto);
		return generateDto(savedEntity);
	}

	@Transactional
	@Override
	public Boolean delete(AppUserDto obj,int userId) {
		AppUserDto deleteAuditDto = generateDto(repo.findById(obj.getId()).get());
		if(!ObjectUtils.isEmpty(obj.getId())) {
			AppUser entity = new AppUser();
			entity.setId(obj.getId());
			repo.delete(entity);
			commonUtils.auditLoggingForDelete(userId,  ENTITY_NAME, deleteAuditDto);
			return true;
		}else {
			return false;
		}
	}

	@Override
	public AppUserDto getById(int id,int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		Optional<AppUser> dataList = repo.findById(id);
		if(dataList.isEmpty()) {
			return null;
		}else {
			return generateDto(dataList.get());
		}
	}

	@Override
	public List<CommonDropdownModel> getDropdownList(int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		return repo.findDropdownModel();
	}

	@Override
	public Page<AppUserDto> getPageableListData(CommonPageableData pageableData, int userId) {
		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
		Page<AppUser> pageresult = repo.searchPageableList(pageableData.getSearchValue(), pageRequest);
		List<AppUserDto> objlist = convertEntityListToDtoList(pageresult.stream());
		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
	}

	@Override
	public Page<AppUserDto> getPageableListDataForAgentProfile(CommonPageableData pageableData, int userId) {
//		commonUtils.auditLoggingForGet(userId, ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
//		PageRequest pageRequest  = commonUtils.getPageRequest(pageableData.getPage(), pageableData.getSize());
//		Page<AppUser> pageresult = repo.searchPageableListForAgentProfile(pageableData.getSearchValue(), pageableData.getIntParam1(), pageRequest);
//		List<AppUserDto> objlist = convertEntityListToDtoList(pageresult.stream());
//		return new PageImpl<>(objlist,pageRequest,pageresult.getTotalElements());
		return null;
	}


	@Override
	public AppUserDto getByUserName(String username) {
		AppUser appUser = repo.findByUsernameAndActive(username, true);
		if(!ObjectUtils.isEmpty(appUser)) {
			return generateDto(appUser);
		}else {
			return null;
		}
	}

	@Override
	public AppUserDto getByEmpPersonalInfoId(Integer empId, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		//return generateDto(repo.findByEmpPersonalInfoId(empId));
		return null;
	}

	@Override
	public String sendEmailForOTP(String emailAddress, String userName) {
		//------     OTP     -------
		int otpGenerator = (int)(Math.random()* 9000) + 1000 ;
		//------     mailFrom, userName, password, host & Subject  -------
		final String  mailFrom = env.getProperty("send.from.email");
		final String username = env.getProperty("spring.mail.username");
		final String password = env.getProperty("spring.mail.password");
		final String host =  env.getProperty("spring.mail.host");
		final String subject = "Sign Up";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "25");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(mailFrom));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailAddress));

			// Set Subject: header field
			message.setSubject(subject);


			String headPortion = "<!DOCTYPE html>\n" +
					"<html>\n" +
					"  <head>\n" +
					"    <style>\n" +
					"      #frame {width: 60%;margin: 0 auto;box-shadow: 0px 16px 32px -8px rgba(0, 0, 0, 0.08);}\n" +
					"      #block-title {background-color: #0dcaf0;color: white;padding: 1%;text-align: center;\n" +
					"        border-bottom: 2px solid #36454F;margin: 0 auto;}\n" +
					"      #otp {background-color: green;color: white;padding: 5px;text-align: center;}\n" +
					"      .text-center{text-align: center;}\n" +
					"      .text-justify{text-align: justify;}\n" +
					"      .padding-left{padding-left: 10px;}\n" +
					"      .padding-right{padding-right: 10px;}\n" +
					"      .padding-bottom{padding-bottom: 20px;}\n" +
					"      .font-size{font-size: 0.7em;}\n" +
					"    </style>\n" +
					"  </head>\n" +
					"  <body>\n" +
					"    <div id=\"frame\">\n" +
					"      <div id=\"block-title\">\n" +
					"        <h1>PAYRA PORT</h1>\n" +
					"      </div>\n" +
					"      <div class=\"padding-bottom\">\n" +
					"          <div class =\"padding-left padding-right\">\n" +
					"               <p>Dear ";
			String afterUserNamePortion = "</p>\n" +
					"        <p  class =\"text-justify\">";
			String tailPortion = " is your One Time Password (OTP) for Sign Up. Validity for OTP is 10 minutes.\n" +
					"        Sending positive vibes your way and hoping that you achieve all that you desire!\n" +
					"        </p>\n" +
					"               <code class=\"font-size\"><b>*Never share OTP with others</b></code>\n" +
					"          </div>\n" +
					"             </div>\n" +
					"    </div>\n" +
					"  </body>\n" +
					"</html>";

			// Send the actual HTML message, as big as you like
			message.setContent(headPortion+userName+afterUserNamePortion+otpGenerator+tailPortion,
					"text/html");
			// Send message
			Transport.send(message);
			System.err.println("Mail Successfully sent for "+emailAddress+"\twith userName: "+userName);
			//return String.valueOf(otpGenerator * 13);
			return String.valueOf(otpGenerator * 13);

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Transactional
	@Override
	public AppUserDto changePassword(ChangePasswordModel body) {
		AppUser appUser =  repo.findByUsernameAndActive(body.getUsername(), true);
		if(!ObjectUtils.isEmpty(appUser)){
			appUser.setPassword(encoder.encode(body.getPassword()));
			appUser.setUpdateUser(appUser.getEntryUser());
			appUser.setUpdateDate(new Date());
			return generateDto(repo.save(appUser));
		}else{
			return null;
		}
	}



	@Override
	public Optional<AppUserDto> getByUsernameIgnoreCase(String username, int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	@Override
	public List<AppUserDto> getByActive(boolean active,int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<AppUserDto> getByEmployeeCodeAndActive(String code, boolean active,int userId) {
		commonUtils.auditLoggingForGet(userId,  ENTITY_NAME, Thread.currentThread().getStackTrace()[1].getMethodName());
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppUser getByEmail(String email) {
		System.out.println(email);
		List<Integer> list = repo.checkByEmail(email);
		if(list.size()>0){
			return new AppUser();
		}
		return null;
	}

	//..................... Generate Model....................//

	private AppUser generateEntity(AppUserDto dto, int userId, Boolean isSaved) {
		AppUser entity = new AppUser();

		String previousPassword = null;
		if(isSaved) {
			entity.setEntryUser(userId);
			commonUtils.setEntryUserInfo(entity);
		}else {
			AppUser dbEntity = repo.findById(dto.getId()).get();
			previousPassword = dbEntity.getPassword();
			entity.setUpdateUser(userId);
			commonUtils.setUpdateUserInfo(entity, dbEntity);
			entity = dbEntity;
		}

		BeanUtils.copyProperties(dto, entity);

		if(!ObjectUtils.isEmpty(dto.getPasswordPolicyId())) {
			PasswordPolicy passwordPolicy = new PasswordPolicy();
			passwordPolicy.setId(dto.getPasswordPolicyId());
			entity.setPasswordPolicy(passwordPolicy);
		}
		if(!ObjectUtils.isEmpty(dto.getPassword())){
			entity.setPassword(encoder.encode(dto.getPassword()));
		}else {
			entity.setPassword(previousPassword);
		}

		if(ObjectUtils.isEmpty(dto.getUserTypeId()) || dto.getUserTypeId()!=2){
			entity.setUserTypeId(USER_TYPE_SYS_ADMIN);
		}

//		if(!ObjectUtils.isEmpty(dto.getEmpPersonalInfoId())) {
//			EmpPersonalInfo empPersonalInfo = new EmpPersonalInfo();
//			empPersonalInfo.setId(dto.getEmpPersonalInfoId());
//			entity.setEmpPersonalInfo(empPersonalInfo);
//		}
//		if(!ObjectUtils.isEmpty(dto.getCustomerInfoId())) {
//			CustomerInfo customerInfo = new CustomerInfo();
//			customerInfo.setId(dto.getCustomerInfoId());
//			entity.setCustomerInfo(customerInfo);
//		}
		return entity;
	}

	private List<AppUserDto> convertEntityListToDtoList(Stream<AppUser> entityList) {
		return entityList.map(entity -> {
			return generateDto(entity);
		}).collect(Collectors.toList());
	}

	private AppUserDto generateDto(AppUser entity) {
		AppUserDto dto = modelMapper.map(entity, AppUserDto.class);
		//dto.setEmpCode(entity.getEmployeeCode());

		if(!ObjectUtils.isEmpty(entity.getPasswordPolicy())) {
			dto.setPasswordPolicyId(entity.getPasswordPolicy().getId());
			dto.setPasswordPolicyName(entity.getPasswordPolicy().getName());
		}
//		if(!ObjectUtils.isEmpty(entity.getEmpPersonalInfo())){
//			dto.setEmpPersonalInfoId(entity.getEmpPersonalInfo().getId());
//			dto.setEmpPersonalInfoName(entity.getEmpPersonalInfo().getEmpName());
//		}
//		if(!ObjectUtils.isEmpty(entity.getCustomerInfo())){
//			dto.setCustomerInfoId(entity.getCustomerInfo().getId());
//			dto.setCustomerInfoName(entity.getCustomerInfo().getName());
//		}
		dto.setPassword(null);

		return dto;
	}


}
