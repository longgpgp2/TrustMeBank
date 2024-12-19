package com.trustme.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
@Getter
@Setter
@Builder
public class Response<T> {
    private HttpStatusCode code;
//    message attributes for error,
//    user's exceptions or server notification
    private String message;
    private T result;
    public Response(HttpStatusCode code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
    public static class ResponseBuilder<T> {
        private HttpStatusCode code;
        private String message;
        private T result;

        public ResponseBuilder<T> code(HttpStatusCode code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder<T> result(T result) {
            this.result = result;
            return this;
        }

        public Response<T> build() {
            return new Response<>(code, message, result);
        }
    }
}
