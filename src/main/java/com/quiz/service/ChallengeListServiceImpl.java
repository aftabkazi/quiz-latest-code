package com.quiz.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.Challenge;
import com.quiz.entity.ChallengeList;
import com.quiz.entity.ChallengeListDTO;
import com.quiz.entity.Hint;
import com.quiz.exception.challenge.ChallengeListNotFoundException;
import com.quiz.exception.user.TokenExpiryException;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.repository.ChallengeListRepository;
import com.quiz.repository.ChallengeRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;
import com.quiz.service.interfaces.ChallengeListService;

@Service
public class ChallengeListServiceImpl implements ChallengeListService {

	@Autowired
	private ChallengeListRepository challengeListRepository;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	@Value("${extern.resourse.ipaddress}")
	private String basePath;

	@Value("${extern.resources.Dir}")
	private String path;

	// method to post challenge
	@Override
	public ResponseEntity<GlobalResponse> addChallengeList(ChallengeList challengeList) {

		List<Challenge> challengesList = challengeRepository.findAll();

		Map<String, String> errorMessages = new HashMap<>();
		List<String> challengesNames = challengeList.getChallenges();
		List<String> challengeIds = new ArrayList<>();

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedCurrentDate = currentDate.format(formatter);
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

		if (challengeList.getChallengeListName() == null || challengeList.getChallengeListName().isBlank()) {
			errorMessages.put("ChallengeList Name", "Enter CTF name");
		} else {
			String regex = "[a-zA-Z ]*$";
			Boolean validChallengeListName = Pattern.compile(regex).matcher(challengeList.getChallengeListName().trim())
					.matches();
			if (validChallengeListName == false) {
				errorMessages.put("Challenge Name", "Only characters are allowed");
			}
		}

		if (challengeList.getPublish() == null) {
			challengeList.setPublish(true);
		}

		if (challengeList.getVisibility() == null || challengeList.getVisibility().isEmpty()) {
			errorMessages.put("Visibility", " Visibility cannot be null");
		} else if (!(challengeList.getVisibility().equals("publish")
				|| challengeList.getVisibility().equals("unPublish")
				|| challengeList.getVisibility().equals("admin"))) {
			errorMessages.put("Visibility", "Enter valid visibility");
		}

		// searching for a challenge with a specific name in the challengeList if
		// challenge list is not null
		if (challengesNames != null && challengesNames.size()>0) {
			for (String name : challengesNames) {

				// checking if specified name matches or not if matches then taking id of that
				// challenge name
				Optional<String> idOptional = challengesList.stream()
						.filter(challenge -> challenge.getChallengeName().equalsIgnoreCase(name.trim()))
						.map(Challenge::getChallengeId).findFirst(); // taking id of the challenge whose name matches

				if (idOptional.isPresent()) {
					String id = idOptional.get();
					challengeIds.add(id); // taking id and adding in the list
				} else {
					errorMessages.put("Challenge List", "challenge name is not present");
				}
			}
		} else {
			errorMessages.put("challenge name ", "select atleast one challenge ");
		}

		if (challengeList.getStartDate() == null || challengeList.getStartDate().isBlank()
				|| challengeList.getStartDate().equals("NA")) {

			if (!challengeList.getEndDate().equals("NA")) {
				errorMessages.put("Start Date", "Enter start date");
			} else {
				challengeList.setStartDate("NA");
				challengeList.setEndDate("NA");
			}

		} else {

//			String dateRegex = "\\b\\d{1,2}/\\d{1,2}/\\d{4}, \\d{1,2}:\\d{2}:\\d{2} [ap]m\\b";
			String dateRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}, (0[0-9]|1[0-2]):[0-5][0-9]:[0-5][0-9] (am|pm)$";

			Boolean validStartDate = Pattern.compile(dateRegex).matcher(challengeList.getStartDate().trim()).matches();

			if (!validStartDate) {
				errorMessages.put("Start Date", "Enter Valid Start date");
			}

			if (challengeList.getEndDate() == null || challengeList.getEndDate().isBlank()
					|| challengeList.getEndDate().equals("NA")) {
				errorMessages.put("End date", "Enter End date");
			} else {
				Boolean validEndDate = Pattern.compile(dateRegex).matcher(challengeList.getEndDate().trim()).matches();
				if (validEndDate == false) {
					errorMessages.put("End Date", "Enter Valid End Date");
				}
			}
			try {
				Date current = formatter1.parse(formattedCurrentDate);
				Date startDate = formatter1.parse(challengeList.getStartDate());
				Date endDate = formatter1.parse(challengeList.getEndDate());

				if (current.compareTo(startDate) > 0) {
					errorMessages.put("Start Date", "Start Date cannot be less than current date");
				}
				if (startDate.compareTo(endDate) > 0) {
					errorMessages.put("End Date", "End Date cannot be less than Start Date");
				}
			}

			catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {
			
			challengeList.setChallenges(challengeIds);
			challengeListRepository.save(challengeList);
			return ResponseEntity.status(HttpStatus.OK).body(
					new GlobalResponse(challengeList.getChallengeListId(), "Challenge list added sucessfully", true));
		}
	}

	// method to update challengeList
	@Override
	public ResponseEntity<GlobalResponse> updateChallengeList(ChallengeList challengeList) {

		ChallengeList challengeListId = challengeListRepository
				.findByChallengeListId(challengeList.getChallengeListId());

		List<Challenge> challengesList = challengeRepository.findAll();

		if (challengeListId == null) {
			throw new ChallengeListNotFoundException();
		}
		Map<String, String> errorMessages = new HashMap<>();
		List<String> challengesNames = challengeList.getChallenges();
		List<String> challengeIds = new ArrayList<>();

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedCurrentDate = currentDate.format(formatter);
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");

		if (challengeList.getChallengeListName() == null || challengeList.getChallengeListName().isBlank()) {
			errorMessages.put("ChallengeList Name", "Enter CTF name");
		} else {
			String regex = "[a-zA-Z ]*$";
			Boolean validChallengeListName = Pattern.compile(regex).matcher(challengeList.getChallengeListName().trim())
					.matches();
			if (validChallengeListName == false) {
				errorMessages.put("Challenge Name", "Only characters are allowed");
			}
		}

		if (challengeList.getPublish() == null) {
			challengeList.setPublish(true);
		}

		if (challengeList.getVisibility() == null || challengeList.getVisibility().isEmpty()) {
			errorMessages.put("Visibility", " Visibility cannot be null");
		} else if (!(challengeList.getVisibility().equals("publish")
				|| challengeList.getVisibility().equals("unPublish")
				|| challengeList.getVisibility().equals("admin"))) {
			errorMessages.put("Visibility", "Enter valid visibility");
		}

		// searching for a challenge with a specific name in the challengeList if
		// challenge list is not null
		if (challengesNames != null && challengesNames.size()>0) {
			for (String name : challengesNames) {
				Optional<String> idOptional = challengesList.stream()
						.filter(challenge -> challenge.getChallengeName().equalsIgnoreCase(name.trim())) // checking if
																									// specified
																									// name matches or
																									// not
						.map(Challenge::getChallengeId).findFirst(); // taking id of the challenge whose name matches

				if (idOptional.isPresent()) {
					String id = idOptional.get();
					challengeIds.add(id); // taking id and adding in the list
				} else {
					errorMessages.put("Challenge List", "challenge name is not present");
				}
			}
		}
		else {
			errorMessages.put("challenge name ", "select atleast one challenge ");
		}

		if (challengeList.getStartDate() == null || challengeList.getChallengeListName().isBlank()
				|| challengeList.getStartDate().equals("NA")) {

			if (!challengeList.getEndDate().equals("NA")) {
				errorMessages.put("Start Date", "Enter start date");
			} else {
				challengeList.setStartDate("NA");
				challengeList.setEndDate("NA");
			}

		} else {
//			String dateRegex = "\\b\\d{1,2}/\\d{1,2}/\\d{4}, \\d{1,2}:\\d{2}:\\d{2} [ap]m\\b";
			String dateRegex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}, (0[0-9]|1[0-2]):[0-5][0-9]:[0-5][0-9] (am|pm)$";
			Boolean validStartDate = Pattern.compile(dateRegex).matcher(challengeList.getStartDate().trim()).matches();

			if (!validStartDate) {
				errorMessages.put("Start Date", "Enter Valid Start date");
			}

			if (challengeList.getEndDate() == null || challengeList.getEndDate().isBlank()
					|| challengeList.getEndDate().equals("NA")) {
				errorMessages.put("End date", "Enter End date");
			} else {
				Boolean validEndDate = Pattern.compile(dateRegex).matcher(challengeList.getEndDate().trim()).matches();
				if (validEndDate == false) {
					errorMessages.put("End Date", "Enter Valid End Date");
				}
			}
			try {
//				Date current = formatter1.parse(formattedCurrentDate);
				Date startDate = formatter1.parse(challengeList.getStartDate());
				Date endDate = formatter1.parse(challengeList.getEndDate());

//				if (current.compareTo(startDate) > 0) {
//					errorMessages.put("Start Date", "Start Date cannot be less than current date");
//				}
				
				if (startDate.compareTo(endDate) > 0) {
					errorMessages.put("End Date", "End Date cannot be less than Start Date");
				}
			}

			catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {
			challengeList.setCreatedDate(challengeListId.getCreatedDate());
			challengeList.setChallenges(challengeIds);
			challengeList.setEmailList(challengeListId.getEmailList());

			challengeListRepository.save(challengeList);
			return ResponseEntity.status(HttpStatus.OK).body(
					new GlobalResponse(challengeList.getChallengeListId(), "Challenge list updated sucessfully", true));
		}

	}

	// method to get all challenges
	@Override
	public ResponseEntity<List<ChallengeListDTO>> getAllChallengesList() {

		List<ChallengeList> challengesList = challengeListRepository.findAll();

		List<ChallengeListDTO> dtoList = new ArrayList<>();
		List<String> decryptedEmails = new ArrayList<>();
	
		for (ChallengeList challengeList : challengesList) {

			List<String> emails = challengeList.getEmailList();

			if (emails != null) {
				for (String email : emails) {
					decryptedEmails.add(encryptDecryptHandler.decrypt(email));
				}
			}

			ChallengeListDTO challengeListDTO = new ChallengeListDTO();

			challengeListDTO.setStartDate(challengeList.getStartDate());
			challengeListDTO.setEndDate(challengeList.getEndDate());
			challengeListDTO.setPublish(challengeList.getPublish());
			challengeListDTO.setChallengeListId(challengeList.getChallengeListId());
			challengeListDTO.setChallengeListName(challengeList.getChallengeListName());
			challengeListDTO.setEmailList(decryptedEmails);
			challengeListDTO.setCreatedDate(challengeList.getCreatedDate());
			challengeListDTO.setUpdatedDate(challengeList.getUpdatedDate());
			challengeListDTO.setVisibility(challengeList.getVisibility());

			List<String> challengesById = challengeList.getChallenges();

			List<Challenge> challengeEntities = StreamSupport
					.stream(challengeRepository.findAllById(challengesById).spliterator(), false)
					.collect(Collectors.toList());

			for (Challenge challenge : challengeEntities) {

				challenge.setFiles(challenge.getFiles().stream().map(file -> basePath + "/" + path + "/" + file)
						.collect(Collectors.toList()

						));
			}
			challengeListDTO.setChallenges(challengeEntities);
			dtoList.add(challengeListDTO);
		}

		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	// method to get challenges by chalenge list id
	@Override
	public ResponseEntity<List<ChallengeListDTO>> getChallengeListById(String challengeListId) {

		ChallengeList challengesList = challengeListRepository.findByChallengeListId(challengeListId);
		List<ChallengeListDTO> dtoList = new ArrayList<>();
		List<String> decryptedEmails = new ArrayList<>();

		if (challengesList == null) {
			throw new ChallengeListNotFoundException();
		} else {

			List<String> emails = challengesList.getEmailList();
			if (emails != null) {
				for (String email : emails) {
					decryptedEmails.add(encryptDecryptHandler.decrypt(email)); // decrypting email and adding to the
																				// list
				}
			}

			ChallengeListDTO challengeListDTO = new ChallengeListDTO();

			challengeListDTO.setStartDate(challengesList.getStartDate());
			challengeListDTO.setEndDate(challengesList.getEndDate());
			challengeListDTO.setPublish(challengesList.getPublish());
			challengeListDTO.setChallengeListId(challengesList.getChallengeListId());
			challengeListDTO.setChallengeListName(challengesList.getChallengeListName());
			challengeListDTO.setCreatedDate(challengesList.getCreatedDate());
			challengeListDTO.setEmailList(decryptedEmails); // adding list of decrypted emails to the challengeListDTO
			challengeListDTO.setUpdatedDate(challengesList.getUpdatedDate());
			challengeListDTO.setVisibility(challengesList.getVisibility());

			List<String> challengesById = challengesList.getChallenges();

			List<Challenge> challengeEntities = StreamSupport
					.stream(challengeRepository.findAllById(challengesById).spliterator(), false)
					.collect(Collectors.toList());

			for (Challenge challenge : challengeEntities) {
				Map<String, Hint> hints = challenge.getHints();
				Set<String> hintIds = hints.keySet();
				Map<String, Hint> hintIdMap = new HashMap<>();

				for (String hintId : hintIds) {
					hintIdMap.put(hintId, null); // Populate the map with hint IDs and null values
				}
				challenge.setHints(hintIdMap);
				challenge.setFlags(null);

				challenge.setFiles(challenge.getFiles().stream().map(file -> basePath + "/" + path + "/" + file)
						.collect(Collectors.toList()

						));
			}
			challengeListDTO.setChallenges(challengeEntities);
			dtoList.add(challengeListDTO);

		}
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}

	// method to delete challenge by id
	@Override
	public ResponseEntity<GlobalResponse> deleteChallengeById(String challengeListId) {

		ChallengeList challengeList = challengeListRepository.findByChallengeListId(challengeListId);

		if (challengeList == null) {
			throw new ChallengeListNotFoundException();
		}

		challengeListRepository.deleteById(challengeListId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new GlobalResponse(null, "ChallengeList deleted sucessfully", true));
	}

	// method to update email's in the challenge list
	@Override
	public ResponseEntity<GlobalResponse> updateEmailsforChallengesList(ChallengeList challengeList) {

		ChallengeList challengeListData = challengeListRepository
				.findByChallengeListId(challengeList.getChallengeListId());

		if (challengeListData == null) { // if challengeListId is invalid
			throw new ChallengeListNotFoundException();
		} else {
			List<String> listOfEmails = new ArrayList<>();

			challengeList.setChallengeListName(challengeListData.getChallengeListName());
			challengeList.setStartDate(challengeListData.getStartDate());
			challengeList.setEndDate(challengeListData.getEndDate());
			challengeList.setPublish(challengeListData.getPublish());
			challengeList.setChallenges(challengeListData.getChallenges());
			challengeList.setCreatedDate(challengeListData.getCreatedDate());
			challengeList.setVisibility(challengeListData.getVisibility());
			for (String email : challengeList.getEmailList()) {

				try {
					listOfEmails.add(encryptDecryptHandler.encrypt(email)); // encrypt email and add to the list
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			challengeList.setEmailList(listOfEmails);
		}
		challengeListRepository.save(challengeList);

		return ResponseEntity.status(HttpStatus.OK).body(
				new GlobalResponse(challengeList.getChallengeListId(), "ChallengeList Updated Successfully", true));
	}

	// method to get challenges by white listed email's
	@Override
	public ResponseEntity<List<ChallengeListDTO>> getChallengeByWhiteListedEmails() {

		List<ChallengeList> challengesList = challengeListRepository.findAll();
		String email = AuthTokenFilter.userEmail.get(); // taking email from the token

		List<String> emailList = new ArrayList<>();
		List<ChallengeListDTO> dtoList = new ArrayList<>();

		if (email == null) { // if token has expired
			throw new TokenExpiryException();
		} else {
			challengesList.stream()
					.filter(challengeList -> challengeList.getEmailList() != null
							&& challengeList.getEmailList().contains(email) && challengeList.getPublish())
					.map(challengeList -> {

						ChallengeListDTO challengeListDTO = new ChallengeListDTO();
						emailList.add(email);
						challengeListDTO.setChallengeListId(challengeList.getChallengeListId());
						challengeListDTO.setChallengeListName(challengeList.getChallengeListName());
						challengeListDTO.setStartDate(challengeList.getStartDate());
						challengeListDTO.setEndDate(challengeList.getEndDate());
						challengeListDTO.setPublish(challengeList.getPublish());
						challengeListDTO.setStartDate(challengeList.getStartDate());
						challengeListDTO.setEndDate(challengeList.getEndDate());
						challengeListDTO.setEmailList(emailList);
						challengeListDTO.setCreatedDate(challengeList.getCreatedDate());
						challengeListDTO.setUpdatedDate(challengeList.getUpdatedDate());
						challengeListDTO.setVisibility(challengeList.getVisibility());

						List<String> challengesById = challengeList.getChallenges();

						List<Challenge> challengeEntities = StreamSupport
								.stream(challengeRepository.findAllById(challengesById).spliterator(), false)
								.collect(Collectors.toList());

						for (Challenge challenge : challengeEntities) {

							Map<String, Hint> hints = challenge.getHints();
							Set<String> hintIds = hints.keySet();
							Map<String, Hint> hintIdMap = new HashMap<>();

							for (String hintId : hintIds) {
								hintIdMap.put(hintId, null); // Populate the map with hint IDs and null values
							}
							challenge.setHints(hintIdMap);
							challenge.setFlags(null);
							challenge.setFiles(challenge.getFiles().stream()
									.map(file -> basePath + "/" + path + "/" + file).collect(Collectors.toList()

									));
						}
						challengeListDTO.setChallenges(challengeEntities);
						dtoList.add(challengeListDTO);
						return challengeListDTO;
					}).collect(Collectors.toList());

			return ResponseEntity.ok(dtoList);
		}
	}

}
