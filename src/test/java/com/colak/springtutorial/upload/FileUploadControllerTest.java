package com.colak.springtutorial.upload;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;

@WebFluxTest(FileUploadController.class)
class FileUploadControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testFileUpload() {

        // Perform the file upload request
        webTestClient.post()
                .uri("/upload")
                .accept(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(generateBody())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    // Validate the response message
                    assert responseBody.contains("File uploaded successfully: testFile.txt");
                });
    }

    private MultiValueMap<String, HttpEntity<?>> generateBody() {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ClassPathResource("/foo.txt", FileUploadControllerTest.class));
        return builder.build();
    }

}