<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import CustomComboBox from "@/views/custom/CustomBasicForm.vue";
import { ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';

// 입력 필드 목록 정의
const formFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '땡땡땡', placeholder: '이름 입력', required: true, disabled: true },
  { label: '사원번호', name: 'employeeId', type: 'password', value: '1348684', placeholder: '사원번호 입력', required: true, disabled: true },
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: true, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: true, disabled: false },
  { label: '직위', name: 'position', type: 'text', value: '', placeholder: '직위 입력', required: true, disabled: false },
  { label: '메일주소', name: 'email', type: 'text', value: 'example@domain.com', placeholder: 'example@domain.com', required: true, disabled: true },
  { label: '전화번호', name: 'phone', type: 'text', value: '', placeholder: '02-0000-0000', required: true, disabled: false },
  { label: '휴대전화', name: 'mobile', type: 'text', value: '', placeholder: '010-0000-0000', required: false, disabled: false },
  { label: '입사일자', name: 'hireDate', type: 'date', value: '2018-03-02', placeholder: 'YYYY-MM-DD', required: true, disabled: true },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: true, disabled: false },
  { label: '직책', name: 'jobTitle', type: 'select', value: '', options: ['사원', '대리', '과장', '차장', '부장'], required: true, disabled: false },
  { label: '사용자 권한', name: 'userRole', type: 'select', value: '', options: ['사용자', '관리자', '슈퍼 관리자'], required: true, disabled: false },
  { label: '사용유무', name: 'activeStatus', type: 'select', value: '', options: ['Y', 'N'], required: true, disabled: false }
]);

const onSave = (formData : any)=>{
  console.log('save', formData);
}

</script>

<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="사용자 관리">
                <CustomComboBox :formFields="formFields" :onSave="onSave"/>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped lang="scss">

</style>