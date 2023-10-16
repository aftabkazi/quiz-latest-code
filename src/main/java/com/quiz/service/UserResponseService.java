package com.quiz.service;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.entity.User;
import com.quiz.entity.Quiz;
import com.quiz.entity.UserResponseData;
import com.quiz.exception.quiz.QuizNotFoundException;
import com.quiz.exception.user.UserNotFoundException;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.entity.UserResponse;
import com.quiz.repository.SingleChoiceQuestionRepository;
import com.quiz.repository.UserRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.repository.UserResponseRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;

import net.sf.jasperreports.engine.JRException;

@Service
public class UserResponseService {

	@Autowired
	private UserResponseRepository userResponseRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SingleChoiceQuestionRepository questionRepository;

	@Autowired
	private QuizRepository quizRepository;

	private EmailService emailService;

	@Autowired
	private CertificateService certificateService;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	@Autowired
	public UserResponseService(EmailService emailService) {
		this.emailService = emailService;
	}

	// method to attend quiz
	public GlobalResponse attendQuiz(UserResponse userResponse) throws FileNotFoundException, JRException {

		String certStatus = "notIssued", fileName = "";

		Quiz quiz = quizRepository.findByQuizId(userResponse.getQuizId());

		User emailId = userRepository.findByEmail(userResponse.getEmail());

		String decryptedEmail = "";
		try {
			decryptedEmail = encryptDecryptHandler.decrypt(userResponse.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// quiz Id validation
		if (quiz == null) {
			throw new QuizNotFoundException();
		}

		// email id validation
		if (emailId == null) {
			throw new UserNotFoundException();
		}

		int correct = 0, wrong = 0, attempted = 0, unAttempted = 0, totalScore = 0, totalMarks = 0;

		for (UserResponseData data : userResponse.getQuizData()) {

			SingleChoiceQuestion entity = questionRepository.findByQuestionId(data.getQuestionId());

			// if question type is single
			if (entity.getType().equalsIgnoreCase("single")) {
				totalMarks = totalMarks + entity.getScore();

				if (!(data.getMarkedOption().trim().isBlank())) {
					attempted++;
				}

				if (data.getMarkedOption().isBlank() || data.getMarkedOption() == null) {
					unAttempted++;

					if (unAttempted == 0) {
						userResponse.setUnattempted(0);
					}
				} else if (entity.getCorrectOption().equals(data.getMarkedOption())) {
					correct++;
					totalScore += entity.getScore();
				}

				else {
					wrong++;
				}
			}

			// if question type is fill
			else if (entity.getType().equalsIgnoreCase("fill")) {
				totalMarks = totalMarks + entity.getScore();
				if (!(data.getMarkedOption().trim().isBlank())) {
					attempted++;
				}
				if (data.getMarkedOption().isBlank() || data.getMarkedOption() == null) {
					unAttempted++;

					if (unAttempted == 0) {
						userResponse.setUnattempted(0);
					}
				} else if (entity.getCorrectOption().equalsIgnoreCase(data.getMarkedOption())) {
					correct++;
					totalScore = totalScore + entity.getScore();
				} else {
					wrong++;
				}
			}

			// if question type is true or false
			else if (entity.getType().equalsIgnoreCase("tf")) {
				totalMarks = totalMarks + entity.getScore();
				if (!(data.getMarkedOption().trim().isBlank())) {
					attempted++;
				}

				if (data.getMarkedOption().isBlank() || data.getMarkedOption() == null) {
					unAttempted++;

					if (unAttempted == 0) {
						userResponse.setUnattempted(0);
					}
				}

				else if (entity.getCorrectOption().equals(data.getMarkedOption())) {
					correct++;
					totalScore += entity.getScore();
				}

				else {
					wrong++;
				}
			}
		}
		int totalQuestions = attempted + unAttempted;
		int percentage = (totalScore * 100) / totalMarks;

		// if user passes the quiz
		if (percentage >= quiz.getPassPercentage()) {
			userResponse.setStatus("passed");

			// certificate generation
			certStatus = certificateService.generateCertificate(quiz.getQuizTitle(), quiz.getCertFileName(),
					emailId.getUsername(), userResponse.getPercentage());

			fileName = Paths.get(certStatus).getFileName().toString();
			userResponse.setCertFileName(fileName);

			// message body to send in email
			StringBuffer messageBody = new StringBuffer();
			String subject = "Winner - Quiz";
			messageBody.append("<p>Dear " + emailId.getUsername() + ",</p>");
			messageBody.append("<p>Greetings of the day!</p>");
			messageBody.append("<p>You have successfully completed the <b>" + quiz.getQuizTitle()
					+ " quiz </b>organized by CDAC Hyderabad</p>");
			messageBody.append("<p>Regards,<br/>Quiz Administrator</p>");
			String message = messageBody.toString();

			// method call to send email
			int i = emailService.sendEmailWithAttachment(decryptedEmail, subject, message, certStatus);
			if (i == 1) {
				System.out.println("mail send");
			} else {
				System.out.println("error while sending mail");
			}
		}

		// if user fails the quiz
		else {
			userResponse.setCertFileName("notissued");
			userResponse.setStatus("failed");
		}

		userResponse.setTotalQuestions(totalQuestions);
		userResponse.setUsername(emailId.getUsername());
		userResponse.setCorrect(correct);
		userResponse.setWrong(wrong);
		userResponse.setAttempted(attempted);
		userResponse.setUnattempted(unAttempted);
		userResponse.setPercentage(percentage);
		userResponse.setTotalMarks(totalMarks);
		userResponse.setTotalScore(totalScore);
//		userResponse.setCertFileName(fileName);
		userResponse.setSelectedQuiz(quiz.getQuizTitle());

		userResponseRepository.save(userResponse);
		return new GlobalResponse(userResponse.getUserResponseId(), "Quiz completed sucessfully", true);
	}

	// method to get all the user response
	public List<UserResponse> getAllUserResponse() {
		return userResponseRepository.findAll();
	}

	// method to get user Response by id
	public UserResponse getUserResponseById(String id) {
		return userResponseRepository.findByUserResponseId(id);
	}

	// method to get user response by email
	public List<UserResponse> getAllUserResponseByEmail() {
		String email = AuthTokenFilter.userEmail.get(); // taking email from token
		List<UserResponse> userResponse;

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UserNotFoundException();
		} else {
			userResponse = userResponseRepository.findByEmail(email);
		}
		return userResponse;
	}

	public ResponseEntity<GlobalResponse> getCountOfQuizById(String unique_id) {

//		String encryptedEmail = "";
		int count = 0;
		User user = userRepository.findByUniqueId(unique_id);

		if (user == null) {
			return ResponseEntity.ok(new GlobalResponse(count));
		} else {
			
//			encryptedEmail = user.getEmail();
			
			List<UserResponse> quizAttendances = userResponseRepository.findByEmail(user.getEmail());
			return ResponseEntity.ok(new GlobalResponse(quizAttendances.size()));
		}
	}
}