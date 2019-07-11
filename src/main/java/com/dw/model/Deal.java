package com.dw.model;

import com.opencsv.bean.*;
import lombok.Data;


@Data
public class Deal {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "fromCurrency")
    private String fromCurrency;

    @CsvBindByName(column = "amount")
    private String amount;

    @CsvBindByName(column = "timestamp")
    private String timestamp;

    @CsvBindByName(column = "toCurrency")
    private String toCurrency;

}
