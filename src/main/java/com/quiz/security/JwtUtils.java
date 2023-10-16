package com.quiz.security;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.quiz.entity.User;
import com.quiz.repository.UserRepository;
import com.quiz.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {

	@Autowired
	private UserRepository userRepository;

	@Value("${spring.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${spring.secret}")
	private String jwtSecret;

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	// JWT token generation method
	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		String token = Jwts.builder()
				.setSubject((userPrincipal.getEmail()))
				.claim("username", userPrincipal.getUsername())  
				.claim("roles", userPrincipal.getAuthorities().toString()).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		return token;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public String getRoleFromJwtToken(String token) {
		System.out.println("method called");
		 Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		 String rolesClaim = (String) claims.get("roles");
		 System.out.println("Roles -- "+rolesClaim);
		 
		 return null;
	}
	
	// JWT token validation method
	public boolean validateJwtToken(String authToken) {

		User user = null;

		try {
			Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();
			
			String email = claims.getSubject();
			user = userRepository.findByEmail(email);
			
			// token mismatched validation
			// if (!authToken.equals(user.getJwtToken()) && !user.getMode().equals("google")) {
			if (!authToken.equals(user.getJwtToken())) {
//				System.out.println("token comparasion");
				return false;
			}
			
			// if token is null or NA
			else if (user.getJwtToken().equals("NA")) {
//				System.out.println("If token is null");
				return false;
			} 
			else {
//				 System.out.println("inside else");
				Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
				return true;
			}
		}
		catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} 
		catch (MalformedJwtException e) {
			System.out.println("inside catch in jwt utils");
			logger.error("Invalid JWT token: {}", e.getMessage());
		} 
		catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		}
		catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		}
		catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}
}
