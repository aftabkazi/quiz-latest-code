package com.quiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.entity.Feedback;
import com.quiz.repository.FeedbackRepository;
import com.quiz.response.GlobalResponse;

@Service
public class FeedbackService {
	
	@Autowired
	private FeedbackRepository feedbackRepository;

	public GlobalResponse addFeedback(Feedback feedback) {
		feedbackRepository.save(feedback);
		return new GlobalResponse(feedback.getFeedbackId(),"Feedback created successfull",true);
	}

	public List<Feedback> getallFeedback() {
		return feedbackRepository.findAll();
	}

	public Feedback getFeedbackById(String id) {
		return feedbackRepository.findByFeedbackId(id);
	}

	public GlobalResponse updateFeedback(Feedback feedback) {
		feedbackRepository.save(feedback);
		return new GlobalResponse(feedback.getFeedbackId(),"Feedback updated",true);
	}

	public GlobalResponse deleteFeedback(String id) {
		feedbackRepository.deleteById(id);
		return new GlobalResponse(null,"feedback deleted",true);
	}
}
