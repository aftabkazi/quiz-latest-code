package com.quiz.exception.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.quiz.response.GlobalResponse;



@ControllerAdvice
public class UserExceptionController {
	
	@ExceptionHandler(value=UserNotFoundException.class)
	public ResponseEntity<Object> exception(UserNotFoundException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("User not found", "Invalid email Id");
		
		return new ResponseEntity<Object>(new GlobalResponse("Error occured due to following reasons",errorMessages,false),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value=TokenExpiryException.class)
	public ResponseEntity<Object> exception(TokenExpiryException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("UnAuthorized"," Token has expired, Please login again" );
		
		return new ResponseEntity<Object>(new GlobalResponse("Error occured due to following reasons",errorMessages,false),HttpStatus.UNAUTHORIZED);
	}
	
}