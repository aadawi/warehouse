package com.dw.repository;

import com.dw.model.InvalidDeal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidDealRepository extends MongoRepository<InvalidDeal, String> {
    Long countByFileName(String fileName);

    void deleteByFileName(String fileName);

}
