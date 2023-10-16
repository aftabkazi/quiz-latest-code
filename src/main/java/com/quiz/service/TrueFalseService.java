package com.quiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.Quiz;
import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.exception.question.QuestionNotFoundException;
import com.quiz.repository.QuizRepository;
import com.quiz.repository.SingleChoiceQuestionRepository;
import com.quiz.response.GlobalResponse;

@Service
public class TrueFalseService {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private SingleChoiceQuestionRepository questionRepository;

	public ResponseEntity<GlobalResponse> addQuestion(SingleChoiceQuestion question) {

		Map<String, String> errorMessages = new HashMap<>();

//		Quiz quiz = quizRepository.findByQuizId(question.getQuizId());

//		if (quiz == null) {
//			errorMessages.put("Quiz", "please Select valid quiz");
//		}

		if (question.getQuestionTitletext() == null || question.getQuestionTitletext().isBlank()) {
			errorMessages.put("Question title text", "Enter Question title text");
		}

		if ((question.getType() == null) || question.getType().isBlank()) {
			question.setType("tf");
		}

		if (question.getLevel() == null || question.getLevel().isBlank()) {
			question.setLevel("easy");
		}

		if (question.getCorrectOption() == null || question.getCorrectOption().isBlank()) {
			errorMessages.put("Correct Option", "Choose Correct Option");
		}

		if (question.getImageName() == null || question.getImageName().isBlank()) {
			question.setImageName("NA");
		}

		if (question.getScore() == null) {
			question.setScore(1);
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new GlobalResponse("Failed to Add Question due to following " + "reasons", errorMessages, false));
		} else {

			Map<String, String> MapOfQuizNameAndIds = new HashMap<>();
			List<String> selectedQuizIds = new ArrayList<>();
			
			for(String q:question.getSelectedQuizIds()) {
				Quiz quizId= quizRepository.findByQuizId(q);
				
				if(quizId != null) {
					MapOfQuizNameAndIds.put(q,quizId.getQuizTitle());
					selectedQuizIds.add(q);
				}
				
			}
			
			question.setSelectedQuizIds(selectedQuizIds);
			question.setQuizIdsAndNames(MapOfQuizNameAndIds);
//			question.setSelectQuiz(quiz.getQuizTitle().toLowerCase());
			question.setLevel(question.getLevel().toLowerCase());
			question.setType("tf");
			questionRepository.save(question);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(question.getQuestionId(), "Question Added Successfully", true));
		}
	}

	public ResponseEntity<GlobalResponse> updateQuestion(SingleChoiceQuestion question) {

		SingleChoiceQuestion questionId = questionRepository.findByQuestionId(question.getQuestionId());
//		Quiz quiz = quizRepository.findByQuizId(question.getQuizId());

		Map<String, String> errorMessages = new HashMap<>();

		if (question.getType().equals("tf")) {

			if (questionId == null) {
				throw new QuestionNotFoundException();
			}

//			if (quiz == null) {
//				errorMessages.put("Quiz", "Please Select valid Quiz");
//			}

			if (question.getQuestionTitletext() == null || question.getQuestionTitletext().isBlank()) {
				errorMessages.put("Question title text", "Enter Question title text");
			}

			if (question.getCorrectOption() == null || question.getCorrectOption().isBlank()) {
				errorMessages.put("Correct Answer", "Enter Correct Answer");
			}

			if (question.getLevel() == null || question.getLevel().isBlank()) {
				question.setLevel("easy");
			}

			if (question.getImageName() == null || question.getImageName().isBlank()) {
				question.setImageName("NA");
			}

			if (question.getScore() == null) {
				question.setScore(1);
			}

			if (errorMessages.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GlobalResponse(
						"" + "Failed to Update Question due to following reasons", errorMessages, false));
			}

			else {
				
				Map<String,String> MapOfQuizNameAndIds= new HashMap<>();
				List<String> selectedQuizIds = new ArrayList<>();
				
				for(String q:question.getSelectedQuizIds()) {
					Quiz quizId= quizRepository.findByQuizId(q);
					
					if(quizId != null) {
						MapOfQuizNameAndIds.put(q,quizId.getQuizTitle());
						selectedQuizIds.add(q);
					}
					
				}
				
				question.setSelectedQuizIds(selectedQuizIds);
				question.setQuizIdsAndNames(MapOfQuizNameAndIds);
				question.setType(question.getType().toLowerCase());
				question.setLevel(question.getLevel().toLowerCase());
//				question.setSelectQuiz(quiz.getQuizTitle().toLowerCase());
				question.setCreatedDate(questionId.getCreatedDate());

				questionRepository.save(question);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new GlobalResponse(question.getQuestionId(), "Question updated sucessfully", true));
			}
		}

		else {
			return ResponseEntity.status(HttpStatus.OK).body(
					new GlobalResponse(question.getQuestionId(), "This is not true or false type of question", false));
		}
	}

}
