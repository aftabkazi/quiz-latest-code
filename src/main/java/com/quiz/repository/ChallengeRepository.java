package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.quiz.entity.Challenge;

@Repository
public interface ChallengeRepository extends MongoRepository<Challenge, String>{
	
	Challenge findByChallengeId(String id);
	
}
