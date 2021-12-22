package com.example.demo.controller;

import com.example.demo.service.BatchFileProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {
    private final BatchFileProcessorService fileProcessorService;

    @PostMapping(path = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadBatch(@RequestParam("file") MultipartFile file) {
        String type = file.getContentType();
        long size = file.getSize();
        fileProcessorService.process(file);
    }

}
