package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.quiz.entity.Quiz;

@Repository
public interface QuizRepository extends MongoRepository<Quiz,String>{
	
	Quiz findByQuizId(String id);
	
	Quiz findByQuizTitle(String quizTitle);
	
	Quiz findByShortCode(String shortCode);	
	
	Quiz findByPassPercentage(String id);
	
}
