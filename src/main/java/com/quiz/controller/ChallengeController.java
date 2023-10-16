package com.quiz.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.quiz.entity.Challenge;
import com.quiz.response.GlobalResponse;
import com.quiz.service.interfaces.ChallengeService;

@RestController
@CrossOrigin("*")
@RequestMapping("/challenge")
public class ChallengeController {
	
	@Autowired
	private ChallengeService challengeService;
	
	@PostMapping("/add")
	public ResponseEntity<GlobalResponse> addChallenge(@RequestParam String challenge,@RequestParam(name = "files",required = false) List<MultipartFile> files)throws IOException{
		return challengeService.addChallenge(challenge,files);
	}
	
	@PutMapping("/update")
	public ResponseEntity<GlobalResponse> updateChallengeById(@RequestParam String challenge,@RequestParam(name = "files",required = false) List<MultipartFile> files)throws IOException{
		return challengeService.updateChallengeById(challenge,files);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<Challenge>> getAllChallenges(){
		return challengeService.getAllChallenges();
	}
	
	@GetMapping("/get-by-visibility")
	public ResponseEntity<List<Challenge>> getAllChallangesByState(){
		return challengeService.getAllChallangesByState();
	}
	
	@GetMapping("/get/{challengeId}")
	public ResponseEntity<Challenge> getChallangeById(@PathVariable String challengeId){
		return challengeService.getChallangeById(challengeId);
	}
	
	@DeleteMapping("/delete/{challengeId}")
	public ResponseEntity<GlobalResponse> deleteChallengeById(@PathVariable String challengeId){  
		return challengeService.deleteChallengeById(challengeId);
	}
	
	@DeleteMapping("/delete/challenge-image/{challengeId}/{fileName}")
	public ResponseEntity<GlobalResponse> deleteChallengeImage(@PathVariable String challengeId,@PathVariable String fileName){
		return challengeService.deleteChallengeImage(challengeId,fileName);
	}
	
	@GetMapping("/get/hint/{challengeId}/{key}")
	public ResponseEntity<?> getHintsbasedonKey(@PathVariable String challengeId,@PathVariable String key){
		return challengeService.getHintsbasedonKey(challengeId,key);
	}
	
}
