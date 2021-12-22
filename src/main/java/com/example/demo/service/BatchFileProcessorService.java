package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface BatchFileProcessorService {
    void process(MultipartFile file);
}
