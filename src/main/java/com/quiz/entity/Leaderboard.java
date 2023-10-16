package com.quiz.entity;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "Leaderboard")
public class Leaderboard {
	
	@Id
	private String leaderBoardId;
	private String userId;
	private String challengeId;
	private String challengeListId;
	private String username;
	private String email;
	private String challengeName;
	private Integer score;
	private String timeStamp;
	private String visibility;
	
	public Leaderboard() {
		super();
	}

	public Leaderboard(String leaderBoardId, String userId, String username, String challengeId, Integer score,
			String timeStamp,String email,String challengeName,String challengeListId,String visibility) {
		super();
		this.leaderBoardId = leaderBoardId;
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.challengeId = challengeId;
		this.score = score;
		this.challengeListId = challengeListId;
		this.timeStamp = timeStamp;
		this.challengeName = challengeName;
		this.visibility = visibility;
	}

	public String getChallengeListId() {
		return challengeListId;
	}

	public void setChallengeListId(String challengeListId) {
		this.challengeListId = challengeListId;
	}

	public String getLeaderBoardId() {
		return leaderBoardId;
	}

	public void setLeaderBoardId(String leaderBoardId) {
		this.leaderBoardId = leaderBoardId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		return "Leaderboard [leaderBoardId=" + leaderBoardId + ", userId=" + userId + ", challengeId=" + challengeId
				+ ", challengeListId=" + challengeListId + ", username=" + username + ", challengeName=" + challengeName
				+ ", score=" + score + ", timeStamp=" + timeStamp + ", visibility=" + visibility + "]";
	}
	
}
