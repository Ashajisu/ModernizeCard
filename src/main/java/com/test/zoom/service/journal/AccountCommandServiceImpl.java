package com.test.zoom.service.journal;

import com.test.zoom.dto.journal.request.AccountCreateRequest;
import com.test.zoom.dto.journal.request.AccountUpdateRequest;
import com.test.zoom.entity.journal.Account;
import com.test.zoom.repository.journal.AccountRepository;
import com.test.zoom.repository.journal.JournalLineRepository;
import com.test.zoom.service.journal.AccountCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountCommandServiceImpl implements AccountCommandService {

    private final AccountRepository accountRepository;
    private final JournalLineRepository journalLineRepository;

    @Override
    public Long create(AccountCreateRequest request) {
        if (accountRepository.findByCode(request.getCode()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 계정코드입니다: " + request.getCode());
        }

        Account.AccountCategory category = parseCategory(request.getCategory());
        Account parent = resolveParent(request.getParentId());

        Account account = new Account();
        account.setCode(request.getCode());
        account.setName(request.getName());
        account.setCategory(category);
        account.setParent(parent);
        account.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        account.setActive(true);

        accountRepository.save(account);
        return account.getId();
    }

    @Override
    public void update(Long id, AccountUpdateRequest request) {
        Account account = findAccount(id);
        Account.AccountCategory newCategory = parseCategory(request.getCategory());

        boolean hasHistory = journalLineRepository.existsByAccount_Id(id);
        if (hasHistory && newCategory != account.getCategory()) {
            throw new IllegalStateException(
                    "이미 분개 이력이 있는 계정은 분류(자산/부채/자본/수익/비용)를 변경할 수 없습니다. " +
                    "새 계정을 만들어 이관하는 것을 권장합니다.");
        }

        Account newParent = resolveParent(request.getParentId());
        if (newParent != null) {
            guardNoCycle(account, newParent);
        }

        account.setName(request.getName());
        account.setCategory(newCategory);
        account.setParent(newParent);
        account.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : account.getSortOrder());
        account.setActive(request.isActive());
    }

    @Override
    public void deactivate(Long id) {
        Account account = findAccount(id);
        boolean hasActiveChildren = !accountRepository.findByParent_Id(id).isEmpty();
        if (hasActiveChildren) {
            throw new IllegalStateException("하위 계정이 있는 계정은 비활성화할 수 없습니다. 하위 계정을 먼저 정리해주세요.");
        }
        account.setActive(false);
    }

    @Override
    public void activate(Long id) {
        Account account = findAccount(id);
        account.setActive(true);
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("계정과목이 존재하지 않습니다. id=" + id));
    }

    private Account resolveParent(Long parentId) {
        if (parentId == null) return null;
        return accountRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("상위 계정이 존재하지 않습니다. id=" + parentId));
    }

    private Account.AccountCategory parseCategory(String raw) {
        try {
            return Account.AccountCategory.valueOf(raw);
        } catch (Exception e) {
            throw new IllegalArgumentException("올바르지 않은 계정 분류입니다: " + raw
                    + " (ASSET/LIABILITY/EQUITY/REVENUE/EXPENSE 중 하나여야 합니다)");
        }
    }

    /** 새 부모가 자기 자신이거나 자신의 하위 계정인 경우(순환 참조) 방지 */
    private void guardNoCycle(Account account, Account newParent) {
        Account cursor = newParent;
        while (cursor != null) {
            if (cursor.getId().equals(account.getId())) {
                throw new IllegalArgumentException("상위 계정을 자기 자신이나 하위 계정으로 설정할 수 없습니다.");
            }
            cursor = cursor.getParent();
        }
    }
}
