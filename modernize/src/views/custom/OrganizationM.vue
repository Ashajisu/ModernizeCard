<script setup lang="ts">
import {computed, ref, watch} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import UiChildCard from "@/components/shared/UiChildCard.vue";
import AppBaseCard from "@/components/shared/AppBaseCard.vue";
import CustomPlainForm from "@/components/custom/form/CustomPlainForm.vue";
import CustomTreeview from "@/components/custom/tree/CustomTreeview.vue";

// 입력 필드 목록 정의
const groupFields = ref<FormField[]>([
  { label: '조직명', name: 'dept1', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '상위조직명', name: 'dept2', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '조직코드', name: 'deptcode1', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '상위조직코드', name: 'deptcode2', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '조직레벨', name: 'deptlevel', type: 'text', value: '', placeholder: '', required: true, disabled: false },
  { label: '정렬순서', name: 'asc', type: 'number', value: '', placeholder: '', required: true, disabled: false },
  { label: '사용여부', name: 'activeStatus', type: 'check', value: false, required: true, disabled: false },
]);
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '땡땡땡', placeholder: '이름 입력', required: true, disabled: true },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '1348684', placeholder: '사원번호 입력', required: true, disabled: true },
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

const personFields = ref<FormField[]>([
  { label: '조직명', name: 'dept1', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '상위조직명', name: 'dept2', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '조직코드', name: 'deptcode1', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '상위조직코드', name: 'deptcode2', type: 'text', value: '', placeholder: '', required: true, disabled: true },
  { label: '조직레벨', name: 'deptlevel', type: 'text', value: '', placeholder: '', required: true, disabled: false },
  { label: '정렬순서', name: 'asc', type: 'number', value: '', placeholder: '', required: true, disabled: false }
]);

const formData = computed(()=> {
  const data: Record<string, any> = {};
  groupFields.value.forEach(field => {
    data[field.name] = field.value;
  });
  return data;
});

const onSave = ()=>{
  // 읽기모드로 전환
  handleEdit(false);
  console.log('Form Data:', formData); // formData.value 값 확인
  console.log('save OrganizationM', formData);
}

const selectedId = ref({ id: 0, type: 'person'});

//조직 상세정보 편집
const edit = ref<boolean>(false);
const handleEdit = (bool : boolean) => {
  // 수정 / 읽기 화면으로 변경
  edit.value = bool;
  console.log('handleEdit', edit.value);
};

// selectedId 값이 변경될 때, 데이터 불러와 Fields 를 업데이트 : 예시
watch(selectedId, (newSelectedId) => {
  if (newSelectedId.type === 'person') {
    userFields.value.forEach(field => {
      if (field.name === 'employeeId') field.value = String(newSelectedId.id);
    });
  } else {
    groupFields.value.forEach(field => {
      if (field.name === 'deptcode1') field.value = String(newSelectedId.id);
    });
  }
})

</script>

<template>
  <v-card elevation="10">
    <AppBaseCard>
      <template v-slot:leftpart>
        <CustomTreeview v-model:selectedId="selectedId"/>
      </template>
      <template v-slot:rightpart>
<!--        조직 상세-->
        <UiChildCard title="조직 상세 정보" v-if="selectedId.type === 'group'">
          <CustomPlainForm :formFields="groupFields" :colsPerRow="2" :edit="false"/>
          <v-row>
            <v-col cols="12" sm="9" offset-sm="10" v-if="!edit">
              <v-btn color="primary" flat @click="handleEdit(true)">수정</v-btn>
            </v-col>
            <v-col cols="12" sm="9" offset-sm="10" v-else>
              <v-btn color="primary" flat @click="onSave()">저장</v-btn>
              <v-btn color="primary" flat @click="handleEdit(false)">취소</v-btn>
            </v-col>
          </v-row>
        </UiChildCard>
<!--        사용자 상세-->
        <div v-else>
          <UiChildCard title="사용자 조직 정보">
            <CustomPlainForm :formFields="personFields" :colsPerRow="2" :edit="true"/>
          </UiChildCard>
          <UiChildCard title="사용자 상세 정보">
            <CustomPlainForm :formFields="userFields" :colsPerRow="2" :edit="false"/>
          </UiChildCard>
        </div>
      </template>
      <template v-slot:mobileLeftContent>
        <v-sheet>
          <CustomTreeview/>
        </v-sheet>
      </template>
    </AppBaseCard>
  </v-card>
</template>

<style scoped lang="scss">

</style>