package com.quiz.entity;

public class JwtResponse {
	
//	private String username;
//	private String type = "Bearer";
//	private String id;
//	private List<String> roles;
	private String email;
	private String token;

	public JwtResponse(String accessToken,String email) {
		this.token=accessToken;
		this.email=email;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
