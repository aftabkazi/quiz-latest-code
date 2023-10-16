package com.quiz.entity;

import java.util.List;
import javax.persistence.Entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection="QuizResponse")
public class UserResponse {
	
	@Id
	private String userResponseId;
	private String quizId;
	private String selectedQuiz;
	private String email;
	private String username;
	private List<com.quiz.entity.UserResponseData> QuizData;
	private Integer totalQuestions;
	private Integer unattempted;
	private Integer totalMarks;
	private Integer totalScore;
	private Integer attempted;
	private Integer correct;
	private Integer wrong;
	private Integer percentage;
	private String status;
	private String certFileName;
	
	@CreatedDate
	private String createdDate;  

	public Integer getPercentage() {
		return percentage;
	}
	
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}
	
	public UserResponse() {
		super();
	}

	public UserResponse(String userResponseId, String quizId, String selectedQuiz, String email,String username,
			List<UserResponseData> quizData, Integer totalQuestions, Integer unattempted, Integer totalMarks,
			Integer totalScore, Integer attempted, Integer correct, Integer wrong, Integer percentage, String status,
			String certFileName, String createdDate) {
		super();
		this.userResponseId = userResponseId;
		this.quizId = quizId;
		this.selectedQuiz = selectedQuiz;
		this.email = email;
		this.username=username;
		QuizData = quizData;
		this.totalQuestions = totalQuestions;
		this.unattempted = unattempted;
		this.totalMarks = totalMarks;
		this.totalScore = totalScore;
		this.attempted = attempted;
		this.correct = correct;
		this.wrong = wrong;
		this.percentage = percentage;
		this.status = status;
		this.certFileName = certFileName;
		this.createdDate = createdDate;
	}

	public Integer getTotalQuestions() {
		return totalQuestions;
	}


	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}


	public String getUserResponseId() {
		return userResponseId;
	}


	public void setUserResponseId(String userResponseId) {
		this.userResponseId = userResponseId;
	}


	public String getQuizId() {
		return quizId;
	}


	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}
	
	public String getSelectedQuiz() {
		return selectedQuiz;
	}

	public void setSelectedQuiz(String selectedQuiz) {
		this.selectedQuiz = selectedQuiz;
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

	public List<com.quiz.entity.UserResponseData> getQuizData() {
		return QuizData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setQuizData(List<com.quiz.entity.UserResponseData> quizData) {
		QuizData = quizData;
	}


	public Integer getCorrect() {
		return correct;
	}


	public void setCorrect(Integer correct) {
		this.correct = correct;
	}


	public Integer getWrong() {
		return wrong;
	}


	public void setWrong(Integer wrong) {
		this.wrong = wrong;
	}


	public Integer getAttempted() {
		return attempted;
	}


	public void setAttempted(Integer attempted) {
		this.attempted = attempted;
	}


	public Integer getUnattempted() {
		return unattempted;
	}


	public void setUnattempted(Integer unattempted) {
		this.unattempted = unattempted;
	}

	public Integer getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCertFileName() {
		return certFileName;
	}

	public void setCertFileName(String certFileName) {
		this.certFileName = certFileName;
	}
	
}
