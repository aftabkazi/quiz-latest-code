package com.quiz.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quiz.entity.ChallengeResponse;

public interface ChallengeResponseRepository extends MongoRepository<ChallengeResponse, String>{
	
	 int countByEmailAndChallengeId(String email,String challengeId);

	 ChallengeResponse findBychallengeResponseId(String challengeResponseId);
	 
	 List<ChallengeResponse> findByEmail(String email);

	List<ChallengeResponse> findByEmailAndChallengeId(String email, String challengeId);
}
