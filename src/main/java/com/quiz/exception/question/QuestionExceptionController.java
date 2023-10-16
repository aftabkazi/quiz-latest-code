package com.quiz.exception.question;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.quiz.response.GlobalResponse;

@ControllerAdvice
public class QuestionExceptionController {
	
	@ExceptionHandler(value=QuestionNotFoundException.class)
	public ResponseEntity<Object> exception(QuestionNotFoundException e){
		
		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Question not found","Invalid question Id" );
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new GlobalResponse("Error occured due to following reasons", errorMessages, false));
	
	}

}
