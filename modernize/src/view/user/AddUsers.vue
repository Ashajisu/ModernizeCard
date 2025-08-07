<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { SHCardItem } from '@/types/custom/DataTableTypes';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import CustomSlotDialog from '@/components/custom/dialog/CustomSlotDialog.vue';
import { useTableManager } from '@/common/useTableManager';
import ExcelUploadDialogBtn from '@/common/excel/ExcelUploadDialogBtn.vue';
import { apiClient } from '@/data/Axios';

//검색
const formFields = ref<FormField[]>([
    { label: 'd', name: 'd', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
    { label: '아이디', name: 'id', type: 'text', value: '', required: false, disabled: false },
    { label: '사원번호', name: 'employeeId', type: 'search', value: '', searchObj: [], required: false, disabled: false },
    { label: '사용자명', name: 'username', type: 'search_list', value: '', searchObj: [], required: false, disabled: false }
]);
//테이블헤더
const headers = ref<any[]>([
    { title: '아이디', align: 'start', key: 'id' },
    { title: '거래일', align: 'start', key: 'transactionDate' },
    { title: '카드구분', align: 'start', key: 'cardType' },
    { title: '이용카드', align: 'start', key: 'usedCard' },
    { title: '가맹점명', align: 'start', key: 'merchantName' },
    { title: '승인번호', align: 'center', key: 'approvalNumber' },
    { title: '금액', align: 'end', key: 'amount' },
    { title: '매입구분', align: 'center', key: 'purchaseType' },
    { title: '이용구분', align: 'center', key: 'usageType' },
    { title: '거래통화', align: 'center', key: 'currency' },
    { title: '결제일', align: 'center', key: 'paymentDate' }
]);
//상세정보
const userFields = ref<FormField[]>([
    { label: '아이디', name: 'id', type: 'hidden', value: '', required: false, disabled: true },
    { label: '거래일', name: 'transactionDate', type: 'date', value: '', required: true, disabled: false },
    { label: '카드구분', name: 'cardType', type: 'text', value: '', placeholder: '체크(신용)', required: true, disabled: false },
    { label: '이용카드', name: 'usedCard', type: 'text', value: '', placeholder: '본인509*', required: true, disabled: false },
    { label: '가맹점명', name: 'merchantName', type: 'text', value: '', placeholder: '가맹점명 입력', required: true, disabled: false },
    { label: '승인번호', name: 'approvalNumber', type: 'text', value: '', placeholder: '승인번호 입력', required: false, disabled: false },
    { label: '금액', name: 'amount', type: 'text', value: '', placeholder: '금액 입력', required: false, disabled: false },
    { label: '매입구분', name: 'purchaseType', type: 'text', value: '', placeholder: '결제확정', required: false, disabled: false },
    { label: '이용구분', name: 'usageType', type: 'text', value: '', placeholder: '사용자명 입력', required: false, disabled: false },
    { label: '거래통화', name: 'currency', type: 'text', value: '', placeholder: '0,000', required: false, disabled: false },
    { label: '결제일', name: 'paymentDate', type: 'date', value: '', placeholder: 'YYYY-MM-DD', required: false, disabled: false }
]);

const setUsers = (userList: SHCardItem[]) => {
    users.value = userList;
    console.log('사용자 데이터:', users.value);
};
// mockApi 로 데이터 불러오기.
const users = ref<SHCardItem[]>([]); // 사용자 데이터를 저장할 변수
onMounted(async () => {
    // 초기화 또는 초기 작업 수행
    try {
        const response = await apiClient.get('/card/list');
        setUsers(response.list);
    } catch (e) {
        console.error('데이터 로드 중 오류 발생:', e);
    }
});
// `users` 값을 동적으로 반영하도록 useTableManager 를 수정하였습니다.
//모듈 호출 : 기존코드 동일
const identifierField: string = 'id';
const { onSearch, resetSearch, filteredList, selectedEmpId, onSelectionChange, edit, handleEdit, onNew, onSave, onDelete, onExcelSave } =
    useTableManager<SHCardItem>(users, formFields, userFields, identifierField);

function formatMoney(value: any) {
    if (value === null || value === undefined || value === '') return '';
    const number = parseInt(value.toString().replace(/[^0-9]/g, ''));
    const result = isNaN(number) ? '' : number.toLocaleString(); // 12,345 형식
    return result;
}

const saveToServer = async (validateForm: any) => {
    const formData = await validateForm();
    try {
        const response = await apiClient.post('/card/save', formData);
        console.log('서버 저장:', response);
        return response;
    } catch (error) {
        console.error('서버 저장 실패:', error);
        throw error;
    }
};
</script>
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="사용자 관리">
                <!--                <v-row>-->
                <!--                    <v-btn @click="setUsers(users.slice(0, 2))">test</v-btn>-->
                <!--                    <p>{{ JSON.stringify(users) }}</p>-->
                <!--                    <p>{{ JSON.stringify(filteredList) }}</p>-->
                <!--                </v-row>-->
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
                            <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0])"
                                ><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제
                            </v-btn>
                        </div>
                    </v-col>
                    <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                            <ExcelUploadDialogBtn :save="onExcelSave" title="엑셀 업로드" />
                        </div>
                    </v-col>
                </v-row>
                <v-row>
                    <v-data-table
                        items-per-page="5"
                        :headers="headers"
                        :items="filteredList"
                        item-value="employeeId"
                        select-strategy="single"
                        show-select
                        class="border rounded-md"
                        v-model="selectedEmpId"
                        @update:model-value="onSelectionChange"
                    >
                        <!-- amount 컬럼 커스텀 렌더링 -->
                        <template #item.amount="{ item }">
                            {{ formatMoney(item.amount) }}
                        </template>
                        <template #item.currency="{ item }">
                            {{ formatMoney(item.currency) }}
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
                                        <!--                                        <CustomSlotDialog ref="passwordDialog" title="패스워드초기화" />-->
                                        <!--                                        <v-btn color="grey" variant="outlined" @click="($refs.passwordDialog as any)?.open()"-->
                                        <!--                                            >패스워드초기화</v-btn-->
                                        <!--                                        >-->
                                        <v-btn flat color="primary" variant="outlined" @click="onSave(() => saveToServer(validateForm))"
                                            >저장</v-btn
                                        >
                                    </div>
                                </v-col>
                            </v-row>
                        </template>
                    </CustomSearchChecksForm>
                </template>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped></style>