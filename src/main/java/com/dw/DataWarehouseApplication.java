package com.dw;

import com.dw.configuration.Properties;
import com.dw.model.CurrencyCode;
import com.dw.model.Deal;
import com.dw.service.MongoDBService;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class DataWarehouseApplication {

    private Properties properties;

    private MongoDBService mongoDBService;

    @Autowired
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setMongoDBService(MongoDBService mongoDBService) {
        this.mongoDBService = mongoDBService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DataWarehouseApplication.class, args);
    }

    @PostConstruct
    private void generateTestData() {

        List<Deal> dealList = new ArrayList();
        CurrencyCode currencyCode[] = CurrencyCode.values();
        List<CurrencyCode> currencyCodeList = Arrays.asList(currencyCode);
        for (int i = 0; i < 110000; i++) {

            Collections.shuffle(currencyCodeList);

            Deal deal = new Deal();
            String uuid = UUID.randomUUID().toString();
            Integer uuidHashCode = Math.abs(uuid.hashCode());
            String uuidHashCodeStr = uuidHashCode.toString();
            Integer uuidHashCodeFirstDigit = Integer.parseInt(uuidHashCodeStr.substring(0, 1));
            Integer uuidHashCodeStrLastDigit = Integer.parseInt(uuidHashCodeStr.substring(uuidHashCodeStr.length() - 1, uuidHashCodeStr.length()));

            deal.setId(uuid);
            deal.setAmount(String.format("%1$.2f", ThreadLocalRandom.current().nextDouble(1.0, 1000.1)));
            deal.setFromCurrency(currencyCodeList.get(uuidHashCodeFirstDigit).name());
            deal.setToCurrency(currencyCodeList.get(uuidHashCodeStrLastDigit).name());
            deal.setTimestamp(new Date().getTime() + "");
            if (i/(System.nanoTime()/10000000000l) == 0) {
                deal.setToCurrency("");
                deal.setTimestamp("Time");
            }
            dealList.add(deal);
        }

        try {
            Writer writer = new FileWriter(properties.getTestDateFilePath());
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withApplyQuotesToAll(false).build();
            beanToCsv.write(dealList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
