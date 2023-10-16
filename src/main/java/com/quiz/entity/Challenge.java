package com.quiz.entity;

import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "Challenge")
public class Challenge {

	@Id
	private String challengeId;
	private String challengeType;
	private String challengeName;
	private String category;
	private String description;
	private Integer value; 
	private Map<String,Flag> flags; // correct answers
	private List<String> files;
	private String link;
	private String status;
	private Map<String, Hint> hints;
	private String state; // hidden/ visible
	private Integer noOfAttempts;
	
	@CreatedDate
	private String createdDate;

	@LastModifiedDate
	private String updatedDate;
	
	
	public Challenge() {
		super();
	}

	public Challenge(String challengeId, String challengeType, String challengeName, String category,
			String description, Integer value, Map<String, Flag> flags, List<String> files, Map<String, Hint> hints,
			String link,String state, Integer noOfAttempts,String status, String createdDate, String updatedDate) {
		super();
		this.challengeId = challengeId;
		this.challengeType = challengeType;
		this.challengeName = challengeName;
		this.category = category;
		this.description = description;
		this.status = status;
		this.value = value;
		this.flags = flags;
		this.files = files;
		this.hints = hints;
		this.link = link;
		this.state = state;
		this.noOfAttempts = noOfAttempts;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public String getChallengeType() {
		return challengeType;
	}

	public void setChallengeType(String challengeType) {
		this.challengeType = challengeType;
	}

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Map<String, Flag> getFlags() {
		return flags;
	}

	public void setFlags(Map<String, Flag> flags) {
		this.flags = flags;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public Map<String, Hint> getHints() {
		return hints;
	}

	public void setHints(Map<String, Hint> hints) {
		this.hints = hints;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getNoOfAttempts() {
		return noOfAttempts;
	}

	public void setNoOfAttempts(Integer noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "Challenge [challengeId=" + challengeId + ", challengeType=" + challengeType + ", challengeName="
				+ challengeName + ", category=" + category + ", description=" + description + ", value=" + value
				+ ", flags=" + flags + ", files=" + files + ", link=" + link + ", status=" + status + ", hints=" + hints
				+ ", state=" + state + ", noOfAttempts=" + noOfAttempts + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + "]";
	}
}
