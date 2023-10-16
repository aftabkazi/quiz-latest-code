package com.quiz.entity;


public class UserResponseData {
	
	private String questionId;
	private String markedOption;
	
	public UserResponseData() {
		super();
	}

	public UserResponseData(String questionId, String markedOption) {
		super();
		this.questionId = questionId;
		this.markedOption = markedOption;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getMarkedOption() {
		return markedOption;
	}

	public void setMarkedOption(String markedOption) {
		this.markedOption = markedOption;
	}
}