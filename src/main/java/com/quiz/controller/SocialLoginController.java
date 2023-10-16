package com.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.IVPDetails;
import com.quiz.response.GlobalResponse;
import com.quiz.service.SocialLoginService;


@RestController
@CrossOrigin("*")
@RequestMapping("/social")
public class SocialLoginController {
	
	@Autowired
	private SocialLoginService socialLoginService;
	
	@PostMapping("/google/login")
	public ResponseEntity<GlobalResponse> saveUserEnrollCourseDetails(@RequestHeader("Authorization") String token){
		return socialLoginService.googleLogin(token);
		
	}
	
	@PostMapping("/IVP/login")
	public ResponseEntity<GlobalResponse> IVPLogin(@RequestBody IVPDetails iVPDetails){
		return socialLoginService.IVPLogin(iVPDetails);
	}
	
}
