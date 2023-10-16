package com.quiz.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.quiz.entity.SingleChoiceQuestion;

@Repository
public interface SingleChoiceQuestionRepository extends MongoRepository<SingleChoiceQuestion,String>{
	
	Long save(String filePath);
	
	List<SingleChoiceQuestion> findByQuizId(String id);
	
	SingleChoiceQuestion findByQuestionId(String id);
	
	List<SingleChoiceQuestion> findByselectedQuizIds(String id);
}
	