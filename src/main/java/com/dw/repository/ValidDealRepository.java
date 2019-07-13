package com.dw.repository;

import com.dw.model.ValidDeal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidDealRepository extends MongoRepository<ValidDeal, String> {

    Long countByFileName(String fileName);

    void deleteByFileName(String fileName);
}
