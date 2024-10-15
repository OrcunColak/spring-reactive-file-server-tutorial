package com.colak.springtutorial.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@Slf4j
public class FileUploadController {

    @Value("${upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public Mono<ResponseEntity<String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        return Mono.fromCallable(() -> {
            Path directory = Paths.get(uploadDir);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String originalFilename = sanitizeFileName(Objects.requireNonNull(file.getOriginalFilename()));
            Path filePath = directory.resolve(originalFilename);
            log.info("File path: {}", filePath);
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + originalFilename);
        }).onErrorResume(IOException.class, exception -> {
            log.error("File upload failed: {}", exception.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to store file " + file.getOriginalFilename()));
        });
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // Replaces dangerous characters with underscores
    }
}
