package com.quiz.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection="Questions")
public class SingleChoiceQuestion {
	
	@Id
	private String questionId;
	private String quizId;
	private String selectQuiz;
	private List<String> selectedQuizIds;
	private Map<String,String>QuizIdsAndNames;
	private String level;
	private String questionTitletext;
	private String type;
	private Integer count;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String option5;
	private String option6;
	private String correctOption;
	private String imageName;
	private Integer score;
	
	@CreatedDate
	private String createdDate; 
	
	@LastModifiedDate
	private String updatedDate;
	
	public SingleChoiceQuestion(Integer score) {
		super();
		this.score = score;
	}
	
	public SingleChoiceQuestion() {
		super();
	}

	public SingleChoiceQuestion(String questionId, String quizId, String selectQuiz, String level,
			String questionTitletext, String type,Integer count,String option1, String option2, String option3, String option4,
			String option5,List<String> selectedQuizIds, String option6, String correctOption, String imageName, Integer score, String createdDate,
			String updatedDate,Map<String,String>QuizIdsAndNames) {
		super();
		this.questionId = questionId;
		this.quizId = quizId;
		this.selectQuiz = selectQuiz;
		this.level = level;
		this.selectedQuizIds = selectedQuizIds;
		this.questionTitletext = questionTitletext;
		this.type = type;
		this.QuizIdsAndNames = QuizIdsAndNames;
		this.count = count;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.option5 = option5;
		this.option6 = option6;
		this.correctOption = correctOption;
		this.imageName = imageName;
		this.score = score;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuizId() {
		return quizId;
	}

	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

	public String getSelectQuiz() {
		return selectQuiz;
	}

	public void setSelectQuiz(String selectQuiz) {
		this.selectQuiz = selectQuiz;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getQuestionTitletext() {
		return questionTitletext;
	}

	public void setQuestionTitletext(String questionTitletext) {
		this.questionTitletext = questionTitletext;
	}

	public String getType() {
		return type;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getOption5() {
		return option5;
	}

	public void setOption5(String option5) {
		this.option5 = option5;
	}

	public String getOption6() {
		return option6;
	}

	public void setOption6(String option6) {
		this.option6 = option6;
	}

	public String getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(String correctOption) {
		this.correctOption = correctOption;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public List<String> getSelectedQuizIds() {
		return selectedQuizIds;
	}

	public void setSelectedQuizIds(List<String> selectedQuizIds) {
		this.selectedQuizIds = selectedQuizIds;
	}

	public Map<String, String> getQuizIdsAndNames() {
		return QuizIdsAndNames;
	}

	public void setQuizIdsAndNames(Map<String, String> quizIdsAndNames) {
		QuizIdsAndNames = quizIdsAndNames;
	}

	@Override
	public String toString() {
		return "SingleChoiceQuestion [questionId=" + questionId + ", quizId=" + quizId + ", selectQuiz=" + selectQuiz
				+ ", selectedQuizIds=" + selectedQuizIds + ", QuizIdsAndNames=" + QuizIdsAndNames + ", level=" + level
				+ ", questionTitletext=" + questionTitletext + ", type=" + type + ", count=" + count + ", option1="
				+ option1 + ", option2=" + option2 + ", option3=" + option3 + ", option4=" + option4 + ", option5="
				+ option5 + ", option6=" + option6 + ", correctOption=" + correctOption + ", imageName=" + imageName
				+ ", score=" + score + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}
}
