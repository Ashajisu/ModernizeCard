package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.JournalCreateRequest;
import com.test.zoom.dto.journal.request.JournalUpdateRequest;

public interface JournalCommandService {

    /** 수기입력 전표 생성. 반환값: 생성된 전표 ID */
    Long create(JournalCreateRequest request);

    /** 수기입력(MANUAL) 전표만 수정 가능 — 자동생성 전표는 예외 발생 */
    void update(Long id, JournalUpdateRequest request);

    /** 소프트 삭제 (deleted=true) */
    void delete(Long id);

    /** 미확인(is_confirmed=false) 전표를 확정 처리 */
    void confirm(Long id);

}