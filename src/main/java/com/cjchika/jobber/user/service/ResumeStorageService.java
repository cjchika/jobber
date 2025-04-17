package com.cjchika.jobber.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
public class ResumeStorageService {
    private final S3Client s3Client;
    private final String bucketName;

    public ResumeStorageService(S3Client s3Client,
                                @Value("${aws.s3.bucketName}") String bucketName){
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public String uploadResume(MultipartFile file, UUID userId) throws IOException {

        // Validate file
        validateFile(file);

        // Generate unique file key
        String fileKey = String.format("resumes/%s/%s_%s",
                userId,
                UUID.randomUUID(),
                file.getOriginalFilename());

        // Upload file
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(
                file.getInputStream(), file.getSize()));

        // Generate pre-signed URL (public access alternative)
        GetUrlRequest urlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        return s3Client.utilities().getUrl(urlRequest).toString();
    }

    private void validateFile(MultipartFile file){
        if (file.isEmpty()){
            throw new IllegalArgumentException("File cannot be empty");
        }

        if(file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new IllegalArgumentException("File size exceeds 5MB limit");
        }

        String contentType = file.getContentType();
        if(!"application/pdf".equals(contentType) &&
           !"application/msword".equals(contentType) &&
           !"application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType)){
            throw new IllegalArgumentException("Only PDF and Word documents are allowed!");
        }
    }

    public void deleteResume(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            String fileKey = extractS3KeyFromUrl(fileUrl);
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
        } catch (S3Exception e) {
            if (e.statusCode() != 404) { // Ignore "Not Found" errors
                throw new RuntimeException("Failed to delete resume", e);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete resume", ex);
        }
    }

    private String extractS3KeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();

            // Handle both path-style and virtual-hosted-style URLs
            if (path.startsWith("/" + bucketName + "/")) {
                return path.substring(bucketName.length() + 2);
            }
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid S3 URL format", e);
        }
    }
}

















