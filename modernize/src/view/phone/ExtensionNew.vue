<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import type { ExtensionItem } from "@/types/custom/DataTableTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { useTableManager } from "@/common/useTableManager";

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
                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
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