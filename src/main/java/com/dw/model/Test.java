package com.dw.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "users")
public class Test {

    @Id
    private String id;
    private String name;



}
