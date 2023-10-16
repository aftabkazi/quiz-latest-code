package com.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.entity.Feedback;
import com.quiz.response.GlobalResponse;
import com.quiz.service.FeedbackService;

@RestController
public class FeedbackController {
	
	@Autowired
	private FeedbackService feedbackService;

	@PostMapping("/feedback/add")
	public GlobalResponse addFeedback(@RequestBody Feedback feedback) {
		return feedbackService.addFeedback(feedback);
	}
	@GetMapping("/feedback/get")
	public List<Feedback> getallFeedback(){
		return feedbackService.getallFeedback();
	}
	@GetMapping("/feedback/get/{id}")
	public Feedback getFeedbackById(@PathVariable String id) {
		return feedbackService.getFeedbackById(id);
	}
	@PutMapping("/feedback/update")
	public GlobalResponse updateFeedback(@RequestBody Feedback feedback) {
		return feedbackService.updateFeedback(feedback);
	}
	@DeleteMapping("/feedback/delete/{id}")
	public GlobalResponse deleteFeedback(@PathVariable String id) {
		return feedbackService.deleteFeedback(id);
	}
}
