package com.quiz.entity;

import java.util.Set;

public class SignupRequest {
	
//	private String username;
	private String name;
    private String email;   
    private String mobile;
    private Set<String> roles;
    private String password;
    private String captchaHash;
	private String captchaImage;
	private String randomString;
    
    public SignupRequest() {
		super();
	}

	public SignupRequest(String name, String email, String mobile, Set<String> roles, String password,
			String captchaHash, String captchaImage, String randomString) {
		super();
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.roles = roles;
		this.password = password;
		this.captchaHash = captchaHash;
		this.captchaImage = captchaImage;
		this.randomString = randomString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptchaHash() {
		return captchaHash;
	}

	public void setCaptchaHash(String captchaHash) {
		this.captchaHash = captchaHash;
	}

	public String getCaptchaImage() {
		return captchaImage;
	}

	public void setCaptchaImage(String captchaImage) {
		this.captchaImage = captchaImage;
	}

	public String getRandomString() {
		return randomString;
	}

	public void setRandomString(String randomString) {
		this.randomString = randomString;
	}

	@Override
	public String toString() {
		return "SignupRequest [name=" + name + ", email=" + email + ", mobile=" + mobile + ", roles=" + roles
				+ ", password=" + password + ", captchaHash=" + captchaHash + ", captchaImage=" + captchaImage
				+ ", randomString=" + randomString + "]";
	}
}
