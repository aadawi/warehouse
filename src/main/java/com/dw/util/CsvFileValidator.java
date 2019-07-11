package com.dw.util;

import com.dw.model.CurrencyCode;
import com.dw.model.InvalidDeal;
import com.dw.model.ValidDeal;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CsvFileValidator {

    public void filterDeals(CSVRecord dealArray, List<InvalidDeal> invalidDealList, List<ValidDeal> validDealList, String fileName) {
        if (validateDealValues(dealArray)) {
            saveValidDeal(dealArray, validDealList, fileName);
        } else {
            saveInvalidDeal(dealArray, invalidDealList, fileName);
        }
    }

    private void saveValidDeal(CSVRecord deal, List<ValidDeal> validDealList, String fileName) {
        ValidDeal validDeal = new ValidDeal();
        validDeal.setAmount(getAmountFromRecord(deal));
        validDeal.setFromCurrency(getFromCurrencyFromRecord(deal));
        validDeal.setId(getIdFromRecord(deal));
        validDeal.setTimestamp(getTimestampFromRecord(deal));
        validDeal.setToCurrency(getToCurrencyFromRecord(deal));
        validDeal.setFileName(fileName);

        validDealList.add(validDeal);
    }

    public void saveInvalidDeal(CSVRecord deal, List<InvalidDeal> invalidDealList, String fileName) {

        if (StringUtils.isBlank(getIdFromRecord(deal)) &&
            StringUtils.isBlank(getAmountFromRecord(deal)) &&
            StringUtils.isBlank(getFromCurrencyFromRecord(deal)) &&
            StringUtils.isBlank(getToCurrencyFromRecord(deal)) &&
            StringUtils.isBlank(getTimestampFromRecord(deal))) {
            return ;
        }

        InvalidDeal invalidDeal = new InvalidDeal();
        invalidDeal.setAmount(getAmountFromRecord(deal));
        invalidDeal.setFromCurrency(getFromCurrencyFromRecord(deal));
        invalidDeal.setId(getIdFromRecord(deal));
        invalidDeal.setTimestamp(getTimestampFromRecord(deal));
        invalidDeal.setToCurrency(getToCurrencyFromRecord(deal));
        invalidDeal.setFileName(fileName);

        invalidDealList.add(invalidDeal);
    }

    public boolean validateDealValues(CSVRecord deal) {

        if (deal.size() != 5) {
            return false;
        }

        String amount = getAmountFromRecord(deal);
        String timestamp = getTimestampFromRecord(deal);
        String fromCurrency = getFromCurrencyFromRecord(deal);
        String toCurrency = getToCurrencyFromRecord(deal);

        if (StringUtils.isBlank(getIdFromRecord(deal)) ||
            StringUtils.isBlank(amount) ||
            StringUtils.isBlank(getFromCurrencyFromRecord(deal)) ||
            StringUtils.isBlank(getToCurrencyFromRecord(deal)) ||
            StringUtils.isBlank(getTimestampFromRecord(deal))) {
            return false;
        }
        try {
            Double.valueOf(amount);
        } catch (NumberFormatException e) {
            return false;
        }
        try {
            new Date(Long.valueOf(timestamp));
        } catch (Exception e) {
            return false;
        }
        if (!EnumUtils.isValidEnum(CurrencyCode.class, fromCurrency)) {
            return false;
        }
        if (!EnumUtils.isValidEnum(CurrencyCode.class, toCurrency)) {
            return false;
        }

        return true;
    }

    private String getRecordValue(CSVRecord record, String columnName) {
        try {
            return record.get(columnName);
        } catch (Exception e) {
            return null;
        }
    }

    private String getIdFromRecord(CSVRecord record) {
        return getRecordValue(record, "ID");
    }

    private String getAmountFromRecord(CSVRecord record) {
        return getRecordValue(record, "AMOUNT");
    }

    private String getFromCurrencyFromRecord(CSVRecord record) {
        return getRecordValue(record, "FROMCURRENCY");
    }

    private String getToCurrencyFromRecord(CSVRecord record) {
        return getRecordValue(record, "TOCURRENCY");
    }

    private String getTimestampFromRecord(CSVRecord record) {
        return getRecordValue(record, "TIMESTAMP");
    }
}
