package com.test.zoom.service.journal;

import com.test.zoom.dto.Search;
import com.test.zoom.dto.journal.request.JournalSearchRequest;
import com.test.zoom.dto.journal.response.JournalDetailResponse;
import com.test.zoom.dto.journal.response.JournalResponse;
import com.test.zoom.entity.journal.JournalEntry;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JournalQueryService {

    /** 조건 검색 (월별/계정별/금액별/날짜별/거래처별) */
    List<JournalEntry> searchTest(Search search);
    Page<JournalResponse> search(JournalSearchRequest request);

    /** 단건 상세 (분개라인 포함) */
    JournalDetailResponse getDetail(Long id);
}