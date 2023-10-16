package com.quiz.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.entity.CaptchaResponse;
import com.quiz.entity.LoginRequest;
import com.quiz.entity.SignupRequest;
import com.quiz.response.GlobalResponse;
import com.quiz.service.UserService;
import com.quiz.service.interfaces.CaptchaService;

@CrossOrigin("*")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CaptchaService captchaService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
		
		// taking captcha details
		CaptchaResponse captchaResponse = new CaptchaResponse(loginRequest.getCaptchaHash(),
				loginRequest.getCaptchaImage(), loginRequest.getRandomString());

		// captcha method call
		ResponseEntity<GlobalResponse> globalResponse = captchaService.validateCaptchaAPI(request.getRemoteAddr(),
				captchaResponse);

		// condition to check captcha validation
		if (globalResponse.getBody().getStatus().equals(false)) {
			Map<String, String> errorMessages = new HashMap<>();

			errorMessages.put("Invalid Request", "Invalid captcha");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("login failed due" + "to following reasons ", errorMessages, false));
		} else {
			return userService.authenticateUser(loginRequest);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> RegisterUser(HttpServletRequest request, @RequestBody SignupRequest signUpRequest) {

		// Getting captcha details
		CaptchaResponse captchaResponse = new CaptchaResponse(signUpRequest.getCaptchaHash(),
				signUpRequest.getCaptchaImage(), signUpRequest.getRandomString());

		// captcha method call
		ResponseEntity<GlobalResponse> globalResponse = captchaService.validateCaptchaAPI(request.getRemoteAddr(),
				captchaResponse);

		// Condition to check captcha validation
		if (globalResponse.getBody().getStatus().equals(false)) {

			Map<String, String> errorMessages = new HashMap<>();
			errorMessages.put("Invalid Request", "Invalid captcha");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("registration failed due to following reasons ", errorMessages, false));
		} 
		else {
			return userService.RegisterUser(signUpRequest);
		}
	}
	
	@GetMapping("/signout")
	public ResponseEntity<?> logoutUser(){
		return userService.logoutUser();
	}
	
	@GetMapping("/listofemails")
	public ResponseEntity<?> getListOfAllEmails(){
		 return userService.getListOfAllEmails();
	}
}
