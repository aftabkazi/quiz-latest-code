package com.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ForgotPassword {

	@Id
	private String id;
	private String email;
	private String encryptEmail;
	private Integer endTime;
	private String flag;
	private String captchaHash;
	private String captchaImage;
	private String randomString;

	public ForgotPassword() {
		super();
	}

	public ForgotPassword(String id, String email, String encryptEmail, Integer endTime, String flag,
			String captchaHash, String captchaImage, String randomString) {
		super();
		this.id = id;
		this.email = email;
		this.encryptEmail = encryptEmail;
		this.endTime = endTime;
		this.flag = flag;
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

	public String getEncryptEmail() {
		return encryptEmail;
	}

	public void setEncryptEmail(String encryptEmail) {
		this.encryptEmail = encryptEmail;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
		return "ForgotPassword [id=" + id + ", email=" + email + ", encryptEmail=" + encryptEmail + ", endTime="
				+ endTime + ", flag=" + flag + ", captchaHash=" + captchaHash + ", captchaImage=" + captchaImage
				+ ", randomString=" + randomString + "]";
	}
}
