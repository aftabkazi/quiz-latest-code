package com.quiz.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.entity.ChallengeList;
import com.quiz.entity.Quiz;
import com.quiz.entity.SingleChoiceQuestion;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.repository.ChallengeListRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.repository.SingleChoiceQuestionRepository;
import com.quiz.response.GlobalResponse;

@Service
public class FilesUploadService {

	public FilesUploadService() throws Exception {

	}

	@Value("${extern.resourse.ipaddress}")
	private String basePath;

	@Value("${extern.resources.Dir}")
	private String path;

	@Autowired
	private SingleChoiceQuestionRepository singleChoiceQuestion;

	@Autowired
	QuizRepository quizRepository;

	@Autowired
	private ChallengeListRepository challengeListRepository;

	@Value("${extern.resoures.path}")
	private String imageUpload; // getting values from the properties file

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	// method to upload excel in database
	public ResponseEntity<?> uploadExcel(MultipartFile file, String excelUpload) {

		Map<String, String> errorMessages = new HashMap<>();
		List<SingleChoiceQuestion> bulkList = new ArrayList<>();
		Map<String, String> MapOfQuizNameAndIds = new HashMap<>();
		List<String> selectedQuizIds = new ArrayList<>();

		String errorMessage = "";

		SingleChoiceQuestion question = null;
		Tika tika = new Tika();
		XSSFWorkbook workbook;

		long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024; // calculating size of file in MB

		try {

			/* DeSerialization of JSON (String) to java object */
			question = mapper.readValue(excelUpload, SingleChoiceQuestion.class);

//			Quiz quiz = quizRepository.findByQuizId(question.getQuizId());
//
//			if (quiz == null) {
//				errorMessages.put("Quiz id", "Invalid Quiz id");
//			}

			String mimeType = tika.detect(file.getInputStream());

			// empty file validation
			if (file.isEmpty()) {
				errorMessages.put("File", "File cannot be empty");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
			}

			// double extension validation
			if (file.getOriginalFilename().split("\\.").length > 2) {
				errorMessages.put("Invalid file", "Multiple[dot] extension found");
			}

			// file format validation
			if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				errorMessages.put("Invalid file", "only xlsx file formst supported");
			}

			/* mime type validation */
			if (!isSupportedContentType(mimeType)) {
				errorMessages.put("Invalid file", "Invlaid XLSX file");
			}

			// file size validation
			if (fileSizeInMB > 2) {
				errorMessages.put("file size", "Allowed file size is upto 2 MB");
			}

			if (errorMessages.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
			} else {

				workbook = new XSSFWorkbook(file.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				DataFormatter formatter = new DataFormatter();

				for (int i = 1; i <= worksheet.getLastRowNum(); i++) {
					SingleChoiceQuestion questions = new SingleChoiceQuestion();
					XSSFRow row = worksheet.getRow(i);

					/* To check if the complete row is empty */
					if (row == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new GlobalResponse(null, "Error: Row " + i + " is empty", false));
					}

					String questionTitletext = "NA";
					String option1 = "NA";
					String option2 = "NA";
					String option3 = "NA";
					String option4 = "NA";
					String option5 = "NA";
					String option6 = "NA";
					String correctOption = "NA";
					String level = "NA";
					String type = "NA";
					String imageName = "NA";
					Integer score = null;

					/*
					 * ConvertNumToColString(0)= returns the alphabet corresponding to digit Example
					 * 0=A,1=B and so on (i+1)= in this i represent row index,and row numbers are
					 * typically 1-based (not zero-based), we add 1 to get actual row number At the
					 * end both are concatenated to get actual cell value Example- A23,etc.
					 */

					Cell questionTitleCell = row.getCell(0);
					questionTitletext = formatter.formatCellValue(questionTitleCell);
					if (questionTitletext.trim().isEmpty()) {
						String cellAddress = CellReference.convertNumToColString(0) + (i + 1);
						errorMessage += "SrNo : " + i + " question title in cell " + cellAddress + " is empty;\n";
					} else {
						questionTitletext = formatter.formatCellValue(questionTitleCell);
					}

					Cell option1TitleCell = row.getCell(1);
					option1 = formatter.formatCellValue(option1TitleCell);
					if (option1.trim().isEmpty()) {
						String cellAddress = CellReference.convertNumToColString(1) + (i + 1);
						errorMessage += "SrNo : " + i + " Option 1 in cell " + cellAddress + " is empty;\n";
					} else {
						option1 = formatter.formatCellValue(option1TitleCell);
					}

					Cell option2TitleCell = row.getCell(2);
					option2 = formatter.formatCellValue(option2TitleCell);
					if (option2.trim().isEmpty()) {
						String cellAddress = CellReference.convertNumToColString(2) + (i + 1);
						errorMessage += "SrNo : " + i + " Option 2 in cell " + cellAddress + " is empty;\n";
					} else {
						option2 = formatter.formatCellValue(option2TitleCell);
					}

					option3 = formatter.formatCellValue(row.getCell(3));
					option4 = formatter.formatCellValue(row.getCell(4));
					option5 = formatter.formatCellValue(row.getCell(5));
					option6 = formatter.formatCellValue(row.getCell(6));

					Cell correctOptionTitleCell = row.getCell(7);
					correctOption = formatter.formatCellValue(correctOptionTitleCell);
					if (correctOption.trim().isEmpty()) {
						String cellAddress = CellReference.convertNumToColString(7) + (i + 1);
						errorMessage += "SrNo : " + i + " Correct Option in cell " + cellAddress + " is empty;\n";
					} else {
						correctOption = formatter.formatCellValue(row.getCell(7)).toLowerCase();

						if (!(correctOption.equalsIgnoreCase("a") || correctOption.equalsIgnoreCase("b")
								|| correctOption.equalsIgnoreCase("c") || correctOption.equalsIgnoreCase("d")
								|| correctOption.equalsIgnoreCase("e") || correctOption.equalsIgnoreCase("f"))) {
							String cellAddress = CellReference.convertNumToColString(7) + (i + 1);
							errorMessage += "SrNo : " + i + " Enter valid correct option in cell " + cellAddress
									+ ";\n";
						}

					}
					Cell levelCell = row.getCell(8);
					if (levelCell != null && (!(row.getCell(8).getStringCellValue().equals("")))) {
						level = row.getCell(8).getStringCellValue().trim().toLowerCase();
						if (!(level.equalsIgnoreCase("easy") || level.equalsIgnoreCase("medium")
								|| level.equalsIgnoreCase("hard"))) {
							String cellAddress = CellReference.convertNumToColString(8) + (i + 1);
							errorMessage += "SrNo : " + i + " Enter valid level in cell " + cellAddress + ";\n";
						}
					} else {
						level = "easy";
					}

					Cell typeCell = row.getCell(9);
					if (typeCell != null && (!(row.getCell(9).getStringCellValue().equals("")))) {
						type = row.getCell(9).getStringCellValue().trim().toLowerCase();
					} else {
						type = "single";
					}

					if (row.getCell(10) != null) {
						imageName = row.getCell(10).getStringCellValue().trim().replaceAll("[\\t\\n\\r]+", " ");
					}

					Cell scoreCell = row.getCell(11);
					if (scoreCell != null) {
						score = (int) row.getCell(11).getNumericCellValue();
					} else {
						score = 1;
					}

					for (String q : question.getSelectedQuizIds()) {
						Quiz quizId = quizRepository.findByQuizId(q);
						if (quizId != null) {
							MapOfQuizNameAndIds.put(q, quizId.getQuizTitle());
							selectedQuizIds.add(q);
						}
					}

					questions.setSelectedQuizIds(selectedQuizIds);
					questions.setQuizIdsAndNames(MapOfQuizNameAndIds);
					questions.setQuestionTitletext(HtmlUtils.htmlEscape(questionTitletext));
					questions.setOption1(HtmlUtils.htmlEscape(option1));
					questions.setOption2(HtmlUtils.htmlEscape(option2));
					questions.setOption3(HtmlUtils.htmlEscape(option3));
					questions.setOption4(HtmlUtils.htmlEscape(option4));
					questions.setOption5(HtmlUtils.htmlEscape(option5));
					questions.setOption6(HtmlUtils.htmlEscape(option6));
					questions.setCorrectOption(correctOption);
					questions.setLevel(level);
//					e.setType(HtmlUtils.htmlEscape(type));
					questions.setType(type);
					questions.setImageName(HtmlUtils.htmlEscape(imageName));
					questions.setScore(score);

//					questions.setQuizId(question.getQuizId());
//					questions.setSelectQuiz(quiz.getQuizTitle());
//					questions.setSelectedQuizIds(question.getSelectedQuizIds());

					new Date();
					bulkList.add(questions);
				}

				if (errorMessage.length() > 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new GlobalResponse(null, errorMessage, false));
				} else {
					singleChoiceQuestion.saveAll(bulkList);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(new GlobalResponse(null, "Questions added to DB", true));
	}

	// method to upload emails in Challenge list using excel
	public ResponseEntity<?> uploadEmailsInChallengeList(MultipartFile file, String excelUpload) {

		Map<String, String> errorMessages = new HashMap<>();
		List<String> emailList = new ArrayList<>();
		List<String> encryptedEnailList = new ArrayList<>();

		String errorMessage = "";

		long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024; // file size calculation

		ChallengeList challengeList = null;
		XSSFWorkbook workbook;
		Tika tika = new Tika();

		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		try {
			// deserialization of JSON (String ) to java object . JSON will be mapped to
			// challengeList class
			challengeList = mapper.readValue(excelUpload, ChallengeList.class);
			ChallengeList ChallengeList = challengeListRepository
					.findByChallengeListId(challengeList.getChallengeListId());

			if (ChallengeList == null) {
				errorMessages.put("Challenge id", "Invalid challenge list id");
			}

			String mimeType = tika.detect(file.getInputStream());

			// file empty validation
			if (file.isEmpty()) {
				errorMessages.put("File", "File cannot be empty");
			}

			// double extension validation
			if (file.getOriginalFilename().split("\\.").length > 2) {
				errorMessages.put("Invalid file", "Multiple[dot] extension found");
			}

			// file format validation
			if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				errorMessages.put("Invalid file", "only xlsx file formst supported");
			}

			/* mime type validation */
			if (!isSupportedContentType(mimeType)) {
				errorMessages.put("Invalid file", "Invlaid XLSX file");
			}

			// file size validation
			if (fileSizeInMB > 2) {
				errorMessages.put("file size", "Allowed file size is upto 2 MB");
			}

			if (errorMessages.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
			} else {
				workbook = new XSSFWorkbook(file.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				DataFormatter formatter = new DataFormatter();

				for (int i = 1; i <= worksheet.getLastRowNum(); i++) {

					XSSFRow row = worksheet.getRow(i);

					/* To check if the complete row is empty */
					if (row == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new GlobalResponse(null, "Error: Row " + i + " is empty", false));
					}

					Cell emails = row.getCell(0);
					String email = formatter.formatCellValue(emails);

					if (!email.matches(emailRegex)) { // validating email format
						errorMessage = "Enter valid email in the cell " + i;
					} else {
						emailList.add(email);
					}
				}
				if (errorMessage.length() > 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new GlobalResponse(null, errorMessage, false));
				} else {

					challengeList.setChallengeListName(ChallengeList.getChallengeListName());
					challengeList.setStartDate(ChallengeList.getStartDate());
					challengeList.setEndDate(ChallengeList.getEndDate());
					challengeList.setPublish(ChallengeList.getPublish());
					challengeList.setChallenges(ChallengeList.getChallenges());
					challengeList.setCreatedDate(ChallengeList.getCreatedDate());

					for (String email : emailList) {
						encryptedEnailList.add(encryptDecryptHandler.encrypt(email)); // adding encryoted email to the
																						// list
					}
					challengeList.setEmailList(encryptedEnailList);
					challengeListRepository.save(challengeList);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(new GlobalResponse(null, "emails added to DB", true));
	}

	// method to upload emails in Quiz using excel
	public ResponseEntity<?> uploadEmailsInQuiz(MultipartFile file, String excelUpload) {

		Map<String, String> errorMessages = new HashMap<>();
		List<String> emailList = new ArrayList<>();
		List<String> encryptedEnailList = new ArrayList<>();

		Quiz quiz = null;
		Tika tika = new Tika();
		XSSFWorkbook workbook;

		long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024; // file size calculation

		String errorMessage = "";
		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		try {
			quiz = mapper.readValue(excelUpload, Quiz.class);

			Quiz Quiz = quizRepository.findByQuizId(quiz.getQuizId());

			if (Quiz == null) {
				errorMessages.put("Quiz id", "Invalid Quiz id");
			}

			String mimeType = tika.detect(file.getInputStream());

			// file empty validation
			if (file.isEmpty()) {
				errorMessages.put("File", "File cannot be empty");
			}

			// double extension validation
			if (file.getOriginalFilename().split("\\.").length > 2) {
				errorMessages.put("Invalid file", "Multiple[dot] extension found");
			}

			// file format validation
			if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				errorMessages.put("Invalid file", "only xlsx file formst supported");
			}

			/* mime type validation */
			if (!isSupportedContentType(mimeType)) {
				errorMessages.put("Invalid file", "Invlaid XLSX file");
			}

			// file size validation
			if (fileSizeInMB > 2) {
				errorMessages.put("file size", "Allowed file size is upto 2 MB");
			}

			if (errorMessages.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
			} else {
				workbook = new XSSFWorkbook(file.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);
				DataFormatter formatter = new DataFormatter();

				for (int i = 1; i <= worksheet.getLastRowNum(); i++) {

					XSSFRow row = worksheet.getRow(i);

					/* To check if the complete row is empty */
					if (row == null) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new GlobalResponse(null, "Error: Row " + i + " is empty", false));
					}

					Cell emails = row.getCell(0);
					String email = formatter.formatCellValue(emails);

					if (!email.matches(emailRegex)) { // validating email format
						errorMessage = "Enter valid email in the cell " + i;
					} else {
						emailList.add(email);
					}
				}
				if (errorMessage.length() > 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new GlobalResponse(null, errorMessage, false));
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
					for (String email : emailList) {
						encryptedEnailList.add(encryptDecryptHandler.encrypt(email)); // adding encrypted email to the
																						// list
					}
					quiz.setEmailList(encryptedEnailList);
					quizRepository.save(quiz);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResponseEntity.ok(new GlobalResponse(null, "emails added to DB", true));
	}

	// method to upload image in folder
	public ResponseEntity<GlobalResponse> uploadImage(MultipartFile file) throws IOException {

		Map<String, String> errorMessages = new HashMap<>();

		Tika tika = new Tika();
		String mimeType = tika.detect(file.getInputStream());

		long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024;

		/* File empty validation */
		if (file.isEmpty()) {
			errorMessages.put("File", "File cannot be empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("failed due to following reasons", errorMessages, false));
		}
		/* Multiple dots extension validation */
		if (file.getOriginalFilename().split("\\.").length > 2) {
			errorMessages.put("Invalid file", "Multiple[dot] extension found");
		}

		/* Mime type validation */
		if (!isSupportedContentTypeForImage(mimeType)) {
			errorMessages.put("Invalid file", "Invlaid file format");
		}

		/* Size validation */
		if (fileSizeInMB > 1) {
			errorMessages.put("file size", "Allowed file size is upto 1 MB");
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("failed due to following reasons", errorMessages, false));
		} else {

			// taking original file name
			String originalFileName = file.getOriginalFilename();

			// if file name has space in the name , replace it with underscore
			if (originalFileName.contains(" ")) {
				originalFileName = originalFileName.replace(" ", "_");
			}

			Files.copy(file.getInputStream(), Paths.get(imageUpload + File.separator + originalFileName),
					StandardCopyOption.REPLACE_EXISTING); // saving images in the folder (location - c:/uploads)

			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(null, "File uploaded successfully", true));
		}
	}

	// method to upload multiple images in folder
	public ResponseEntity<GlobalResponse> uploadCertificate(MultipartFile[] files) {

		List<String> filenames = new ArrayList<>();
		Map<String, String> errorMessages = new HashMap<>();

		// max only three files are allowed
		if (files.length > 3) {
			errorMessages.put("Invalid file", "Maximum three files are allowed");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("failed due to following reasons", errorMessages, false));
		}

		// to check all the files validations
		for (MultipartFile file : files) {

			try {
				long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024;
				Tika tika = new Tika();
				String mimeType = tika.detect(file.getInputStream());

				// if file is empty
				if (file.isEmpty()) {
					errorMessages.put("File", "File cannot be empty");
				}

				// file size validation
				if (fileSizeInMB > 1) {
					errorMessages.put("file size", "Allowed file size is upto 1 MB");
				}

				// double extension validation
				if (file.getOriginalFilename().split("\\.").length > 2) {
					errorMessages.put("Invalid file", "Multiple[dot] extension found");
				}

				if (!isSupportedContentTypeForJasper(mimeType)) {
					errorMessages.put("Invalid file", "Invlaid file format");
				}

				if (errorMessages.size() > 0) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new GlobalResponse("failed due to following reasons", errorMessages, false));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// to upload all the files in folder
		for (MultipartFile file : files) {
			String filename = file.getOriginalFilename();
			try {
				// location to upload files , files will be added in /static/report in project
				// folder
				File fileLocation = new ClassPathResource("/static/reports").getFile();
				String filePath = fileLocation + File.separator + filename;
				File destinationFile = new File(filePath);

				file.transferTo(destinationFile);
				filenames.add(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse(null, "File uploaded successfully", true));
	}

	// method to download File
	public ResponseEntity<?> downloadImage(String fileName) {

		// appending URL to the fileName
		String imageURL = basePath + "/" + path + "/" + fileName;

		// retrieving the image data as bytes array
		byte[] imageBytes = fetchImageBytes(imageURL);

		if (imageBytes != null) {
			// object of HTTP headers to set response headers
			HttpHeaders headers = new HttpHeaders();

			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

			// specifying the media type which is set to APPLICATION_OCTET_STREAM which is
			// set to any time of file
			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

			headers.setContentType(mediaType);

			return ResponseEntity.ok().headers(headers).contentLength(imageBytes.length).contentType(mediaType)
					.body(imageBytes);
		} else {
			return ResponseEntity.ok(new GlobalResponse("Invalid Image Name"));
		}

	}

	private byte[] fetchImageBytes(String imageURL) {

		try {
			// creating the object of URL from the given imageURL
			URL url = new URL(imageURL);

			// open connection using url connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// set request method to GET
			connection.setRequestMethod("GET");

			// check if HTTP response code is 200 or not
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				// take an input stream to read the response data
				InputStream inputStream = connection.getInputStream();

				// creating an byte output stream to store image data
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int bytesRead;

				// read data from input stream and write it to output stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				return outputStream.toByteArray();
			} else {
				// handle error here if image name is not valid
				return null;
			}
		} catch (Exception e) {
			// Handle exceptions, e.g., network errors or invalid URLs
			e.printStackTrace();
			return null;
		}
	}

	/* Method for mime type validation for XLSX file format */
	private boolean isSupportedContentType(String contentType) {
		return contentType.equals("application/zip") || contentType.equals("application/x-tika-ooxml");
	}

	/* Method for mime type validation for image type file format */
	private boolean isSupportedContentTypeForImage(String contentType) {
		return contentType.equals("image/jpeg") || contentType.equals("image/png");
	}

	// method for mime type validation for jasper,jrxml and image type file format
	private boolean isSupportedContentTypeForJasper(String contentType) {
		return contentType.equals("application/octet-stream") || contentType.equals("application/x-jasper")
				|| contentType.equals("application/xml") || contentType.equals("text/xml")
				|| contentType.equals("image/jpeg") || contentType.equals("image/png");
	}

}
