package com.test.zoom.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@NoArgsConstructor
public class MultiDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.ENGLISH)
    };

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().trim();

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new RuntimeException("지원하지 않는 날짜 형식: " + value);
    }
}
