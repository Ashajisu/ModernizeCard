<script setup lang="ts">
import { ref } from 'vue';
import { apiClient } from '@/data/Axios';
import { format } from 'date-fns';
import UiParentCard from '@/components/shared/UiParentCard.vue';
import type { CardOption } from '@/types/custom/DataTableTypes';

// 다이얼로그 열기/닫기
const dialog = ref(false);

// 입력값 — 다이얼로그를 닫아도 유지되도록 컴포넌트 top-level에 둔다 (요구사항 6)
const selectedCard = ref<CardOption | null>(null);
const paymentDate = ref(format(new Date(), 'yyyy-MM-dd'));

// 이용카드 옵션 — 최초 다이얼로그 오픈 시 한 번만 불러오고 캐시
const cardOptions = ref<CardOption[]>([]);
const cardOptionsLoaded = ref(false);
const cardOptionsLoading = ref(false);

const generating = ref(false);

// 연계 SETTLEMENT 삭제 확인 다이얼로그
const confirmDialog = ref(false);
const confirmMessage = ref('');

// 결과 스낵바
const snackbar = ref(true);
const snackbarColor = ref<'success' | 'error'>('success');
const snackbarMessage = ref('error');

const showSnackbar = (message: string, color: 'success' | 'error') => {
    snackbarMessage.value = message;
    snackbarColor.value = color;
    snackbar.value = true;
};

const openDialog = async () => {
    dialog.value = true;
    if (!cardOptionsLoaded.value) {
        cardOptionsLoading.value = true;
        try {
            cardOptions.value = await apiClient.get('/card/company/code');
            cardOptionsLoaded.value = true;
        } catch (e: any) {
            showSnackbar(e?.response?.data?.message || '이용카드 목록을 불러오지 못했습니다.', 'error');
        } finally {
            cardOptionsLoading.value = false;
        }
    }
};

const callRegenerate = async (confirmCascade: boolean) => {
    const card = selectedCard.value!;
    return apiClient.post(
        `/journal/card-statement/regenerate?cardCompanyCode=${card.cardCompanyCode}&paymentDate=${paymentDate.value}&confirmCascade=${confirmCascade}`,
        {}
    );
};

const onGenerate = async () => {
    if (!selectedCard.value) {
        showSnackbar('이용카드를 선택해주세요.', 'error');
        return;
    }

    generating.value = true;
    try {
        const result = await callRegenerate(false);
        onSuccess(result);
    } catch (e: any) {
        console.log(e);
        if (e?.response?.status === 409) {
            // 후속 SETTLEMENT 전표 존재 안내 — 사용자 확인 후 재시도
            confirmMessage.value = e?.response?.data?.message || '';
            confirmDialog.value = true;
        } else {
            showSnackbar(e?.response?.data?.message || '전표 생성 중 오류가 발생했습니다.', 'error');
        }
    } finally {
        generating.value = false;
    }
};

const onConfirmCascadeDelete = async () => {
    confirmDialog.value = false;
    generating.value = true;
    try {
        const result = await callRegenerate(true);
        onSuccess(result);
    } catch (e: any) {
        showSnackbar(e?.response?.data?.message || '전표 생성 중 오류가 발생했습니다.', 'error');
    } finally {
        generating.value = false;
    }
};

const onSuccess = (result: any) => {
    const deleted = result.deletedSettlementPaymentDates ?? [];
    const deletedMsg = deleted.length > 0 ? ` (연계 정산전표 ${deleted.length}건도 함께 재생성됨)` : '';
    showSnackbar(`집계전표가 생성되었습니다. (전표 ID: ${result.entryId})${deletedMsg}`, 'success');
    dialog.value = false;
};
</script>

<template>
    <v-btn color="primary" variant="outlined" @click="openDialog">카드명세서 집계전표 생성</v-btn>
    
    <v-dialog v-model="dialog" max-width="480">
        <v-card>
            <UiParentCard :title="'카드명세서 집계전표 생성'" style="height: 100%">
                <div>
                    <v-alert type="info" variant="tonal" density="compact" class="mb-4">
                        이용카드와 실제 카드대금 결제일을 선택하면 해당 카드내역을 usageType 기준으로 집계합니다. 카드내역 화면에서 usageType
                        검토/확정을 먼저 마친 뒤 실행하세요.
                    </v-alert>
    
                    <v-select
                        v-model="selectedCard"
                        :items="cardOptions"
                        item-title="label"
                        :loading="cardOptionsLoading"
                        label="이용카드"
                        density="compact"
                        class="mb-3"
                        return-object
                    />
                    <v-text-field v-model="paymentDate" type="date" label="결제일" density="compact" />
                </div>
                <v-card-actions class="justify-end">
                    <v-spacer />
                    <v-btn variant="text" @click="dialog = false">닫기</v-btn>
                    <v-btn color="primary" :loading="generating" @click="onGenerate">생성</v-btn>
                </v-card-actions>
            </UiParentCard>
        </v-card>
    </v-dialog>

    <v-dialog v-model="confirmDialog" max-width="480">
        <v-card>
            <UiParentCard :title="'주의 — 연계 정산전표 삭제 확인'" class="text-error" style="height: 100%">
                <v-card-text style="white-space: pre-line">
                    {{ confirmMessage }}
                </v-card-text>
                <v-card-actions>
                    <v-spacer />
                    <v-btn variant="text" @click="confirmDialog = false">취소</v-btn>
                    <v-btn color="error" @click="onConfirmCascadeDelete">확인하고 함께 삭제 후 재생성</v-btn>
                </v-card-actions>
            </UiParentCard>
        </v-card>
    </v-dialog>
</template>

<style scoped></style>
