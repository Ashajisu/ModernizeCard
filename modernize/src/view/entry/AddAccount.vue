<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';
import type { AccountItem } from '@/types/custom/DataTableTypes';
import { AccountTypeMapping, CategoryTypeMapping } from '@/types/custom/DataTableTypes';
import CustomSearchChecksForm from '@/components/custom/form/CustomSearchChecksForm.vue';
import { useTableManager } from '@/common/useTableManager_renewal';
import ExcelUploadDialogBtn from '@/common/excel/ExcelUploadDialogBtn.vue';
import { apiClient } from '@/data/Axios';
import { saveToServer } from '@/utils/common';
import { getLabelByValue } from '@/utils/common';

//검색
const formFields = ref<FormField[]>([
    { label: '계정명', name: 'accountName', type: 'text', value: '', required: false, disabled: false },
    { label: '구분', name: 'accountType', type: 'text', value: '', required: false, disabled: false },
    { label: '유형', name: 'categoryType', type: 'text', value: '', required: false, disabled: false },
    { label: '소속', name: 'bankName', type: 'text', value: '', required: false, disabled: false }
]);
//테이블헤더
const headers = ref<any[]>([
    { title: 'No.', align: 'start', key: 'id' },
    { title: '계정코드', align: 'start', key: 'accountCode' },
    { title: '계정명', align: 'start', key: 'accountName' },
    { title: '구분', align: 'center', key: 'accountType' },
    { title: '유형', align: 'center', key: 'categoryType' },
    { title: '소속', align: 'start', key: 'bankName' }
]);
//상세정보
const userFields = ref<FormField[]>([
    { label: 'No.', name: 'id', type: 'hidden', value: '', required: true, disabled: true },
    { label: '계정코드', name: 'accountCode', type: 'text', value: '', required: true, disabled: false },
    { label: '계정명', name: 'accountName', type: 'text', value: '', required: true, disabled: false },
    { label: '구분', name: 'accountType', type: 'select', value: '', returnObject:false, required: true, disabled: false,
        itemTitle:'label', itemValue:'value', options: AccountTypeMapping },
    { label: '유형', name: 'categoryType', type: 'text', value: '', required: true, disabled: false },
    { label: '소속', name: 'bankName', type: 'text', value: '', required: false, disabled: false },
    { label: '활성화', name: 'active', type: 'switch', value: '', options:[true,false], required: false, disabled: false }
]);

//데이터 조회
const accounts = ref<AccountItem[]>([]);
onMounted(async () => {
    await apiClient.get('/entry/accounts').then((res) => (accounts.value = res));
});
const identifierField: string = 'id';
const { onSearch, resetSearch, filteredList, selectedEmpId, onSelectionChange, edit, handleEdit, onNew, onSave, onDelete, onExcelSave } =
    useTableManager<AccountItem>(accounts, formFields, userFields, identifierField);
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
                            <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0], '/entry/account/delete')"
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
                        <template #[`item.accountType`]="{ item }">
                            <span>{{ getLabelByValue(item.accountType, AccountTypeMapping) }}</span>
                        </template>
                        <template #[`item.categoryType`]="{ item }">
                            <span>{{ getLabelByValue(item.categoryType, CategoryTypeMapping) }}</span>
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