package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.AccountCreateRequest;
import com.test.zoom.dto.journal.request.AccountUpdateRequest;

public interface AccountCommandService {

    Long create(AccountCreateRequest request);

    void update(Long id, AccountUpdateRequest request);

    /** 비활성화 (soft) — 과거 전표 참조가 있어도 안전 */
    void deactivate(Long id);

    void activate(Long id);
}
