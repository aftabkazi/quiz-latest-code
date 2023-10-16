package com.quiz.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.response.GlobalResponse;
import com.quiz.service.TrueFalseService;

@RestController
@CrossOrigin("*")
@RequestMapping("/true-false")
public class TrueFalseController {
	
	@Autowired
	private TrueFalseService trueFalseService;
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> addQuestion(@RequestBody SingleChoiceQuestion question) throws IOException {
		return trueFalseService.addQuestion(question);
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> updateQuestion(@RequestBody SingleChoiceQuestion question) throws IOException {
		return trueFalseService.updateQuestion(question);
	}
	
	
	
}
