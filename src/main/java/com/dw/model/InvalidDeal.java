package com.dw.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "invalidDeals")
public class InvalidDeal extends Deal {
    private String fileName;
}
