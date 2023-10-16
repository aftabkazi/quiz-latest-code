package com.quiz.entity;

public class LoginRequest {

	private String username;
	private String id;
	private String email;
	private String password;
	private String captchaHash;
	private String captchaImage;
	private String randomString;

	public LoginRequest() {
		super();
	}

	public LoginRequest(String username, String id, String email, String password, String captchaHash,
			String captchaImage, String randomString) {
		super();
		this.username = username;
		this.id = id;
		this.email = email;
		this.password = password;
		this.captchaHash = captchaHash;
		this.captchaImage = captchaImage;
		this.randomString = randomString;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "LoginRequest [username=" + username + ", id=" + id + ", email=" + email + ", password=" + password
				+ ", captchaHash=" + captchaHash + ", captchaImage=" + captchaImage + ", randomString=" + randomString
				+ "]";
	}
	
	
}
