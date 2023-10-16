package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quiz.entity.ForgotPassword;


public interface ForgotPasswordRepository extends  MongoRepository<ForgotPassword,String>{
	
	ForgotPassword findByEncryptEmail(String encryptEmail);

	ForgotPassword findByEmail(String email);
}
