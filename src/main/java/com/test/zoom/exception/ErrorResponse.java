package com.test.zoom.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    /** @Valid 검증 실패 시 필드별 상세 오류 (없으면 null) */
    private List<FieldErrorDetail> errors;

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now(), null);
    }

    public static ErrorResponse of(int status, String message, List<FieldErrorDetail> errors) {
        return new ErrorResponse(status, message, LocalDateTime.now(), errors);
    }

    @Data
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String message;
    }
}
