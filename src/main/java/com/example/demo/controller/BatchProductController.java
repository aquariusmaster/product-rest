package com.example.demo.controller;

import com.example.demo.exception.BatchHandleException;
import com.example.demo.exception.ErrorMessage;
import com.example.demo.exception.ParsingProductException;
import com.example.demo.service.BatchHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class BatchProductController {
    private final BatchHandlerService handlerService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadBatch(@RequestParam("file") MultipartFile file) {
        handlerService.handle(file);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = BatchHandleException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> batchError(BatchHandleException ex) {
        return new ResponseEntity<>(ErrorMessage.ofNow(INTERNAL_SERVER_ERROR.value(), ex.getMessage()), INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = ParsingProductException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> badRequest(ParsingProductException ex) {
        return new ResponseEntity<>(ErrorMessage.ofNow(BAD_REQUEST.value(), ex.getMessage()), BAD_REQUEST);
    }

}
