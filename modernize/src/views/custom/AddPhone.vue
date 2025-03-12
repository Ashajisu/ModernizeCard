<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, ref} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import {BasicDatatables} from "@/_mockApis/components/datatable/dataTable";
import CustomPlainForm from "@/components/custom/CustomPlainForm.vue";
import type {Datatables} from "@/types/components/datatables";
import {searchSugg} from "@/_mockApis/headerData";
import CustomSearchForm from "@/components/custom/CustomSearchForm.vue";

const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '사용자명', name: 'username', type: 'search', value: '', placeholder: '이름 입력', required: false, disabled: false },
  { label: '내선번호', name: 'phone', type: 'text', value: '', required: false, disabled: false },
  { label: 'PSTN 번호', name: 'pstnN', type: 'search', value: '', placeholder: '사원번호 입력', required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'search', value: '', placeholder: '사원번호 입력', required: false, disabled: false },
]);
const headers = ref<any[]>([
  { title: '부서명', align: 'start', key: 'post' },
  { title: '팀명', align: 'start', key: 'project' },
  { title: '사용자명', align: 'start', key: 'name' },
  { title: '사원번호', align: 'start', key: 'status' },
  { title: '내선번호', align: 'center', key: 'budget' },
  { title: 'PSTN 번호', align: 'center', key: 'budget' },
  { title: '...', align: 'center', key: 'budget' },
  { title: '통화녹음', align: 'end', key: 'budget' },
])
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '', placeholder: '이름 입력', required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '', placeholder: '사원번호 입력', required: false, disabled: false },
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '직위', name: 'position', type: 'select', value: '', options: ['사원', '대리', '과장', '차장', '부장'], required: false, disabled: false },
  { label: '내선번호', name: 'phone', type: 'text', value: '', placeholder: '02-0000-0000', required: true, disabled: false },
  { label: 'PSTN 번호', name: 'pstnN', type: 'text', value: '', placeholder: '', required: false, disabled: false },
  { label: '당겨받기', name: 'pullNumber', type: 'select', value: '기술2팀', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: 'IP 전화기', name: 'hireDate', type: 'search', value: 'Avaya J159_308152', searchObj:searchSugg , required: false, disabled: false },
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: 'WorkplaceBiz', options: ['WorkplaceBiz', '...'], required: true, disabled: false },
  { label: '통화녹음', name: 'rec', type: 'select', value: '미사용', options: ['미사용', '선택녹취','전수녹취'], required: false, disabled: false }
]);
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
  let index = 0;
  const selectedItem: Datatables[] | undefined = BasicDatatables.filter((user) => {
    return !selectedName || user.name.includes(selectedName);
  });

  console.log("선택된 정보:", selectedItem);
  if (!selectedItem) {
    return;
  }
  //동명이인이 있는 경우 팝업띄우기
  if(selectedItem.length > 1) {
    if(confirm(`${selectedName} 사용자가 ${selectedItem.length}명 검색되었습니다. 사용자를 선택해 주세요.\n ${selectedItem[0].name} or ${selectedItem[1].name}`)){
      index = 1;
    }
  }

  userFields.value.forEach(field => {
    switch (field.label) {
      case '사용자명':
        field.value = selectedItem[index].name;
        break;
      case '부서명':
        field.value = selectedItem[index].post;
        break;
      case '팀명':
        field.value = selectedItem[index].project;
        break;
      case '사원번호':
        field.value = selectedItem[index].status;
        break;
      case '직위':
        field.value = selectedItem[index].budget;
        break;
      case '메일주소':
        field.value = selectedItem[index].budget; // budget 키가 중복 사용됨
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

const viewDialog = ref<boolean>(false);
//팝업
const handleDialog = () => {
  viewDialog.value = true;
}

</script>
<!-- 각 버튼의 기능 구현 전 -->
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="줌 폰 사용자 관리">
              <v-row>
                <CustomPlainForm :formFields="formFields" :colsPerRow="5" :edit="true">
                  <v-btn color="primary" flat @click="onSearch">조회</v-btn>
                </CustomPlainForm>
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
                    <v-btn color="grey" variant="outlined" @click="handleDialog">사용자 전화 기능키 관리</v-btn>
                    <v-btn flat color="primary" variant="outlined" @click="onSave">저장</v-btn>
                  </div>
                </v-col>
              </v-row>
              <v-row>
                <CustomSearchForm :formFields="userFields" :colsPerRow="4" :edit="edit"></CustomSearchForm>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>