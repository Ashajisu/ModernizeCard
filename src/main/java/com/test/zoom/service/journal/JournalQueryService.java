package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalSearchRequest;
import com.test.zoom.dto.journal.response.JournalDetailResponse;
import com.test.zoom.dto.journal.response.JournalResponse;
import org.springframework.data.domain.Page;

public interface JournalQueryService {

    Page<JournalResponse> search(JournalSearchRequest request);

    JournalDetailResponse findById(Long journalId);

}