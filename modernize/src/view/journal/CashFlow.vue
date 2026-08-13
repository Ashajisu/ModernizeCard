<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';
import { format } from 'date-fns';

interface CashFlowLine {
    accountCode: string;
    accountName: string;
    amount: number;
}

interface SalaryFlow {
    salaryIncome: number;
    fixedLivingItems: CashFlowLine[];
    fixedLivingTotal: number;
    savingsOutflow: number;
    loanPrincipalOutflow: number;
    loanInterestOutflow: number;
    remainingCash: number;
}

interface EmergencyFlow {
    variableLivingItems: CashFlowLine[];
    usageTotal: number;
}

interface DutchPayFlow {
    prepaidAmount: number;
    settledAmount: number;
    unsettledAmount: number;
}

const yearMonth = ref(format(new Date(), 'yyyy-MM'));
const loading = ref(false);

const salaryFlow = ref<SalaryFlow | null>(null);
const emergencyFlow = ref<EmergencyFlow | null>(null);
const dutchPayFlow = ref<DutchPayFlow | null>(null);
const totalCashFlow = ref(0);

const fetchCashFlow = async (ym: string) => {
    loading.value = true;
    try {
        const response = await apiClient.get('/cash-flow', { yearMonth: ym });
        salaryFlow.value = response.salaryFlow;
        emergencyFlow.value = response.emergencyFlow;
        dutchPayFlow.value = response.dutchPayFlow;
        totalCashFlow.value = response.totalCashFlow;
    } finally {
        loading.value = false;
    }
};

onMounted(() => fetchCashFlow(yearMonth.value));
const onSearch = () => fetchCashFlow(yearMonth.value);
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="자금원별 현금흐름">
                <v-row align="center" class="mb-2">
                    <v-col cols="12" md="3">
                        <v-text-field v-model="yearMonth" type="month" label="조회월" density="compact" @change="onSearch" />
                    </v-col>
                    <v-col cols="12" md="3">
                        <v-btn color="primary" flat :loading="loading" @click="onSearch">조회</v-btn>
                    </v-col>
                    <v-col cols="12" md="6" class="d-flex justify-end">
                        <v-sheet class="pa-3 rounded-lg" :color="totalCashFlow < 0 ? 'red-lighten-4' : 'green-lighten-4'">
                            <span class="text-caption text-medium-emphasis mr-2">전체 현금흐름</span>
                            <span class="text-h6 font-weight-bold" :class="{ 'text-error': totalCashFlow < 0 }">
                                {{ formatMoney(totalCashFlow) }}
                            </span>
                        </v-sheet>
                    </v-col>
                </v-row>
            </UiParentCard>
        </v-col>

        <!-- 급여 흐름 -->
        <v-col cols="12" md="4" v-if="salaryFlow">
            <v-card variant="outlined" class="pa-4">
                <div class="text-subtitle-1 font-weight-bold mb-3">급여 흐름</div>

                <div class="d-flex justify-space-between mb-2">
                    <span>급여 수입</span>
                    <span class="font-weight-bold">{{ formatMoney(salaryFlow.salaryIncome) }}</span>
                </div>

                <div class="text-caption text-medium-emphasis mt-3 mb-1">생활비</div>
                <div v-for="item in salaryFlow.fixedLivingItems" :key="item.accountCode" class="d-flex justify-space-between pl-3">
                    <span class="text-body-2">{{ item.accountName }}</span>
                    <span class="text-body-2">{{ formatMoney(item.amount) }}</span>
                </div>
                <div class="d-flex justify-space-between font-weight-bold border-t-sm pt-1 mt-1">
                    <span>생활비 합계</span>
                    <span>{{ formatMoney(salaryFlow.fixedLivingTotal) }}</span>
                </div>

                <div class="d-flex justify-space-between mt-3">
                    <span>저축/적금</span>
                    <span>{{ formatMoney(salaryFlow.savingsOutflow) }}</span>
                </div>
                <div class="d-flex justify-space-between">
                    <span>대출 원리금</span>
                    <span>{{ formatMoney(salaryFlow.loanPrincipalOutflow) }}</span>
                </div>

                <v-divider class="my-3" />
                <div class="d-flex justify-space-between">
                    <span class="text-subtitle-2 font-weight-bold">급여 잔여금</span>
                    <span class="text-h6 font-weight-bold" :class="{ 'text-error': salaryFlow.remainingCash < 0 }">
                        {{ formatMoney(salaryFlow.remainingCash) }}
                    </span>
                </div>
            </v-card>
        </v-col>

        <!-- 비상금 흐름 -->
        <v-col cols="12" md="4" v-if="emergencyFlow">
            <v-card variant="outlined" class="pa-4">
                <div class="text-subtitle-1 font-weight-bold mb-3">비상금 흐름</div>

                <div v-for="item in emergencyFlow.variableLivingItems" :key="item.accountCode" class="d-flex justify-space-between mb-1">
                    <span class="text-body-2">{{ item.accountName }}</span>
                    <span class="text-body-2">{{ formatMoney(item.amount) }}</span>
                </div>

                <v-alert v-if="emergencyFlow.variableLivingItems.length === 0" type="info" variant="tonal" density="compact" class="mt-2">
                    이번 달 비상금 사용 내역이 없습니다.
                </v-alert>

                <v-divider class="my-3" />
                <div class="d-flex justify-space-between">
                    <span class="text-subtitle-2 font-weight-bold">비상금 사용액</span>
                    <span class="text-h6 font-weight-bold text-error">
                        {{ formatMoney(emergencyFlow.usageTotal) }}
                    </span>
                </div>
            </v-card>
        </v-col>

        <!-- 더치페이 흐름 -->
        <v-col cols="12" md="4" v-if="dutchPayFlow">
            <v-card variant="outlined" class="pa-4">
                <div class="text-subtitle-1 font-weight-bold mb-3">더치페이 흐름</div>

                <div class="d-flex justify-space-between mb-2">
                    <span>선지출</span>
                    <span>{{ formatMoney(dutchPayFlow.prepaidAmount) }}</span>
                </div>
                <div class="d-flex justify-space-between mb-2">
                    <span>정산 입금</span>
                    <span>{{ formatMoney(dutchPayFlow.settledAmount) }}</span>
                </div>

                <v-divider class="my-3" />
                <div class="d-flex justify-space-between">
                    <span class="text-subtitle-2 font-weight-bold">미정산</span>
                    <span class="text-h6 font-weight-bold" :class="{ 'text-error': dutchPayFlow.unsettledAmount > 0 }">
                        {{ formatMoney(dutchPayFlow.unsettledAmount) }}
                    </span>
                </div>
            </v-card>
        </v-col>
    </v-row>
</template>

<style scoped></style>
