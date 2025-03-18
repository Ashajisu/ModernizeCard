<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, type ComputedRef, ref} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import {ZoomPhoneDataTables} from "@/_mockApis/custom/RecordingData";
import type {ZoomPhoneItem} from "@/types/custom/DataTableTypes";
import {searchSugg} from "@/_mockApis/headerData";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";

const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '사용자명', name: 'username', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'text', value: '', required: false, disabled: false },
  { label: 'PSTN 번호', name: 'pstnNumber', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '', name: '_', type: 'none', value: '', required: false, disabled: false },
]);
const headers = ref<any[]>([
  { title: '부서명', align: 'start', key: 'department' },
  { title: '팀명', align: 'start', key: 'team' },
  { title: '사용자명', align: 'start', key: 'username' },
  { title: '사원번호', align: 'start', key: 'employeeId' },
  { title: '내선번호', align: 'center', key: 'mobile' },
  { title: 'PSTN 번호', align: 'center', key: 'pstnNumber' },
  { title: '...', align: 'center', key: 'pstnNumber' },
  { title: '통화녹음', align: 'end', key: 'callRecording' },
])
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '', placeholder: '이름 입력', required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '', placeholder: '사원번호 입력', required: false, disabled: false },
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '직위', name: 'position', type: 'select', value: '', options: ['사원', '대리', '과장', '차장', '부장'], required: false, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'search', value: '', searchObj:searchSugg, view:false, required: true, disabled: false },
  { label: 'PSTN 번호', name: 'pstnNumber', type: 'search', value: '', searchObj:searchSugg,view:false, required: false, disabled: false },
  { label: '당겨받기', name: 'pullNumber', type: 'search', value: '', searchObj:searchSugg,view:false, required: false, disabled: false },
  { label: 'IP 전화기', name: 'ipPhone', type: 'search', value: 'Avaya J159_308152', searchObj:searchSugg,view:false, required: false, disabled: false },
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: 'WorkplaceBiz', options: ['WorkplaceBiz', '...', 'Zoom Phone Basic '], required: true, disabled: false },
  { label: '통화녹음', name: 'callRecording', type: 'select', value: '미사용', options: ['미사용', '선택녹취','전수녹취'], required: false, disabled: false }
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
  if (!search.value) return ZoomPhoneDataTables;

  let isAllEmpty = true;
  for (const key in search.value) {
    if (search.value[key] !== "") {
      isAllEmpty = false;
      break;
    }
  }
  if (isAllEmpty) return ZoomPhoneDataTables;

  return ZoomPhoneDataTables.filter((user: any) => {
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
// const selectedData = ref<Datatables>();
const onSelectionChange = () => {
  console.log("onSelectionChange", selectedName);
  updateUserFields(selectedName.value as string);
};

//사용자 상세정보 출력
const updateUserFields = (selectedName: string) => {
  let index = 0;
  const selectedItem: ZoomPhoneItem[] | undefined = ZoomPhoneDataTables.filter((user) => {
    return !selectedName || user.username.includes(selectedName);
  });

  console.log("선택된 정보:", selectedItem);
  if (!selectedItem) {
    return;
  }
  //동명이인이 있는 경우 팝업띄우기
  if(selectedItem.length > 1) {
    if(confirm(`${selectedName} 사용자가 ${selectedItem.length}명 검색되었습니다. 사용자를 선택해 주세요.\n ${selectedItem[0].username} or ${selectedItem[1].username}`)){
      index = 1;
    }
  }

  // selectedData.value = selectedItem[index];

  userFields.value.forEach(field => {
    const key = field.name; // field.name이 selectedItem의 key 값과 일치
    if (selectedItem[index][key] !== undefined) {
      field.value = selectedItem[index][key]; // 값 매칭
    } else {
      field.value = ''; // 매칭되지 않으면 빈 값 설정
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

//기능키 관리 : Zoom Phone Basic 사용자는 사용할 수 없음
const isProLicense: ComputedRef<boolean> = computed(()=>{
  const licenseVar = 'zoomLicense';
  const license: FormField | undefined = userFields.value.find((user: any) => {
    return !licenseVar || user.name.includes(licenseVar);
  });
  const licenseName:string = license?.value as string;
  return licenseName.toLowerCase().includes('basic');
});

</script>
<!-- 각 버튼의 기능 구현 전 -->
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="줌 폰 사용자 관리">
              <v-row>
                <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true">
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
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="username"
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
                    <v-btn color="grey" variant="outlined" @click="handleDialog" :disabled="isProLicense">사용자 전화 기능키 관리</v-btn>
                    <CustomSlotDialog title="d" v-model:view="viewDialog">
                        <template v-slot:inCard>
                          <v-data-table items-per-page="5" :headers="headers" :items="formFields" item-value="username">

                          </v-data-table>

                        </template>
                    </CustomSlotDialog>
                    <v-btn flat color="primary" variant="outlined" @click="onSave">저장</v-btn>
                  </div>
                </v-col>
              </v-row>
              <v-row>
                <CustomSearchChecksForm :formFields="userFields" :colsPerRow="4" :edit="edit"></CustomSearchChecksForm>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>