package com.farmover.server.farmover.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.farmover.server.farmover.services.impl.S3ServiceImpl;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    @Autowired
    private S3ServiceImpl s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file) throws IOException {
        String url = s3Service.uploadFile(file);
        return new ResponseEntity<String>(url, HttpStatus.CREATED);
    }

}
