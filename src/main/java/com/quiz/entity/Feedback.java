package com.quiz.entity;

import javax.persistence.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection="Feedback")
public class Feedback {
	
	@Id
	private String feedbackId;
	private String title;
	private String description;
	private String type;
	
	public Feedback() {
		super();
	}

	public Feedback(String feedbackId, String title, String description, String type) {
		super();
		this.feedbackId = feedbackId;
		this.title = title;
		this.description = description;
		this.type = type;
	}

	public String getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(String feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
