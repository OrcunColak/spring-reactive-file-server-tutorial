package com.colak.springtutorial.upload;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@WebFluxTest(FileUploadController.class)
class FileUploadControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testReactiveFileUpload() {
        // Create a MockMultipartFile for testing
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes()
        );

        // Perform the file upload request
        webTestClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", file))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(responseBody -> {
                    // Validate the response message
                    assert responseBody.contains("File uploaded successfully: testFile.txt");
                });
    }

}