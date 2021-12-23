package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchHandlerService {

    private final List<BatchFileHandler> handlers;
    private BatchFileHandler headHandler;

    @PostConstruct
    public void init() {
        if (handlers.isEmpty()) {
            throw new IllegalStateException("No Batch handlers found");
        }
        if (handlers.size() == 1) {
            return;
        }
        int i = 0;
        var prev = headHandler = handlers.get(i++);
        while (i < handlers.size()) {
            var current = handlers.get(i++);
            prev.setNextHandler(current);
            prev = current;
        }
        log.info("{} batch handler(s) registered", handlers.size());
    }

    public void handle(MultipartFile file) {
        Objects.requireNonNull(file);
        headHandler.handle(file);
    }
}
