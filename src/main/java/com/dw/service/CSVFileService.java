package com.dw.service;

import com.dw.configuration.Properties;
import com.dw.exception.BusinessException;
import com.dw.model.InvalidDeal;
import com.dw.model.ValidDeal;
import com.dw.util.CsvFileValidator;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CSVFileService {

    Logger LOGGER = LogManager.getLogger(CSVFileService.class);

    private MongoDBService mongoDBService;
    private CsvFileValidator csvFileValidator;
    private Properties properties;

    @Autowired
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    @Autowired
    public void setMongoDBService(MongoDBService mongoDBService) {
        this.mongoDBService = mongoDBService;
    }
    @Autowired
    public void setCsvFileValidator(CsvFileValidator csvFileValidator) {
        this.csvFileValidator = csvFileValidator;
    }

    public String processFile(MultipartFile file) {
        Instant start = Instant.now();

        List<ValidDeal> validDealList = new ArrayList<>();
        List<InvalidDeal> invalidDealList = new ArrayList<>();

        AtomicInteger numberOfValidDeals = new AtomicInteger();
        AtomicInteger numberOfInvalidDeals = new AtomicInteger();

        Reader reader = null;

        try {
            String fileName = file.getOriginalFilename();

            if (mongoDBService.isFileImported(fileName)){
                throw new BusinessException("CSV file already imported");
            }

            if (!fileName.endsWith(".csv")) {
                throw new BusinessException("Please upload CSV file");
            }

            reader = new InputStreamReader(file.getInputStream());
            final Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);

            records.forEach(record -> {
                csvFileValidator.filterDeals(record, invalidDealList, validDealList, fileName);

                if (validDealList.size() > properties.getBatchSize()) {
                    numberOfValidDeals.addAndGet(validDealList.size());
                    mongoDBService.saveAllValidDeals(validDealList);
                }
                if (invalidDealList.size() > properties.getBatchSize()) {
                    numberOfInvalidDeals.addAndGet(invalidDealList.size());
                    mongoDBService.saveAllInvalidDeals(invalidDealList);
                }
            });
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error importing CSV file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    LOGGER.error("Error closing the File Reader.");
                }
            }
        }

        numberOfValidDeals.addAndGet(validDealList.size());
        numberOfInvalidDeals.addAndGet(invalidDealList.size());

        if (numberOfValidDeals.get() == 0 && numberOfInvalidDeals.get() == 0) {
            throw new BusinessException("Invalid CSV file");
        }

        validateAfterListsFinish(invalidDealList, validDealList);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        return ("Time to finish:" + timeElapsed / 100 + " seconds, Number of valid deals:" + numberOfValidDeals.get() + " Number of invalid Deals:" + numberOfInvalidDeals.get());
    }

    private void validateAfterListsFinish(List<InvalidDeal> invalidDealList, List<ValidDeal> validDealList) {

        if (!validDealList.isEmpty()) {
            mongoDBService.saveAllValidDeals(validDealList);
        }
        if (!invalidDealList.isEmpty()) {
            mongoDBService.saveAllInvalidDeals(invalidDealList);
        }
    }

}
