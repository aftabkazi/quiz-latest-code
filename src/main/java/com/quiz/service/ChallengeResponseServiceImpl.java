package com.quiz.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.quiz.entity.Challenge;
import com.quiz.entity.ChallengeList;
import com.quiz.entity.ChallengeResponse;
import com.quiz.entity.Flag;
import com.quiz.entity.Hint;
import com.quiz.entity.Leaderboard;
import com.quiz.entity.User;
import com.quiz.exception.challenge.ChallengeListNotFoundException;
import com.quiz.exception.challenge.ChallengeNotFoundException;
import com.quiz.exception.quiz.AttemptsException;
import com.quiz.exception.user.UserNotFoundException;
import com.quiz.repository.ChallengeListRepository;
import com.quiz.repository.ChallengeRepository;
import com.quiz.repository.ChallengeResponseRepository;
import com.quiz.repository.LeaderboardRepository;
import com.quiz.repository.UserRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;
import com.quiz.service.interfaces.ChallengeResponseService;

@Service
public class ChallengeResponseServiceImpl implements ChallengeResponseService {

	@Autowired
	private ChallengeResponseRepository challengeResponseRepository;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Autowired
	private ChallengeListRepository challengeListRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LeaderboardRepository leaderboardRepository;

	// method to attend challenge
	@Override
	public ResponseEntity<GlobalResponse> attendChallenge(ChallengeResponse challengeResponse) {

		Challenge challenge = challengeRepository.findByChallengeId(challengeResponse.getChallengeId());
		ChallengeList challengeList = challengeListRepository
				.findByChallengeListId(challengeResponse.getChallengeListId());

		// taking email from the token
		String email = AuthTokenFilter.userEmail.get();

		User emailId = userRepository.findByEmail(email);

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a");
		String formattedDate = dateFormat.format(currentDate);

		List<String> hintsList = new ArrayList<>();

		// if challenge id is invalid
		if (challenge == null) {
			throw new ChallengeNotFoundException();
		}

		// if challenge list if is invalid
		if (challengeList == null) {
			throw new ChallengeListNotFoundException();
		}

		int count = challengeResponseRepository.countByEmailAndChallengeId(email, challenge.getChallengeId());
		
		// to check attempts of a user, and no of attempts should be greater then 0
		if (count >= challenge.getNoOfAttempts() && challenge.getNoOfAttempts() > 0) {
			throw new AttemptsException();
		}

		Map<String, Flag> flags = challenge.getFlags();
		int scoredValue = 0;

		for (Flag flag : flags.values()) {
			if (flag.getFlag().equalsIgnoreCase(challengeResponse.getFlag())) {
				
				// if flag is case sensitive
				if(flag.getIsCaseSensitive()) {
					if(flag.getFlag().equals(challengeResponse.getFlag())){
						scoredValue = challenge.getValue();
					}
				}
				// if flag is case in-sensitive
				else {
					scoredValue = challenge.getValue();
				}
			}
		}

		// if hints is used
		if (challengeResponse.getHints() != null) {

			Map<String, Hint> hints = challenge.getHints();
			List<String> responseHints = challengeResponse.getHints();

			for (Hint hint : hints.values()) {
				String hintValue = hint.getHint();
				if (responseHints.contains(hintValue)) {
					hintsList.add(hintValue);
					scoredValue = scoredValue - hint.getValue(); // reducing scored marks because hint is used
				}
			}
		}

		if (scoredValue < 0) {
			scoredValue = 0;
		}

		challengeResponse.setHints(hintsList);
		challengeResponse.setUsername(emailId.getUsername());
		challengeResponse.setScoredValue(scoredValue);
		challengeResponse.setValue(challenge.getValue());
		challengeResponse.setSelectedChallenge(challenge.getChallengeName());
		challengeResponse.setEmail(email);
		challengeResponse.setCreatedDate(formattedDate);
		challengeResponse.setChallengeListName(challengeList.getChallengeListName());

		challengeResponseRepository.save(challengeResponse);

		Leaderboard leaderboard = new Leaderboard();

		leaderboard.setEmail(email);
		leaderboard.setChallengeListId(challengeResponse.getChallengeListId());
		leaderboard.setChallengeId(challengeResponse.getChallengeId());
		leaderboard.setChallengeName(challengeResponse.getSelectedChallenge());
		leaderboard.setUsername(challengeResponse.getUsername());
		leaderboard.setUserId(emailId.getId());
		leaderboard.setScore(scoredValue);
		leaderboard.setTimeStamp(challengeResponse.getCreatedDate());
		leaderboard.setVisibility(challengeList.getVisibility());

		leaderboardRepository.save(leaderboard);

		return new ResponseEntity<GlobalResponse>(
				new GlobalResponse(challengeResponse.getChallengeId(), "Challenge completed sucessfully", true),
				HttpStatus.OK);
	}

	// method to get challenge Response by id
	@Override
	public ResponseEntity<ChallengeResponse> getChallengeResponseById(String challengeResponseId) {

		ChallengeResponse challenge = challengeResponseRepository.findBychallengeResponseId(challengeResponseId);

		if (challenge == null) {
			throw new ChallengeNotFoundException();
		}
		return ResponseEntity.ok(challenge);

	}

	// method to get all challenge response
	@Override
	public ResponseEntity<List<ChallengeResponse>> getAllChallengesResponse() {

		List<ChallengeResponse> challengeResponseList = challengeResponseRepository.findAll();

		// sorting list to get recent response first
		challengeResponseList.sort(Comparator.comparing(ChallengeResponse::getCreatedDate).reversed());

		return ResponseEntity.ok(challengeResponseList);
	}

	// delete challenge response by id
	@Override
	public ResponseEntity<GlobalResponse> deleteChallengeResponseById(String challengeResponseId) {

		ChallengeResponse challengeResponse = challengeResponseRepository
				.findBychallengeResponseId(challengeResponseId);

		if (challengeResponse == null) {
			throw new ChallengeNotFoundException();
		}
		challengeResponseRepository.delete(challengeResponse);

		return ResponseEntity.ok(new GlobalResponse(null, "Challenge Response deleted", true));
	}

	// get  all challenge Response by email
	@Override
	public ResponseEntity<List<ChallengeResponse>> getAllChallengeResponseByEmail() {

		String email = AuthTokenFilter.userEmail.get(); // taking email from token
		List<ChallengeResponse> challengeResponse;

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UserNotFoundException();
		} else {
			challengeResponse = challengeResponseRepository.findByEmail(email);
		}
		return ResponseEntity.ok(challengeResponse);
	}
	
	// method to get response based on email and challenge id
	public ResponseEntity<List<ChallengeResponse>> getOneChallengeResponseByEmail(String challengeId) {

		String email = AuthTokenFilter.userEmail.get(); // taking email from token
		List<ChallengeResponse> challengeResponse;
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UserNotFoundException();
		} 
		else {
			challengeResponse = challengeResponseRepository.findByEmailAndChallengeId(email,challengeId);
		}
		return ResponseEntity.ok(challengeResponse);
	}

}
