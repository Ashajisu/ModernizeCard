package com.test.zoom.provider;

import com.test.zoom.entity.card.CardTransaction;
import com.test.zoom.entity.card.SHCardTransaction;
import com.test.zoom.repository.card.SHCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ShinhanCardTransactionProvider implements CardTransactionProvider {

    // '예정'(정산 전), '정산'(정산 완료) 중 확정 거래로 볼 상태만 포함
    private static final Set<String> CONFIRMED_PURCHASE_TYPES = Set.of("결제확정", "정산");
//    private static final Map<String, String> USAGE_TYPE_CORRECTIONS = Map.of(
//            "엄지수", "차량"
//    );
    private final SHCardRepository repository;

    @Override
    public List<? extends CardTransaction> findConfirmedTransactions(LocalDateTime from, LocalDateTime to) {
        return repository.findByDeletedFalseAndTransactionDateBetween(from, to).stream()
                .filter(tx -> CONFIRMED_PURCHASE_TYPES.contains(tx.getPurchaseType()))
//                .map(this::withCorrectedUsageType)
                .toList();
    }

    @Override
    public String getCardCompanyCode() {
        return "SHINHAN";
    }

    //필터링 제거 이유: 호환되지 않는 타입: capture of ? extends CardTransaction은(는) SHCardTransaction(으)로 변환할 수 없습니다
    // 엔티티를 직접 mutate하지 않고 새 인스턴스로 보정값 반영
//    private SHCardTransaction withCorrectedUsageType(SHCardTransaction tx) {
//        String corrected = USAGE_TYPE_CORRECTIONS.get(tx.getUsageType());
//        if (corrected != null) {
//            tx.setUsageType(corrected); // 아래 주의사항 참고
//        }
//        return tx;
//    }
}