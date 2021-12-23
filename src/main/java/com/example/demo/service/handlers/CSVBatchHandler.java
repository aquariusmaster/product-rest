package com.example.demo.service.handlers;

import com.example.demo.exception.BatchHandleException;
import com.example.demo.service.BatchFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSVBatchHandler extends BatchFileHandler {

    @Override
    public boolean canHandle(MultipartFile file) {
        var originalFilename = file.getOriginalFilename();
        return originalFilename != null && originalFilename.endsWith(".csv");
    }

    public void process(MultipartFile file) {
        throw new BatchHandleException("This logic hasn't been implemented yet");
    }

}
