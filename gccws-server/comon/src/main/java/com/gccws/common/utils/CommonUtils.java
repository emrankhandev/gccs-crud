package com.gccws.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gccws.base.utils.BaseUtils;
import com.gccws.base.entity.BaseEntity;
import com.gccws.common.entity.AppUser;
import com.gccws.common.entity.AuditLogging;
import com.gccws.common.entity.FileValidator;
import com.gccws.common.repository.AppUserRepository;
import com.gccws.common.repository.AuditLoggingRepository;
import com.gccws.common.repository.FileValidatorRepository;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author    Md. Chabed Alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Component
@AllArgsConstructor
public class CommonUtils extends BaseUtils {

	/*search*/
	public final String SEARCH_SEPARATOR = "008800";

	private AppUserRepository appUserRepo;

	private FileValidatorRepository fileValidatorRepository;

	private final AuditLoggingRepository auditRepo;

    //================================== *** ==================================
	//								Entity User
	//================================== *** ==================================

	public void setEntryUserInfo(Object obj) {
		BaseEntity entity = (BaseEntity) obj;

		/* set entry date */
		entity.setEntryDate(new Date());

		/* set other information */
		AppUser user =  appUserRepo.findById(entity.getEntryUser()).get();
		if (!ObjectUtils.isEmpty(user)){
			//entity.setEntryAppUserCode(user.getEmployeeCode());
		}
//		Integer userId = entity.getEntryUser();
//		AppUser appUser = appUserRepo.findById(userId).get();
//		entity.setEntryAppUserCode(appUser.getEmployeeCode());
	}

	/**
	 * first object is current entity from api
	 * second object is database entity
	 * @param obj
	 * @param obj2
	 */
    public void setUpdateUserInfo(Object obj, Object obj2) {
		BaseEntity entity = (BaseEntity) obj;
		BaseEntity data = (BaseEntity) obj2;
		
		/* set previous entry User Info */
		entity.setEntryUser(data.getEntryUser());
		entity.setEntryDate(data.getEntryDate());
		
		/* set update date */
		entity.setUpdateDate(new Date());

        /* set other information */
//		Integer userId = entity.getUpdateUser();
//		AppUser appUser = appUserRepo.findById(userId).get();
//		entity.setUpdateAppUserCode(appUser.getEmployeeCode());
		AppUser user =  appUserRepo.findById(entity.getEntryUser()).get();
		if (!ObjectUtils.isEmpty(user)){
			//entity.setUpdateAppUserCode(user.getEmployeeCode());
		}
	}

	//================================== *** ==================================
	//								FILE VALIDATION
	//================================== *** ==================================

	public Boolean isFileValid(MultipartFile file, Integer devCode){
		FileValidator fileValidator = fileValidatorRepository.findByDevCode(devCode);
		if(ObjectUtils.isEmpty(fileValidator)){
			return false;
		}

		// check extension
		String extensions = fileValidator.getFileExtensions();
		if(!ObjectUtils.isEmpty(extensions)){
			String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
			String[] extensionList = extensions.split(",");
			Boolean isTypeMatch = false;
			for(int i=0; i< extensionList.length; i++ ){
				if(extensionList[i].equalsIgnoreCase(fileType)){
					isTypeMatch = true;
				}
			}
			if(isTypeMatch){
				// check file size
				Integer storedSize = fileValidator.getFileSize();
				Integer size = Math.toIntExact(file.getSize()/1024); // size in KB
				if(storedSize >= size){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	//================================== *** ==================================
	//								Sanitize HTML
	//================================== *** ==================================

	public String sanitizeHtmlNone(String data){
		return Jsoup.clean(data, Safelist.none()); //if we do not want any html tag
	}

	public String sanitizeHtmlBasic(String data){
		return Jsoup.clean(data, Safelist.basic()); //if we only allow basic html tags
	}

	public String sanitizeHtmlCustom(String data){
		return Jsoup.clean(data, Safelist.relaxed().addAttributes("p", "b")); //    allow selected tags and attributes
	}

	//================================== *** ==================================
	//								audit logging
	//================================== *** ==================================
	public void auditLoggingForSave(Integer userId, String entityName, Object currentData){
		auditSave(userId, entityName, currentData, null, null, AUDIT_SAVE);
	}

	public void auditLoggingForUpdate(Integer userId, String entityName, Object currentData, Object oldData){
		auditSave(userId, entityName, currentData, oldData, null,  AUDIT_UPDATE);
	}

	public void auditLoggingForDelete(Integer userId, String entityName, Object currentData){
		auditSave(userId, entityName, currentData, null, null, AUDIT_DELETE);
	}

	public void auditLoggingForGet(Integer userId, String entityName, String getMethod){
		auditSave(userId, entityName, null, null, getMethod,  AUDIT_GET);
	}

	public void auditLoggingForReportPrint(Integer userId, String entityName, String getMethod){
		auditSave(userId, entityName, null, null, getMethod,  AUDIT_REPORT_PRINT);
	}

	public void auditLoggingForReportDownload(Integer userId, String entityName, String getMethod){
		auditSave(userId, entityName, null, null, getMethod,  AUDIT_REPORT_DOWNLOAD);
	}
	public void auditLoggingForSignIn(Integer userId, String entityName){
		auditSave(userId, entityName, null, null, null,  AUDIT_LOGIN);
	}

	public void auditSave(Integer userId, String entityName, Object currentData, Object oldData, String getMethod, String auditType){
		AuditLogging auditLogging = new AuditLogging();
		auditLogging.setEntryUser(userId);
		auditLogging.setEntryDate(new Date());
		auditLogging.setTableName(entityName);
		auditLogging.setCurrentData(currentData);
		auditLogging.setOldData(oldData);
		auditLogging.setGetMethod(getMethod);
		auditLogging.setAuditType(auditType);
		auditRepo.save(auditLogging);

	}

	//================================== *** ==================================
	//								others
	//================================== *** ==================================

	public static String randomGenerateTransactionId() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		//System.err.println(formatter.format(date));
		return formatter.format(date);
	}

	public Date getCompareDate() throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01");
	}

	public Date convertStringToDate(String stringDate)  {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
		} catch (ParseException e) {
			return null;
		}
	}

	public String convertDateToString(Date date, String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}


}
