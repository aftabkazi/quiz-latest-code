package com.quiz.service;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoDBChangeListener {
	
    private final MongoTemplate mongoTemplate;
    
    private final LeaderboardServiceImpl leaderboardServiceImpl;
    
    @Autowired
    public MongoDBChangeListener(MongoTemplate mongoTemplate, LeaderboardServiceImpl leaderboardServiceImpl) {
        this.mongoTemplate = mongoTemplate;
        this.leaderboardServiceImpl = leaderboardServiceImpl;
    }

	
}
