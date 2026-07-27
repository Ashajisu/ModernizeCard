<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { AccountBalanceItem } from '@/types/custom/JournalTypes';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';
import { format } from 'date-fns';

const today = format(new Date(), 'yyyy-MM-dd');

const formFields = ref<FormField[]>([
    { label: '조회일자', name: 'asOfDate', type: 'date', value: today, required: false, disabled: false }
]);

const headers = ref<any[]>([
    { title: '계정과목', align: 'start', key: 'name' },
    { title: '확정금액', align: 'end', key: 'confirmedAmount' },
    { title: '미확인금액', align: 'end', key: 'unconfirmedAmount' },
    { title: '합계', align: 'end', key: 'totalAmount' }
]);

const assets = ref<AccountBalanceItem[]>([]);
const liabilities = ref<AccountBalanceItem[]>([]);
const summary = ref({
    totalAssets: 0,
    totalLiabilities: 0,
    equity: 0
});

const fetchBalanceSheet = async (asOfDate: string) => {
    const response = await apiClient.get('/journal/balance-sheet', { asOfDate });
    assets.value = response.assets;
    liabilities.value = response.liabilities;
    summary.value = {
        totalAssets: response.totalAssets,
        totalLiabilities: response.totalLiabilities,
        equity: response.equity
    };
};

onMounted(() => fetchBalanceSheet(today));

const onSearch = async (validateForm: any) => {
    const formData = await validateForm();
    await fetchBalanceSheet(formData.asOfDate);
};

// 최상위 그룹(depth 0)은 굵게 표시, 하위 계정은 들여쓰기
const nameStyle = (item: AccountBalanceItem) => ({
    paddingLeft: `${item.depth * 20}px`,
    fontWeight: item.depth === 0 ? 700 : 400
});
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="대차대조표">
                <v-row>
                    <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true" :hide-details="true">
                        <template v-slot:lineBtn="{ validateForm }">
                            <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                            </div>
                        </template>
                    </CustomSearchChecksForm>
                </v-row>

                <v-row>
                    <v-col cols="12" md="6">
                        <h3 class="mb-2">자산</h3>
                        <v-data-table :headers="headers" :items="assets" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <!-- 계정과목 컬럼: 계층 들여쓰기 -->
                            <template #item.name="{ item }">
                                <span :style="nameStyle(item)">{{ item.name }}</span>
                            </template>
                            <!-- 금액 컬럼 커스텀 렌더링 -->
                            <template #item.confirmedAmount="{ item }">
                                {{ formatMoney(item.confirmedAmount) }}
                            </template>
                            <template #item.unconfirmedAmount="{ item }">
                                {{ formatMoney(item.unconfirmedAmount) }}
                            </template>
                            <template #item.totalAmount="{ item }">
                                {{ formatMoney(item.totalAmount) }}
                            </template>
                        </v-data-table>
                        <div class="totalSum d-flex justify-space-between pa-2">
                            <span>자산 총계</span>
                            <span>{{ formatMoney(summary.totalAssets) }}</span>
                        </div>
                    </v-col>

                    <v-col cols="12" md="6">
                        <h3 class="mb-2">부채</h3>
                        <v-data-table :headers="headers" :items="liabilities" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <template #item.name="{ item }">
                                <span :style="nameStyle(item)">{{ item.name }}</span>
                            </template>
                            <template #item.confirmedAmount="{ item }">
                                {{ formatMoney(item.confirmedAmount) }}
                            </template>
                            <template #item.unconfirmedAmount="{ item }">
                                {{ formatMoney(item.unconfirmedAmount) }}
                            </template>
                            <template #item.totalAmount="{ item }">
                                {{ formatMoney(item.totalAmount) }}
                            </template>
                        </v-data-table>
                        <div class="totalSum d-flex justify-space-between pa-2">
                            <span>부채 총계</span>
                            <span>{{ formatMoney(summary.totalLiabilities) }}</span>
                        </div>
                        <div class="equitySum d-flex justify-space-between pa-2">
                            <span>자본(순자산)</span>
                            <span>{{ formatMoney(summary.equity) }}</span>
                        </div>
                        <div class="totalSum d-flex justify-space-between pa-2 border-t-sm">
                            <span>부채와 자본 총계</span>
                            <span>{{ formatMoney(summary.totalLiabilities + summary.equity) }}</span>
                        </div>
                    </v-col>
                </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
.totalSum {
    font-weight: 700;
    color: #1d4ed8;
}
.equitySum {
    font-weight: 700;
    color: #c424a4de;
}
</style>