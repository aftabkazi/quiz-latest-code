package com.quiz.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.AddaptiveData;
import com.quiz.entity.Quiz;
import com.quiz.exception.quiz.QuizNotFoundException;
import com.quiz.exception.user.TokenExpiryException;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.repository.QuizRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;

@Service
public class QuizService {

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	// method to post quiz
	public ResponseEntity<GlobalResponse> addQuiz(Quiz quiz) {
		
		Quiz quizShortCode = quizRepository.findByShortCode(quiz.getShortCode().trim());
		Quiz quizTitle = quizRepository.findByQuizTitle(quiz.getQuizTitle());
		List<AddaptiveData> addaptivedata = quiz.getAddaptiveData();

		Map<String, String> errorMessages = new HashMap<>();
		int countOfAddaptiveData = 0;

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedCurrentDate = currentDate.format(formatter);
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

		if (quiz.getQuizTitle() == null || quiz.getQuizTitle().isBlank()) {
			errorMessages.put("Quiz title", "Enter Quiz title");
		} else {
			String regex = "[a-zA-Z ]*$";
			Boolean validQuizTitle = Pattern.compile(regex).matcher(quiz.getQuizTitle().trim()).matches();
			if (validQuizTitle == false) {
				errorMessages.put("Quiz title", "Only characters are allowed");
			}
		}

		if (quiz.getShortCode() == null || quiz.getShortCode().isBlank()) {
			errorMessages.put("Short code", "Enter Quiz short code");
		} else if (quizShortCode != null) {
			errorMessages.put("Quiz short code", "Short code must be unique");
		} else if (quizTitle != null) {
			errorMessages.put("Quiz ttile", "Quiz title must be unique");
		}

		else {
			String regex = "[a-zA-Z ]*$";
			Boolean validQuizShortCode = Pattern.compile(regex).matcher(quiz.getShortCode().trim()).matches();
			if (validQuizShortCode == false) {
				errorMessages.put("Quiz title", "Only characters are allowed");
			}
		}

		if (quiz.getNoOfAttempts() == null) {
			errorMessages.put("No of attempts", "Enter number of attempts");
		}
//		else {
//			String regex = "[0-9]+";
//			Boolean validatenumber = Pattern.compile(regex).matcher(Integer.toString(quiz.getNoOfAttempts())).matches();
//			if (validatenumber == false) {
//				return new GlobalResponse(quiz.getQuizId(), "Enter valid number of attempts", false);
//			}
//		}
		if (quiz.getTotalDuration() == null) {
			errorMessages.put("Total Duration", "Enter Time duration");
		} else if (!(quiz.getTotalDuration() > 0)) {
			errorMessages.put("Total Duration", "Time duration cannot be zero");
		}
//		else {
//			String regex="[a-zA-Z ]*$";
//			Boolean validatenumber=Pattern.compile(regex).matcher(Integer.toString(quiz.getTotalDuration())).matches();
//			if(validatenumber==true) {
//				return new GlobalResponse(quiz.getQuizId() ,"Enter valid minutes",false);
//			}
//		}
		if (quiz.getQuizSize() == null) {
			errorMessages.put("Quiz Size", "Enter Quiz size");
		}

		if (quiz.getStartDate() == null || quiz.getStartDate().isBlank() || quiz.getStartDate().equals("NA")) {

			if (!(quiz.getEndDate().equals("NA"))) {
				errorMessages.put("Start Date", "Enter start date");
			} else {
				quiz.setStartDate("NA");
				quiz.setEndDate("NA");
			}

		} else {
			String dateRegex = "\\b\\d{1,2}/\\d{1,2}/\\d{4}, \\d{1,2}:\\d{2}:\\d{2} [ap]m\\b";
			Boolean validStartDate = Pattern.compile(dateRegex).matcher(quiz.getStartDate().trim()).matches();

			if (validStartDate == false) {
				errorMessages.put("Start Date", "Enter Valid Start date");
			}

			if (quiz.getEndDate() == null || quiz.getEndDate().trim().isBlank() || quiz.getEndDate().equals("NA")) {
				errorMessages.put("End date", "Enter End date");
			} else {
				Boolean validEndDate = Pattern.compile(dateRegex).matcher(quiz.getEndDate().trim()).matches();
				if (validEndDate == false) {
					errorMessages.put("End Date", "Enter Valid End Date");
				}
			}
			try {
				Date current = formatter1.parse(formattedCurrentDate);
				Date startDate = formatter1.parse(quiz.getStartDate());
				Date endDate = formatter1.parse(quiz.getEndDate());
				if (current.compareTo(startDate) > 0) {
					errorMessages.put("Start Date", "Start Date cannot be less than current date");
				}
				if (startDate.compareTo(endDate) > 0) {
					errorMessages.put("End Date", "End Date cannot be less than Start Date");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (quiz.getShowScore() == null) {
			quiz.setShowScore(false);
		}
		if (quiz.getShuffle() == null) {
			quiz.setShuffle(false);
		}
		if (quiz.getPublish() == null) {
			quiz.setPublish(true);
		}
		if (quiz.getPassPercentage() == null) {
			errorMessages.put("Pass Percentage", "Enter Pass Percentage");
		}

		for (AddaptiveData addaptive : addaptivedata) {
			if (addaptive.getIsTrue() == true) {
				if (addaptive.getEasy() != null && addaptive.getMedium() != null && addaptive.getHard() != null) {
					countOfAddaptiveData = addaptive.getEasy() + addaptive.getMedium() + addaptive.getHard();
				}
				if (countOfAddaptiveData != quiz.getQuizSize()) {
					errorMessages.put("Addaptive Data", "Addaptive data and quiz size does not mathces");
				}
			}
		}

		if (!(quiz.getQuizType().equals("regular") || quiz.getQuizType().equals("assignQuiz"))) {
			errorMessages.put("Quiz Type", "Enter valid quiz type");
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {
//			quiz.setQuizTitle(quiz.getQuizTitle().toLowerCase());
			quiz.setQuizTitle(quiz.getQuizTitle());
			quiz.setShortCode(quiz.getShortCode().toLowerCase());

			new Date();
			quizRepository.save(quiz);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(quiz.getQuizId(), "Quiz added sucessfully", true));
		}
	}

	// method to get all quiz
	public ResponseEntity<List<Quiz>> getAllQuiz() {

		List<Quiz> allQuiz = quizRepository.findAll();
		// returning recently created quiz based on created date
		List<Quiz> listOfRecentlyCreatedQuiz = allQuiz.stream()
				.sorted(Comparator.comparing(Quiz::getCreatedDate).reversed()).collect(Collectors.toList());

		return new ResponseEntity<List<Quiz>>(listOfRecentlyCreatedQuiz, HttpStatus.OK);

	}

	// method to get quiz by quizId
	public Quiz getQuizById(String quizid) {
		Quiz quizId = quizRepository.findByQuizId(quizid);
		List<String> decryptedEmails = new ArrayList<>();

		if (quizId == null) {
			throw new QuizNotFoundException();
		} else {
			List<String> emails = quizId.getEmailList();

			if (emails != null) {
				for (String email : emails) {
					decryptedEmails.add(encryptDecryptHandler.decrypt(email)); // decrypting email and adding to the
																				// list
				}
			}
		}
		
		// new object of quiz to set the decrypted emails 
		Quiz quiz = new Quiz();
		
		quiz.setQuizId(quizId.getQuizId());
		quiz.setQuizTitle(quizId.getQuizTitle());
		quiz.setShortCode(quizId.getShortCode());
		quiz.setDescription(quizId.getDescription());
		quiz.setNoOfAttempts(quizId.getNoOfAttempts());
		quiz.setTotalDuration(quizId.getTotalDuration());
		quiz.setQuizSize(quizId.getQuizSize());
		quiz.setShuffle(quizId.getShuffle());
		quiz.setPublish(quizId.getPublish());
		quiz.setShowScore(quizId.getShowScore());
		quiz.setQuizType(quizId.getQuizType());
		quiz.setEmailList(decryptedEmails);
		quiz.setStartDate(quizId.getStartDate());
		quiz.setEndDate(quizId.getEndDate());
		quiz.setPassPercentage(quizId.getPassPercentage());
		quiz.setCertFileName(quizId.getCertFileName());
		quiz.setAddaptiveData(quizId.getAddaptiveData());
		quiz.setCreatedDate(quizId.getCreatedDate());
		quiz.setUpdatedDate(quizId.getUpdatedDate());
		
		return quiz;
	}

	// method to get quiz by quiz title
	public Quiz getQuizByQuizTitle(String quiztitle) {
		Quiz quizTitle = quizRepository.findByQuizTitle(quiztitle);

		if (quizTitle == null) {
			throw new QuizNotFoundException();
		}
		return quizRepository.findByQuizTitle(quiztitle);
	}

	// method to delete quiz
	public GlobalResponse deleteQuizById(String quizid) {
		Quiz quizId = quizRepository.findByQuizId(quizid);

		if (quizId == null) {
			throw new QuizNotFoundException();
		}
		quizRepository.deleteById(quizid);
		/*
		 * If You want to delete all the questions related to the quiz that is deleted
		 * then uncomment this code
		 * 
		 * Step 1--
		 * 
		 * @Autowired SingleChoiceQuestionRepository singleChoiceQuestionRepository;
		 * Step 2-- List<SingleChoiceQuestion> questions =
		 * singleChoiceQuestionRepository.findByQuizId(quizid); Step 3 --
		 * singleChoiceQuestionRepository.deleteAll(questions);
		 */

		return new GlobalResponse(null, "Quiz Deleted Successfully", true);
	}

	// method to update quiz
	public ResponseEntity<GlobalResponse> updateQuiz(Quiz quiz) {

		Quiz quizId = quizRepository.findByQuizId(quiz.getQuizId());
//		List<AddaptiveData> addaptivedata=quiz.getAddaptiveData();

		Map<String, String> errorMessages = new HashMap<>();
		List<AddaptiveData> addaptivedata = quiz.getAddaptiveData();

		int countOfAddaptiveData = 0;

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedCurrentDate = currentDate.format(formatter);
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

		if (quizId == null) {
			throw new QuizNotFoundException();
		}

		if (quiz.getQuizTitle() == null || quiz.getQuizTitle().isBlank()) {
			errorMessages.put("Quiz title", "Enter Quiz title");
		} else {
			String regex = "[a-zA-Z ]*$";
			Boolean validQuizTitle = Pattern.compile(regex).matcher(quiz.getQuizTitle().trim()).matches();
			if (validQuizTitle == false) {
				errorMessages.put("Quiz title", "Only characters are allowed");
			}
		}

		if (quiz.getShortCode() == null || quiz.getShortCode().isBlank()) {
			errorMessages.put("Short code", "Enter Quiz Short code");
		}

		else {

			if (quizId.getShortCode().equals(quiz.getShortCode().toLowerCase())) {
				String regex = "[a-zA-Z ]*$";
				Boolean validQuizShortCode = Pattern.compile(regex).matcher(quiz.getShortCode().trim()).matches();
				if (validQuizShortCode == false) {
					errorMessages.put("Short Code", "Only charactars are allowed");
				}
			} else {
				Quiz quizShortCode = quizRepository.findByShortCode(quiz.getShortCode().toLowerCase());

				if (quizShortCode != null) {
					errorMessages.put("Short Code", "Enter unique short code");
				}
			}
		}

		if (quiz.getNoOfAttempts() == null) {
			errorMessages.put("Number of Attempts", "Enter Number of Attempts");
		}
		if (quiz.getTotalDuration() == null) {
			errorMessages.put("Total Duration", "Enter Time Duration");
		} else if (!(quiz.getTotalDuration() > 0)) {
			errorMessages.put("Titak Duration", "Time Duration cannot be zero");
		}
		if (quiz.getQuizSize() == null) {
			errorMessages.put("Quiz Size", "Enter Quiz size");
		}

		if (quiz.getStartDate() == null || quiz.getStartDate().isBlank() || quiz.getStartDate().equals("NA")) {

			if (!(quiz.getEndDate().equals("NA"))) {
				errorMessages.put("Start Date", "Enter start date");
			} else {
				quiz.setStartDate("NA");
				quiz.setEndDate("NA");
			}

		} else {
			String dateRegex = "\\b\\d{1,2}/\\d{1,2}/\\d{4}, \\d{1,2}:\\d{2}:\\d{2} [ap]m\\b";
			Boolean validStartDate = Pattern.compile(dateRegex).matcher(quiz.getStartDate().trim()).matches();

			if (validStartDate == false) {
				errorMessages.put("Start Date", "Enter Valid Start date");
			}

			if (quiz.getEndDate() == null || quiz.getEndDate().isBlank() || quiz.getEndDate() == "NA") {
				errorMessages.put("End date", "Enter End date");
			} else {
				Boolean validEndDate = Pattern.compile(dateRegex).matcher(quiz.getEndDate().trim()).matches();
				if (validEndDate == false) {
					errorMessages.put("End Date", "Enter Valid End Date");
				}
			}
			try {
				Date current = formatter1.parse(formattedCurrentDate);
				Date startDate = formatter1.parse(quiz.getStartDate());
				Date endDate = formatter1.parse(quiz.getEndDate());
				if (current.compareTo(startDate) > 0) {
					errorMessages.put("Start Date", "Start Date cannot be less than current date");
				}
				if (startDate.compareTo(endDate) > 0) {
					errorMessages.put("End Date", "End Date cannot be less than Start Date");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (quiz.getShowScore() == null) {
			quiz.setShowScore(false);
		}
		if (quiz.getShuffle() == null) {
			quiz.setShuffle(false);
		}
		if (quiz.getPublish() == null) {
			quiz.setPublish(true);
		}
		if (quiz.getPassPercentage() == null) {
			errorMessages.put("Pass Percentage", "Enter Pass Percentage");
		}

		for (AddaptiveData addaptive : addaptivedata) {
			if (addaptive.getIsTrue() == true) {
				countOfAddaptiveData = addaptive.getEasy() + addaptive.getMedium() + addaptive.getHard();

				if (countOfAddaptiveData != quiz.getQuizSize()) {
					errorMessages.put("Addaptive Data",
							"Addaptive data and quiz size does not mathces .Enter valid addaptive data or quiz size");
				}
			}
		}

		if (quiz.getQuizType() == null) {
			errorMessages.put("Quiz Type", "Enter quiz type");
		}

		else if (!(quiz.getQuizType().equals("regular") || quiz.getQuizType().equals("assignQuiz"))) {
			errorMessages.put("Quiz Type", "Enter valid quiz type");
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {
			quiz.setEmailList(quizId.getEmailList());
//			quiz.setQuizTitle(quiz.getQuizTitle().toLowerCase());
			quiz.setQuizTitle(quiz.getQuizTitle());
			quiz.setShortCode(quiz.getShortCode().toLowerCase());
			quiz.setCreatedDate(quizId.getCreatedDate());

			quizRepository.save(quiz);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(quiz.getQuizId(), "Quiz Updated Successfully", true));
		}
	}

	// method to get quiz count
	public int getTotalCountOfQuiz() {
		int totalQuiz = (int) quizRepository.count();
		return totalQuiz;
	}

	// method to get by startDate and end date
	public List<Quiz> getQuizByStartDateAndEndDate() throws ParseException {
		List<Quiz> listOfQuiz = quizRepository.findAll();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, h:mm:ss a");
		LocalDateTime currentDateTime = LocalDateTime.now();

		List<Quiz> newList = new ArrayList<>();
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;

		for (Quiz quiz : listOfQuiz) {
			if (quiz.getQuizType().equals("regular")) {

				// if start date and end date is NA then it will be added to the list
				if (quiz.getStartDate().equals("NA") || quiz.getEndDate().equals("NA")) {
					newList.add(quiz);
				} else {

					// converting format of dates
					startDateTime = LocalDateTime.parse(quiz.getStartDate(), formatter);
					endDateTime = LocalDateTime.parse(quiz.getEndDate(), formatter);

					if ((currentDateTime.isAfter(startDateTime) || currentDateTime.isEqual(startDateTime))
							&& (currentDateTime.isBefore(endDateTime)) && (quiz.getPublish())) {
						newList.add(quiz);
					}
				}
			}
		}
		return newList;
	}

	// method to get quiz by whitelisted emails
	public List<Quiz> getQuizByWhitelistedEmails() {

		List<Quiz> Quiz = quizRepository.findAll();
		String email = AuthTokenFilter.userEmail.get();

		if (email == null) {
			throw new TokenExpiryException();
		}

		List<Quiz> QuizList = new ArrayList<>();

		for (Quiz quiz : Quiz) {
			if (quiz.getQuizType() != null) {
				if (quiz.getQuizType().equals("assignQuiz") && quiz.getEmailList() != null) {
					for (String listOfEmails : quiz.getEmailList()) {
						if (listOfEmails.contains(email)) {
							QuizList.add(quiz);
						}
					}
				}
			}
		}
		return QuizList;
	}

	// method to update list of email's in the quiz
	public ResponseEntity<GlobalResponse> updateEmailListForQuiz(Quiz quiz) {

		Quiz Quiz = quizRepository.findByQuizId(quiz.getQuizId());
		List<String> listOfEmails = new ArrayList<>();

		if (Quiz == null) {
			throw new QuizNotFoundException();
		} else {
			quiz.setQuizTitle(Quiz.getQuizTitle());
			quiz.setShortCode(Quiz.getShortCode());
			quiz.setDescription(Quiz.getDescription());
			quiz.setNoOfAttempts(Quiz.getNoOfAttempts());
			quiz.setTotalDuration(Quiz.getTotalDuration());
			quiz.setQuizSize(Quiz.getQuizSize());
			quiz.setShuffle(quiz.getShuffle());
			quiz.setPublish(Quiz.getPublish());
			quiz.setShowScore(Quiz.getShowScore());
			quiz.setQuizType("assignQuiz");
			quiz.setStartDate(Quiz.getStartDate());
			quiz.setEndDate(Quiz.getEndDate());
			quiz.setPassPercentage(Quiz.getPassPercentage());
			quiz.setCertFileName(Quiz.getCertFileName());
			quiz.setAddaptiveData(Quiz.getAddaptiveData());
			quiz.setCreatedDate(Quiz.getCreatedDate());
			quiz.setUpdatedDate(Quiz.getUpdatedDate());

			for (String email : quiz.getEmailList()) {
				try {
					listOfEmails.add(encryptDecryptHandler.encrypt(email)); // adding encrypted email to the list
				} catch (Exception e) {
				}
			}
			quiz.setEmailList(listOfEmails);
		}
		quizRepository.save(quiz);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new GlobalResponse(quiz.getQuizId(), "Quiz Updated Successfully", true));
	}
}
