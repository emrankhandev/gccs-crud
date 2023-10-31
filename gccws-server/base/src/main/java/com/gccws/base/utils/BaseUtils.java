package com.gccws.base.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gccws.base.model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author    Md. Chabed alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Component
public class BaseUtils implements BaseConstants {

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	Environment environment;

	//================================== *** ==================================
	//									Server Message
	//================================== *** ==================================
	
	public BaseResponse generateSuccessResponse(Object obj, String... message) {
		BaseResponse response = new BaseResponse();
		response.setStatus(true);
		response.setData(obj);
		if (message.length > 1 && message[0] != null && message[1] != null) {
			response.setMessage(message[0]);
			response.setMessageBn(message[1]);
        }
		return response;
	}

    public BaseResponse generateErrorResponse(Exception e) {
		BaseResponse response = new BaseResponse();
		response.setStatus(false);
		String msgType = getMessageType(e.getMessage());
		if(msgType.equals("uk") || msgType.equals("re")) {
			response.setMessage(DATA_ALRADY_EXISTS_MESSAGE);
			response.setMessageBn(DATA_ALRADY_EXISTS_MESSAGE_BN);
		}else if(msgType.equals("fk")) {
			response.setMessage(CHILD_RECORD_FOUND);
			response.setMessageBn(CHILD_RECORD_FOUND_BN);
		}else {
			response.setMessage(e.getMessage());
		}
		return response;
	}

	public BaseResponse generateValidationResponse(HttpServletResponse httpRes, Object obj, String... message) {
		BaseResponse response = new BaseResponse();
		response.setStatus(false);
		response.setData(obj);
		if (message.length > 1 && message[0] != null && message[1] != null) {
			response.setMessage(message[0]);
			response.setMessageBn(message[1]);
		}
		httpRes.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return response;
	}

	//================================== *** ==================================
	//								SERVICE IMPL
	//================================== *** ==================================

	public PageRequest getPageRequest(int page, int size) {
//      PageRequest.of(page, size, Sort.by("price").descending().and(Sort.by("name")));
//      PageRequest pageRequest = PageRequest.of(page, size, Sort.by("entryDate").descending());
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
		return pageRequest;
	}

	public String getSearchValue(String searchValue) {
		return searchValue.equals("0") ? "" : searchValue;
	}


	//================================== *** ==================================
	//									Helper
	//================================== *** ==================================
	
	public String commonCode(String str, String lastNumber) {
		return str.concat(lastNumber);
	}

    private String getMessageType(String message) {
//    	System.out.println("12 > " + message);
		if(message != null && message.length() > 55) {
			return message.substring(52,54);
		}
		return "";
	}
	
	public String getRendom4Digit() {
		Random random = new Random();
		return String.format("%04d", random.nextInt(10000));
	}
	
	public String generateCode(String prefix, Integer maxId, Integer totalDigit) {
		String padding = String.format("%0"+totalDigit+"d" , maxId);
		return prefix.concat(padding);
	}

	public boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}


	//================================== *** ==================================
	//									IMAGE UPLOAD
	//================================== *** ==================================
	public void deleteFileFormServer(String oldFile) throws IOException {
		/*delete file if already exist*/
		if(!ObjectUtils.isEmpty(oldFile)) {
			Path path = Paths.get(oldFile);
			if (Files.exists(path)) {
				Files.delete(path);
			}
		}
	}

	public Map<String, String> uploadImageToServer(MultipartFile file, String imageName, String oldFile) throws IOException {

		Map<String, String> response = new HashMap<>();

		/*delete file if already exist*/
		deleteFileFormServer(oldFile);

		/*dir & file name*/
		String dir = environment.getProperty("image.upload.dir");
		dir = dir.replace("\\", "/");

		String fileName = file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(imageName)) {
			fileName = imageName + getCurrentMilliSecond() +  ".png";
		}

		/*create dir*//*
		Path root = Paths.get(dir);
		if (!Files.exists(root)) {
			Files.createDirectories(Paths.get(dir));
		}

		*//*change file name*//*
		byte[] bytes = file.getBytes();
		String insPath = dir + fileName;

		//save to dir
		Files.write(Paths.get(insPath), bytes);

		response.put(KEY_FILE_LOCATION, dir);
		response.put(KEY_FILE_NAME, fileName);

		return response;*/
		return saveToDir(dir, file, fileName);
	}

	public Map<String, String> uploadImageToServer(MultipartFile file, String imageName) throws IOException {

		/*dir & file name*/
		String dir = environment.getProperty("image.upload.dir");
		dir = dir.replace("\\", "/");

		String fileName = file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(imageName)) {
			fileName = imageName + getCurrentMilliSecond() +  ".png";
		}
		return saveToDir(dir, file, fileName);
	}

	public Map<String, String> uploadVideoToServer(MultipartFile file, String videoName, String oldFile) throws IOException {

		/*delete file if already exist*/
		deleteFileFormServer(oldFile);

		/*dir & file name*/
		String dir = environment.getProperty("video.upload.dir");
		dir = dir.replace("\\", "/");

		String fileName = file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(videoName)) {
			fileName = videoName + getCurrentMilliSecond() +  ".mp4";
		}
		return saveToDir(dir, file, fileName);
	}

	public Map<String, String> uploadVideoToServer(MultipartFile file, String videoName) throws IOException {

		/*dir & file name*/
		String dir = environment.getProperty("video.upload.dir");
		dir = dir.replace("\\", "/");

		String fileName = file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(videoName)) {
			fileName = videoName + getCurrentMilliSecond() +  ".mp4";
		}
		return saveToDir(dir, file, fileName);
	}

	private Map<String, String> saveToDir(String dir, MultipartFile file, String fileName) throws IOException{
		Map<String, String> response = new HashMap<>();

		/*create dir*/
		Path root = Paths.get(dir);
		if (!Files.exists(root)) {
			Files.createDirectories(Paths.get(dir));
		}

		/*change file name*/
		byte[] bytes = file.getBytes();
		String insPath = dir + fileName;

		//save to dir
		Files.write(Paths.get(insPath), bytes);

		response.put(KEY_FILE_LOCATION, dir);
		response.put(KEY_FILE_NAME, fileName);

		return response;
	}

	public Resource getImageFromServer(String fileName){
		String image_dir = environment.getProperty("image.upload.dir") + fileName;
		Path path = Paths.get(image_dir);
		if (Files.exists(path)) {
			return resourceLoader.getResource("file:" + environment.getProperty("image.upload.dir") + fileName);
		}
		return null;
	}

	public Resource getVideoFromServer(String fileName){
		String video_dir = environment.getProperty("video.upload.dir") + fileName;
		Path path = Paths.get(video_dir);
		if (Files.exists(path)) {
			return resourceLoader.getResource("file:" + environment.getProperty("video.upload.dir") + fileName);
		}else{
			return resourceLoader.getResource("file:" + environment.getProperty("video.upload.dir") + fileName);
		}
	}

	//================================== *** ==================================
	//									FILE UPLOAD
	//================================== *** ==================================
	public void deleteExistingFile(String oldFile) throws IOException{
		/*delete file if already exist*/
		if(!ObjectUtils.isEmpty(oldFile)) {
			Path path = Paths.get(oldFile);
			if (Files.exists(path)) {
				Files.delete(path);
			}
		}
	}

	public Map<String, String> uploadFile(MultipartFile file, String additionalFileName, String oldFile) throws IOException {

		Map<String, String> response = new HashMap<>();

		/*delete file if already exist*/
		deleteExistingFile(oldFile);

		/*dir & file name*/
		String dir = environment.getProperty("file.upload.dir");
		dir = dir.replace("\\", "/");

		String fileName = file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(additionalFileName)) {
			fileName = additionalFileName + getCurrentMilliSecond() + "_" +  fileName;
		}

		/*create dir*/
		Path root = Paths.get(dir);
		if (!Files.exists(root)) {
			Files.createDirectories(Paths.get(dir));
		}

		/*change file name*/
		byte[] bytes = file.getBytes();
		String insPath = dir + fileName;

		//save to dir
		Files.write(Paths.get(insPath), bytes);

		response.put(KEY_FILE_LOCATION, dir);
		response.put(KEY_FILE_NAME, fileName);

		return response;
	}

	public Resource getFileFromServer(String fileName){
//		return resourceLoader.getResource("file:" + FILE_UPLOAD_DIR + fileName);
		String file_dir = environment.getProperty("file.upload.dir") + fileName;
		Path path = Paths.get(file_dir);
		if (Files.exists(path)) {
			return resourceLoader.getResource("file:" + environment.getProperty("video.upload.dir") + fileName);
		}else{
			return resourceLoader.getResource("file:" + environment.getProperty("video.upload.dir") + "no-image-found.png");
		}
	}

	public byte[] getByteFileArray(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[(int)file.length()];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		}finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}

	//================================== *** ==================================
	//									Date
	//================================== *** ==================================

	public String getCurrentMilliSecond(){
		Date today = new Date();
		return "_" + today.getTime();
	}

	
	//================================== *** ==================================
	//									Date Helper
	//================================== *** ==================================
	
	public String formateSqlDate(Date sqlDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
		return dateFormat.format(sqlDate);  
	}
	
	public String formateSqlTime(String sqlTime) {
		try {
			DateFormat sqlTimeFormat = new SimpleDateFormat("HH:mm");
			Date sTime = sqlTimeFormat.parse(sqlTime);
			DateFormat formTimeFormat = new SimpleDateFormat("hh:mm a");
			return formTimeFormat.format(sTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}  
		
	}

	public Integer getMonthIntegerFormDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH)+ 1;
	}

	public Integer getYearIntegerFormDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

}
