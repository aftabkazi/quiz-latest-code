package com.quiz.controller;

import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.quiz.entity.UserResponse;
import com.quiz.response.GlobalResponse;
import com.quiz.service.UserResponseService;

import net.sf.jasperreports.engine.JRException;

@CrossOrigin("*")
@RestController
public class UserResponseController {
	
	@Autowired 	
	private UserResponseService userResponseService;
	
	@PostMapping("/quiz/attend")
	public ResponseEntity<GlobalResponse> attendQuiz(@RequestBody UserResponse userResponse) throws FileNotFoundException, JRException {
		GlobalResponse attendQuiz = userResponseService.attendQuiz(userResponse);
		return new ResponseEntity<GlobalResponse>(attendQuiz,HttpStatus.OK);
	}
	
	@GetMapping("/userresponse/get")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUserResponse(){
		List<UserResponse> userResponse= userResponseService.getAllUserResponse();
		return new ResponseEntity<List<UserResponse>>(userResponse,HttpStatus.OK);
	}	  
	
	@GetMapping("/userresponse/get/email")
	public ResponseEntity<List<UserResponse>> getAllUserResponseByEmail(){
	 	List<UserResponse> userResponse= userResponseService.getAllUserResponseByEmail();
	 	return new ResponseEntity<List<UserResponse>>(userResponse,HttpStatus.OK);
	} 
	 
	@GetMapping("/userresponse/get/id/{id}")
	public ResponseEntity<UserResponse> getUserResponseById(@PathVariable String id) {
		UserResponse userResponse = userResponseService.getUserResponseById(id);
		return new ResponseEntity<>(userResponse,HttpStatus.OK);
	}  
	
	// updated email id to unique_id
	@GetMapping("/userresponse/count/{unique_id}")
	public ResponseEntity<GlobalResponse> getCountOfQuizById (@PathVariable String unique_id) {
		return userResponseService.getCountOfQuizById(unique_id);
	}
	
	
}