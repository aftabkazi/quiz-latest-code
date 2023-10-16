package com.quiz.response;

import java.util.Map;

public class GlobalResponse {
	
	private String Id;
	private String msg;
	private Map<String, String> errors;
	private Boolean status;
	private int count;
	
	public GlobalResponse() {
		super();
	}
	
	public GlobalResponse(int count) {
		super();
		this.count = count;
	}
	
	public GlobalResponse(String msg) {
		this.msg = msg;
	}
	
	public GlobalResponse(String msg, Map<String, String> errors, Boolean status) {
		this.msg = msg;
		this.errors = errors;
		this.status = status;
	}
	
	public GlobalResponse(String id, String msg, Boolean status) {
		super();
		Id = id;
		this.msg = msg;
		this.status = status;
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Boolean getStatus() {
		return status;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}
}
