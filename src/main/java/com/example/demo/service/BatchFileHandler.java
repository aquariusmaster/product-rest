package com.example.demo.service;

import com.example.demo.exception.BatchHandleException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public abstract class BatchFileHandler {
    private BatchFileHandler nextHandler;

    public void handle(MultipartFile file) {
        if (canHandle(file)) {
            process(file);
        } else {
            callNextHandler(file);
        }
    }

    public abstract void process(MultipartFile file);

    public abstract boolean canHandle(MultipartFile file);

    public void setNextHandler(BatchFileHandler handler) {
        nextHandler = handler;
    }

    private void callNextHandler(MultipartFile file) {
        if (nextHandler == null) {
            throw new BatchHandleException("No appropriate handler found to process the file: " + file.getOriginalFilename());
        }
        nextHandler.handle(file);
    }
}
