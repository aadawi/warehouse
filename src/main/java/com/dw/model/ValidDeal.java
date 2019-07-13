package com.dw.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "validDeals")
public class ValidDeal extends Deal {
    private String fileName;
}
