package com.quiz.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.quiz.entity.UserResponse;

@Repository
public interface UserResponseRepository extends MongoRepository<UserResponse,String> {
	
	 int countByEmailAndQuizId(String email, String quizId);
	 	
	 List<UserResponse> findByEmail(String email);

	 UserResponse findByUserResponseId(String id);
	 
}
