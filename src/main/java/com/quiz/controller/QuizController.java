package com.quiz.controller;

import java.text.ParseException;
import java.util.ArrayList;
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
import com.quiz.entity.Quiz;
import com.quiz.response.GlobalResponse;
import com.quiz.service.QuizService;

@RestController
@CrossOrigin("*")
@RequestMapping("/quiz")
public class QuizController {
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping("/add")
	public ResponseEntity<GlobalResponse> addQuiz(@RequestBody Quiz quiz) {
		return quizService.addQuiz(quiz);
	}
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> updateQuiz(@RequestBody Quiz quiz) {
		return quizService.updateQuiz(quiz);
	}
	
	@PutMapping("/updateemails")
	public ResponseEntity<GlobalResponse> updateEmailListForQuiz(@RequestBody Quiz quiz){
		return quizService.updateEmailListForQuiz(quiz);
	}
	
	@GetMapping("/get")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Quiz>> getAllQuiz(){
		 return quizService.getAllQuiz();
	}
	
	@GetMapping("/get/{quizid}")
	public ResponseEntity<Quiz> getQuizById(@PathVariable String quizid) {
		Quiz quiz=quizService.getQuizById(quizid);
		return new ResponseEntity<Quiz>(quiz,HttpStatus.OK);
	}
	
	@GetMapping("/get/quiztitle/{quiztitle}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Quiz> getQuizByQuizTitle(@PathVariable String quiztitle) {
		Quiz quiz=quizService.getQuizByQuizTitle(quiztitle);
		return new ResponseEntity<Quiz>(quiz,HttpStatus.OK);
	}
	
	@GetMapping("/count")
	@PreAuthorize("hasRole('ADMIN')")
	public int getToalCountOfQuiz() {
		return quizService.getTotalCountOfQuiz();
	}
	
	@GetMapping("/get-quiz-by-startdate-and-enddate")
	public ResponseEntity<List<Quiz>> getQuizByStartDateAndEndDate() throws ParseException {
		List<Quiz> quiz = quizService.getQuizByStartDateAndEndDate();
		return new ResponseEntity<List<Quiz>>(quiz,HttpStatus.OK);
	}
	
	@GetMapping("/get-quiz-by-whitelisted-emails")
	public ResponseEntity<List<Quiz>> getQuizByWhitelistedEmails(){
		List<Quiz> listOfEmails = new ArrayList<>();
		listOfEmails = quizService.getQuizByWhitelistedEmails();
		return new ResponseEntity<List<Quiz>>(listOfEmails,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{quizid}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> deleteQuizById(@PathVariable String quizid) {
		GlobalResponse quiz = quizService.deleteQuizById(quizid);
		return new ResponseEntity<GlobalResponse>(quiz,HttpStatus.OK);
	}
	
}
