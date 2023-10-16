package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.quiz.entity.Feedback;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback,String> {
	
	Feedback findByFeedbackId(String id);

}
