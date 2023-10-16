package com.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.quiz.entity.User;
import com.quiz.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UsernameNotFoundException("User not found with Email : "+email);
		}

		return UserDetailsImpl.build(user);
	}
	
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		User user = userRepository.findByEmail(email)
//				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with Email: " + email));
//
//		return UserDetailsImpl.build(user);
//	}
}
