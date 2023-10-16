package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.quiz.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
//	public User findByUsername(String userName);
	public User findByPassword(String password);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

//	Optional<User> findByEmail(String email);
	User findByEmail(String email);

	@Query("{ 'unique_id' : ?0 }")
	User findByUniqueId(String uniqueId);

}
