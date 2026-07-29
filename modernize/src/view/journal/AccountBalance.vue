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
    { title: '잔액', align: 'end', key: 'totalAmount' }
]);

const assets = ref<AccountBalanceItem[]>([]);
const liabilities = ref<AccountBalanceItem[]>([]);
const equity = ref<AccountBalanceItem[]>([]);
const revenues = ref<AccountBalanceItem[]>([]);
const expenses = ref<AccountBalanceItem[]>([]);

const fetchBalances = async (asOfDate: string) => {
    const response = await apiClient.get('/journal/account-balance', { asOfDate });
    assets.value = response.assets;
    liabilities.value = response.liabilities;
    equity.value = response.equity;
    revenues.value = response.revenues;
    expenses.value = response.expenses;
};

onMounted(() => fetchBalances(today));

const onSearch = async (validateForm: any) => {
    const formData = await validateForm();
    await fetchBalances(formData.asOfDate);
};

const nameStyle = (item: AccountBalanceItem) => ({
    paddingLeft: `${item.depth * 20}px`,
    fontWeight: item.depth === 0 ? 700 : 400
});
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="계정별 잔액">
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
                            <template #item.name="{ item }"><span :style="nameStyle(item)">{{ item.name }}</span></template>
                            <template #item.totalAmount="{ item }">{{ formatMoney(item.totalAmount) }}</template>
                        </v-data-table>
                    </v-col>
                    <v-col cols="12" md="6">
                        <h3 class="mb-2">부채</h3>
                        <v-data-table :headers="headers" :items="liabilities" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <template #item.name="{ item }"><span :style="nameStyle(item)">{{ item.name }}</span></template>
                            <template #item.totalAmount="{ item }">{{ formatMoney(item.totalAmount) }}</template>
                        </v-data-table>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col cols="12" md="4">
                        <h3 class="mb-2">자본</h3>
                        <v-data-table :headers="headers" :items="equity" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <template #item.name="{ item }"><span :style="nameStyle(item)">{{ item.name }}</span></template>
                            <template #item.totalAmount="{ item }">{{ formatMoney(item.totalAmount) }}</template>
                        </v-data-table>
                    </v-col>
                    <v-col cols="12" md="4">
                        <h3 class="mb-2">수익 (누적)</h3>
                        <v-data-table :headers="headers" :items="revenues" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <template #item.name="{ item }"><span :style="nameStyle(item)">{{ item.name }}</span></template>
                            <template #item.totalAmount="{ item }">{{ formatMoney(item.totalAmount) }}</template>
                        </v-data-table>
                    </v-col>
                    <v-col cols="12" md="4">
                        <h3 class="mb-2">비용 (누적)</h3>
                        <v-data-table :headers="headers" :items="expenses" hide-default-footer items-per-page="-1" class="border rounded-md">
                            <template #item.name="{ item }"><span :style="nameStyle(item)">{{ item.name }}</span></template>
                            <template #item.totalAmount="{ item }">{{ formatMoney(item.totalAmount) }}</template>
                        </v-data-table>
                    </v-col>
                </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped></style>
