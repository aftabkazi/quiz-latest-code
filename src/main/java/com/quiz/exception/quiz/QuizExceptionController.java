package com.quiz.exception.quiz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.quiz.response.GlobalResponse;


@ControllerAdvice
public class QuizExceptionController {
	
	@ExceptionHandler(value=QuizNotFoundException.class)
	public ResponseEntity<Object> exception(QuizNotFoundException e){

		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Quiz not found","Invalid quiz Id" );

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new GlobalResponse("Error occured due to following reasons", errorMessages, false));	
	}
	
	@ExceptionHandler(value=QuizSizeException.class)
	public ResponseEntity<Object> exception(QuizSizeException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Quiz size","Unable to attempt quiz as number of questions are less then quiz size" );
		
		return new ResponseEntity<Object>(new GlobalResponse("Error occured due to following reasons",errorMessages,false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=AttemptsException.class)
	public ResponseEntity<Object> exception(AttemptsException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Attempts","Maximum number of attempts are completed");
		
		return new ResponseEntity<Object>(new GlobalResponse("Error occured due to following reasons",errorMessages ,false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=QuizPassedException.class)
	public ResponseEntity<Object> exception(QuizPassedException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Quiz error","You have already passed the quiz");
		
		return new ResponseEntity<Object>(new GlobalResponse("Error occured due to following reasons",errorMessages ,false),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=QuizLevelException.class)
	public ResponseEntity<Object> exception(QuizLevelException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Quiz size","Unable to attempt quiz as the addaptive level is not matching with question level" );
		
		return new ResponseEntity<Object>(new GlobalResponse("Error occured due to following reasons",errorMessages,false),HttpStatus.BAD_REQUEST);
	}
	
	
	
	
}
