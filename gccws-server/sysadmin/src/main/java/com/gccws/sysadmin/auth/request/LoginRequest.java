package com.gccws.sysadmin.auth.request;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */
public class LoginRequest {

	public String username;
	public String password;
	
	public LoginRequest() {
	}
	
	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
