package com.test.zoom.controller;

import com.test.zoom.dto.journal.request.AccountCreateRequest;
import com.test.zoom.dto.journal.request.AccountUpdateRequest;
import com.test.zoom.dto.journal.response.AccountResponse;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.service.journal.AccountCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/journal/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountCommandService accountCommandService;

    /** 전체 계정과목 목록 (수기입력 라인 선택, 조회 필터용, 관리화면용 — 비활성 계정 포함 전체 반환) */
    @GetMapping
    public List<AccountResponse> list() {
        List<Account> all = accountRepository.findAll();
        Set<Long> parentIds = new HashSet<>();
        all.forEach(a -> {
            if (a.getParent() != null) parentIds.add(a.getParent().getId());
        });

        return all.stream()
                .sorted(Comparator.comparing(Account::getCode))
                .map(a -> new AccountResponse(
                        a.getId(),
                        a.getCode(),
                        a.getName(),
                        a.getCategory().name(),
                        a.getParent() != null ? a.getParent().getId() : null,
                        !parentIds.contains(a.getId()),
                        a.isActive()
                ))
                .toList();
    }

    /** 계정과목 신설 */
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody AccountCreateRequest request) {
        return ResponseEntity.ok(accountCommandService.create(request));
    }

    /** 계정과목 수정 (이름/분류/상위계정/정렬순서/활성여부) — 코드는 변경 불가 */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequest request) {
        accountCommandService.update(id, request);
        return ResponseEntity.ok().build();
    }

    /** 비활성화 (soft) — 하위 계정이 있으면 실패 */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        accountCommandService.deactivate(id);
        return ResponseEntity.ok().build();
    }

    /** 재활성화 */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        accountCommandService.activate(id);
        return ResponseEntity.ok().build();
    }
}
