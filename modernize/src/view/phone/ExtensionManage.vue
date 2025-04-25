<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import type { ExtensionItem } from "@/types/custom/DataTableTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { useTableManager } from "@/common/useTableManager";
import ExcelUploadDialogBtn from "@/common/excel/ExcelUploadDialogBtn.vue";

//데이터 정보
const ExtensionList:ExtensionItem[] = [
  { number: '308152', provision: '박OO', assignment: 'Phone User' },
  { number: '308201', provision: '기술지원팀', assignment: 'Group Call Pickup' },
  { number: '308305', provision: '아리시스 대표번호', assignment: 'Auto Receptionist' },
  { number: '308403', provision: '고객지원부서 Queue', assignment: 'Call Queue' },
  { number: '308561', provision: '기술지원 1팀', assignment: 'Paging' },
  { number: '308612', provision: '경영지원팀', assignment: 'Shared Line Group' },
];
const assignmentTypes = ['Phone User', 'Group Call Pickup', 'Auto Receptionist', 'Call Queue', 'Paging', 'Shared Line Group'];
//검색
const formFields = ref<FormField[]>([
  { label: 'Extension 번호', name: 'number', type: 'search_list', value: '', searchObj:ExtensionList, required: false, disabled: false },
  { label: '할당', name: 'assignment', type: 'select', value: '', options:assignmentTypes, required: false, disabled: false },
]);
//테이블헤더
const headers = ref<any[]>([
  { title: "Extension 번호", align: "center", key: "number" },
  { title: "표시이름", align: "center", key: "provision" },
  { title: "할당", align: "center", key: "assignment" }
]);


//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
} = useTableManager<ExtensionItem>(ExtensionList, formFields,null,'number');

// 엑셀 다운로드 기능 수정
const handleExcelDownload = async (validateForm: () => Promise<File | null>): Promise<boolean> => {
  console.log("ExcelUploadDialogBtn save prop 호출됨");
  const file = await validateForm(); // ExcelUploadForm의 유효성 검사 및 파일 가져오기
  if (file) {
    // TODO: 실제 엑셀 업로드 로직 구현 (파일 처리)
    console.log("업로드할 파일:", file);
    // 성공 시 true 반환하여 다이얼로그 닫기
    return true;
  }
  // 실패 시 false 반환
  return false;
};
</script>
<template>
  <v-row>
    <v-col cols="12" md="12">
      <UiParentCard title="내선번호">
        <v-row class="mb-6 mx-2">
          <h6 class="text-subtitle-1">💡 줌 폰에서 사용하는 내선번호를 관리 할 수 있습니다. 내선번호에는 사용자 내선번호, 담겨받기, Shared Group Line, Call Queue 등이 있습니다. ( 조회만 가능 )</h6>
        </v-row>
        <v-row>
          <CustomSearchChecksForm :formFields="formFields" :colsPerRow="3" :edit="true" :hide-details="true">
            <template v-slot:lineBtn="{ validateForm }">
                <v-btn color="primary" @click="onSearch(validateForm)">조회</v-btn>
                <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                <ExcelUploadDialogBtn class="ml-2" :save="handleExcelDownload" />
            </template>
          </CustomSearchChecksForm>
        </v-row>
        <v-row>
          <v-data-table items-per-page="10" :headers="headers" :items="filteredList" class="border rounded-md"></v-data-table>
        </v-row>
      </UiParentCard>
    </v-col>
  </v-row>
  </template>
<style scoped>
</style>