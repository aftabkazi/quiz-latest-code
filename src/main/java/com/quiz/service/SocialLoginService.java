package com.quiz.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quiz.entity.ERole;
import com.quiz.entity.IVPDetails;
import com.quiz.entity.Role;
import com.quiz.entity.User;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.repository.RoleRepository;
import com.quiz.repository.UserRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.JwtUtils;

@Service
public class SocialLoginService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	@Value("${spring.userPassword}")
	private String password;

	@Value("${spring.google.clientId}")
	private String clientId;

	// google login
	public ResponseEntity<GlobalResponse> googleLogin(String token) {

		String email = null, googleClientId = null, encryptedEmail = "", name = null, jwt = null;

		long iatTime, expTime, currentTime;

		String[] tokenString = token.split("\\.");

		// Handle invalid token format
		if (tokenString.length < 2) {
			throw new IllegalArgumentException("Invalid token format");
		} else {
			Base64.Decoder decoder = Base64.getUrlDecoder();
			String payload = new String(decoder.decode(tokenString[1]));
			JsonObject jsonString = JsonParser.parseString(payload).getAsJsonObject();

			iatTime = jsonString.get("iat").getAsLong() * 1000; // converting seconds to milliseconds
			expTime = jsonString.get("exp").getAsLong() * 1000;
			googleClientId = jsonString.get("aud").getAsString();
			currentTime = System.currentTimeMillis();

			// token expire validation
			if (currentTime > expTime) {
				Map<String, String> errorMessages = new HashMap<>();
				errorMessages.put("Invalid Request", "Token has expired");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new GlobalResponse("Login" + " Failed because of following reasons", errorMessages, false));
			}

			// Google client Id validation
			if (!googleClientId.equals(clientId)) {
				Map<String, String> errorMessages = new HashMap<>();
				errorMessages.put("Invalid Request", "Invalid request");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new GlobalResponse("Login" + " Failed because of following reasons", errorMessages, false));
			}

			else {
				// taking email and name from the jwt token
				email = jsonString.get("email").getAsString();
				name = jsonString.get("name").getAsString();

				try {
					// encrypting email
					encryptedEmail = encryptDecryptHandler.encrypt(email);

				} catch (Exception e) {
					e.printStackTrace();
				}
				User user = userRepository.findByEmail(encryptedEmail);

				// if user mode if regular
				if (user != null && (user.getMode().equals("regular"))) {

					Map<String, String> errorMessages = new HashMap<>();
					errorMessages.put("Invalid Request", "Please try normal login");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
							new GlobalResponse("Login" + " Failed because of following reasons", errorMessages, false));
				}

				// if user mode is google or IVP
				else if (user != null && (user.getMode().equals("google") || user.getMode().equals("ivp"))) {

					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(encryptedEmail, password));

					SecurityContextHolder.getContext().setAuthentication(authentication);
					jwt = jwtUtils.generateJwtToken(authentication);
					// System.out.println("in google login API ");
					// System.out.println("Jwt token --"+jwt);
					user.setJwtToken(jwt);
					userRepository.save(user);

					return ResponseEntity.ok(new GlobalResponse(jwt, "login successful", true));
				}

				// if its a new user
				else {
					User newUser = new User();
					Set<Role> roles = new HashSet<>();

					newUser.setUsername(name);
					newUser.setEmail(encryptedEmail);
					newUser.setPassword(encoder.encode(password));
					newUser.setMode("google");

					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error:Role is not found"));
					roles.add(userRole);

					newUser.setRoles(roles);
					userRepository.save(newUser);
					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(encryptedEmail, password));

					SecurityContextHolder.getContext().setAuthentication(authentication);
					jwt = jwtUtils.generateJwtToken(authentication);

					newUser.setJwtToken(jwt);
					userRepository.save(newUser);

					return ResponseEntity.ok(new GlobalResponse(jwt, "login successful", true));
				}
			}
		}
	}

	// IVP login
	public ResponseEntity<GlobalResponse> IVPLogin(IVPDetails iVPDetails) {

		String encryptedEmail = "",  jwt = null;

		try {
			encryptedEmail = encryptDecryptHandler.encrypt(iVPDetails.getEmail());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		User user = userRepository.findByEmail(encryptedEmail);
		
		// if user mode if regular 
		if (user != null && (user.getMode().equals("regular"))) {

			Map<String, String> errorMessages = new HashMap<>();
			errorMessages.put("Invalid Request", "Please try normal login");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Login" + " Failed because of following reasons", errorMessages, false));
		}
		
		// if user mode is google or IVP
		else if (user != null && (user.getMode().equals("ivp") || user.getMode().equals("google"))) {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(encryptedEmail, password));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			jwt = jwtUtils.generateJwtToken(authentication);

			user.setJwtToken(jwt);
			userRepository.save(user);

			return ResponseEntity.ok(new GlobalResponse(jwt, "login successful", true));
		}
		// if its a new user
		else {
			User newUser = new User();
			Set<Role> roles = new HashSet<>();

			newUser.setUsername(iVPDetails.getName());
			newUser.setEmail(encryptedEmail);
			newUser.setPassword(encoder.encode(password));
			newUser.setMode("ivp");
			newUser.setUnique_id(iVPDetails.getUnique_id());

			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error:Role is not found"));
			roles.add(userRole);

			newUser.setRoles(roles);

			userRepository.save(newUser);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(encryptedEmail, password));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			jwt = jwtUtils.generateJwtToken(authentication);

			newUser.setJwtToken(jwt);
			userRepository.save(newUser);
			return ResponseEntity.ok(new GlobalResponse(jwt, "login successful", true));
		}
	}
}
