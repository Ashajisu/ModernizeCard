package com.test.zoom.service;

import com.test.zoom.dto.VoucherCreateRequest;
import com.test.zoom.entity.entry.*;
import com.test.zoom.repository.entry.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final EntryRepository entryRepository;
    private final AccountRepository accountRepository;
    
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

    /**
     * 거래 유형 + 계정 성격 기준으로 차/대변 결정
     */
//    private DcPair resolveDc(VoucherType type, Account de, Account cre) {
//
//        return switch (type) {
//
//            case TRANSFER -> new DcPair(
//                    de,     // 받는 계좌 증가
//                    cre    // 보내는 계좌 감소 또는 수익의 증가
//            );
//
//            case CARD -> new DcPair(
//                    de,     // 비용 증가
//                    cre    // 카드부채 증가
//            );
//
//            case CARD_PAYMENT -> new DcPair(
//                    de,   // 카드부채 감소
//                    cre      // 계좌 감소
//            );
//
//            case CASH -> new DcPair(
//                    cre,     // 비용 증가
//                    de    // 현금 감소
//            );
//
//            case ADJUST -> new DcPair(
//                    cre,
//                    de
//            );
//        };
//    }
//
//    private record DcPair(Account debit, Account credit) {}
}
