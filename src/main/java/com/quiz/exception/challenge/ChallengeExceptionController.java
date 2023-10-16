package com.quiz.exception.challenge;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.quiz.response.GlobalResponse;

@ControllerAdvice
public class ChallengeExceptionController {

	@ExceptionHandler(value=ChallengeNotFoundException.class)
	public ResponseEntity<Object> exception(ChallengeNotFoundException e) {

		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Not Found", "Invalid Id");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));

	}
	
	@ExceptionHandler(value=ChallengeListNotFoundException.class)
	public ResponseEntity<Object> exception(ChallengeListNotFoundException e) {

		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("Challenge List not found", "Invalid challenge list Id");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
	}
}
