package com.quiz.service.interfaces;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.quiz.entity.ChallengeList;
import com.quiz.entity.ChallengeListDTO;
import com.quiz.response.GlobalResponse;

public interface ChallengeListService {

	ResponseEntity<GlobalResponse> addChallengeList(ChallengeList challengeList);

	ResponseEntity<GlobalResponse> updateChallengeList(ChallengeList challengeList);

	ResponseEntity<List<ChallengeListDTO>> getAllChallengesList();
	
	ResponseEntity<List<ChallengeListDTO>> getChallengeListById(String challengeListId);

	ResponseEntity<GlobalResponse> deleteChallengeById(String challengeListId);

	ResponseEntity<GlobalResponse> updateEmailsforChallengesList(ChallengeList challengeList);

	ResponseEntity<List<ChallengeListDTO>> getChallengeByWhiteListedEmails();

}

