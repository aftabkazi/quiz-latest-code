package com.quiz.service.interfaces;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.quiz.entity.ChallengeResponse;
import com.quiz.response.GlobalResponse;

public interface ChallengeResponseService {

	ResponseEntity<GlobalResponse> attendChallenge(ChallengeResponse challengeResponse);

	ResponseEntity<ChallengeResponse> getChallengeResponseById(String challengeResponseId);

	ResponseEntity<List<ChallengeResponse>> getAllChallengesResponse();

	ResponseEntity<GlobalResponse> deleteChallengeResponseById(String challengeResponseId);

	ResponseEntity<List<ChallengeResponse>> getAllChallengeResponseByEmail();

	ResponseEntity<List<ChallengeResponse>> getOneChallengeResponseByEmail(String challengeId);

}
