package com.quiz.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quiz.entity.Leaderboard;

public interface LeaderboardRepository extends MongoRepository<Leaderboard, String> {
	
	List<Leaderboard> findByChallengeListId(String id);

}
