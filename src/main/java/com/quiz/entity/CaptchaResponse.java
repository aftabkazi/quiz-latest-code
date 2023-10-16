package com.quiz.entity;

public class CaptchaResponse {
	
	private String captchaHash;
	private String captchaImage;
	private String randomString;

	public CaptchaResponse() {
		super();
	}

	public CaptchaResponse(String captchaHash, String captchaImage,String randomString) {
		this.captchaHash = captchaHash;
		this.captchaImage = captchaImage;
		this.randomString=randomString;
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
}
