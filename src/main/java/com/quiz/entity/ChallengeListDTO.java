package com.quiz.entity;

import java.util.List;

public class ChallengeListDTO {
	
	private String challengeListId;
	private String challengeListName;
	private String startDate;
	private String endDate;
	private Boolean publish;
	List<Challenge> challenges;
	private List<String> emailList;
	private String createdDate;
	private String updatedDate;
	private String visibility;
	
	public ChallengeListDTO() {
		super();
	}
	
	public ChallengeListDTO(String challengeListId, String challengeListName, String startDate, String endDate,
			Boolean publish, List<Challenge> challenges,String visibility, List<String> emailList, String createdDate,
			String updatedDate) {
		super();
		this.challengeListId = challengeListId;
		this.challengeListName = challengeListName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.publish = publish;
		this.challenges = challenges;
		this.visibility = visibility;
		this.emailList = emailList;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public String getChallengeListId() {
		return challengeListId;
	}

	public void setChallengeListId(String challengeListId) {
		this.challengeListId = challengeListId;
	}

	public String getChallengeListName() {
		return challengeListName;
	}

	public void setChallengeListName(String challengeListName) {
		this.challengeListName = challengeListName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

	public List<Challenge> getChallenges() {
		return challenges;
	}
	
	public void setChallenges(List<Challenge> challenges) {
		this.challenges = challenges;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<String> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	
	
	
}

