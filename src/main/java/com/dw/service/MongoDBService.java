package com.dw.service;

import com.dw.configuration.Properties;
import com.dw.model.InvalidDeal;
import com.dw.model.ValidDeal;
import com.dw.model.Test;
import com.dw.repository.InvalidDealRepository;
import com.dw.repository.ValidDealRepository;
import com.dw.repository.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MongoDBService {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private ValidDealRepository validDealRepository;

    private InvalidDealRepository invalidDealRepository;

    @Autowired
    public void setValidDealRepository(ValidDealRepository validDealRepository) {
        this.validDealRepository = validDealRepository;
    }

    @Autowired
    public void setInvalidDealRepository(InvalidDealRepository invalidDealRepository) {
        this.invalidDealRepository = invalidDealRepository;
    }

    public void saveAllValidDeals(List<ValidDeal> dealsList) {
        List<ValidDeal> deals = new ArrayList<>();
        deals.addAll(dealsList);
        dealsList.clear();
        executor.execute(() ->{
            validDealRepository.saveAll(deals);
        });
    }

    public void saveAllInvalidDeals(List<InvalidDeal> dealsList) {
        List<InvalidDeal> deals = new ArrayList<>();
        deals.addAll(dealsList);
        dealsList.clear();
        executor.execute(() ->{
            invalidDealRepository.saveAll(deals);
        });
    }

    public void deleteAll() {
        invalidDealRepository.deleteAll();
        validDealRepository.deleteAll();
    }

    public boolean isFileImported(String fileName) {
        if (validDealRepository.countByFileName(fileName) != 0) {
            return true;
        }
        if (validDealRepository.countByFileName(fileName) != 0){
            return true;
        }
        return false;
    }

}
