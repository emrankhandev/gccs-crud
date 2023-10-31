package com.gccws.sysadmin.auth.response;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */
public class MessageResponse {

	public String message;

	
	public MessageResponse() {
	}


	public MessageResponse(String message) {
		this.message = message;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
