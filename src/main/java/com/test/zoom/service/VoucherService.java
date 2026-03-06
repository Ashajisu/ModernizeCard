package com.test.zoom.service;

import com.test.zoom.dto.VoucherCreateRequest;
import com.test.zoom.dto.VoucherSelectDto;
import com.test.zoom.entity.entry.*;
import com.test.zoom.repository.entry.*;
import com.test.zoom.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final EntryRepository entryRepository;
    private final AccountRepository accountRepository;
    private final DateUtil dateUtil;

    public List<VoucherSelectDto> getVouchers() {
        List<VoucherSelectDto> list = voucherRepository.getVoucherList(null,null,null);
        return list;
    }
    public List<VoucherSelectDto> getVouchersByDate(String start, String end) {
        LocalDate sqlStart = dateUtil.normalizeDate(start);
        LocalDate sqlEnd = dateUtil.normalizeDate(end);
        List<VoucherSelectDto> list = voucherRepository.getVoucherList(sqlStart,sqlEnd,null);
        return list;
    }
    
    public Long createVoucher(VoucherCreateRequest req) {

        Account debitAccount = accountRepository.findById(req.getDebitAccountId())
                .orElseThrow(() -> new IllegalArgumentException("차변계정 없음"));

        Account creditAccount = accountRepository.findById(req.getCreditAccountId())
                .orElseThrow(() -> new IllegalArgumentException("대변계정 없음"));

        // 1. 전표 생성
        Voucher voucher = Voucher.builder()
                .voucherDate(req.getVoucherDate())
                .voucherType(req.getVoucherType())
                .description(req.getDescription())
                .build();

        voucherRepository.save(voucher);

        // 2. 차변/대변 방향 결정
//        DcPair dcPair = resolveDc(req.getVoucherType(), dr, cr);

        // 3. 분개 생성
        Entry debit = Entry.builder()
                .voucher(voucher)
                .account(debitAccount)
                .dc_type(DcType.D)
                .amount(req.getAmount())
                .build();

        Entry credit = Entry.builder()
                .voucher(voucher)
                .account(creditAccount)
                .dc_type(DcType.C)
                .amount(req.getAmount())
                .build();

        entryRepository.save(debit);
        entryRepository.save(credit);

        return voucher.getId();
    }

    public List<Account> getAccounts() {
        List<Account> list = accountRepository.findAllByOrderByAccountCodeAsc();
        return list;
    }

    @Transactional
    public Account deleteAccount(Long accountId) {
        Account found = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatusCode.valueOf(422), accountId + " not found"));
        log.info("found account: {}", found);
        found.setActive(false);
        return found;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> saveAccounts(List<Account> reversedList) {
        return accountRepository.saveAll(reversedList);
    }
}
