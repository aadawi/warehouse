package com.dw.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "accumulativeDeal")
public class AccumulativeDeal {

    @Id
    private String currencyCode;
    private Long dealsCount;

    public AccumulativeDeal() {
    }

    public AccumulativeDeal(String currencyCode, Long dealsCount) {
        this.currencyCode = currencyCode;
        this.dealsCount = dealsCount;
    }
}
