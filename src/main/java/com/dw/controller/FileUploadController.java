package com.dw.controller;

import com.dw.exception.BusinessException;
import com.dw.service.CSVFileService;
import com.dw.service.MongoDBService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@Controller
public class FileUploadController {

    @Autowired
    private CSVFileService csvFileService;

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, HttpServletResponse response) {

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
}
