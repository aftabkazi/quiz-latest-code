package com.quiz.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.entity.Challenge;
import com.quiz.entity.Flag;
import com.quiz.entity.Hint;
import com.quiz.exception.challenge.ChallengeNotFoundException;
import com.quiz.repository.ChallengeRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.service.interfaces.ChallengeService;

@Service
public class ChallengeServiceImpl implements ChallengeService {

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ObjectMapper mapper;

	@Value("${extern.resoures.path}")
	private String imageUpload;

	@Value("${extern.resourse.ipaddress}")
	private String basePath;

	@Value("${extern.resources.Dir}")
	private String path;

	// method to add challenges
	@Override
	public ResponseEntity<GlobalResponse> addChallenge(String challenge, List<MultipartFile> files) throws IOException {

		Challenge challengeEntity = new Challenge();
		Map<String, String> errorMessages = new HashMap<>();
		List<String> fileNames = new ArrayList<>();

		try {
			challengeEntity = mapper.readValue(challenge, Challenge.class);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		if (challengeEntity.getChallengeName() == null || challengeEntity.getChallengeName().isBlank()) {
			errorMessages.put("challange name", "Enter challenge name");
		} else {
			String regex = "^[a-zA-Z0-9\s]*$";
			Boolean validChallengeName = Pattern.compile(regex).matcher(challengeEntity.getChallengeName().trim())
					.matches();
			if (!validChallengeName) {
				errorMessages.put("Challenge name", "Only characters , numbers and space is allowed");
			}
		}

		if (challengeEntity.getDescription() == null || challengeEntity.getDescription().isBlank()) {
			errorMessages.put("description", "Enter description");
		}

		if (challengeEntity.getValue() == null) {
			errorMessages.put("value", "Enter value");
		}
		if (challengeEntity.getFlags() == null || challengeEntity.getFlags().isEmpty()) {
			errorMessages.put("flag", "Enter flag");
		} else {
			for (Map.Entry<String, Flag> entry : challengeEntity.getFlags().entrySet()) {
					
				if (entry.getValue().getFlag().trim() == null || entry.getValue().getFlag().trim().isBlank()) {
					errorMessages.put("Flag", "Enter flag value");
				}

				if (entry.getValue().getIsCaseSensitive() == null) {
					entry.getValue().setIsCaseSensitive(true);
				}
			}
		}
		
		if (challengeEntity.getState() != null && !challengeEntity.getState().isBlank()) {
			if (!(challengeEntity.getState().equals("visible") || challengeEntity.getState().equals("hidden"))) {
				errorMessages.put("state", "Enter valid state");
			} 
		}else {
			errorMessages.put("state", "Enter State");
		}
			
		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {

			if (files != null) {
				for (MultipartFile file : files) {

					String originalFileName = file.getOriginalFilename();

					long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024;
					String fileName = file.getOriginalFilename();

					if (file.getOriginalFilename().split("\\.").length > 2) { // double extension file validation
						errorMessages.put("Invalid file", "Multiple[dot] extension found");
					}

					if (fileSizeInMB > 1) { // file size validation
						errorMessages.put("file size", "Allowed file size is upto 1 MB");
					}

					if (originalFileName.contains(" ")) {
						originalFileName = originalFileName.replace(" ", "_");
					}

					if (errorMessages.size() > 0) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new GlobalResponse("failed due to following reasons", errorMessages, false));
					} else {
						Files.copy(file.getInputStream(), Paths.get(imageUpload + File.separator + originalFileName),
								StandardCopyOption.REPLACE_EXISTING);

						fileNames.add(fileName);
					}
				}
			}

			challengeEntity.setFiles(fileNames);
			challengeEntity.setNoOfAttempts(0);
			challengeRepository.save(challengeEntity);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(challengeEntity.getChallengeId(), "Challenge added sucessfully", true));
		}
	}

	// method to update challenge
	@Override
	public ResponseEntity<GlobalResponse> updateChallengeById(String challenge, List<MultipartFile> files)
			throws IOException {

		Challenge challengeEntity = new Challenge();
		Map<String, String> errorMessages = new HashMap<>();
		List<String> fileNames = new ArrayList<>();

		try {
			challengeEntity = mapper.readValue(challenge, Challenge.class);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		Challenge challengeId = challengeRepository.findByChallengeId(challengeEntity.getChallengeId());

		if (challengeId == null) {
			throw new ChallengeNotFoundException();
		}

		if (challengeEntity.getChallengeName() == null || challengeEntity.getChallengeName().isBlank()) {
			errorMessages.put("challange name", "Enter challenge name");
		} else {
			String regex = "^[a-zA-Z0-9\s]*$";
			Boolean validChallengeName = Pattern.compile(regex).matcher(challengeEntity.getChallengeName().trim())
					.matches();
			if (!validChallengeName) {
				errorMessages.put("Challenge name", "Only characters , numbers and space is allowed");
			}
		}

		if (challengeEntity.getDescription() == null || challengeEntity.getDescription().isBlank()) {
			errorMessages.put("description", "Enter description");
		}

		if (challengeEntity.getValue() == null) {
			errorMessages.put("value", "Enter value");
		}

		if (challengeEntity.getFlags() == null || challengeEntity.getFlags().isEmpty()) {
			errorMessages.put("flag", "Enter flag");
		} else {
			for (Map.Entry<String, Flag> entry : challengeEntity.getFlags().entrySet()) {

				if (entry.getValue().getFlag().trim() == null || entry.getValue().getFlag().trim().isBlank()) {
					errorMessages.put("Flag", "Enter flag value");
				}

				if (entry.getValue().getIsCaseSensitive() == null) {
					entry.getValue().setIsCaseSensitive(true); // by default case sensitive is taken as true
				}
			}
		}

		if (!(challengeEntity.getState().equals("visible") || challengeEntity.getState().equals("hidden"))) {
			errorMessages.put("state", "Enter valid state");
		}else {
			errorMessages.put("state", "Enter State");
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Error occured due to following reasons", errorMessages, false));
		} else {

			if (challengeEntity.getNoOfAttempts() == null) {
				challengeEntity.setNoOfAttempts(0);
			}

			if (files != null) {
				for (MultipartFile file : files) {

					String originalFileName = file.getOriginalFilename();

					long fileSizeInMB = Math.round(file.getSize() / 1024) / 1024;
					String fileName = file.getOriginalFilename();

					if (file.getOriginalFilename().split("\\.").length > 2) { // double extension file validation
						errorMessages.put("Invalid file", "Multiple[dot] extension found");
					}

					if (fileSizeInMB > 1) { // file size validation
						errorMessages.put("file size", "Allowed file size is upto 1 MB");
					}

					if (originalFileName.contains(" ")) {
						originalFileName = originalFileName.replace(" ", "_");
					}

					if (errorMessages.size() > 0) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new GlobalResponse("failed due to following reasons", errorMessages, false));
					} else {
						Files.copy(file.getInputStream(), Paths.get(imageUpload + File.separator + originalFileName),
								StandardCopyOption.REPLACE_EXISTING);

						fileNames.add(fileName);
					}
				}
			}
			fileNames.addAll(challengeId.getFiles());
			challengeEntity.setFiles(fileNames);
			challengeEntity.setCreatedDate(challengeId.getCreatedDate());
			challengeEntity.setFiles(fileNames);

			challengeRepository.save(challengeEntity);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GlobalResponse(challengeEntity.getChallengeId(), "Challenge updated sucessfully", true));
		}
	}

	// get all challenges without state
	@Override
	public ResponseEntity<List<Challenge>> getAllChallenges() {

		List<Challenge> challenges = challengeRepository.findAll();

		// appending URL to the filename
		challenges.forEach(challenge -> challenge.setFiles(challenge.getFiles().stream()
				.map(file -> basePath + "/" + path + "/" + file).collect(Collectors.toList())));

		// sorting list to get recently created challenge first
		challenges.sort(Comparator.comparing(Challenge::getCreatedDate).reversed());

		return ResponseEntity.ok(challenges);

	}

	// method to get all challenges by state (if state is hidden then it will not be
	// added in list)
	@Override
	public ResponseEntity<List<Challenge>> getAllChallangesByState() {
		List<Challenge> challenges = challengeRepository.findAll();

		// checking the state. is state is visible then it will be added in the list
		challenges = challenges.stream().filter(challenge -> challenge.getState().equals("visible"))
				.collect(Collectors.toList());

		// appending URL to the filename
		challenges.forEach(challenge -> challenge.setFiles(challenge.getFiles().stream()
				.map(file -> basePath + "/" + path + "/" + file).collect(Collectors.toList())));

		// sorting list to get recently created challenge first
		challenges.sort(Comparator.comparing(Challenge::getCreatedDate).reversed());

		return ResponseEntity.ok(challenges);
	}

	// method to get challenge by id
	@Override
	public ResponseEntity<Challenge> getChallangeById(String challengeId) {

		Challenge challenge = challengeRepository.findByChallengeId(challengeId);

		if (challenge == null) {
			throw new ChallengeNotFoundException();
		}

		// appending URL to the file name
		challenge.setFiles(challenge.getFiles().stream().map(file -> basePath + "/" + path + "/" + file)
				.collect(Collectors.toList()));

		return ResponseEntity.ok(challenge);
	}

	// method to delete challenge by id
	@Override
	public ResponseEntity<GlobalResponse> deleteChallengeById(String challengeId) {

		Challenge challenge = challengeRepository.findByChallengeId(challengeId);

		if (challenge == null) {
			throw new ChallengeNotFoundException();
		}
		challengeRepository.deleteById(challengeId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new GlobalResponse(null, "Challenge deleted Successfully", true));
	}

	// method to delete the file using challenge id
	@Override
	public ResponseEntity<GlobalResponse> deleteChallengeImage(String challengeId, String fileName) {

		Challenge challengeData = challengeRepository.findByChallengeId(challengeId);

		if (challengeData == null) {
			throw new ChallengeNotFoundException();
		}
		List<String> files = challengeData.getFiles();
		String filename = "";

		for (String file : files) {
			if (file.equals(fileName)) {
				filename = file;
			}
		}

		files.remove(filename); // removing the file name from the list
		challengeRepository.save(challengeData); // updating the latest list in the database

		return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse(null, "File removed ", true));
	}

	// method to get hint based on challenge id and hint key
	@Override
	public ResponseEntity<?> getHintsbasedonKey(String challengeId, String key) {

		Challenge challenge = challengeRepository.findByChallengeId(challengeId);

		HashMap<String, Hint> hintsHashMap = new HashMap<>();

		if (challenge != null) {

			// taking hints in a map
			Map<String, Hint> hints = challenge.getHints();

			// if key is valid then add the data in the hintsHashMap
			if (hints.containsKey(key)) {
				hintsHashMap.put(key, hints.get(key));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new GlobalResponse("Hint with key " + key + " not found."));
			}
		} else {
			throw new ChallengeNotFoundException();
		}
		return ResponseEntity.ok(hintsHashMap);
	}

}
