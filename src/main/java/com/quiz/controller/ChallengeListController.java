package com.quiz.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.entity.ChallengeList;
import com.quiz.entity.ChallengeListDTO;
import com.quiz.response.GlobalResponse;
import com.quiz.service.interfaces.ChallengeListService;

@RestController
@CrossOrigin("*")
@RequestMapping("/challengelist")
public class ChallengeListController {

	@Value("${extern.resourse.ipaddress}")
	private String basePath;

	@Value("${extern.resources.Dir}")
	private String path;

	@Autowired
	private ChallengeListService challengeListService;

	@PostMapping("/add")
	public ResponseEntity<GlobalResponse> addChallengeList(@RequestBody ChallengeList challengeList) {
		return challengeListService.addChallengeList(challengeList);
	}

	@PutMapping("/update")
	public ResponseEntity<GlobalResponse> updateChallengeList(@RequestBody ChallengeList challengeList) {
		return challengeListService.updateChallengeList(challengeList);
	}

	@PutMapping("/update/emails")
	public ResponseEntity<GlobalResponse> updateEmailsforChallengesList(@RequestBody ChallengeList challengeList) {
		return challengeListService.updateEmailsforChallengesList(challengeList);
	}

	@GetMapping("/get")
	public ResponseEntity<List<ChallengeListDTO>> getAllChallengesList() {
		return challengeListService.getAllChallengesList();
	}

	@GetMapping("/get/{challengeListId}")
	public ResponseEntity<List<ChallengeListDTO>> getChallengeListById(@PathVariable String challengeListId) {
		return challengeListService.getChallengeListById(challengeListId);
	}

	@GetMapping("/get/whitelistedemails")
	public ResponseEntity<List<ChallengeListDTO>> getChallengeByWhiteListedEmails() {
		return challengeListService.getChallengeByWhiteListedEmails();
	}

	@DeleteMapping("/delete/{challengeListId}")
	public ResponseEntity<GlobalResponse> deleteChallengeById(@PathVariable String challengeListId) {
		return challengeListService.deleteChallengeById(challengeListId);
	}
}
