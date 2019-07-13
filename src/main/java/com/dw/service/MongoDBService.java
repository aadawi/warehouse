package com.dw.service;

import com.dw.model.AccumulativeDeal;
import com.dw.model.InvalidDeal;
import com.dw.model.ValidDeal;
import com.dw.repository.AccumulativeDealRepository;
import com.dw.repository.InvalidDealRepository;
import com.dw.repository.ValidDealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class MongoDBService {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private ValidDealRepository validDealRepository;

    private InvalidDealRepository invalidDealRepository;

    private AccumulativeDealRepository accumulativeDealRepository;

    @Autowired
    public void setAccumulativeDealRepository(AccumulativeDealRepository accumulativeDealRepository) {
        this.accumulativeDealRepository = accumulativeDealRepository;
    }

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

    public void addAndUpdateAccumulativeDealsCount(Map<String, Long> currencyCodeMap){
        List<AccumulativeDeal> accumulativeDealList = accumulativeDealRepository.findAll();

        accumulativeDealList.forEach(accumulativeDeal -> {
            Long newDealsCount = currencyCodeMap.get(accumulativeDeal.getCurrencyCode());
            if (newDealsCount != null) {
                accumulativeDeal.setDealsCount(accumulativeDeal.getDealsCount()+newDealsCount);
                currencyCodeMap.remove(accumulativeDeal.getCurrencyCode());
            }
        });
        if (!currencyCodeMap.isEmpty()) {
            List<AccumulativeDeal> newAccumulativeDealList = currencyCodeMap.entrySet().stream().map(entry ->
                    new AccumulativeDeal(entry.getKey(), entry.getValue())).collect(Collectors.toList());
            accumulativeDealList.addAll(newAccumulativeDealList);
        }

        accumulativeDealRepository.saveAll(accumulativeDealList);
    }

    public void deleteByFileName(String fileName){
        invalidDealRepository.deleteByFileName(fileName);
        validDealRepository.deleteByFileName(fileName);
    }

}
