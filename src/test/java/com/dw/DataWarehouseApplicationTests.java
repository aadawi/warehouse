package com.dw;

import com.dw.configuration.Properties;
import com.dw.controller.FileUploadController;
import com.dw.service.CSVFileService;
import com.dw.service.MongoDBService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class DataWarehouseApplicationTests {

    @Autowired
    private Properties properties;
    @Mock
    private MongoDBService mongoDBService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void testUploadFile() {

        File file = new File(properties.getTestDateFilePath());
        String testFileName = System.currentTimeMillis()+"TestFile.csv";

        try {

            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", testFileName,
                "text/plain", Files.readAllBytes(file.toPath()));

            MvcResult result = mvc.perform(MockMvcRequestBuilders.fileUpload("/").file(mockMultipartFile).contentType(MediaType.MULTIPART_FORM_DATA)).andReturn();

            Assert.assertNotNull(result.getFlashMap().get("message"));
            Assert.assertNull(result.getFlashMap().get("errorMessage"));

            result = mvc.perform(MockMvcRequestBuilders.fileUpload("/").file(mockMultipartFile).contentType(MediaType.MULTIPART_FORM_DATA)).andReturn();

            Assert.assertNull(result.getFlashMap().get("message"));
            Assert.assertNotNull(result.getFlashMap().get("errorMessage"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
