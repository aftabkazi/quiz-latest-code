package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.quiz.entity.ChallengeList;

@Repository
public interface ChallengeListRepository extends MongoRepository<ChallengeList,String>{
	
	ChallengeList findByChallengeListId(String id);
	
}
