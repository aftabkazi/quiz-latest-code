package com.quiz.entity;

public class Flag {

	private String flag;
	private Boolean isCaseSensitive;
	
	public Flag() {
		super();
	}

	public Flag(String flag, Boolean isCaseSensitive) {
		super();
		this.flag = flag;
		this.isCaseSensitive = isCaseSensitive;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Boolean getIsCaseSensitive() {
		return isCaseSensitive;
	}

	public void setIsCaseSensitive(Boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
	}	
}
