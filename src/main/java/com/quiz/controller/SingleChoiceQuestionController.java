package com.quiz.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.response.GlobalResponse;
import com.quiz.service.SingleChoiceQuestionService;

@CrossOrigin("*")
@RestController
@RequestMapping("/question")
public class SingleChoiceQuestionController {

	@Autowired
	private SingleChoiceQuestionService questionService;	
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> addQuestion(@RequestBody SingleChoiceQuestion question) throws IOException {
		return questionService.addQuestion(question);
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> updateQuestion(@RequestBody SingleChoiceQuestion question) throws IOException {
		return questionService.updateQuestion(question);
	}

	@GetMapping("/get")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<SingleChoiceQuestion>> getQuestions() {
		List<SingleChoiceQuestion> question = questionService.getQuestions();
		return new ResponseEntity<List<SingleChoiceQuestion>>(question,HttpStatus.OK);
	}

	@GetMapping("/get/{questionid}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<SingleChoiceQuestion> getQuestionById(@PathVariable String questionid) {
		SingleChoiceQuestion question = questionService.getQuestionById(questionid);
		return new ResponseEntity<>(question,HttpStatus.OK);
	}

	/*
	 * get random questions based on quiz id(if shuffle option is TRUE then
	 * questions will be randomized else not)
	 * 
	 */

	@GetMapping("/random/{quizid}")
	public ResponseEntity<List<SingleChoiceQuestion>> getRandomQuestions(@PathVariable String quizid) {
		List<SingleChoiceQuestion> question = questionService.getRandomQuestions(quizid);
		return new ResponseEntity<List<SingleChoiceQuestion>>(question,HttpStatus.OK);
	}

	
	 @GetMapping("/{quizId}") 
	 public ResponseEntity<List<SingleChoiceQuestion>> getQuestionsWithoutRandomization(@PathVariable String quizId){
		 List<SingleChoiceQuestion> question = questionService.getQuestionsWithoutRandommization(quizId);
		 return new ResponseEntity<List<SingleChoiceQuestion>>(question,HttpStatus.OK);
	}
	  
	@GetMapping("/count")
	public int getTotalCountOfQuestions() {
		return questionService.getTotalCountOfQuestions();
	}

	@GetMapping("/count/{quizId}")
	public int getTotalCountOfQuestionsInParticularQuiz(@PathVariable String quizId) {
		return questionService.getTotalCountOfQuestionsInParticularQuiz(quizId);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> deleteQuesionById(@PathVariable String id) {
		GlobalResponse SingleChoiceQuestion = questionService.deleteQuesionById(id);
		return new ResponseEntity<>(SingleChoiceQuestion,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/question-image/{id}/{imageName}")
	public ResponseEntity<GlobalResponse> deleteQuestionImage(@PathVariable String id,@PathVariable String imageName){
		return questionService.deleteQuestionImage(id, imageName);
	}
}
