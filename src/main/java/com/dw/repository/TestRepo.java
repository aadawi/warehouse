package com.dw.repository;

import com.dw.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepo extends MongoRepository<Test,String> {

    Test findByName(String name);
}
