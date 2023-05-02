package com.fitnesspartner.log;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogApiRepository extends MongoRepository<LogApi, String> {
}
