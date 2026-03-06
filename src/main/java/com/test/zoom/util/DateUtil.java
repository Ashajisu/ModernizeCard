package com.test.zoom.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Component
public class DateUtil {

    private static final DateTimeFormatter SQL_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DAILY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter ZOOM_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:mm:ss'Z'");

    private static final DateTimeFormatter VUE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").localizedBy(new Locale("ko", "KR"));
    
    public String toSqlString(LocalDate date){
        return date.format(DAILY_FORMATTER);
    }
    public String toSqlString(LocalDateTime dateTime){
        return dateTime.format(SQL_FORMATTER);
    }
    
    public static LocalDateTime parse(String date){
        return LocalDateTime.parse(date, SQL_FORMATTER);
    }
    public static LocalDateTime parseZoom(String date){
        return LocalDateTime.parse(date, ZOOM_FORMATTER);
    }
    
    /** 사용자 입력값 검증로직 
     * 1. 포맷 검증
     * 2. 의미 있는 시간인지 검증
     * 3. SQL 전달용 포맷으로 반환**/
    public static LocalDate normalizeDate(String input) {
        return LocalDate.parse(normalizeDateTime(input), SQL_FORMATTER);
    }
    
    public static String normalizeDateTime(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("날짜/시간 값이 비어있습니다.");
        }

        try {
            LocalDateTime dt = LocalDateTime.parse(input, SQL_FORMATTER);
            return dt.format(SQL_FORMATTER); // 정규화된 문자열
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜/시간 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)", e);
        }
    }

    public String[] getTodayTimes() {
        return new String[]{
                toSqlString(LocalDate.now().atStartOfDay()),
                toSqlString(LocalDate.now().atTime(23, 59, 59))
        };
    }
    public String[] getDateTimes(LocalDate date) {
        return new String[]{
                toSqlString(date.atStartOfDay()),
                toSqlString(date.atTime(23, 59, 59))
        };
    }
}
