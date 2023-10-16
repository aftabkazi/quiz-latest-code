package com.quiz.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.ChallengeList;
import com.quiz.entity.ERole;
import com.quiz.entity.Leaderboard;
import com.quiz.entity.Role;
import com.quiz.exception.challenge.ChallengeListNotFoundException;
import com.quiz.repository.ChallengeListRepository;
import com.quiz.repository.LeaderboardRepository;
import com.quiz.repository.UserRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;
import com.quiz.service.interfaces.LeaderboardService;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

	@Autowired
	private LeaderboardRepository leaderboardReository;

	@Autowired
	private UserRepository userReository;

	@Autowired
	private ChallengeListRepository challengeListRepository;

	@Override
	public ResponseEntity<?> getScoreBasedOnUserName() {

		List<Leaderboard> allUsersScore = leaderboardReository.findAll();

		// map to store topScore
		Map<String, Integer> topScores = new HashMap<>();

		// Map to store user IDs
		Map<String, String> userIdsMap = new HashMap<>();

		// Map to store last timestamps of a user
		Map<String, String> timestampsMap = new HashMap<>();

		for (Leaderboard leaderboard : allUsersScore) {

			// generate a key that combines username and challengeName
			String key = leaderboard.getUsername() + "_" + leaderboard.getChallengeName();
			String username = leaderboard.getUsername();
			String timestamp = leaderboard.getTimeStamp();

			// score contains score of a user in a particular challenge
			int score = leaderboard.getScore();

			// Check if the score is higher than the previously stored top score for this
			// user and challenge
			if (!topScores.containsKey(key) || score > topScores.get(key)) {
				topScores.put(key, score);
			}

			// if there is no time stamp for the user, update in the timeStampMap
			if (timestampsMap.get(username) == null) {
				timestampsMap.put(username, timestamp);
			} else {

				// defining the format of date
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");

				try {
					// parsing the date to 24 hours and updating is the new time is more recent
					// i.e. this is the last entry of this user
					Date date1 = inputFormat.parse(timestampsMap.get(username));
					Date date2 = inputFormat.parse(timestamp);

					if (date1 == null || date2.after(date1)) {
						timestampsMap.put(username, timestamp);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		// map to store total top score for each user
		Map<String, Integer> totalTopScores = new HashMap<>();

		// map to store total top score for each user
		for (Map.Entry<String, Integer> entry : topScores.entrySet()) {

			String[] parts = entry.getKey().split("_"); // username_challengeName
			String username = parts[0]; // taking user name
			int topScore = entry.getValue(); // taking top score of the user

			// Add the top score to the total top score for the user
			totalTopScores.merge(username, topScore, Integer::sum);
		}

		Map<String, Integer> sortedScore = totalTopScores.entrySet().stream().sorted((entry1, entry2) -> {
			int scoreComparison = entry2.getValue().compareTo(entry1.getValue());
			if (scoreComparison != 0) {
				return scoreComparison;
			} else {
				String username1 = entry1.getKey();
				String username2 = entry2.getKey();
				String timestamp1 = timestampsMap.get(username1);
				String timestamp2 = timestampsMap.get(username2);
				return timestamp1.compareTo(timestamp2);
			}
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		// map to return the JSON format
		Map<Integer, Object> responseMap = new LinkedHashMap<>();
		int counter = 1;
		for (Map.Entry<String, Integer> entry : sortedScore.entrySet()) {
			String username = entry.getKey();

			int score = entry.getValue();
			String userId = userIdsMap.get(username); // Get the user ID from the userIdsMap
			String timestamp = timestampsMap.get(username);

			// Create a new map to store userName, userId, and score
			Map<String, Object> userScoreMap = new HashMap<>();
			userScoreMap.put("username", username);
			userScoreMap.put("userId", userId);
			userScoreMap.put("score", score);
			userScoreMap.put("Timestamp", timestamp);

			// Add the userScoreMap to the responseMap
			responseMap.put(counter++, userScoreMap);
		}

		return ResponseEntity.ok(responseMap);
	}

	@Override
	public ResponseEntity<?> getScoreBasedOnChallengeListIdAndUserName(String challengeListId) {

		ChallengeList challengeList = challengeListRepository.findByChallengeListId(challengeListId);

		if (challengeList == null) {
			throw new ChallengeListNotFoundException();
		}

		// if visibility is unPublished
		if (challengeList.getVisibility().equals("unPublish")) {
			// System.out.println("visibility is unPublish");
			return ResponseEntity.ok(new GlobalResponse("visibility is unPublish"));
		}

		/* Taking user email id */
		String email = AuthTokenFilter.userEmail.get();
		Set<Role> roles = userReository.findByEmail(email).getRoles();

		// extracting the role of user
		ERole userRole = null;
		for (Role role : roles) {
			userRole = role.getName();
		}
		List<Leaderboard> allUsersScore = leaderboardReository.findByChallengeListId(challengeListId);

		// map to store topScore
		Map<String, Integer> topScores = new HashMap<>();

		// Map to store user IDs
		Map<String, String> userIdsMap = new HashMap<>();

		// Map to store last timestamps of a user
		Map<String, String> timestampsMap = new HashMap<>();

		for (Leaderboard leaderboard : allUsersScore) {

			// generate a key that combines username and challengeName
			String key = leaderboard.getUsername() + "_" + leaderboard.getChallengeName();
			String userId = leaderboard.getUserId();
			String username = leaderboard.getUsername();
			int score = leaderboard.getScore();
			String timestamp = leaderboard.getTimeStamp();

			// Check if the score is higher than the previously stored top score for this
			// user and challenge
			if (!topScores.containsKey(key) || score > topScores.get(key)) {
				topScores.put(key, score);
				userIdsMap.put(username, userId);
			}

			// if there is no time stamp for the user, update in the timeStampMap
			if (timestampsMap.get(username) == null) {
				timestampsMap.put(username, timestamp);
			} else {

				// defining the format of date
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");

				try {

					// parsing the date to 24 hours and updating is the new time is more recent
					// i.e. this is the last entry of this user
					Date date1 = inputFormat.parse(timestampsMap.get(username));
					Date date2 = inputFormat.parse(timestamp);

					if (date1 == null || date2.after(date1)) {
						timestampsMap.put(username, timestamp);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		// map to store total top score for each user
		Map<String, Integer> totalTopScores = new HashMap<>();

		// Iterate through topScores to calculate total top scores
		for (Map.Entry<String, Integer> entry : topScores.entrySet()) {

			// split the key in username_challengeName
			String[] parts = entry.getKey().split("_");

			// taking user name
			String username = parts[0];

			// taking top score of the user
			int topScore = entry.getValue();

			// Add the top score to the total top score for the user
			totalTopScores.merge(username, topScore, Integer::sum);
		}

		Map<String, Integer> sortedScore = totalTopScores.entrySet().stream().sorted((entry1, entry2) -> {
			int scoreComparison = entry2.getValue().compareTo(entry1.getValue());
			if (scoreComparison != 0) {
				return scoreComparison;
			} else {
				String username1 = entry1.getKey();
				String username2 = entry2.getKey();
				String timestamp1 = timestampsMap.get(username1);
				String timestamp2 = timestampsMap.get(username2);
				return timestamp1.compareTo(timestamp2);
			}
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		// map to return the JSON format
		Map<Integer, Object> responseMap = new LinkedHashMap<>();
		int counter = 1;
		for (Map.Entry<String, Integer> entry : sortedScore.entrySet()) {

			String username = entry.getKey();
			int score = entry.getValue();
			String userId = userIdsMap.get(username); // Get the user ID from the userIdsMap
			String timestamp = timestampsMap.get(username);

			// Create a new map to store userName, userId, and score
			Map<String, Object> userScoreMap = new HashMap<>();
			userScoreMap.put("username", username);
			userScoreMap.put("userId", userId);
			userScoreMap.put("score", score);
			userScoreMap.put("Timestamp", timestamp);

			// Add the userScoreMap to the responseMap
			responseMap.put(counter++, userScoreMap);
		}

		// if visibility is publish
		if (challengeList.getVisibility().equals("publish")) {
			// System.out.println("publish");
			if (responseMap.size() == 0) {
				return ResponseEntity.ok(new GlobalResponse("No data avaliable"));
			} else {
				return ResponseEntity.ok(responseMap);
			}

		}

		// if visibility is admin only
		else if (challengeList.getVisibility().equals("admin") && userRole.toString().equals("ROLE_ADMIN")) {
			// System.out.println("visibility is admin only");
			return ResponseEntity.ok(responseMap);

		}
		// System.out.println("else");
		return ResponseEntity.ok(new GlobalResponse("Visibility is Admin only"));

	}

}
