package com.quiz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.quiz.entity.ERole;
import com.quiz.entity.LoginRequest;
import com.quiz.entity.Role;
import com.quiz.entity.SignupRequest;
import com.quiz.entity.User;
import com.quiz.helper.EncryptDecryptHandler;
import com.quiz.repository.RoleRepository;
import com.quiz.repository.UserRepository;
import com.quiz.response.GlobalResponse;
import com.quiz.security.AuthTokenFilter;
import com.quiz.security.JwtUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	EncryptDecryptHandler encryptDecryptHandler;

	@Autowired
	AuthenticationManager authenticationManager;

	public ResponseEntity<GlobalResponse> authenticateUser(LoginRequest loginRequest) {

		Map<String, String> errorMessages = new HashMap<>();
		String encryptedEmail = "";
//		EncryptDecryptHandler encryptDecryptHandler;
		try {
//			encryptDecryptHandler = new EncryptDecryptHandler();
			encryptedEmail = encryptDecryptHandler.encrypt(loginRequest.getEmail());

		} catch (Exception e) {
			e.printStackTrace();
		}
		User user = userRepository.findByEmail(encryptedEmail);

		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		if (loginRequest.getEmail().isBlank() || loginRequest.getEmail() == null) {
			errorMessages.put("Email", "Please Enter Email !");
		} else if ((!loginRequest.getEmail().matches(emailRegex))) {
			errorMessages.put("Email", "Please Enter valid Email !");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Login" + " Failed because of following reasons", errorMessages, false));

		}

		if (loginRequest.getPassword().isBlank() || loginRequest.getPassword() == null) {
			errorMessages.put("Password", "Please Enter Password !");
		} else if ((user == null) || !(encoder.matches(loginRequest.getPassword(), user.getPassword()))) {
			errorMessages.put("Email or Password", "Email or password is incorrect !");
		}

		if (errorMessages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new GlobalResponse("Login" + " Failed because of following reasons", errorMessages, false));
		} else {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(encryptedEmail, loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			
//			System.out.println("inside login API");
//			System.out.println("jwt --"+jwt);
			
			user.setJwtToken(jwt);
			userRepository.save(user);

			return ResponseEntity.ok(new GlobalResponse(jwt, "login successful", true));
		}
	}

	public ResponseEntity<?> RegisterUser(SignupRequest signUpRequest) {

		Map<String, String> errorMessages = new HashMap<>();
		String encryptedEmail = "";
		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^])[A-Za-z\\d@$!%*?&#^]{8,}$";
		String nameRegex = "^[a-zA-Z ]+$";
		String mobileregex = "^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[6789]\\d{9}$";

//		EncryptDecryptHandler encryptDecryptHandler = null;
		try {
//			encryptDecryptHandler = new EncryptDecryptHandler();
			encryptedEmail = encryptDecryptHandler.encrypt(signUpRequest.getEmail());

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (signUpRequest.getName() == null || signUpRequest.getName().isBlank()) {
			errorMessages.put("UserName", "Enter username");
		}

		else if (!(signUpRequest.getName().matches(nameRegex))) {
			errorMessages.put("UserName", " Name can contain only characters and space");
		}
		if (signUpRequest.getEmail() == null || signUpRequest.getEmail().isBlank()) {
			errorMessages.put("Email", "Enter email");
		} else if (!(signUpRequest.getEmail().matches(emailRegex))) {
			errorMessages.put("Email", "Please enter a valid email !");
		}
		if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isBlank()) {
			errorMessages.put("Password", "Enter password");
		} else if (!(signUpRequest.getPassword().matches(passwordRegex))) {
			errorMessages.put("Password", "Error :Password must contain atleast 8 charcters"
					+ ",1 Uppercase,1 Lowercase,1 Digit,1 special symbol and no white spaces !");
		}
		if (signUpRequest.getMobile() == null || signUpRequest.getMobile().isBlank()) {
			errorMessages.put("Mobile", "Enter mobile number");
		} else if (!(signUpRequest.getMobile().matches(mobileregex))) {
			errorMessages.put("Mobile", "Please enter a valid mobile number !");
		}

		
		if (userRepository.existsByEmail(encryptedEmail)) {
			errorMessages.put("Email", " Email allready taken !");
		}
		if (errorMessages.size() > 0) {
			return ResponseEntity.badRequest()
					.body(new GlobalResponse("Registration failed because of following reasons", errorMessages, false));
		} else {

			User user = new User(signUpRequest.getName(), encryptedEmail,
					encryptDecryptHandler.encrypt(signUpRequest.getMobile()),
					encoder.encode(signUpRequest.getPassword()));

			Set<String> strRoles = signUpRequest.getRoles();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error:Role is not found"));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error:Role is not found"));
						roles.add(adminRole);
						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}
				});
			}
			user.setRoles(roles);
			user.setMode("regular");
			userRepository.save(user);
			return ResponseEntity.ok(new GlobalResponse(null, "User registered successfully!", true));
		}
	}

	public ResponseEntity<?> logoutUser() {

		String email = AuthTokenFilter.userEmail.get();
		if (email == null) {
			return ResponseEntity.ok(new GlobalResponse(null, "Logout successfull", true));
		} else {

			User user = userRepository.findByEmail(email);

			user.setJwtToken("NA");
			userRepository.save(user);

			return ResponseEntity.ok(new GlobalResponse(null, "Logout successfull", true));
		}
	}

	// method to get list of all the email
	public ResponseEntity<?> getListOfAllEmails() {
		
		List<User> Users=userRepository.findAll();
		List<String> ListOfEmails = new ArrayList<>();
		
		for(User user : Users) {
			for(Role roles:user.getRoles()) {
				
				if(roles.getName() != ERole.ROLE_ADMIN) { // checking role of email ,if not admin then add in list
					ListOfEmails.add(encryptDecryptHandler.decrypt(user.getEmail()));	
				}	
			}				
		}
		return ResponseEntity.ok(ListOfEmails);
	}
}
