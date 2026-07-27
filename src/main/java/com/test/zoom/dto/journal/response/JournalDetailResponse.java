package com.test.zoom.dto.journal.response;

import com.test.zoom.entity.journal.JournalEntry;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JournalDetailResponse {

    private Long id;

    private LocalDate entryDate;

    private LocalDate paymentDate;

    private String description;

    private String vendor;

    private String memberTag;

    private JournalEntry.Source source;

    private String sourceCardCompany;

    private Long sourceRefId;

    private boolean confirmed;

    private LocalDateTime confirmedAt;

    private List<JournalLineResponse> lines;

}