package com.quiz.entity;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "challengeList")
public class ChallengeList {

	@Id
	private String challengeListId;
	private String challengeListName;
	private String startDate;
	private String endDate;
	private Boolean publish;
	private List<String> challenges;
	private List<String> emailList;
	private String visibility;

	@CreatedDate
	private String createdDate;

	@LastModifiedDate
	private String updatedDate;

	public ChallengeList() {
		super();
	}

	public ChallengeList(String challengeListId, String challengeListName,String visibility, String startDate, String endDate,
			Boolean publish, List<String> challenges, List<String> emailList, String createdDate, String updatedDate) {
		super();
		this.challengeListId = challengeListId;
		this.challengeListName = challengeListName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.publish = publish;
		this.challenges = challenges;
		this.emailList = emailList;
		this.visibility = visibility;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
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

	public String getChallengeListName() {
		return challengeListName;
	}

	public void setChallengeListName(String challengeListName) {
		this.challengeListName = challengeListName;
	}

	public String getChallengeListId() {
		return challengeListId;
	}

	public void setChallengeListId(String challengeListId) {
		this.challengeListId = challengeListId;
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

	public List<String> getChallenges() {
		return challenges;
	}

	public void setChallenges(List<String> challenges) {
		this.challenges = challenges;
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

	@Override
	public String toString() {
		return "ChallengeList [challengeListId=" + challengeListId + ", challengeListName=" + challengeListName
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", publish=" + publish + ", challenges="
				+ challenges + ", emailList=" + emailList + ", visibility=" + visibility + ", createdDate="
				+ createdDate + ", updatedDate=" + updatedDate + "]";
	}
}

