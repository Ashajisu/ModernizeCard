<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import ExcelUploadDialogBtn from '@/common/excel/ExcelUploadDialogBtn.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import { useTableManager } from '@/common/useTableManager';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';

interface OpeningBalanceRow extends Record<string, any> {
    accountCode: string;
    amount: number;
    asOfDate: string;
    journalEntryId?: number;
}

// 검색
const formFields = ref<FormField[]>([
    { label: '계정코드', name: 'accountCode', type: 'text', value: '', required: false, disabled: false }
]);

const headers = ref<any[]>([
    { title: '계정코드', align: 'start', key: 'accountCode' },
    { title: '계정명', align: 'start', key: 'accountName' },
    { title: '개시잔액', align: 'end', key: 'amount' },
    { title: '기준일자', align: 'center', key: 'asOfDate' },
    { title: '전표ID', align: 'center', key: 'journalEntryId' }
]);

const rows = ref<OpeningBalanceRow[]>([]);

onMounted(async () => {
    try {
        const response = await apiClient.get('/journal/opening-balance');
        rows.value = response.list ?? response; // 다른 화면과 동일하게 list 래핑 여부 방어적으로 처리
    } catch (e) {
        console.error('개시잔액 조회 중 오류:', e);
    }
});

// identifierField: 이 데이터는 서버발급 PK가 없고 accountCode가 유니크 키이므로 이걸 사용
const identifierField: string = 'accountCode';
const { onSearch, resetSearch, filteredList, onExcelSave } = useTableManager<OpeningBalanceRow>(rows, formFields, null, identifierField);
</script>

<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="초기 잔액 등록">
                <v-row>
                    <CustomSearchChecksForm :formFields="formFields" :colsPerRow="5" :edit="true" :hide-details="true">
                        <template v-slot:lineBtn="{ validateForm }">
                            <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                                <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                            </div>
                        </template>
                    </CustomSearchChecksForm>
                </v-row>

                <v-row>
                    <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <ExcelUploadDialogBtn :save="onExcelSave" :url="'/journal/opening-balance'" title="초기잔액 엑셀 업로드" />
                        </div>
                    </v-col>
                </v-row>

                <v-row>
                    <v-data-table items-per-page="20" :headers="headers" :items="filteredList" class="border rounded-md">
                        <!-- 금액 컬럼 커스텀 렌더링 -->
                        <template #item.amount="{ item }">
                            {{ formatMoney(item.amount) }}
                        </template>
                    </v-data-table>
                </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped></style>