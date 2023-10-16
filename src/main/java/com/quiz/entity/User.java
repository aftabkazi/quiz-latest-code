package com.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.HashSet;
import java.util.Set;

@Entity
@Document(collection = "User")
public class User {

	@Id
	private String id;
	private String username;
	private String email;
	private String mobile;
	private String password;
	private String captchaHash;
	private String captchaImage;
	private String randomString;
	private String mode;
	private String jwtToken;
	private String unique_id;

	@DBRef
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String id, String username, String email, String mobile, String password, String captchaHash,
			String captchaImage, String randomString,String mode, String jwtToken,String unique_id,Set<Role> roles) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.mobile = mobile;
		this.password = password;
		this.captchaHash = captchaHash;
		this.captchaImage = captchaImage;
		this.randomString = randomString;
		this.mode=mode;
		this.unique_id=unique_id;
		this.roles = roles;
		this.jwtToken=jwtToken;
	}
	
	public User(String username, String email, String mobile, String password) {
		super();
		this.username = username;
		this.email = email;
		this.mobile = mobile;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", mobile=" + mobile + ", password="
				+ password + ", captchaHash=" + captchaHash + ", captchaImage=" + captchaImage + ", randomString="
				+ randomString + ", mode=" + mode + ", jwtToken=" + jwtToken + ", unique_id=" + unique_id + ", roles="
				+ roles + "]";
	}
}
