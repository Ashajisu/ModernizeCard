<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { UsageItem, StatsItem } from '@/types/custom/DataTableTypes';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { useTableManager } from '@/common/useTableManager';
import ExcelUploadDialogBtn from '@/common/excel/ExcelUploadDialogBtn.vue';
import { apiClient } from '@/data/Axios';
import { formatMoney, saveToServer } from '@/utils/common';
import { format } from 'date-fns';

//검색
const formFields = ref<FormField[]>([
    { label: '거래일', name: 'transactionDate', type: 'due', value: { startDate: '', endDate: '' }, required: false, disabled: false },
    { label: '이용카드', name: 'cardCompany', type: 'select', value:'',options:['KOOKMIN','SAMSUNG','SHINHAN'], required: false, disabled: false },
    { label: '가맹점명', name: 'merchantName', type: 'text', value: '', required: false, disabled: false },
    { label: '매입구분', name: 'purchaseType', type: 'text', value: '', required: false, disabled: false },
    { label: '이용구분', name: 'usageType', type: 'text', value: '', required: false, disabled: false }
]);
//테이블헤더
const headers = ref<any[]>([
    { title: 'No.', align: 'start', key: 'id' },
    { title: '거래일', align: 'start', key: 'transactionDate' },
    { title: '이용카드', align: 'start', key: 'cardCompany' },
    { title: '가맹점명', align: 'start', key: 'merchantName' },
    { title: '매입구분', align: 'center', key: 'purchaseType' },
    { title: '이용구분', align: 'center', key: 'usageType' },
    { title: '거래통화', align: 'center', key: 'currency' },
]);

//집계 검색
const startDate = format(new Date().setDate(1), 'yyyy-MM-dd');
const endDate = format(new Date().setDate(31), 'yyyy-MM-dd');
const statFormFields = ref<FormField[]>([
    { label: '거래시작일', name: 'startDate', type: 'date', value: startDate, required: false, disabled: false },
    { label: '거래종료일', name: 'endDate', type: 'date', value: endDate, required: false, disabled: false }
]);
//집계 테이블헤더
const statHeaders = ref<any[]>([
    { title: '이용구분', align: 'center', key: 'title' },
    { title: '금액', align: 'center', key: 'stat1' },
    { title: '삼성', align: 'center', key: 'stat2' },
    { title: '국민', align: 'center', key: 'stat3' },
    { title: '신한', align: 'center', key: 'stat4' },
]);

const setUsers = (userList: UsageItem[]) => {
    users.value = userList;
    // console.log('사용자 데이터:', users.value);
};
const setStats = (statList: StatsItem[]) => {
    stats.value = statList;
    // console.log('집계 데이터:', stats.value);
};
// mockApi 로 데이터 불러오기.
const users = ref<UsageItem[]>([]); // 사용자 데이터를 저장할 변수
const stats = ref<StatsItem[]>([]); // 집계 데이터를 저장할 변수
onMounted(async () => {
    // 초기화 또는 초기 작업 수행
    try {
        const response = await apiClient.get('/card/list/usage');
        setUsers(response.list);
        if (response.list) {
            const response = await apiClient.post('/card/usageTypeStats/usage', { startDate, endDate });
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
    const response = await apiClient.post('/card/usageTypeStats/usage', formData);
    setStats(response.list);
};
// `users` 값을 동적으로 반영하도록 useTableManager 를 수정하였습니다.
//모듈 호출 : 기존코드 동일
const identifierField: string = 'id';
const { onSearch, resetSearch, filteredList, selectedEmpId, onSelectionChange, onNew, onDelete, onExcelSave } =
    useTableManager<UsageItem>(users, formFields, null, identifierField);
</script>
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="삼성카드">
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
<!--                    <v-col>-->
<!--                        <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">-->
<!--                            <v-btn flat color="primary" variant="outlined" @click="onNew"-->
<!--                                ><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록-->
<!--                            </v-btn>-->
<!--                            <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0], '/card/delete/samsung')"-->
<!--                                ><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제-->
<!--                            </v-btn>-->
<!--                        </div>-->
<!--                    </v-col>-->
                    <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
<!--                            <ExcelUploadDialogBtn :save="onExcelSave" :url="'/card/saveList/samsung'" title="엑셀 업로드" />-->
                        </div>
                    </v-col>
                </v-row>
                <v-row>
                    <v-data-table
                        items-per-page="5"
                        :headers="headers"
                        :items="filteredList"
                        select-strategy="single"
                        class="border rounded-md"
                        v-model="selectedEmpId"
                        :item-value="identifierField"
                        @update:model-value="onSelectionChange"
                    >
                        <!-- amount 컬럼 커스텀 렌더링 -->
                        <template #item.currency="{ item }">
                            {{ formatMoney(item.currency) }}
                        </template>

                        <!-- date 컬럼 커스텀 렌더링 -->
                        <template #item.transactionDate="{ item }">
                            {{ item.transactionDate.substring(0, 10) }}
                        </template>
                    </v-data-table>
                </v-row>
            </UiParentCard>
            <br />
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
                    <v-data-table hide-default-footer :headers="statHeaders" :items="stats" class="border rounded-md" items-per-page="20">
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