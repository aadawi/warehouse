package com.dw.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessor {
    /**
     * Process File
     *
     * @param file MultiPart FIle
     * @return Message
     */
    String processFile(MultipartFile file);
}
