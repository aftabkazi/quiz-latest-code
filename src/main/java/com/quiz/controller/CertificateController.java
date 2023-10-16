package com.quiz.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.service.CertificateService;

@RestController
@CrossOrigin("*")
@RequestMapping("/certificate")
public class CertificateController {

	@Autowired
	private CertificateService certificateSevice;

	@PostMapping("/add")
	public String issueCertificate(HttpServletRequest request, @RequestParam("quizName") String quizName,
			@RequestParam("certFileName") String certFileName,
			@RequestParam("userName") String userName, @RequestParam("percentage") Integer percentage)
			throws IOException {
		return certificateSevice.generateCertificate(quizName, certFileName,userName, percentage);
	}
	
	@GetMapping("/get/{certificateName}")
	public ResponseEntity<?> getCertificate(@PathVariable String certificateName){
		return certificateSevice.getCertificate(certificateName);
	}
	
	@GetMapping("/getall")
	public ResponseEntity<?> getAllCertificatesByName(){
		return certificateSevice.getAllCertificatesByName();
	}

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello world";
	}

}
