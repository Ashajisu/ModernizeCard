package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalCreateRequest;
import com.test.zoom.dto.journal.request.JournalUpdateRequest;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JournalCommandServiceImpl implements JournalCommandService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalLineRepository journalLineRepository;
    private final AccountRepository accountRepository;

    @Override
    public Long create(JournalCreateRequest request) {

        // TODO
        // 1. DTO 검증
        // 2. Entity 생성
        // 3. Account 조회
        // 4. JournalLine 생성
        // 5. 차변=대변 검증
        // 6. 저장

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void update(Long journalId, JournalUpdateRequest request) {

        // TODO

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void delete(Long journalId) {

        // TODO Soft Delete

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void confirm(Long journalId) {

        // TODO 확정

        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void unconfirm(Long journalId) {

        // TODO 확정취소

        throw new UnsupportedOperationException("Not implemented yet.");
    }

}