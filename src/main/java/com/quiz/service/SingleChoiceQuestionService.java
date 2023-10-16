package com.quiz.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.entity.UserResponse;
import com.quiz.entity.AddaptiveData;
import com.quiz.entity.Quiz;
import com.quiz.exception.question.QuestionNotFoundException;
import com.quiz.exception.quiz.AttemptsException;
import com.quiz.exception.quiz.QuizLevelException;
import com.quiz.exception.quiz.QuizNotFoundException;
import com.quiz.exception.quiz.QuizPassedException;
import com.quiz.exception.quiz.QuizSizeException;
import com.quiz.repository.SingleChoiceQuestionRepository;
import com.quiz.repository.UserResponseRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;

@Service
public class SingleChoiceQuestionService {

	@Autowired
	private SingleChoiceQuestionRepository questionRepository;

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private UserResponseRepository userResponseRepository;

	@Value("${extern.resourse.ipaddress}")
	private String basePath;

	@Value("${extern.resources.Dir}")
	private String path;

	// method to add question
	public ResponseEntity<GlobalResponse> addQuestion(SingleChoiceQuestion question) throws IOException {

		Map<String, String> errorMessages = new HashMap<>();

//		Quiz quiz = quizRepository.findByQuizId(question.getQuizId());
//
//		if (quiz == null) {
//			errorMessages.put("Quiz", "please Select valid quiz");
//		}

		if (question.getQuestionTitletext() == null || question.getQuestionTitletext().isBlank()) {
			errorMessages.put("Question title text", "Enter Question title text");
		}

		if ((question.getType() == null) || question.getType().isBlank()) {
			question.setType("single");
		}

		if (question.getLevel() == null || question.getLevel().isBlank()) {
			question.setLevel("easy");
		}

		if (question.getOption1() == null || question.getOption1().isBlank()) {
			errorMessages.put("Option 1", "Enter option 1");
		}

		if (question.getOption2() == null || question.getOption2().isBlank()) {
			errorMessages.put("Option 2", "Enter option 2");
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
//			question.setSelecedQuiz();
//			question.setSelectQuiz(quiz.getQuizTitle());
			question.setLevel(question.getLevel().toLowerCase());
			question.setType("single");

			questionRepository.save(question);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(question.getQuestionId(), "Question Added Successfully", true));
		}
	}

	// method to get all questions
	public List<SingleChoiceQuestion> getQuestions() {

		List<SingleChoiceQuestion> questions = questionRepository.findAll();

		for (SingleChoiceQuestion question : questions) {
			if (question.getImageName() != null && !question.getImageName().isEmpty()
					&& !question.getImageName().equals("NA")) {

				String imagePath = question.getImageName();
				String filePath = basePath + "/" + path + "/" + imagePath;
				question.setImageName(filePath);
			}
		}

		List<SingleChoiceQuestion> listOfRecentlyCreatedQuestions = questions.stream()
				.sorted(Comparator.comparing(SingleChoiceQuestion::getCreatedDate).reversed())
				.collect(Collectors.toList());

		return listOfRecentlyCreatedQuestions;
	}

	// method to get questions based on question Id
	public SingleChoiceQuestion getQuestionById(String questionid) {
		SingleChoiceQuestion questionId = questionRepository.findByQuestionId(questionid);

		if (questionId == null) {
			throw new QuestionNotFoundException();
		}

		if (questionId.getImageName() != null && !questionId.getImageName().isEmpty()
				&& !questionId.getImageName().equals("NA")) {

			String imagePath = questionId.getImageName();
			String filePath = basePath + "/" + path + "/" + imagePath;
			questionId.setImageName(filePath);

			return questionId;
		} else {
			return questionRepository.findByQuestionId(questionid);
		}
	}

	// method to delete questions based on question id
	public GlobalResponse deleteQuesionById(String id) {
		SingleChoiceQuestion questionId = questionRepository.findByQuestionId(id);

		if (questionId == null) {
			throw new QuestionNotFoundException();
		}
		questionRepository.deleteById(id);

		return new GlobalResponse(null, "Question Deleted Successfully", true);
	}

	// method to update question
	public ResponseEntity<GlobalResponse> updateQuestion(SingleChoiceQuestion question) throws IOException {

		SingleChoiceQuestion questionId = questionRepository.findByQuestionId(question.getQuestionId());
//		Quiz quiz = quizRepository.findByQuizId(question.getQuizId());

		Map<String, String> errorMessages = new HashMap<>();

		if (question.getType().equals("single")) {

			if (questionId == null) {
				throw new QuestionNotFoundException();
			}

//			if (quiz == null) {
//				errorMessages.put("Quiz", "Please Select valid Quiz");
//			}

			if (question.getQuestionTitletext() == null || question.getQuestionTitletext().isBlank()) {
				errorMessages.put("Question title text", "Enter Question title text");
			}

			if (question.getOption1() == null || question.getOption1().isBlank()) {
				errorMessages.put("Option 1", "Enter Option 1");
			}

			if (question.getOption2() == null || question.getOption2().isBlank()) {
				errorMessages.put("Option 2", "Enter Option 2");
			}

			if (question.getCorrectOption() == null || question.getCorrectOption().isBlank()) {
				errorMessages.put("Correct Answer", "Enter Correct Answer");
			}

			if (question.getLevel() == null || question.getLevel().isBlank()) {
				question.setLevel("easy");
			}

			if (question.getImageName() != null && !question.getImageName().isEmpty()
					&& !question.getImageName().equals("NA")) {
				question.setImageName(question.getImageName());
			}

			else {
				question.setImageName(questionId.getImageName());
			}

			if (question.getScore() == null) {
				question.setScore(1);
			}

			if (errorMessages.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GlobalResponse(
						"" + "Failed to Update Question due to following reasons", errorMessages, false));
			} else {
				
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
//				question.setSelectQuiz(quiz.getQuizTitle());
				question.setCreatedDate(questionId.getCreatedDate());

				questionRepository.save(question);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new GlobalResponse(question.getQuestionId(), "Question updated sucessfully", true));
			}
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(question.getQuestionId(), "This is not Single type of question", false));
		}
	}

	// method to get questions without randomization from the db in a particular
	// quiz
	public List<SingleChoiceQuestion> getQuestionsWithoutRandommization(String quizId) {

		Quiz quiz = quizRepository.findByQuizId(quizId);

		if (quiz == null) {
			throw new QuizNotFoundException();
		} else {

//			List<SingleChoiceQuestion> totalQuestions = questionRepository.findByQuizId(quizId);

			List<SingleChoiceQuestion> totalQuestions = questionRepository.findByselectedQuizIds(quizId);

			if (totalQuestions.size() >= 0) {

				for (int i = 0; i < totalQuestions.size(); i++) {

					SingleChoiceQuestion question = totalQuestions.get(i);

					if (question.getImageName() != null && !question.getImageName().isEmpty()
							&& !question.getImageName().equals("NA")) {
						String imagePath = question.getImageName();
						String filePath = basePath + "/" + path + "/" + imagePath;
						question.setImageName(filePath);
					} else {
						question.setImageName(question.getImageName());
					}
				}
			}

			// collecting recently created questions in the list
			List<SingleChoiceQuestion> listOfRecentlyCreatedQuestions = totalQuestions.stream()
					.sorted(Comparator.comparing(SingleChoiceQuestion::getCreatedDate).reversed())
					.collect(Collectors.toList());

			return listOfRecentlyCreatedQuestions;
		}
	}

	// method to get total count of questions
	public int getTotalCountOfQuestions() {
		int totalQuestions = (int) questionRepository.count();
		return totalQuestions;
	}

	// method to get total count of question from a particular quiz
	public int getTotalCountOfQuestionsInParticularQuiz(String quizId) {

//		List<SingleChoiceQuestion> totalQuestions = questionRepository.findByQuizId(quizId);
		List<SingleChoiceQuestion> totalQuestions = questionRepository.findByselectedQuizIds(quizId);

		return totalQuestions.size();
	}

	// method to get random questions from db in a particular quiz
	public List<SingleChoiceQuestion> getRandomQuestions(String quizid) {

		List<SingleChoiceQuestion> ListOfQuestions = new ArrayList<>();

		Quiz quiz = quizRepository.findByQuizId(quizid);

		//		List<SingleChoiceQuestion> totalQuestions = questionRepository.findByQuizId(quizid);
		List<SingleChoiceQuestion> totalQuestions = questionRepository.findByselectedQuizIds(quizid);

		/* Taking user email id */
		String email = AuthTokenFilter.userEmail.get();

		if (quiz == null) {
			throw new QuizNotFoundException();
		}

		/* If questions are less then quiz size */
		if (totalQuestions.size() < quiz.getQuizSize()) {
			throw new QuizSizeException();
		}

		/* If user has completed his/her attempts */
		int count = userResponseRepository.countByEmailAndQuizId(email, quizid);
		if (count >= quiz.getNoOfAttempts()) {
			throw new AttemptsException();
		}

		List<UserResponse> userResponses = userResponseRepository.findByEmail(email);

		/* If user has passed the quiz */
		for (UserResponse ur : userResponses) {
			if (ur.getStatus().equals("passed") && ur.getQuizId().equals(quizid)) {
				throw new QuizPassedException();
			}
		}
		
		if (quiz.getShuffle()) {
			Collections.shuffle(totalQuestions);
		}

		List<AddaptiveData> addaptivedata = quiz.getAddaptiveData();

		for (AddaptiveData addaptive : addaptivedata) {
			if (!addaptive.getIsTrue() && totalQuestions.size() >= 0) { // if addaptive data is false
				for (int i = 0; i < quiz.getQuizSize(); i++) {
					if (totalQuestions.get(i).getImageName() != null && !totalQuestions.get(i).getImageName().isEmpty()
							&& !totalQuestions.get(i).getImageName().equals("NA")) {
						String imagePath = totalQuestions.get(i).getImageName();
						String filePath = basePath + "/" + path + "/" + imagePath;
						totalQuestions.get(i).setImageName(filePath);
					}
					ListOfQuestions.add(totalQuestions.get(i));
				}
				if (quiz.getShuffle()) {
					Collections.shuffle(ListOfQuestions); // if shuffle is true
					return ListOfQuestions;
				} else {
					return ListOfQuestions; // is shuffle is false
				}
			} else if (addaptive.getIsTrue()) {
				int easy = 0, medium = 0, hard = 0;

				// if addaptive data is true then code here
				List<SingleChoiceQuestion> easyList = new ArrayList<>();

				for (SingleChoiceQuestion totalQuestion : totalQuestions) {
					easyList.add(totalQuestion);
					Collections.shuffle(easyList);
				}

//				for (SingleChoiceQuestion totalQuestion : totalQuestions) {
//					if (totalQuestion.getLevel().equals("easy")) {
//						easyList.add(totalQuestion);
//						Collections.shuffle(easyList);
//					} else if (totalQuestion.getLevel().equals("medium")) {
//						mediumList.add(totalQuestion);
//						Collections.shuffle(mediumList);
//					} else {
//						hardList.add(totalQuestion);
//						Collections.shuffle(hardList);
//					}
//				}

				for (SingleChoiceQuestion totalQuestion : totalQuestions) {
					if (totalQuestion.getLevel().equals("easy") && addaptive.getEasy() > 0
							&& easy < addaptive.getEasy()) {
						easy++;
						totalQuestion.setCorrectOption("");
						if (totalQuestion.getImageName() != null && !totalQuestion.getImageName().isEmpty()
								&& !totalQuestion.getImageName().equals("NA")) {
							String imagePath = totalQuestion.getImageName();
							String filePath = basePath + "/" + path + "/" + imagePath;
							totalQuestion.setImageName(filePath);
						}
						ListOfQuestions.add(totalQuestion);
					}

					else if (totalQuestion.getLevel().equals("medium") && addaptive.getMedium() > 0
							&& medium < addaptive.getMedium()) {
						medium++;
						totalQuestion.setCorrectOption("");
						if (totalQuestion.getImageName() != null && !totalQuestion.getImageName().isEmpty()
								&& !totalQuestion.getImageName().equals("NA")) {
							String imagePath = totalQuestion.getImageName();
							String filePath = basePath + "/" + path + "/" + imagePath;
							totalQuestion.setImageName(filePath);
						}
						ListOfQuestions.add(totalQuestion);
					}

					else if (totalQuestion.getLevel().equals("hard") && addaptive.getHard() > 0
							&& hard < addaptive.getHard()) {
						hard++;
						totalQuestion.setCorrectOption("");
						if (totalQuestion.getImageName() != null && !totalQuestion.getImageName().isEmpty()
								&& !totalQuestion.getImageName().equals("NA")) {
							String imagePath = totalQuestion.getImageName();
							String filePath = basePath + "/" + path + "/" + imagePath;
							totalQuestion.setImageName(filePath);
						}
						ListOfQuestions.add(totalQuestion);
					}
				}
				if (addaptive.getEasy() != easy || addaptive.getMedium() != medium || addaptive.getHard() != hard) {
					throw new QuizLevelException();
				}
			}
		}
		if (quiz.getShuffle()) {
			Collections.shuffle(ListOfQuestions);
			return ListOfQuestions;
		} else {
			return ListOfQuestions;
		}
	}

	// method to delete question image by id and name
	public ResponseEntity<GlobalResponse> deleteQuestionImage(String id, String fileName) {

		SingleChoiceQuestion singleChoiceQuestionData = questionRepository.findByQuestionId(id);

		if (singleChoiceQuestionData == null) {
			throw new QuestionNotFoundException();
		}

		singleChoiceQuestionData.setImageName("NA");

		questionRepository.save(singleChoiceQuestionData);

		return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse(null, "File removed ", true));
	}

}