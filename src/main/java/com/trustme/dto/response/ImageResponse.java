package com.trustme.dto.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ImageResponse extends ResponseEntity<Response<Byte[]>> {

    public ImageResponse(HttpStatusCode code, String message, Byte[] imageData) {
        super(
                Response.<Byte[]>builder()
                        .code(code)
                        .message(message)
                        .result(imageData)
                        .build(),
                createHeaders(),
                code
        );
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"");
        return headers;
    }
}
