package com.dw.controller;

import com.dw.exception.BusinessException;
import com.dw.model.DealsFileSummary;
import com.dw.service.FileProcessor;
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

import java.io.IOException;

@Controller
public class MainController {

    @Autowired
    private FileProcessor csvFileService;

    private MongoDBService mongoDBService;

    @Autowired
    public void setMongoDBService(MongoDBService mongoDBService) {
        this.mongoDBService = mongoDBService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload it");
        } else {
            try {
                redirectAttributes.addFlashAttribute("message", csvFileService.processFile(file));
            } catch (BusinessException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }
            redirectAttributes.addFlashAttribute("fileName", file.getOriginalFilename());
        }
        return "redirect:/upload";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {
        return "uploadForm";
    }

    @GetMapping("/summary")
    public String showSummary(Model model) throws IOException {
        return "summary";
    }

    @PostMapping("/summary")
    public String getSummary(@RequestParam("fileName") String fileName,
                             RedirectAttributes redirectAttributes) throws IOException {
        DealsFileSummary dealsFileSummary = mongoDBService.getFileSummaryByFileName(fileName);
        redirectAttributes.addFlashAttribute("result",dealsFileSummary);
        redirectAttributes.addFlashAttribute("fileName",fileName);
        return "redirect:/summary";
    }
}
