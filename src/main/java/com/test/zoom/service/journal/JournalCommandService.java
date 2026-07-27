package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalCreateRequest;
import com.test.zoom.dto.journal.request.JournalUpdateRequest;

public interface JournalCommandService {

    Long create(JournalCreateRequest request);

    void update(Long journalId, JournalUpdateRequest request);

    void delete(Long journalId);

    void confirm(Long journalId);

    void unconfirm(Long journalId);

}