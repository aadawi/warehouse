package com.dw.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "dealsFileSummary")
public class DealsFileSummary {

    @Id
    private String fileName;
    private Integer numberOfValidDeails;
    private Integer numberOfinvalidDeals;
    private Date date;
    private Long duration;

}
