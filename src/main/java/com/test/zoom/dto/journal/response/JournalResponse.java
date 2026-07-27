package com.test.zoom.dto.journal.response;

import com.test.zoom.entity.journal.JournalEntry;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JournalResponse {

    private Long id;

    private LocalDate entryDate;

    private String description;

    private String vendor;

    private String memberTag;

    private JournalEntry.Source source;

    private boolean confirmed;

    /** 차변합계 */
    private String debitAmount;

    /** 대변합계 */
    private String creditAmount;

}