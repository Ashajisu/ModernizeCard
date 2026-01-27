<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { SSCardItem, StatsItem } from '@/types/custom/DataTableTypes';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { useTableManager } from '@/common/useTableManager';
import ExcelUploadDialogBtn from '@/common/excel/ExcelUploadDialogBtn.vue';
import { apiClient } from '@/data/Axios';
import { formatMoney, saveToServer } from '@/utils/common';
import { format } from 'date-fns';

//검색
const formFields = ref<FormField[]>([
    { label: '이용구분', name: 'usageType', type: 'text', value: '', required: false, disabled: false },
    { label: '매입구분', name: 'purchaseType', type: 'text', value: '', required: false, disabled: false },
    { label: '거래일', name: 'transactionDate', type: 'date', value: '', required: false, disabled: false },
    { label: '결제일', name: 'paymentDate', type: 'date', value: '', required: false, disabled: false },
    { label: '가맹점명', name: 'merchantName', type: 'text', value: '', required: false, disabled: false }
]);
//테이블헤더
const headers = ref<any[]>([
    { title: 'No.', align: 'start', key: 'id' },
    { title: '거래일', align: 'start', key: 'transactionDate' },
    { title: '이용카드', align: 'start', key: 'usedCard' },
    { title: '가맹점명', align: 'start', key: 'merchantName' },
    { title: '금액', align: 'end', key: 'amount' },
    { title: '매입구분', align: 'center', key: 'purchaseType' },
    { title: '이용구분', align: 'center', key: 'usageType' },
    { title: '이용혜택', align: 'center', key: 'benefitType' },
    { title: '혜택금액', align: 'center', key: 'benefitAmount' },
    { title: '거래통화', align: 'center', key: 'currency' },
    { title: '결제일', align: 'center', key: 'paymentDate' }
]);
//상세정보
const userFields = ref<FormField[]>([
    { label: 'No.', name: 'id', type: 'hidden', value: '', required: true, disabled: true },
    { label: '거래일', name: 'transactionDate', type: 'datetime', value: '', required: true, disabled: false },
    { label: '이용카드', name: 'usedCard', type: 'text', value: '', placeholder: '본인509*', required: false, disabled: false },
    { label: '가맹점명', name: 'merchantName', type: 'text', value: '', placeholder: '가맹점명 입력', required: false, disabled: false },
    { label: '금액', name: 'amount', type: 'text', value: '', placeholder: '금액 입력', required: true, disabled: false },
    { label: '매입구분', name: 'purchaseType', type: 'text', value: '', placeholder: '결제확정', required: false, disabled: false },
    { label: '이용구분', name: 'usageType', type: 'text', value: '', placeholder: '사용자명 입력', required: true, disabled: false },
    { label: '이용혜택', name: 'benefitType', type: 'text', value: '', placeholder: '사용자명 입력', required: false, disabled: false },
    { label: '혜택금액', name: 'benefitAmount', type: 'text', value: '', placeholder: '0,000', required: false, disabled: false },
    { label: '거래통화', name: 'currency', type: 'text', value: '', placeholder: '0,000', required: true, disabled: false },
    { label: '결제일', name: 'paymentDate', type: 'datetime', value: '', placeholder: 'YYYY-MM-DD', required: false, disabled: false },
    { label: '총할부금액', name: 'installmentTotal', type: 'text', value: '', placeholder: '0,000', required: false, disabled: false },
    { label: '할부 개월', name: 'installmentMonths', type: 'number', value: '', placeholder: '0', required: false, disabled: false },
    { label: '할부 회차', name: 'installmentNumber', type: 'number', value: '', placeholder: '0', required: false, disabled: false }
]);

//집계 검색
const payDate = format(new Date().setDate(27), 'yyyy-MM-dd'); // 매월 결제일 27일.
const statFormFields = ref<FormField[]>([
    // { label: '거래시작일', name: 'startDate', type: 'date', value: '', required: false, disabled: false },
    // { label: '거래종료일', name: 'endDate', type: 'date', value: '', required: false, disabled: false },
    { label: '결제일', name: 'payDate', type: 'date', value: payDate, required: false, disabled: false }
]);
//집계 테이블헤더
const statHeaders = ref<any[]>([
    { title: '이용구분', align: 'center', key: 'title' },
    { title: '금액', align: 'center', key: 'stat1' },
    { title: '할인', align: 'center', key: 'stat2' },
    { title: '결제확정', align: 'center', key: 'stat3' },
    { title: '할부잔액', align: 'center', key: 'stat4' }
]);

const setUsers = (userList: SSCardItem[]) => {
    users.value = userList;
    // console.log('사용자 데이터:', users.value);
};
const setStats = (statList: StatsItem[]) => {
    stats.value = statList;
    // console.log('집계 데이터:', stats.value);
};
// mockApi 로 데이터 불러오기.
const users = ref<SSCardItem[]>([]); // 사용자 데이터를 저장할 변수
const stats = ref<StatsItem[]>([]); // 집계 데이터를 저장할 변수
onMounted(async () => {
    // 초기화 또는 초기 작업 수행
    try {
        const response = await apiClient.get('/card/list/woori');
        setUsers(response.list);
        if (response.list) {
            const response = await apiClient.post('/card/usageTypeStats/woori', { payDate });
            setStats(response.list);
        }
    } catch (e) {
        console.error('데이터 로드 중 오류 발생:', e);
    }
});

const onSearchStats = async (validateForm: any) => {
    const formData = await validateForm();
    //빈문자열 제거
    for (const key in formData) {
        if (formData[key] === '') {
            delete formData[key];
        }
    }
    const response = await apiClient.post('/card/usageTypeStats/woori', formData);
    setStats(response.list);
};
// `users` 값을 동적으로 반영하도록 useTableManager 를 수정하였습니다.
//모듈 호출 : 기존코드 동일
const identifierField: string = 'id';
const { onSearch, resetSearch, filteredList, selectedEmpId, onSelectionChange, edit, handleEdit, onNew, onSave, onDelete, onExcelSave } =
    useTableManager<SSCardItem>(users, formFields, userFields, identifierField);
</script>
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="우리카드">
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
                        <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <v-btn flat color="primary" variant="outlined" @click="onNew"
                                ><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록
                            </v-btn>
                            <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0], '/card/delete/woori/'+selectedEmpId[0])"
                                ><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제
                            </v-btn>
                        </div>
                    </v-col>
                    <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                            <ExcelUploadDialogBtn :save="onExcelSave" :url="'/card/saveList/woori'" title="엑셀 업로드" />
                        </div>
                    </v-col>
                </v-row>
                <v-row>
                    <v-data-table
                        items-per-page="5"
                        :headers="headers"
                        :items="filteredList"
                        select-strategy="single"
                        show-select
                        class="border rounded-md"
                        v-model="selectedEmpId"
                        :item-value="identifierField"
                        @update:model-value="onSelectionChange"
                    >
                        <!-- amount 컬럼 커스텀 렌더링 -->
                        <template #item.amount="{ item }">
                            {{ formatMoney(item.amount) }}
                        </template>
                        <template #item.currency="{ item }">
                            {{ formatMoney(item.currency) }}
                        </template>
                        <template #item.benefitAmount="{ item }">
                            {{ formatMoney(item.benefitAmount) }}
                        </template>

                        <!-- date 컬럼 커스텀 렌더링 -->
                        <template #item.transactionDate="{ item }">
                            {{ item.transactionDate.substring(0, 10) }}
                        </template>
                        <template #item.paymentDate="{ item }">
                            {{ item.paymentDate.substring(0, 10) }}
                        </template>
                    </v-data-table>
                </v-row>
            </UiParentCard>
            <br />
            <UiParentCard title="사용자 상세정보" :key="selectedEmpId[0]" v-if="selectedEmpId[0]">
                <template v-slot:action></template>
                <template v-slot:default>
                    <CustomSearchChecksForm :formFields="userFields" :colsPerRow="4" :edit="edit">
                        <template v-slot:topBtn="{ validateForm }">
                            <v-row>
                                <v-col cols="5">
                                    <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                        <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)">편집</v-btn>
                                        <v-btn flat color="error" variant="outlined" @click="handleEdit(false)">취소</v-btn>
                                    </div>
                                </v-col>
                                <v-col cols="7">
                                    <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                        <v-btn flat color="primary" variant="outlined" @click="onSave(() => saveToServer(validateForm, '/card/save/woori'))" >
                                            저장
                                        </v-btn>
                                    </div>
                                </v-col>
                            </v-row>
                        </template>
                    </CustomSearchChecksForm>
                </template>
            </UiParentCard>
            <UiParentCard>
                <v-row>
                    <CustomSearchChecksForm :formFields="statFormFields" :colsPerRow="5" :edit="true" :hide-details="true">
                        <template v-slot:lineBtn="{ validateForm }">
                            <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                <v-btn color="primary" flat @click="onSearchStats(validateForm)">조회</v-btn>
                            </div>
                        </template>
                    </CustomSearchChecksForm>
                </v-row>
                <v-row>
                    <v-data-table hide-default-footer :headers="statHeaders" :items="stats" class="border rounded-md">
                        <template #item="{ item }">
                            <tr :style="item.title === '합계' ? 'font-weight: 700; color: #1D4ED8;' : ''">
                                <td class="v-data-table__td v-data-table-column--align-center">{{ item.title }}</td>
                                <td class="v-data-table__td v-data-table-column--align-center">{{ formatMoney(item.stat1) }}</td>
                                <td class="v-data-table__td v-data-table-column--align-center">{{ formatMoney(item.stat2) }}</td>
                                <td class="v-data-table__td v-data-table-column--align-center">{{ formatMoney(item.stat3) }}</td>
                                <td class="v-data-table__td v-data-table-column--align-center">{{ formatMoney(item.stat4) }}</td>
                            </tr>
                        </template>
                    </v-data-table>
                </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped></style>