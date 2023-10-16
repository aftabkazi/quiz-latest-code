package com.quiz.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.CaptchaResponse;
import com.quiz.entity.ForgotPassword;
import com.quiz.entity.User;
import com.quiz.response.GlobalResponse;
import com.quiz.service.ForgotPasswordService;
import com.quiz.service.interfaces.CaptchaService;

@RestController
@CrossOrigin("*")
public class ForgotPasswordController {

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	private CaptchaService captchaService;

	@PostMapping("/forgotpass")
	public ResponseEntity<?> sendLinkOnEmail(HttpServletRequest request, @RequestBody ForgotPassword forgotPassword) {

		CaptchaResponse captchaResponse = new CaptchaResponse(forgotPassword.getCaptchaHash(),
				forgotPassword.getCaptchaImage(), forgotPassword.getRandomString());

		ResponseEntity<GlobalResponse> globalResponse = captchaService
				.validateCaptchaAPI(request.getRemoteAddr(), captchaResponse);

		if (globalResponse.getBody().getStatus().equals(false)) {
			Map<String, String> errorMessages = new HashMap<>();

			errorMessages.put("Invalid Request", "Invalid captcha");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("login failed due" + "to following reasons ", errorMessages, false));
		} else {
			return forgotPasswordService.sendLinkOnEmail(forgotPassword);
		}
	}

	@PostMapping("/reset/{token}")
	public ResponseEntity<?> resetPassword(HttpServletRequest request,@RequestBody User user, @PathVariable String token) {
		
		CaptchaResponse captchaResponse = new CaptchaResponse(user.getCaptchaHash(),
				user.getCaptchaImage(), user.getRandomString());

		ResponseEntity<GlobalResponse> globalResponse = captchaService
				.validateCaptchaAPI(request.getRemoteAddr(), captchaResponse);

		if (globalResponse.getBody().getStatus().equals(false)) {
			Map<String, String> errorMessages = new HashMap<>();

			errorMessages.put("Invalid Request", "Invalid captcha");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("login failed due" + "to following reasons ", errorMessages, false));
		} else {
			return forgotPasswordService.resetPassword(user, token);
		}
	}
}
