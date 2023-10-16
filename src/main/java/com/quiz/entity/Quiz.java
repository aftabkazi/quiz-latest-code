package com.quiz.entity;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection = "Quiz")
public class Quiz {

	@Id
	private String quizId;
	private String quizTitle;
	private String shortCode;
	private String description;
	private Integer noOfAttempts;
	private Integer totalDuration;
	private Integer quizSize;
	private Boolean shuffle;
	private Boolean publish;
	private Boolean showScore;
	private String quizType;
	private List<String> emailList;
	private String startDate;
	private String endDate;
	private Integer passPercentage;
	private String certFileName;
	private List<AddaptiveData> addaptiveData;

	@CreatedDate
	private String createdDate;

	@LastModifiedDate
	private String updatedDate;

	public Quiz() {
		super();
	}

	public Quiz(String quizId, String quizTitle, String shortCode, String description, Integer noOfAttempts,
			Integer totalDuration, Integer quizSize,String quizType,String certFileName, Boolean shuffle, Boolean publish,
			Boolean showScore, String startDate, String endDate, Integer passPercentage,
			List<AddaptiveData> addaptiveData, List<String>emailList,String createdDate, String updatedDate) {
		super();
		this.quizId = quizId;
		this.quizTitle = quizTitle;
		this.shortCode = shortCode;
		this.description = description;
		this.certFileName = certFileName;
		this.noOfAttempts = noOfAttempts;
		this.totalDuration = totalDuration;
		this.quizSize = quizSize;
		this.emailList=emailList;
		this.shuffle = shuffle;
		this.publish = publish;
		this.showScore = showScore;
		this.startDate = startDate;
		this.quizType = quizType;
		this.endDate = endDate;
		this.passPercentage = passPercentage;
		this.addaptiveData = addaptiveData;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public String getQuizId() {
		return quizId;
	}

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoOfAttempts() {
		return noOfAttempts;
	}

	public void setNoOfAttempts(Integer noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}

	public List<String> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}

	public Integer getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}

	public Integer getQuizSize() {
		return quizSize;
	}

	public void setQuizSize(Integer quizSize) {
		this.quizSize = quizSize;
	}

	public Boolean getShuffle() {
		return shuffle;
	}

	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}

	public void setShuffle(Boolean shuffle) {
		this.shuffle = shuffle;
	}

	public Boolean getShowScore() {
		return showScore;
	}

	public void setShowScore(Boolean showScore) {
		this.showScore = showScore;
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

	public Integer getPassPercentage() {
		return passPercentage;
	}

	public void setPassPercentage(Integer passPercentage) {
		this.passPercentage = passPercentage;
	}

	public List<com.quiz.entity.AddaptiveData> getAddaptiveData() {
		return addaptiveData;
	}

	public void setAddaptiveData(List<AddaptiveData> addaptiveData) {
		this.addaptiveData = addaptiveData;
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

	public String getCertFileName() {
		return certFileName;
	}

	public void setCertFileName(String certFileName) {
		this.certFileName = certFileName;
	}

	public String getQuizType() {
		return quizType;
	}

	public void setQuizType(String quizType) {
		this.quizType = quizType;
	}

	@Override
	public String toString() {
		return "Quiz [quizId=" + quizId + ", quizTitle=" + quizTitle + ", shortCode=" + shortCode + ", description="
				+ description + ", noOfAttempts=" + noOfAttempts + ", totalDuration=" + totalDuration + ", quizSize="
				+ quizSize + ", shuffle=" + shuffle + ", publish=" + publish + ", showScore=" + showScore
				+ ", quizType=" + quizType + ", emailList=" + emailList + ", startDate=" + startDate + ", endDate="
				+ endDate + ", passPercentage=" + passPercentage + ", certFileName=" + certFileName + ", addaptiveData="
				+ addaptiveData + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}
	

}
