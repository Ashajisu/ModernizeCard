<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { VoucherItem } from '@/types/custom/DataTableTypes';
import { AccountTypeMapping, CategoryTypeMapping, VoucherTypeMapping } from '@/types/custom/DataTableTypes';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { useTableManager } from '@/common/useTableManager_renewal';
import ExcelUploadDialogBtn from '@/common/excel/ExcelUploadDialogBtn.vue';
import { apiClient } from '@/data/Axios';
import { saveToServer } from '@/utils/common';
import { getLabelByValue } from '@/utils/common';

//검색
const formFields = ref<FormField[]>([
    { label: '거래시작', name: 'voucherDate_start', type: 'date', value: '', required: false, disabled: false },
    { label: '거래종료', name: 'voucherDate_end', type: 'date', value: '', required: false, disabled: false },
    { label: '차변', name: 'debitAccount', type: 'text', value: '', required: false, disabled: false },
    { label: '대변', name: 'creditAccount', type: 'text', value: '', required: false, disabled: false },
    { label: '구분', name: 'voucherType', type: 'text', value: '', required: false, disabled: false },
    { label: '내용', name: 'description', type: 'text', value: '', required: false, disabled: false },
]);
//테이블헤더
const headers = ref<any[]>([
    { title: 'No.', align: 'start', key: 'id' },
    { title: '거래날짜', align: 'start', key: 'voucherDate' },
    { title: '거래구분', align: 'center', key: 'voucherType' },
    { title: '내용', align: 'start', key: 'description' },
    { title: '차변계정', align: 'center', key: 'debitAccount' },
    { title: '대변계정', align: 'center', key: 'creditAccount' },
    { title: '금액', align: 'end', key: 'amount' },
]);
//상세정보
const userFields = ref<FormField[]>([
    { label: 'No.', name: 'id', type: 'hidden', value: '', required: false, disabled: false },
    { label: '거래날짜', name: 'voucherDate', type: 'date', value: '', required: false, disabled: false },
    { label: '금액', name: 'amount', type: 'text', value: '', required: false, disabled: false },
    { label: '차변', name: 'debitAccount', type: 'select', value: '', required: false, disabled: false, returnObject: false,
        itemTitle:'label', itemValue:'value', options: VoucherTypeMapping},
    { label: '대변', name: 'creditAccount', type: 'select', value: '', required: false, disabled: false, returnObject: false,
        itemTitle:'label', itemValue:'value', options: VoucherTypeMapping},
    { label: '구분', name: 'voucherType', type: 'select', value: '', required: false, disabled: false, returnObject: false,
        itemTitle:'label', itemValue:'value', options: VoucherTypeMapping},
    { label: '내용', name: 'description', type: 'text', value: '', required: false, disabled: false }
]);

//데이터 조회
const vouchers = ref<VoucherItem[]>([]);
onMounted(async () => {
    await apiClient.get('/entry/vouchers').then((res) => (vouchers.value = res));
});
const identifierField: string = 'id';
const { onSearch, resetSearch, filteredList, selectedEmpId, onSelectionChange, edit, handleEdit, onNew, onSave, onDelete, onExcelSave } =
    useTableManager<VoucherItem>(vouchers, formFields, userFields, identifierField);
</script>

<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="계정관리">
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
                            <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0], '/entry/account/delete/'+selectedEmpId[0])"
                                ><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제
                            </v-btn>
                        </div>
                    </v-col>
                    <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                            <ExcelUploadDialogBtn :save="onExcelSave" :url="'/entry/accounts/save'" title="엑셀 업로드" />
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
                        <template #[`item.voucherType`]="{ item }">
                            <span>{{ getLabelByValue(item.voucherType, VoucherTypeMapping) }}</span>
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
                                        <v-btn
                                            flat
                                            color="primary"
                                            variant="outlined"
                                            @click="onSave(() => saveToServer(validateForm, '/entry/account/save'))"
                                        >
                                            저장
                                        </v-btn>
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

<style scoped lang="scss"></style>