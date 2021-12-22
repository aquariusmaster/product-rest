package com.example.demo.controller;

import com.example.demo.service.BatchFileProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {
    private final BatchFileProcessorService fileProcessorService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadBatch(@RequestParam("file") MultipartFile file) {
        fileProcessorService.process(file);
    }

}
