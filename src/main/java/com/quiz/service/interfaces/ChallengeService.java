package com.quiz.service.interfaces;

import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.quiz.entity.Challenge;
import com.quiz.response.GlobalResponse;

public interface ChallengeService {

	ResponseEntity<GlobalResponse> addChallenge(String challenge, List<MultipartFile> files) throws IOException;

	ResponseEntity<GlobalResponse> updateChallengeById(String challenge, List<MultipartFile> files)throws IOException;

	ResponseEntity<List<Challenge>> getAllChallenges();
	
	ResponseEntity<List<Challenge>> getAllChallangesByState();

	ResponseEntity<Challenge> getChallangeById(String challengeId);

	ResponseEntity<GlobalResponse> deleteChallengeById(String challengeId);

	ResponseEntity<GlobalResponse> deleteChallengeImage(String challengeId,String fileName);

	ResponseEntity<?> getHintsbasedonKey(String challengeId,String key);

}
