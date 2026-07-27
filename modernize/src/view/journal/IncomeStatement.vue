<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { AccountBalanceItem } from '@/types/custom/JournalTypes';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';
import { format } from 'date-fns';

const currentYearMonth = format(new Date(), 'yyyy-MM');

const formFields = ref<FormField[]>([
    { label: '조회월', name: 'yearMonth', type: 'month', value: currentYearMonth, required: false, disabled: false }
]);

const headers = ref<any[]>([
    { title: '계정과목', align: 'start', key: 'name' },
    { title: '금액', align: 'end', key: 'totalAmount' }
]);

const expenses = ref<AccountBalanceItem[]>([]);
const revenues = ref<AccountBalanceItem[]>([]);
const summary = ref({
    totalRevenue: 0,
    totalExpense: 0,
    netIncome: 0
});

const fetchIncomeStatement = async (yearMonth: string) => {
    const response = await apiClient.get('/journal/income-statement', { yearMonth });
    expenses.value = response.expenses;
    revenues.value = response.revenues;
    summary.value = {
        totalRevenue: response.totalRevenue,
        totalExpense: response.totalExpense,
        netIncome: response.netIncome
    };
};

onMounted(() => fetchIncomeStatement(currentYearMonth));

const onSearch = async (validateForm: any) => {
    const formData = await validateForm();
    await fetchIncomeStatement(formData.yearMonth);
};

// const nameStyle = (item: AccountBalanceItem) => ({ paddingLeft: `${item.depth * 20}px` });
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="손익계산서">
                <v-row>
                    <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true" :hide-details="true">
                        <template v-slot:lineBtn="{ validateForm }">
                            <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                            </div>
                        </template>
                    </CustomSearchChecksForm>
                </v-row>

                <v-alert v-if="revenues.length === 0" type="info" variant="tonal" density="compact" class="mb-4">
                    수익 계정에 아직 분개가 없어 0으로 표시됩니다. 급여 등 수입을 손익에 반영하려면 별도 수익 계정/분개 설계가 필요합니다.
                </v-alert>

                <v-row>
                    <v-col cols="12" md="6">
                        <h3 class="mb-2">수익</h3>
                        <v-data-table :headers="headers" :items="revenues" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <!-- amount 컬럼 커스텀 렌더링 -->
                            <template #item.totalAmount="{ item }">
                                {{ formatMoney(item.totalAmount) }}
                            </template>
                        </v-data-table>
                        <div class="totalSum d-flex justify-space-between pa-2">
                            <span>수익 합계</span>
                            <span>{{ formatMoney(summary.totalRevenue) }}</span>
                        </div>
                    </v-col>

                    <v-col cols="12" md="6">
                        <h3 class="mb-2">비용</h3>
                        <v-data-table :headers="headers" :items="expenses" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <template #item.totalAmount="{ item }">
                                {{ formatMoney(item.totalAmount) }}
                            </template>
                        </v-data-table>
                        <div class="totalSum d-flex justify-space-between pa-2">
                            <span>비용 합계</span>
                            <span>{{ formatMoney(summary.totalExpense) }}</span>
                        </div>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12">
                        <div class="netIncome d-flex justify-space-between pa-3 border-t-sm">
                            <span>당기순이익 (수익 − 비용)</span>
                            <span>{{ formatMoney(summary.netIncome) }}</span>
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
.netIncome {
    font-weight: 700;
    font-size: 1.1rem;
}
</style>