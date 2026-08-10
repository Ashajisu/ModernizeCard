<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';
import { format } from 'date-fns';

interface BudgetItem {
    accountId: number;
    accountCode: string;
    accountName: string;
    budgetAmount: number;
    actualAmount: number;
    remainingAmount: number;
    usageRate: number | null;
}

interface BudgetEditItem {
    accountId: number;
    accountCode: string;
    accountName: string;
    budgetAmount: number;
}

const yearMonth = ref(format(new Date(), 'yyyy-MM'));

const items = ref<BudgetItem[]>([]);
const totalBudget = ref(0);
const totalActual = ref(0);
const totalRemaining = ref(0);
const loading = ref(false);

const headers = ref<any[]>([
    { title: '계정과목', align: 'start', key: 'accountName' },
    { title: '예산', align: 'end', key: 'budgetAmount' },
    { title: '지출', align: 'end', key: 'actualAmount' },
    { title: '잔여', align: 'end', key: 'remainingAmount' },
    { title: '사용률', align: 'start', key: 'usageRate', width: '200' }
]);

const fetchStats = async (ym: string) => {
    loading.value = true;
    try {
        const response = await apiClient.get('/journal/budget', { yearMonth: ym });
        items.value = response.items;
        totalBudget.value = response.totalBudget;
        totalActual.value = response.totalActual;
        totalRemaining.value = response.totalRemaining;
    } finally {
        loading.value = false;
    }
};

onMounted(() => fetchStats(yearMonth.value));

const onSearch = () => fetchStats(yearMonth.value);

const progressColor = (rate: number | null) => {
    if (rate === null) return 'grey';
    if (rate >= 100) return 'error';
    if (rate >= 80) return 'warning';
    return 'success';
};

// ── 예산 설정 다이얼로그 ──────────────────────────────
const dialog = ref(false);
const editItems = ref<BudgetEditItem[]>([]);
const editLoading = ref(false);
const savingBudget = ref(false);

const openEditDialog = async () => {
    dialog.value = true;
    editLoading.value = true;
    try {
        editItems.value = await apiClient.get('/journal/budget/edit', { yearMonth: yearMonth.value });
    } finally {
        editLoading.value = false;
    }
};

const onSaveBudgets = async () => {
    savingBudget.value = true;
    try {
        await apiClient.put('/journal/budget', {
            yearMonth: yearMonth.value,
            items: editItems.value.map((i) => ({ accountId: i.accountId, budgetAmount: i.budgetAmount || 0 }))
        });
        dialog.value = false;
        await fetchStats(yearMonth.value);
    } finally {
        savingBudget.value = false;
    }
};
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="월별 예산/지출통계">
                <v-row align="center">
                    <v-col cols="12" md="3">
                        <v-text-field v-model="yearMonth" type="month" label="조회월" density="compact" @change="onSearch" />
                    </v-col>
                    <v-col cols="12" md="3">
                        <v-btn color="primary" flat @click="onSearch">조회</v-btn>
                    </v-col>
                    <v-col cols="12" md="6" class="d-flex justify-end">
                        <v-btn color="primary" variant="outlined" @click="openEditDialog">예산 설정</v-btn>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12" sm="4">
                        <v-sheet class="pa-4 rounded-lg" color="grey-lighten-4">
                            <div class="text-caption text-medium-emphasis">총예산</div>
                            <div class="text-h6 font-weight-bold">{{ formatMoney(totalBudget) }}</div>
                        </v-sheet>
                    </v-col>
                    <v-col cols="12" sm="4">
                        <v-sheet class="pa-4 rounded-lg" color="grey-lighten-4">
                            <div class="text-caption text-medium-emphasis">총지출</div>
                            <div class="text-h6 font-weight-bold">{{ formatMoney(totalActual) }}</div>
                        </v-sheet>
                    </v-col>
                    <v-col cols="12" sm="4">
                        <v-sheet class="pa-4 rounded-lg" :color="totalRemaining < 0 ? 'red-lighten-4' : 'green-lighten-4'">
                            <div class="text-caption text-medium-emphasis">잔여</div>
                            <div class="text-h6 font-weight-bold" :class="{ 'text-error': totalRemaining < 0 }">
                                {{ formatMoney(totalRemaining) }}
                            </div>
                        </v-sheet>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12">
                        <v-data-table :headers="headers" :items="items" :loading="loading" class="border rounded-md" items-per-page="20">
                            <template #item.budgetAmount="{ item }">{{ formatMoney(item.budgetAmount) }}</template>
                            <template #item.actualAmount="{ item }">{{ formatMoney(item.actualAmount) }}</template>
                            <template #item.remainingAmount="{ item }">
                                <span :class="{ 'text-error': item.remainingAmount < 0 }">{{ formatMoney(item.remainingAmount) }}</span>
                            </template>
                            <template #item.usageRate="{ item }">
                                <div v-if="item.usageRate !== null" class="d-flex align-center gap-2">
                                    <v-progress-linear
                                        :model-value="Math.min(item.usageRate, 100)"
                                        :color="progressColor(item.usageRate)"
                                        height="8"
                                        rounded
                                        style="max-width: 120px"
                                    />
                                    <span class="text-caption">{{ item.usageRate.toFixed(0) }}%</span>
                                </div>
                                <span v-else class="text-caption text-medium-emphasis">예산 미설정</span>
                            </template>
                        </v-data-table>
                    </v-col>
                </v-row>
            </UiParentCard>
        </v-col>
    </v-row>

    <v-dialog v-model="dialog" max-width="480">
        <v-card>
            <v-card-title>{{ yearMonth }} 예산 설정</v-card-title>
            <v-card-text style="max-height: 60vh; overflow-y: auto">
                <v-progress-circular v-if="editLoading" indeterminate class="d-block mx-auto my-4" />
                <v-list v-else density="compact">
                    <v-list-item v-for="item in editItems" :key="item.accountId">
                        <v-row align="center" no-gutters>
                            <v-col cols="6">
                                <v-list-item-title>{{ item.accountName }}</v-list-item-title>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field v-model.number="item.budgetAmount" type="number" density="compact" hide-details suffix="원" />
                            </v-col>
                        </v-row>
                    </v-list-item>
                </v-list>
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn variant="text" @click="dialog = false">취소</v-btn>
                <v-btn color="primary" :loading="savingBudget" @click="onSaveBudgets">저장</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style scoped></style>
