package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import com.quiz.entity.ERole;
import com.quiz.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	
	Optional<Role> findByName(ERole name);

}
