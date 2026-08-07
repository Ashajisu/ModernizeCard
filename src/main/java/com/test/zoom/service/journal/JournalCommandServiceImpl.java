package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalCreateRequest;
import com.test.zoom.dto.journal.request.JournalUpdateRequest;
import com.test.zoom.entity.journal.JournalEntry;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalEntryRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class JournalCommandServiceImpl implements JournalCommandService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalLineRepository journalLineRepository;
    private final AccountRepository accountRepository;
    private final JournalAssembler journalAssembler;

    @Override
    public Long create(JournalCreateRequest request) {
        JournalEntry entry = journalAssembler.from(request);
        journalEntryRepository.save(entry);
        return entry.getId();
    }
    
    @Override
    public void update(Long id, JournalUpdateRequest request) {
        JournalEntry entry = findActive(id);

//        if (entry.getSource() != JournalEntry.Source.MANUAL) {
//            throw new IllegalStateException("수기입력(MANUAL) 전표만 수정할 수 있습니다. id=" + id);
//        }

        journalAssembler.updateHeader(
                entry,
                request.getEntryDate(),
                request.getDescription(),
                request.getVendor(),
                request.getMemberTag()
        );
        journalAssembler.replaceLines(entry, request.getLines());
        // @Transactional 범위 내 영속 엔티티 변경 — 별도 save 없이도 flush 시 반영됨
    }

    @Override
    public void delete(Long id) {
        JournalEntry entry = findActive(id);
        entry.setDeleted(true);
    }

    @Override
    public void confirm(Long id) {
        JournalEntry entry = findActive(id);
        if (entry.isConfirmed()) {
            return; // 이미 확정된 전표는 무시
        }
        entry.setConfirmed(true);
        entry.setConfirmedAt(LocalDateTime.now());
    }

    private JournalEntry findActive(Long id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("전표가 존재하지 않습니다. id=" + id));
        if (entry.isDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 전표입니다. id=" + id);
        }
        return entry;
    }

}