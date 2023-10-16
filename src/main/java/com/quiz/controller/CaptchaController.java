package com.quiz.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.entity.CaptchaResponse;
import com.quiz.response.GlobalResponse;
import com.quiz.service.interfaces.CaptchaService;

@RestController
@CrossOrigin("*")
public class CaptchaController {

	@Autowired
	private CaptchaService captchaService;
	
	@GetMapping("/generateCaptcha")
	public CaptchaResponse getCaptcha(HttpServletRequest request) {
		return captchaService.generateCaptch(request.getRemoteAddr());
	}

	@PostMapping("/validateCaptcha")
	public ResponseEntity<GlobalResponse> validateCaptchaAPI(HttpServletRequest request,
			@RequestBody CaptchaResponse validateCaptcha) {
		return captchaService.validateCaptchaAPI(request.getRemoteAddr(), validateCaptcha);
	}
}
