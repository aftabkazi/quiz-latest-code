package com.quiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class FillInTheBlanksService {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private SingleChoiceQuestionRepository questionRepository;

	@Value("${extern.resourse.ipaddress}")
	private String basePath;

	@Value("${extern.resources.Dir}")
	private String path;

	// method to add fill in the blanks question
	public ResponseEntity<GlobalResponse> AddFillInTheBlanksQuestion(SingleChoiceQuestion question) {

//		Quiz quiz = quizRepository.findByQuizId(question.getQuizId());

//		if (quiz == null) {
//			errorMessages.put("Quiz", "Select valid Quiz");
//		}

		Map<String, String> errorMessages = new HashMap<>();

		if (question.getQuestionTitletext() == null || question.getQuestionTitletext().isBlank()) {
			errorMessages.put("Question title text", "Enter Question title text");
		}

		if (question.getCorrectOption() == null || question.getCorrectOption().isBlank()) {
			errorMessages.put("Correct Answer", "Enter Correct Answer");
		}

		if (question.getCount() == null || question.getCount() == 0) {
			question.setCount(question.getCorrectOption().length());
		}

		if (question.getLevel() == null || question.getLevel().isBlank()) {
			question.setLevel("easy");
		}

		if (question.getScore() == null) {
			question.setScore(1);
		}
		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new GlobalResponse("failed to add" + "Question due to following reasons", errorMessages, false));
		} else {

			Map<String, String> MapOfQuizNameAndIds = new HashMap<>();
			List<String> selectedQuizIds = new ArrayList<>();

			for (String q : question.getSelectedQuizIds()) {
				Quiz quizId = quizRepository.findByQuizId(q);

				if (quizId != null) {
					MapOfQuizNameAndIds.put(q, quizId.getQuizTitle());
					selectedQuizIds.add(q);
				}

			}

			question.setSelectedQuizIds(selectedQuizIds);
			question.setQuizIdsAndNames(MapOfQuizNameAndIds);
			question.setType("fill");
//			question.setSelectQuiz(quiz.getQuizTitle().toLowerCase());
			question.setLevel(question.getLevel().toLowerCase());

			questionRepository.save(question);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(question.getQuestionId(), "Question added sucessfully", true));
		}
	}

	// method to update fill in the blanks question
	public ResponseEntity<GlobalResponse> updateQuestion(SingleChoiceQuestion question) {

		Map<String, String> errorMessages = new HashMap<>();

		if (!(question.getType().equals("fill"))) {

			errorMessages.put("Question type", "This is not fill in the blanks type of question");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		}

		else {
			SingleChoiceQuestion questionId = questionRepository.findByQuestionId(question.getQuestionId());

//			Quiz quiz = quizRepository.findByQuizId(question.getQuizId());

			if (questionId == null) {
				throw new QuestionNotFoundException();
			}

//			if (quiz == null) {
//				errorMessages.put("Quiz", "Select valid Quiz");
//			}

			if (question.getQuestionTitletext() == null || question.getQuestionTitletext().isBlank()) {
				errorMessages.put("question title text", "enter Question title text");
			}

			if (question.getCount() == null || question.getCount() == 0) {
				question.setCount(question.getCorrectOption().length());
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
						"failed to update Question " + "due to following reasons", errorMessages, false));
			} else {

				Map<String, String> MapOfQuizNameAndIds = new HashMap<>();
				List<String> selectedQuizIds = new ArrayList<>();

				for (String q : question.getSelectedQuizIds()) {
					Quiz quizId = quizRepository.findByQuizId(q);

					if (quizId != null) {
						MapOfQuizNameAndIds.put(q, quizId.getQuizTitle());
						selectedQuizIds.add(q);
					}

				}
				
				question.setSelectedQuizIds(selectedQuizIds);
				question.setQuizIdsAndNames(MapOfQuizNameAndIds);
				question.setType("fill");
				question.setLevel(question.getLevel().toLowerCase());
//				question.setSelectQuiz(quiz.getQuizTitle().toLowerCase());
				question.setCreatedDate(questionId.getCreatedDate());

				questionRepository.save(question);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new GlobalResponse(question.getQuestionId(), "Question Updated" + " Successfully", true));
			}
		}
	}
}
