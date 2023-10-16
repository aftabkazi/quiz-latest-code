package com.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quiz.entity.ForgotPassword;
import com.quiz.entity.User;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.repository.ForgotPasswordRepository;
import com.quiz.repository.UserRepository;
import com.quiz.response.GlobalResponse;
import java.util.UUID;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ForgotPasswordService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ForgotPasswordRepository forgotPasswordReposiotry;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	@Value("${extern.resourse.applicationURl}")
	private String applicationUrl;

	/* Object of email service */
	private EmailService emailService;

	/* To Generate random String */
	public String generateRandomString() {
		return UUID.randomUUID().toString();
	}

	@Autowired
	public ForgotPasswordService(EmailService emailService) {
		this.emailService = emailService;
	}

	// method to send reset link on email
	public ResponseEntity<?> sendLinkOnEmail(ForgotPassword forgotPassword) {

		StringBuffer messageBody = new StringBuffer();
		// String applicationurl = applicationUrl+"/"+"reset-password";
		applicationUrl = "http://10.244.1.39:3000/reset-password";
//		System.out.println("Application URL --" + applicationUrl);
		String encryptedEmail = "";
		// EncryptDecryptHandler encryptDecryptHandler;
		try {
			// encryptDecryptHandler = new EncryptDecryptHandler();
			encryptedEmail = encryptDecryptHandler.encrypt(forgotPassword.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}

		User user = userRepository.findByEmail(encryptedEmail);
		Map<String, String> errorMessages = new HashMap<>();
		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		if ((!forgotPassword.getEmail().matches(emailRegex))) {

			errorMessages.put("Email", "Please Enter valid Email !");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		}

		if (user == null) {

			errorMessages.put("Invalid Email", "Email is not registered with us");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {
			String username = user.getUsername();
			String token = generateRandomString();

			long hour = 3600 * 1000; // number of milliseconds in one hour
			Date now = new Date(); // current Date
			Date onehourDate = new Date(now.getTime() + hour); // creates new date and adding calculate hour to
																// calculate current time
			long onehourUnixtime = onehourDate.getTime() / 1000L;

			forgotPassword.setEncryptEmail(token);
			forgotPassword.setEndTime((int) onehourUnixtime);
			forgotPassword.setFlag("N");

			String subject = "Forgot Password - Quiz";
			messageBody.append("<p>Dear " + username + ",</p>");
			messageBody.append("<p>Greetings of the day!</p>");
			messageBody
					.append("<p>Please visit the following link to reset your password and activate your account:</p>");
			messageBody.append("<p><a href='" + applicationUrl + "/passwordreset?rand=" + token + "'>" + "Reset Password" + "</a></p>");
			messageBody.append("<p>NOTE: The link will expire on <b>" + onehourDate + "</b>.</p>");
			messageBody.append("<p>Regards,<br/>Quiz Administrator</p>");
			String message = messageBody.toString();

			int i = emailService.sendEmailWithoutAttachment(forgotPassword.getEmail(), subject, message);
			if (i == 1) {
				forgotPasswordReposiotry.save(forgotPassword);
				return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse(null,
						"Mail has been sent with a link. The link will expire in one hour.", true));
			} 
			else {
				errorMessages.put("Time out", "Error occured while sending mail,Try again after some time");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
			}
		}
	}

	// method to reset password 
	public ResponseEntity<?> resetPassword(User user, String token) {

		long endtime;
		Date enddate;
		String flag = "Y";

		ForgotPassword forgotPassword = forgotPasswordReposiotry.findByEncryptEmail(token);
		Map<String, String> errorMessages = new HashMap<>();

		if (forgotPassword == null) {

			errorMessages.put("Invalid Request", "Please recheck the URL once again");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		}

		if (forgotPassword.getFlag().equalsIgnoreCase("Y")) {

			errorMessages.put("Invalid Request", "You have already reset password using link");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		}

		endtime = (long) forgotPassword.getEndTime();
		Date now = new Date();
		enddate = new Date(endtime * 1000L);

		if (!now.before(enddate)) {

			errorMessages.put("Invalid Request", "Link expired");
 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		}

		else {
	
			User userEmail = userRepository.findByEmail(encryptDecryptHandler.encrypt(forgotPassword.getEmail()));
			
			if (userEmail != null) {
				String encryptPassword = encoder.encode(user.getPassword());
				userEmail.setPassword(encryptPassword);
				userRepository.save(userEmail);
				forgotPassword.setFlag(flag);
				forgotPasswordReposiotry.save(forgotPassword);

				return ResponseEntity.status(HttpStatus.OK)
						.body(new GlobalResponse(null, "Password changed sucessfully", false));
			} 
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GlobalResponse(null, "Invalid Request", false));
			}
		}
	}
	
}
