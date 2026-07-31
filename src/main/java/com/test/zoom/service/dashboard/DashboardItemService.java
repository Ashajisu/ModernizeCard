package com.test.zoom.service.dashboard;

import com.test.zoom.dto.dashboard.DashboardItem;
import com.test.zoom.dto.dashboard.DashboardItemResponse;
import com.test.zoom.dto.dashboard.DashboardSettingItemResponse;
import com.test.zoom.dto.dashboard.DashboardSettingUpdateRequest;
import com.test.zoom.dto.journal.response.AccountBalanceDto;
import com.test.zoom.dto.journal.response.AccountBalanceResponse;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.entity.journal.AccountPurposeTag;
import com.test.zoom.repository.AccountPurposeTagRepository;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.DashboardItemRepository;
import com.test.zoom.service.journal.AccountBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 대시보드 커스텀 위젯.
 * dashboard_item은 계정당 1행 — 한 계정은 반드시 하나의 dashboardTag만 가진다.
 * 설정화면은 전체 계정을 한 번에 불러온 뒤, dashboardTag 값 기준으로 화면(탭)에서 클라이언트가 그룹핑한다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DashboardItemService {

    private static final String DEFAULT_TAG = "미지정";

    private final DashboardItemRepository dashboardItemRepository;
    private final AccountPurposeTagRepository accountPurposeTagRepository;
    private final AccountBalanceService accountBalanceService;

    @Transactional(readOnly = true)
    public List<DashboardItemResponse> getVisibleItems() {
        List<DashboardItem> items = dashboardItemRepository.findByVisibleTrueOrderBySortOrder();
        if (items.isEmpty()) return List.of();
        
        Map<Long, BigDecimal> balanceByAccountId = loadBalanceMap();
        Map<Long, String> tagMap = accountPurposeTagRepository.findByAccountIdIn(
                items.stream().map(item -> item.getAccount().getId()).toList()
        ).stream().collect(Collectors.toMap(
                tag -> tag.getAccount().getId(),
                AccountPurposeTag::getTagName
        ));

        return items.stream()
                .map(item -> {
                    Account account = item.getAccount();

                    BigDecimal rawBalance = balanceByAccountId.getOrDefault(account.getId(), BigDecimal.ZERO);
                    BigDecimal displayBalance = account.getCategory() == Account.AccountCategory.LIABILITY
                            ? rawBalance.negate()
                            : rawBalance;

                    return DashboardItemResponse.builder()
                            .title(account.getName())
                            .subtitle(tagMap.getOrDefault(account.getId(), "미지정"))
                            .amount(displayBalance)
                            .negative(displayBalance.signum() < 0)
                            .accountCode(account.getCode())
                            .category(account.getCategory().name())
                            .sortOrder(item.getSortOrder())
                            .build();
                })
                .toList();
    }

    private Map<Long, BigDecimal> loadBalanceMap() {
        AccountBalanceResponse balances = accountBalanceService.generate(LocalDate.now());
        Map<Long, BigDecimal> map = new HashMap<>();
        for (AccountBalanceDto dto : balances.getAssets()) map.put(dto.getAccountId(), dto.getTotalAmount());
        for (AccountBalanceDto dto : balances.getLiabilities()) map.put(dto.getAccountId(), dto.getTotalAmount());
        return map;
    }

    public List<DashboardSettingItemResponse> getAllSettingsList() {
        List<DashboardItem> items = dashboardItemRepository.findAllByOrderBySortOrder();
        List<Long> accountIds = items.stream().map(item -> item.getAccount().getId()).toList();

        Map<Long, String> tagMap = accountPurposeTagRepository.findByAccountIdIn(accountIds)
                .stream().collect(Collectors.toMap(tag -> tag.getAccount().getId(), AccountPurposeTag::getTagName));

        return items.stream().map(item -> {
            Account account = item.getAccount();

            return new DashboardSettingItemResponse(
                    account.getCode(),
                    account.getName(),
                    account.getCategory().name(),
                    tagMap.getOrDefault(account.getId(), "미지정"),
                    item.isVisible(),
                    item.getSortOrder()
            );
        }).toList();
    }

    @Transactional
    public void updateSettings(DashboardSettingUpdateRequest request) {
        request.getItems().forEach(row -> {
            DashboardItem item = dashboardItemRepository.findByAccount_Code(row.getAccountCode())
                    .orElseThrow(() -> new IllegalArgumentException("대시보드 계정을 찾을 수 없습니다."));

            item.setVisible(row.isChecked());
            item.setSortOrder(row.getSortOrder());

            Account account = item.getAccount();

            AccountPurposeTag tag = accountPurposeTagRepository.findByAccountId(account.getId())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        AccountPurposeTag newTag = new AccountPurposeTag();
                        newTag.setAccount(account);
                        return newTag;
                    });

            tag.setTagName(row.getDashboardTag());
            accountPurposeTagRepository.save(tag);
        });
    }
}