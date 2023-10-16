package com.quiz.entity;

public class IVPDetails {
	
	private String email;
	private String name;
	private String unique_id;
	
	public IVPDetails() {
		super();
	}

	public IVPDetails(String email, String name,String unique_id) {
		super();
		this.email = email;
		this.name = name;
		this.unique_id=unique_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	@Override
	public String toString() {
		return "IVPDetails [email=" + email + ", name=" + name + ", unique_id=" + unique_id + "]";
	}

	
}
