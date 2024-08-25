package com.farmover.server.farmover.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3ServiceImpl {

    private String bucketName = "farmover-main";

    @Autowired
    private S3Client s3Client;

    private String endpoint = "https://ipfs.filebase.io/ipfs";

    public String uploadFile(MultipartFile file) throws IOException {
        File tempFile = convertMultipartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        int maxRetries = 3;
        int retryCount = 0;
        boolean uploadSuccessful = false;

        while (retryCount < maxRetries && !uploadSuccessful) {
            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();

                s3Client.putObject(putObjectRequest, tempFile.toPath());
                uploadSuccessful = true;
            } catch (SdkClientException | S3Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new IOException("File upload failed after retries", e);
                }
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("File upload interrupted", ie);
                }
            }
        }

        try {
            Files.delete(tempFile.toPath()); // Clean up the temporary file
        } catch (IOException e) {
            // Log the error if the file deletion fails
            System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }

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

    public String uploadRichText(String content) throws IOException {
        String key = UUID.randomUUID().toString() + ".txt";
        Path tempFile = Files.createTempFile("temp", ".txt");
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        Files.delete(tempFile); // Clean up the temporary file

        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
        String cid = headObjectResponse.metadata().get("cid");

        return getFileUrl(cid);

    }

    // https://ipfs.filebase.io/ipfs/QmVrQV3SjhMH3kScV2Sm2xxfXxpFnKT7sNr85nbkawN365
    public Boolean deleteFile(String url) {
        // Extract the CID from the URL
        String cid = url.substring(url.lastIndexOf("/") + 1);
        System.out.println(url);
        try {
            // Check if the object exists in the S3 bucket
            HeadObjectResponse headObject = s3Client.headObject(builder -> builder.bucket(bucketName).key(cid));
            System.out.println("Object exists: " + headObject);

            // Use the CID as the key to delete the object from the S3 bucket
            DeleteObjectResponse deleteObject = s3Client
                    .deleteObject(builder -> builder.bucket(bucketName).key(cid));
            System.out.println(deleteObject);
            return true;
        } catch (NoSuchKeyException e) {
            System.out.println("Object does not exist.");
            return false;
        } catch (S3Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
