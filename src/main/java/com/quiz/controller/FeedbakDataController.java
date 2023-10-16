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
import com.quiz.entity.FeedbackData;
import com.quiz.response.GlobalResponse;
import com.quiz.service.FeedbackDataService;

@RestController
public class FeedbakDataController {
	
	@Autowired
	private FeedbackDataService feedbackDataService;
	
	@PostMapping("/feedbackdata/add")
	public GlobalResponse addFeedbackQuestion(@RequestBody FeedbackData feedbackData) {
		return feedbackDataService.addFeedbackQuestion(feedbackData);
	}
	@GetMapping("/feedbackdata/get")
	public List<FeedbackData> getAllFeedbackData(){
		return feedbackDataService.getAllFeedbackData();
	}
	@GetMapping("/feedbackdata/get/{id}")
	public FeedbackData getFeedbackDataById(@PathVariable String id) {
		return feedbackDataService.getFeedbackDataById(id);
	}
	@DeleteMapping("/feedbackdata/delete/{id}")
	public GlobalResponse deleteFeedbackdataById(@PathVariable String id) {
		return feedbackDataService.deleteFeedbackdataById(id);
	}
	@PutMapping("/feedbackdata/update")
	public GlobalResponse updateFeedbackData(@RequestBody FeedbackData feedbackData ) {
		return feedbackDataService.updateFeedbackData(feedbackData);
	}
}
