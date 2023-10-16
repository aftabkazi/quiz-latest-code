package com.quiz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.ChallengeResponse;
import com.quiz.response.GlobalResponse;
import com.quiz.service.interfaces.ChallengeResponseService;

@RestController
@CrossOrigin("*")
@RequestMapping("/challenge-response")
public class ChallengeResponseController {

	
	@Autowired
	private ChallengeResponseService challengeResponseService;
	
	@PostMapping("/attend")
	public ResponseEntity<GlobalResponse> attendChallenge (@RequestBody ChallengeResponse challengeResponse){
		return challengeResponseService.attendChallenge(challengeResponse);
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<ChallengeResponse>> getAllChallengesResponse(){
		return challengeResponseService.getAllChallengesResponse();
	}
	
	@GetMapping("/get/email")
	public ResponseEntity<List<ChallengeResponse>> getAllChallengeResponseByEmail(){
		return challengeResponseService.getAllChallengeResponseByEmail();
	}
	
	@GetMapping("/get/email/{challengeId}")
	public ResponseEntity<List<ChallengeResponse>> getOneChallengeResponseByEmail(@PathVariable String challengeId){
		return challengeResponseService.getOneChallengeResponseByEmail(challengeId);
	}
	
	@GetMapping("/get/{challengeResponseId}")
	public ResponseEntity<ChallengeResponse> getChallengeResponseById(@PathVariable String challengeResponseId){
		return challengeResponseService.getChallengeResponseById(challengeResponseId);
	}
	
	@DeleteMapping("/delete/{challengeResponseId}")
	public ResponseEntity<GlobalResponse> deleteChallengeResponseById(@PathVariable String challengeResponseId){
		return challengeResponseService.deleteChallengeResponseById(challengeResponseId);
	}
	
	
}
