package com.quiz.entity;

import java.util.List;

import javax.persistence.Entity;
//import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "ChallengeResponse")
public class ChallengeResponse {

	@Id
	private String challengeResponseId;
	private String challengeId;
	private String challengeListId;
	private String challengeListName;
	private String selectedChallenge;
	private String email;
	private String username;
	private List<String> hints;
	private String flag;
	private Integer value;
	private Integer scoredValue;
//	@CreatedDate
	private String createdDate;

	public ChallengeResponse() {
		super();
	}

	public ChallengeResponse(String challengeResponseId, String challengeId, String challengeListId,
			String challengeListName, String selectedChallenge, String email, String username, List<String> hints,
			String flag, Integer value, Integer scoredValue, String createdDate) {
		super();
		this.challengeResponseId = challengeResponseId;
		this.challengeId = challengeId;
		this.challengeListId = challengeListId;
		this.challengeListName = challengeListName;
		this.selectedChallenge = selectedChallenge;
		this.email = email;
		this.username = username;
		this.hints = hints;
		this.flag = flag;
		this.value = value;
		this.scoredValue = scoredValue;
		this.createdDate = createdDate;
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

	public String getChallengeResponseId() {
		return challengeResponseId;
	}

	public void setChallengeResponseId(String challengeResponseId) {
		this.challengeResponseId = challengeResponseId;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public String getSelectedChallenge() {
		return selectedChallenge;
	}

	public void setSelectedChallenge(String selectedChallenge) {
		this.selectedChallenge = selectedChallenge;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getScoredValue() {
		return scoredValue;
	}

	public void setScoredValue(Integer scoredValue) {
		this.scoredValue = scoredValue;
	}

	public List<String> getHints() {
		return hints;
	}

	public void setHints(List<String> hints) {
		this.hints = hints;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
