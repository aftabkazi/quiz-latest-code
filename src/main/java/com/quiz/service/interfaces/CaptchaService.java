package com.quiz.service.interfaces;

import org.springframework.http.ResponseEntity;

import com.quiz.entity.CaptchaResponse;
import com.quiz.response.GlobalResponse;


public interface CaptchaService {
	
	public CaptchaResponse generateCaptch(String IpAddress);
	
	public ResponseEntity<GlobalResponse> validateCaptchaAPI(String IpAddress,CaptchaResponse validateCaptcha);
}
