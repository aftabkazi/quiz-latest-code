package com.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.response.GlobalResponse;
import com.quiz.service.FillInTheBlanksService;

@RestController
@CrossOrigin("*")
@RequestMapping("/fill-in-the-blanks")
public class FillInTheBlanksController {
	
	@Autowired
	private FillInTheBlanksService fillInTheBlanksService;
	
	@PostMapping("/add")
	public ResponseEntity<GlobalResponse> AddFillInTheBlanksQuestion(@RequestBody SingleChoiceQuestion question) {
		return fillInTheBlanksService.AddFillInTheBlanksQuestion(question);
	}
	
	@PutMapping("/update")
	public ResponseEntity<GlobalResponse> updateQuestion(@RequestBody SingleChoiceQuestion question) {
		return fillInTheBlanksService.updateQuestion(question);
	}
}
