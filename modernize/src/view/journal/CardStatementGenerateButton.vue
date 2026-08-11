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
const yearMonth = ref(format(new Date(), 'yyyy-MM'));

// 이용카드 옵션 — 최초 다이얼로그 오픈 시 한 번만 불러오고 캐시
const cardOptions = ref<CardOption[]>([]);
const cardOptionsLoaded = ref(false);
const cardOptionsLoading = ref(false);

const generating = ref(false);

// 결과 스낵바
const snackbar = ref(false);
const snackbarColor = ref<'success' | 'error'>('success');
const snackbarMessage = ref('');
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

const onGenerate = async () => {
    if (!selectedCard.value) {
        showSnackbar('이용카드를 선택해주세요.', 'error');
        return;
    }
    const card = selectedCard.value!;
    generating.value = true;
    try {
        const result = await apiClient.post(
            `/journal/card-statement/regenerate?cardCompanyCode=${card}&yearMonth=${yearMonth.value}`, {}
        );
        showSnackbar(`집계전표가 생성되었습니다. (전표 ID: ${result.entryId})`, 'success');
    } catch (e: any) {
        showSnackbar(e?.response?.data?.message || '전표 생성 중 오류가 발생했습니다.', 'error');
    } finally {
        generating.value = false;
    }
};
</script>

<template>
    <v-btn color="primary" variant="outlined" @click="openDialog">카드명세서 집계전표 생성</v-btn>
    
    <v-dialog v-model="dialog" max-width="480">
        <v-card>
            <UiParentCard :title="'카드명세서 집계전표 생성(비용확정)'" style="height: 100%">
                <div>
                    <v-alert v-if="!snackbar" type="info" variant="tonal" density="compact" class="mb-4">
                        선택한 카드사의 해당 월(거래일 기준) 카드내역을 usageType 기준으로 집계해 "비용확정" 전표를 만듭니다.
                        결제일/정산과는 무관합니다. 카드내역 화면에서 usageType 검토/확정을 먼저 마친 뒤 실행하세요.
                        
                    </v-alert>
                    <v-alert v-if="snackbar" :type="snackbarColor" variant="tonal" density="compact" class="mb-3">
                        {{ snackbarMessage }}
                    </v-alert>
                    <v-select v-model="selectedCard" :items="cardOptions" item-title="label" item-value="cardCompanyCode" label="카드사" density="compact" class="mb-3" />
                    <v-text-field v-model="yearMonth" type="month" label="대상월" density="compact" />
                </div>
                <v-card-actions class="justify-end">
                    <v-spacer />
                    <v-btn variant="text" @click="dialog = false">닫기</v-btn>
                    <v-btn color="primary" :loading="generating" @click="onGenerate">생성</v-btn>
                </v-card-actions>
            </UiParentCard>
        </v-card>
    </v-dialog>
</template>

<style scoped></style>
