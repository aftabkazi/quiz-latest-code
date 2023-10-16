package com.quiz.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.quiz.response.GlobalResponse;
import com.quiz.service.FilesUploadService;

@RestController
@CrossOrigin("*")
//@RequestMapping("/upload")
public class FilesUploadController {

	@Autowired
	private FilesUploadService filesUploadService;

	// excel upload API- for questions
	@PostMapping("/upload/excel")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam String excelUpload) {
		return filesUploadService.uploadExcel(file, excelUpload);
	}

	// excel upload API- for Emails
	@PostMapping("/upload/quiz/excel-emails")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> uploadEmailsInQuiz(@RequestParam("file") MultipartFile file,
			@RequestParam String excelUpload) {
		return filesUploadService.uploadEmailsInQuiz(file, excelUpload);
	}

	// excel upload API- for Emails
	@PostMapping("/upload/challengelist/excel-emails")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> uploadEmailsInChallengeList(@RequestParam("file") MultipartFile file,
			@RequestParam String excelUpload) {
		return filesUploadService.uploadEmailsInChallengeList(file, excelUpload);
	}

	// Jasper, jrxml files upload API
	@PostMapping("/upload/certificate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> uploadCertificate(@RequestPart("files") MultipartFile[] files)
			throws IOException {
		return filesUploadService.uploadCertificate(files);
	}

	// image upload API
	@PostMapping("/upload/image")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<GlobalResponse> uploadImage(@RequestPart("file") MultipartFile file) throws IOException {
		return filesUploadService.uploadImage(file);

	}
	
	// file download API
	@GetMapping("/download-file/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
		return filesUploadService.downloadImage(fileName);	
	}
}
