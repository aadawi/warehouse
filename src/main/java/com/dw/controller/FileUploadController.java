package com.dw.controller;

import com.dw.exception.BusinessException;
import com.dw.service.CSVFileService;
import com.dw.service.MongoDBService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@Controller
public class FileUploadController {


    @Autowired
    private CSVFileService csvFileService;

    @Autowired
    private MongoDBService mongoDBService;

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file == null || StringUtils.isBlank(file.getOriginalFilename())){
            redirectAttributes.addFlashAttribute("errorMessage","Please select a file to upload it");
        } else {
            try {
                redirectAttributes.addFlashAttribute("message", csvFileService.processFile(file));
            }catch (BusinessException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }
            redirectAttributes.addFlashAttribute("fileName", file.getOriginalFilename());
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        return "uploadForm";
    }


    public void readFile(MultipartFile file) {
        String line = "";
        String cvsSplitBy = ",";


        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            while ((line = br.readLine()) != null) {

                String[] country = line.split(cvsSplitBy);
                System.out.println("Country [code= " + country[0] + " , name=" + country[1] + "]");
                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
                System.out.println(randomUUIDString);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
