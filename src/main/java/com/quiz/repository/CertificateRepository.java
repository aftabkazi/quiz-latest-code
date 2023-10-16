package com.quiz.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.quiz.entity.Certificate;

@Repository
public interface CertificateRepository extends MongoRepository<Certificate,String>{

}
