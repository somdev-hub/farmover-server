package com.farmover.server.farmover.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl {

    private String bucketName = "farmover-main";

    // private String accessKey = "CF93D06BB12FD24B1976";

    // private String secretKey = "RTFNlN5nra6RQNiI2O5b4pLRjnFIwVjNptMa9FHV";

    // private AmazonS3 s3Client;

    @Autowired
    private S3Client s3Client;

    private String endpoint = "https://ipfs.filebase.io/ipfs";

    public String uploadFile(MultipartFile file) throws IOException {
        File tempFile = convertMultipartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, tempFile.toPath());

        Files.delete(tempFile.toPath()); // Clean up the temporary file

        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
        String cid = headObjectResponse.metadata().get("cid");

        return getFileUrl(cid);
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private String getFileUrl(String cid) {
        return String.format("%s/%s", endpoint, cid);
    }

}
