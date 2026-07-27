package com.test.zoom.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 전역 예외 처리.
 * 프론트엔드(apiClient)는 실패 응답 바디의 `message` 필드를 읽어 에러 메시지로 표시한다
 * (예: ManualEntry.vue의 `e?.response?.data?.message`).
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 잘못된 요청/입력값 (예: 존재하지 않는 계정과목 ID, 존재하지 않는 전표 ID 등) */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("잘못된 요청: {}", e.getMessage());
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /** 상태 위반 (예: 자동생성 전표 수정 시도, 이미 삭제된 전표 재삭제 시도, 차변≠대변 등) */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException e) {
        log.warn("상태 위반: {}", e.getMessage());
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    /** JPA 엔티티 조회 실패 (findById 등에서 직접 orElseThrow(EntityNotFoundException)로 던진 경우 대비) */
    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        log.warn("대상 없음: {}", e.getMessage());
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /** @Valid 바디 검증 실패 — 필드별 오류 목록 포함 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        List<ErrorResponse.FieldErrorDetail> details = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldErrorDetail(fe.getField(), fe.getDefaultMessage()))
                .toList();

        log.warn("입력값 검증 실패: {}", details);
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "입력값을 확인해주세요.", details);
        return ResponseEntity.badRequest().body(response);
    }

    /** 그 외 예상 못한 예외 — 상세 내용은 로그로만 남기고 응답은 일반 메시지로 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("처리되지 않은 예외 발생", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ErrorResponse.of(status.value(), message));
    }
}
