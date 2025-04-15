<script setup lang="ts">
import { ref, watch } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import UiChildCard from "@/components/shared/UiChildCard.vue";
import CustomAppBaseCard from "@/components/custom/shared/CustomAppBaseCard.vue";
import CustomTreeview from "@/components/custom/tree/CustomTreeview.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { isDirty, selectedValue, updateGroupDetailFields, FlatTreeData, changedCodeList, onSave } from '@/components/custom/tree/treeService';
// 입력 필드 목록 정의
const groupFields = ref<FormField[]>([
  { label: '조직명', name: 'title', type: 'text', value: '', placeholder: '', required: false, disabled: true },
  { label: '상위조직명', name: 'parentTitle', type: 'text', value: '', placeholder: '', required: false, disabled: true },
  { label: '조직코드', name: 'code', type: 'text', value: '', placeholder: '', required: false, disabled: true },
  { label: '상위조직코드', name: 'parentCode', type: 'text', value: '', placeholder: '', required: false, disabled: true },
  { label: '조직레벨', name: 'level', type: 'text', value: '', placeholder: '', required: true, disabled: false },
  { label: '정렬순서', name: 'index', type: 'text', value: '', placeholder: '', required: true, disabled: false },
  { label: '사용여부', name: 'activeStatus', type: 'switch', value: false, required: false, disabled: false },
]);
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '', placeholder: '이름 입력', required: true, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '', placeholder: '사원번호 입력', required: true, disabled: false },
  { label: '부서명', name: 'department',  type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: true, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: true, disabled: false },
  { label: '직위', name: 'position', type: 'text', value: '', placeholder: '직위 입력', required: true, disabled: false },
  { label: '메일주소', name: 'email', type: 'text', value: '', placeholder: 'example@domain.com', required: true, disabled: false },
  { label: '전화번호', name: 'phone', type: 'text', value: '', placeholder: '02-0000-0000', required: true, disabled: false },
  { label: '휴대전화', name: 'mobile', type: 'text', value: '', placeholder: '010-0000-0000', required: false, disabled: false },
  { label: '입사일자', name: 'hireDate', type: 'date', value: '', placeholder: 'YYYY-MM-DD', required: true, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: true, disabled: false },
  { label: '사용자 권한', name: 'userRole', type: 'select', value: '', options: ['사용자', '관리자', '슈퍼 관리자'], required: true, disabled: false },
  { label: '사용유무', name: 'activeStatus', type: 'select', value: '', options: ['Y', 'N'], required: true, disabled: false },
]);

// selectedId 값이 변경될 때, 데이터 불러와 Fields 를 업데이트
const identifierField:string = 'code';
watch(selectedValue, (newSelect:any) => {
  // console.log('watch:',newSelect);
  updateGroupDetailFields(newSelect.code, identifierField, FlatTreeData, groupFields.value, userFields.value);
},{deep:true});
</script>

<template>
  <div>
    <CustomAppBaseCard>
      <template v-slot:leftpart >
          <CustomTreeview/>
      </template>
      <template v-slot:rightpart>
<!--        조직 상세-->
        <div>
          <UiChildCard title="조직 상세 정보" variant="flat">
            <div class="d-flex justify-end">
                <span v-if="isDirty" class="text-error ma-2" > {{ changedCodeList }}변경사항을 저장하세요. </span>
                <v-btn color="primary" flat @click="onSave" :disabled="!isDirty">저장</v-btn>
            </div>
            <CustomSearchChecksForm :formFields="groupFields" :colsPerRow="2" :edit="false" :hide-details="true"/>
          </UiChildCard>
        </div>
<!--        사용자 상세-->
        <div v-if="selectedValue?.user !==null">
          <UiChildCard title="사용자 상세 정보" variant="flat" class="border-t-sm">
            <CustomSearchChecksForm :formFields="userFields" :colsPerRow="2" :edit="false" :hide-details="true"/>
          </UiChildCard>
        </div>
      </template>
    </CustomAppBaseCard>
  </div>
</template>

<style scoped lang="scss">

</style>