package com.dw.repository;

import com.dw.model.AccumulativeDeal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccumulativeDealRepository extends MongoRepository<AccumulativeDeal, String> {

}
