package com.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.service.interfaces.LeaderboardService;

@RestController
@CrossOrigin("*")
@RequestMapping("/leaderboard")
public class LeaderboardController {
	
	@Autowired
	private LeaderboardService leaderboardService;
	
	@GetMapping("/get")
	public ResponseEntity<?> getScoreBasedOnUserName(){
		return leaderboardService.getScoreBasedOnUserName();
	}
	
	@GetMapping("/get/{challengeListId}")
	public ResponseEntity<?> getScoreBasedOnChallengeListIdAndUserName(@PathVariable String challengeListId){
		return leaderboardService.getScoreBasedOnChallengeListIdAndUserName(challengeListId);
	}
	
}
