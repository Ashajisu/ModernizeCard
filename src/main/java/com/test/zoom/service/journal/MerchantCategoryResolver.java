package com.test.zoom.service.journal;

import com.test.zoom.entity.journal.MerchantCategoryRule;
import com.test.zoom.repository.journal.MerchantCategoryRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 가맹점명(merchantName)에 포함된 키워드를 기반으로 계정과목/가족구성원 태그를 판정한다.
 * 우선순위 규칙: priority 값이 큰 것 우선, priority가 동일하면 키워드 길이가 긴 것 우선.
 * (기존 엑셀 수기 작업의 우선순위 처리 로직을 그대로 이관)
 */
@Component
@RequiredArgsConstructor
public class MerchantCategoryResolver {

    private final MerchantCategoryRuleRepository ruleRepository;

    public static class ResolvedResult {
        public MerchantCategoryRule.RuleType ruleType;
        public com.test.zoom.entity.journal.Account account;   // ACCOUNT / POINT_SETTLEMENT 일 때
        public String memberTag;                                // MEMBER_TAG 일 때
    }

    /** merchantName에 매칭되는 규칙 중 최우선 규칙 하나를 반환. 매칭 없으면 empty. */
    public Optional<ResolvedResult> resolve(String merchantName) {
        if (merchantName == null || merchantName.isBlank()) {
            return Optional.empty();
        }

        List<MerchantCategoryRule> rules = ruleRepository.findByActiveTrue();

        return rules.stream()
                .filter(r -> merchantName.contains(r.getKeyword()))
                .max(Comparator
                        .comparingInt(MerchantCategoryRule::getPriority)
                        .thenComparingInt(r -> r.getKeyword().length()))
                .map(r -> {
                    ResolvedResult result = new ResolvedResult();
                    result.ruleType = r.getRuleType();
                    result.account = r.getAccount();
                    result.memberTag = r.getMemberTagValue();
                    return result;
                });
    }
}
