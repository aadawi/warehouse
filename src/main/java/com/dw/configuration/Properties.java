package com.dw.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Properties {

    @Value("${batch.size:1000}")
    private Long batchSize;

    @Value("${test.date.csv.file.path.name}")
    private String testDateFilePath;
}
