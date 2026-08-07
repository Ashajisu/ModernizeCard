<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { AccountOption, JournalDetail, JournalListItem, PageResponse } from '@/types/custom/JournalTypes';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';
import { useRouter } from 'vue-router';
import { startOfMonth, endOfMonth, format } from 'date-fns';
import { useTableManager } from '@/common/useTableManager';

const router = useRouter();

// 계정과목 목록 (필터용 + 이름→ID 역변환용)
const accounts = ref<AccountOption[]>([]);

const sourceOptions = ['MANUAL', 'CARD_IMPORT', 'SETTLEMENT', 'RECURRING', 'OPENING', 'BANK_IMPORT'];
const confirmedOptions = ['전체', '확정', '미확인'];

const formFields = ref<FormField[]>([
    { label: '조회시작일', name: 'fromDate', type: 'date', value: format(startOfMonth(new Date()), 'yyyy-MM-dd'), required: false, disabled: false },
    { label: '조회종료일', name: 'toDate', type: 'date', value: format(endOfMonth(new Date()), 'yyyy-MM-dd'), required: false, disabled: false },
    { label: '거래처', name: 'vendor', type: 'text', value: '', required: false, disabled: false },
    { label: '출처', name: 'source', type: 'select', value: '', options: sourceOptions, required: false, disabled: false },
    { label: '확정여부', name: 'confirmedLabel', type: 'select', value: '전체', options: confirmedOptions, required: false, disabled: false },
    { label: '확정', name: 'confirmed', type: 'hidden', value: '', required: false, disabled: false },
]);

const headers = ref<any[]>([
    { title: '전표일자', align: 'start', key: 'entryDate' },
    { title: '적요', align: 'start', key: 'description' },
    { title: '거래처', align: 'start', key: 'vendor' },
    { title: '출처', align: 'center', key: 'source' },
    { title: '확정여부', align: 'center', key: 'confirmed' },
    { title: '금액', align: 'end', key: 'amount' },
    { title: '상세', align: 'center', key: 'actions', sortable: false }
]);

const list = ref<JournalListItem[]>([]);
const fromDate = ref<String | null>(null);
const toDate = ref<String | null>(null);
// `users` 값을 동적으로 반영하도록 useTableManager 를 수정하였습니다.
//모듈 호출 : 기존코드 동일
const identifierField: string = 'id';
const { search, resetSearch, filteredList, selectedEmpId, onSelectionChange } =
    useTableManager<any>(list, formFields, null, identifierField);

const detailDialog = ref(false);
const detail = ref<JournalDetail | null>(null);

const loadAccounts = async () => {
    const response = await apiClient.get('/journal/accounts');
    accounts.value = response;
    const accountField = formFields.value.find((f) => f.name === 'accountName');
    if (accountField) {
        accountField.options = accounts.value.filter((a) => a.leaf).map((a) => a.name);
    }
};

const fetchList = async (param?:any) => {
    list.value = await apiClient.get('/journal/entries', param);
};

onMounted(async () => {
    await loadAccounts();
    await fetchList();
});

const onSearchFetch = async (validateForm: any) => {
    const formData = await validateForm();
    const dateParam = {
        fromDate: formData.fromDate,
        toDate: formData.toDate
    };
    if (isChanged(dateParam)){
        fromDate.value = formData.fromDate;
        toDate.value = formData.toDate;
        await fetchList(dateParam);
    }
    search.value = {
        vendor: formData.vendor,
        memberTag: formData.memberTag,
        source: formData.source,
        confirmed: formData.confirmedLabel=='확정'? true : formData.confirmedLabel=='전체'? null : false
    }
    console.log('search: ',search.value);
};

function isChanged(dateParam: any) {
    return dateParam.fromDate !== fromDate.value || dateParam.toDate !== toDate.value;
}

const openDetail = async (item: JournalListItem) => {
    detail.value = await apiClient.get(`/journal/entries/${item.id}`);
    detailDialog.value = true;
};

const goToEdit = (id: number) => {
    router.push({ path: '/journal/manualentry', query: { id } });
};

const onConfirm = async (id: number) => {
    await apiClient.patch(`/journal/entries/${id}/confirm`, id);
    detailDialog.value = false;
    await fetchList();
};

const onDeleteFetch = async (id: number) => {
    if (!confirm('이 전표를 삭제하시겠습니까?')) return;
    await apiClient.delete(`/journal/entries/${id}`);
    detailDialog.value = false;
    await fetchList();
};

const sourceLabel = (source: string) => {
    const map: Record<string, string> = {
        MANUAL: '수기입력',
        CARD_IMPORT: '카드거래',
        SETTLEMENT: '계좌정산',
        RECURRING: '정기분개',
        OPENING: '개시잔액',
        BANK_IMPORT: '은행거래'
    };
    return map[source] ?? source;
};
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="전표조회">
                <v-row>
                    <CustomSearchChecksForm :formFields="formFields" :colsPerRow="5" :edit="true" :hide-details="true">
                        <template v-slot:lineBtn="{ validateForm }">
                            <v-btn color="primary" flat @click="onSearchFetch(validateForm)">조회</v-btn>
                            <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                        </template>
                    </CustomSearchChecksForm>
                </v-row>

                <v-row>
                    <v-data-table
                        items-per-page="20"
                        :headers="headers"
                        :items="filteredList"
                        select-strategy="single"
                        show-select
                        class="border rounded-md"
                        v-model="selectedEmpId"
                        :item-value="identifierField"
                        @update:model-value="onSelectionChange"
                        mobile-breakpoint="sm">
                        <!-- 날짜 컬럼 -->
                        <template #item.entryDate="{ item }">
                            {{ item.entryDate }}
                        </template>

                        <!-- 출처 컬럼 -->
                        <template #item.source="{ item }">
                            <v-chip :color="item.autoGenerated ? 'grey' : 'primary'" size="small" variant="tonal">
                                {{ sourceLabel(item.source) }}
                            </v-chip>
                        </template>

                        <!-- 확정여부 컬럼 -->
                        <template #item.confirmed="{ item }">
                            <v-chip :color="item.confirmed ? 'success' : 'warning'" size="small" variant="tonal">
                                {{ item.confirmed ? '확정' : '미확인' }}
                            </v-chip>
                        </template>

                        <!-- 금액 컬럼 -->
                        <template #item.amount="{ item }">
                            {{ formatMoney(item.amount) }}
                        </template>

                        <!-- 상세보기 버튼 -->
                        <template #item.actions="{ item }">
                            <v-btn size="small" variant="text" color="primary" @click="openDetail(item)">상세</v-btn>
                        </template>
                    </v-data-table>
                </v-row>
            </UiParentCard>
        </v-col>
    </v-row>

    <!-- 전표 상세 다이얼로그 -->
    <v-dialog v-model="detailDialog" max-width="700">
        <v-card v-if="detail">
            <v-card-title class="d-flex justify-space-between align-center">
                <span>전표 상세</span>
                <v-chip :color="detail.confirmed ? 'success' : 'warning'" size="small">
                    {{ detail.confirmed ? '확정' : '미확인' }}
                </v-chip>
            </v-card-title>
            <v-card-text>
                <div class="mb-2"><b>전표일자</b> {{ detail.entryDate }}</div>
                <div class="mb-2"><b>적요</b> {{ detail.description }}</div>
                <div class="mb-2"><b>거래처</b> {{ detail.vendor }}</div>
                <div class="mb-2"><b>가족태그</b> {{ detail.memberTag }}</div>
                <div class="mb-4"><b>출처</b> {{ sourceLabel(detail.source) }}</div>

                <v-table density="compact">
                    <thead>
                        <tr>
                            <th>계정과목</th>
                            <th class="text-end">차변</th>
                            <th class="text-end">대변</th>
                            <th>메모</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="line in detail.lines" :key="line.lineId">
                            <td>{{ line.accountName }}</td>
                            <td class="text-end">{{ line.debitAmount ? formatMoney(line.debitAmount) : '' }}</td>
                            <td class="text-end">{{ line.creditAmount ? formatMoney(line.creditAmount) : '' }}</td>
                            <td>{{ line.memo }}</td>
                        </tr>
                        <tr class="font-weight-bold">
                            <td>합계</td>
                            <td class="text-end">{{ formatMoney(detail.totalDebit) }}</td>
                            <td class="text-end">{{ formatMoney(detail.totalCredit) }}</td>
                            <td></td>
                        </tr>
                    </tbody>
                </v-table>
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" variant="text" @click="goToEdit(detail.id)">수정</v-btn>
                <v-btn v-if="!detail.confirmed" color="success" variant="text" @click="onConfirm(detail.id)">확정처리</v-btn>
                <v-spacer />
                <v-btn color="error" variant="text" @click="onDeleteFetch(detail.id)">삭제</v-btn>
                <v-btn variant="text" @click="detailDialog = false">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style scoped></style>
