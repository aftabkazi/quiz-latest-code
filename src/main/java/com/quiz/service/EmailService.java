package com.quiz.service;

import org.springframework.stereotype.Service;
import java.io.File;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

	@Autowired
	JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Value("${spring.mail.cc}")
	private String ccRecipients;

	@Value("${spring.mail.bcc}")
	private String bccRecipients;

	// sending mail without attachment
	public int sendEmailWithoutAttachment(String email, String subject, String message) {

		int status = 0;
		try {

			/*
			 * Mime message and Mime message Helper is used to support sending HTML content
			 * in email
			 */

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			String senderName = "Quiz Administrator";
			String sender = String.format("%s <%s>", senderName, senderEmail);

			messageHelper.setFrom(sender);
			messageHelper.setTo(email);
			messageHelper.setSubject(subject);

			/* Set HTML content second parameter is set true to indicate that the content is
			 * html
			 */
			messageHelper.setText(message, true);

			// Set CC recipients
			if (ccRecipients != null && !ccRecipients.isEmpty()) {
				String[] ccAddresses = ccRecipients.split(",");
				messageHelper.setCc(ccAddresses);
			}

			// Set BCC recipients
			if (bccRecipients != null && !bccRecipients.isEmpty()) {
				String[] bccAddresses = bccRecipients.split(",");
				messageHelper.setBcc(bccAddresses);
			}
			javaMailSender.send(mimeMessage);
			status = 1;
			System.out.println("mail send sucessfully : " + status);
			return status;
		} catch (Exception e) {
			status = -1;
			System.out.println("Failed to send mail : " + status);
			return status;
		}
	}
	
	// Send email with attachment
	public int sendEmailWithAttachment(String email, String subject, String message, String filename) {

		int status = 0;
		try {

			/*
			 * Mime message and Mime message Helper is used to support sending HTML content
			 * in email
			 */

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			String senderName = "Quiz Administrator";
//			String senderEmail = "kiwibottle2000@gmail.com";
			String sender = String.format("%s <%s>", senderName, senderEmail);
			String customFileName = "Certificate.pdf";

			messageHelper.setFrom(sender);
			messageHelper.setTo(email);
			messageHelper.setSubject(subject);

			if (filename != null && !filename.isEmpty()) {
				FileSystemResource file = new FileSystemResource(new File(filename));
				messageHelper.addAttachment(customFileName, file);
			}

			/*
			 * Set HTML content second parameter is set true to indicate that the content is
			 * html
			 * 
			 */
			messageHelper.setText(message, true);

			// Set CC recipients
			if (ccRecipients != null && !ccRecipients.isEmpty()) {
				String[] ccAddresses = ccRecipients.split(",");
				messageHelper.setCc(ccAddresses);
			}

			// Set BCC recipients
			if (bccRecipients != null && !bccRecipients.isEmpty()) {
				String[] bccAddresses = bccRecipients.split(",");
				messageHelper.setBcc(bccAddresses);
			}

			javaMailSender.send(mimeMessage);
			status = 1;
			System.out.println("mail send sucessfully : " + status);
			return status;
		} catch (Exception e) {
			status = -1;
			sendEmailWithoutAttachment(email, subject, message);
			System.out.println("Failed to send mail : " + status);
			return status;
		}
	}
}
