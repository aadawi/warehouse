package com.dw.repository;

import com.dw.model.DealsFileSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealsFileSummaryRepository extends MongoRepository<DealsFileSummary, String> {

    DealsFileSummary getByFileName(String fileName);
}
