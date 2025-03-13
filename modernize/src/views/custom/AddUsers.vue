<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, ref} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import {BasicDatatables} from "@/_mockApis/components/datatable/dataTable";
import type {Datatables} from "@/types/components/datatables";
import {searchSugg} from "@/_mockApis/headerData";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";

const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: false, disabled: false },
  { label: '사원번호', name: 'employeeId',  type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '사용자명', name: 'username',  type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false }
]);
const headers = ref<any[]>([
  { title: '부서명', align: 'start', key: 'post' },
  { title: '팀명', align: 'start', key: 'project' },
  { title: '사용자명', align: 'start', key: 'name' },
  { title: '사원번호', align: 'start', key: 'status' },
  { title: '직위', align: 'center', key: 'budget' },
  { title: '메일주소', align: 'center', key: 'budget' },
  { title: '...', align: 'center', key: 'budget' },
  { title: '줌폰 라이센스', align: 'end', key: 'budget' },
])
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '', placeholder: '이름 입력', required: true, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '', placeholder: '사원번호 입력', required: true, disabled: false },
  { label: '부서명', name: 'department',  type: 'search', value: '', searchObj:searchSugg, view:false, required: true, disabled: false },
  { label: '팀명', name: 'team',  type: 'search', value: '', searchObj:searchSugg, view:false, required: true, disabled: false },
  { label: '직위', name: 'position', type: 'text', value: '', placeholder: '직위 입력', required: true, disabled: false },
  { label: '메일주소', name: 'email', type: 'text', value: 'example@domain.com', placeholder: 'example@domain.com', required: true, disabled: false },
  { label: '전화번호', name: 'phone', type: 'text', value: '', placeholder: '02-0000-0000', required: true, disabled: false },
  { label: '휴대전화', name: 'mobile', type: 'text', value: '', placeholder: '010-0000-0000', required: false, disabled: false },
  { label: '입사일자', name: 'hireDate', type: 'date', value: '2018-03-02', placeholder: 'YYYY-MM-DD', required: true, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: true, disabled: false },
  { label: '직책', name: 'jobTitle', type: 'select', value: '', options: ['사원', '대리', '과장', '차장', '부장'], required: true, disabled: false },
  { label: '사용자 권한', name: 'userRole', type: 'select', value: '', options: ['사용자', '관리자', '슈퍼 관리자'], required: true, disabled: false },
  { label: '사용유무', name: 'activeStatus', type: 'select', value: '', options: ['Y', 'N'], required: true, disabled: false }
]);
const zoomFields = ref<FormField[]>([
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: 'WorkplaceBiz', options: ['WorkplaceBiz', '...'], required: true, disabled: false },
  { label: '줌폰 라이센스', name: 'phoneLicense', type: 'select', value: 'Phone Pro', options: ['Phone Pro', 'Vip'], required: false, disabled: false },
]);

// 초기화
const resetSearch = ()=>{
  formFields.value.forEach(field => {
    field.value='';
  });
  onSearch();
};

//검색기능
const search = ref();
const searchData = computed(()=> {
  const data: Record<string, any> = {};
  formFields.value.forEach(field => {
    data[field.name] = field.value;
  });
  return data;
});
const onSearch = ()=>{
  console.log('save', searchData.value);
  search.value = searchData.value;
}
const filteredList = computed(() => {
  if (!search.value) return BasicDatatables;

  let isAllEmpty = true;
  for (const key in search.value) {
    if (search.value[key] !== "") {
      isAllEmpty = false;
      break;
    }
  }
  if (isAllEmpty) return BasicDatatables;

  return BasicDatatables.filter((user: any) => {
    return (
        (!search.value.department || user.post.toLowerCase() === search.value.department.toLowerCase()) &&
        (!search.value.team || user.project.toLowerCase() === search.value.team.toLowerCase()) &&
        (!search.value.employmentStatus || user.status.toLowerCase() === search.value.employmentStatus.toLowerCase()) &&
        (!search.value.employeeId || user.status.toLowerCase().includes(search.value.employeeId.toLowerCase())) &&
        (!search.value.username || user.name.toLowerCase().includes(search.value.username.toLowerCase()))
    );
  });
});

// 체크박스
const selectedName = ref<string>();
const onSelectionChange = () => {
  console.log("onSelectionChange", selectedName);
  updateUserFields(selectedName.value as string);
};

//사용자 상세정보 출력
const updateUserFields = (selectedName: string) => {
  const selectedItem: Datatables | undefined = BasicDatatables.find((user) => {
    return !selectedName || user.name.includes(selectedName);
  });

  console.log("선택된 정보:", selectedItem);
  if (!selectedItem) {
    return;
  }

  userFields.value.forEach(field => {
    switch (field.label) {
      case '사용자명':
        field.value = selectedItem.name;
        break;
      case '부서명':
        field.value = selectedItem.post;
        break;
      case '팀명':
        field.value = selectedItem.project;
        break;
      case '사원번호':
        field.value = selectedItem.status;
        break;
      case '직위':
        field.value = selectedItem.budget;
        break;
      case '메일주소':
        field.value = selectedItem.budget; // budget 키가 중복 사용됨
        break;
      default:
        field.value = ''; // 매칭되지 않는 경우 빈 값 설정
    }
  });
};

//사용자 상세정보 편집
const edit = ref<boolean>(false);
const handleEdit = (bool : boolean) => {
  // 수정 / 읽기 화면으로 변경
  edit.value = bool;
  console.log('handleEdit', edit.value);
};

const formData = computed(()=> {
  const data: Record<string, any> = {};
  userFields.value.forEach(field => {
    data[field.name] = field.value;
  });
  return data;
});

//사용자 정보 저장
const onSave = () => {
  console.log(formData.value)
  alert(`저장이 완료되었습니다.`);
}

//사용자 정보 삭제
const onDelete = () => {
  console.log(formData.value)
  if(confirm('줌 폰 사용자도 함께 삭제 됩니다.\n\n선택한 사용자를 삭제 하시겠습니까?')){
    alert('삭제 되었습니다.');
  }
}
</script>
<!-- 각 버튼의 기능 구현 전 -->
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="사용자 관리">
              <v-row>
                <CustomSearchChecksForm :formFields="formFields" :colsPerRow="5" :edit="true">
                  <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn color="primary" flat @click="onSearch">조회</v-btn>
                    <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                  </div>
                </CustomSearchChecksForm>
              </v-row>
              <v-row>
                <v-col>
                  <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn flat color="primary" variant="outlined" ><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
                    <v-btn flat color="error" variant="outlined" @click="onDelete"><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제 </v-btn>
                  </div>
                </v-col>
                <v-col>
                  <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                    <v-btn color="grey" variant="outlined" @click="">엑셀 업로드</v-btn>
                  </div>
                </v-col>
              </v-row>
              <v-row>
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="name"
                              select-strategy="single" show-select class="border rounded-md"
                              v-model="selectedName"
                              @update:model-value="onSelectionChange"
                              >
                </v-data-table>
              </v-row>
            </UiParentCard>
            <UiParentCard title="사용자 상세정보" :key="selectedName" v-if="selectedName?.length">
              <v-row>
                <v-col>
                  <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)" >편집</v-btn>
                    <v-btn flat color="error" variant="outlined" @click="handleEdit(false)">취소</v-btn>
                  </div>
                </v-col>
                <v-col>
                  <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn color="grey" variant="outlined" @click="">패스워드초기화</v-btn>
                    <v-btn flat color="primary" variant="outlined" @click="onSave">저장</v-btn>
                  </div>
                </v-col>
              </v-row>
              <v-row>
                <CustomSearchChecksForm :formFields="userFields" :colsPerRow="4" :edit="edit"></CustomSearchChecksForm>
              </v-row>
              <v-row>
                <CustomSearchChecksForm :formFields="zoomFields" :colsPerRow="2" :edit="edit"></CustomSearchChecksForm>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>