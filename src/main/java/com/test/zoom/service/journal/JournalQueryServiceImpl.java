package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalSearchRequest;
import com.test.zoom.dto.journal.response.JournalDetailResponse;
import com.test.zoom.dto.journal.response.JournalResponse;
import com.test.zoom.repository.journal.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JournalQueryServiceImpl implements JournalQueryService {

    private final JournalEntryRepository journalEntryRepository;

    @Override
    public Page<JournalResponse> search(JournalSearchRequest request) {

        // TODO QueryDSL 또는 Specification 구현

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public JournalDetailResponse findById(Long journalId) {

        // TODO Entity -> DTO 변환

        throw new UnsupportedOperationException("Not implemented yet.");
    }

}