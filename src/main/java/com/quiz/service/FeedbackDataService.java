package com.quiz.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quiz.entity.FeedbackData;
import com.quiz.repository.FeedbackDataRepository;
import com.quiz.response.GlobalResponse;

@Service
public class FeedbackDataService {

	@Autowired
	private FeedbackDataRepository feedbackDataRepository;
	
	public GlobalResponse addFeedbackQuestion(FeedbackData feedbackData) {
		feedbackDataRepository.save(feedbackData);
		return new GlobalResponse(feedbackData.getFeedbackDataId(),"Feedback question added sucessfully",true);
	}

	public List<FeedbackData> getAllFeedbackData() {
		return feedbackDataRepository.findAll();
	}
	
	public FeedbackData getFeedbackDataById(String id) {
		return feedbackDataRepository.findByFeedbackDataId(id);
	}

	public GlobalResponse deleteFeedbackdataById(String id) {
		feedbackDataRepository.deleteById(id);
		return new GlobalResponse(id,"feedback data deleted successfully",true);
	}

	public GlobalResponse updateFeedbackData(FeedbackData feedbackData) {
		feedbackDataRepository.save(feedbackData);
		return new GlobalResponse(feedbackData.getFeedbackDataId(),"Feedback data updated successfully",true);
	}
}