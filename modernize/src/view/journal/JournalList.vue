<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { AccountOption, JournalDetail, JournalListItem, PageResponse } from '@/types/custom/JournalTypes';
import { apiClient } from '@/data/Axios';
import { formatMoney } from '@/utils/common';
import { useRouter } from 'vue-router';

const router = useRouter();

// 계정과목 목록 (필터용 + 이름→ID 역변환용)
const accounts = ref<AccountOption[]>([]);

const sourceOptions = ['MANUAL', 'CARD_IMPORT', 'SETTLEMENT', 'RECURRING'];
const confirmedOptions = ['전체', '확정', '미확인'];

const formFields = ref<FormField[]>([
    { label: '조회시작일', name: 'fromDate', type: 'date', value: '', required: false, disabled: false },
    { label: '조회종료일', name: 'toDate', type: 'date', value: '', required: false, disabled: false },
    { label: '계정과목', name: 'accountName', type: 'select', value: '', options: [], required: false, disabled: false },
    { label: '거래처', name: 'vendor', type: 'text', value: '', required: false, disabled: false },
    { label: '가족태그', name: 'memberTag', type: 'text', value: '', required: false, disabled: false },
    { label: '출처', name: 'source', type: 'select', value: '', options: sourceOptions, required: false, disabled: false },
    { label: '확정여부', name: 'confirmedLabel', type: 'select', value: '전체', options: confirmedOptions, required: false, disabled: false },
    { label: '최소금액', name: 'minAmount', type: 'number', value: '', required: false, disabled: false },
    { label: '최대금액', name: 'maxAmount', type: 'number', value: '', required: false, disabled: false }
]);

const headers = ref<any[]>([
    { title: '전표일자', align: 'start', key: 'entryDate' },
    { title: '적요', align: 'start', key: 'description' },
    { title: '거래처', align: 'start', key: 'vendor' },
    { title: '가족태그', align: 'center', key: 'memberTag' },
    { title: '출처', align: 'center', key: 'source' },
    { title: '확정여부', align: 'center', key: 'confirmed' },
    { title: '금액', align: 'end', key: 'amount' },
    { title: '상세', align: 'center', key: 'actions', sortable: false }
]);

const list = ref<JournalListItem[]>([]);
const totalElements = ref(0);
const page = ref(0);
const size = ref(20);

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

const buildSearchParams = (formData: any) => {
    const params: any = {
        fromDate: formData.fromDate || undefined,
        toDate: formData.toDate || undefined,
        vendor: formData.vendor || undefined,
        memberTag: formData.memberTag || undefined,
        source: formData.source || undefined,
        minAmount: formData.minAmount || undefined,
        maxAmount: formData.maxAmount || undefined,
        page: page.value,
        size: size.value
    };

    // 계정과목명 → accountId 역변환
    if (formData.accountName) {
        const matched = accounts.value.find((a) => a.name === formData.accountName);
        if (matched) params.accountId = matched.id;
    }

    // 확정여부 라벨 → boolean 역변환
    if (formData.confirmedLabel === '확정') params.confirmed = true;
    else if (formData.confirmedLabel === '미확인') params.confirmed = false;

    return params;
};

const fetchList = async (params: any = { page: 0, size: size.value }) => {
    const response: PageResponse<JournalListItem> = await apiClient.get('/journal/entries', params);
    list.value = response.content;
    totalElements.value = response.totalElements;
    page.value = response.number;
};

onMounted(async () => {
    await loadAccounts();
    await fetchList();
});

const onSearch = async (validateForm: any) => {
    const formData = await validateForm();
    page.value = 0;
    await fetchList(buildSearchParams(formData));
};

const openDetail = async (item: JournalListItem) => {
    detail.value = await apiClient.get(`/journal/entries/${item.id}`);
    detailDialog.value = true;
};

const goToEdit = (id: number) => {
    router.push({ path: '/journal/manualentry', query: { id } });
};

const onConfirm = async (id: number) => {
    await apiClient.patch(`/journal/entries/${id}/confirm`);
    detailDialog.value = false;
    await fetchList();
};

const onDelete = async (id: number) => {
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
        RECURRING: '정기분개'
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
                            <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                            </div>
                        </template>
                    </CustomSearchChecksForm>
                </v-row>

                <v-row>
                    <v-data-table :headers="headers" :items="list" hide-default-footer class="border rounded-md">
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

                <v-row class="mt-2">
                    <v-col class="d-flex justify-center">
                        <v-pagination
                            v-model="page"
                            :length="Math.ceil(totalElements / size)"
                            @update:model-value="(p: number) => fetchList({ page: p - 1, size })"
                        />
                    </v-col>
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
                <v-btn v-if="!detail.autoGenerated" color="primary" variant="text" @click="goToEdit(detail.id)">수정</v-btn>
                <v-btn v-if="!detail.confirmed" color="success" variant="text" @click="onConfirm(detail.id)">확정처리</v-btn>
                <v-spacer />
                <v-btn color="error" variant="text" @click="onDelete(detail.id)">삭제</v-btn>
                <v-btn variant="text" @click="detailDialog = false">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style scoped></style>
