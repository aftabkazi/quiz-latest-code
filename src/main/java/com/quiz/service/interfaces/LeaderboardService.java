package com.quiz.service.interfaces;

import org.springframework.http.ResponseEntity;

public interface LeaderboardService {

	ResponseEntity<?> getScoreBasedOnUserName();

	ResponseEntity<?> getScoreBasedOnChallengeListIdAndUserName(String challengeListId);

}
