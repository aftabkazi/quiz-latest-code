package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.quiz.entity.FeedbackData;

@Repository
public interface FeedbackDataRepository extends MongoRepository<FeedbackData,String> {
	
	FeedbackData findByFeedbackDataId(String id );
}
