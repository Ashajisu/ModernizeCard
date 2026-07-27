package com.test.zoom.controller;

import com.test.zoom.dto.journal.response.AccountResponse;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/journal/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    /** 전체 계정과목 목록 (수기입력 라인 선택, 조회 필터용) */
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
                        !parentIds.contains(a.getId())
                ))
                .toList();
    }
}
