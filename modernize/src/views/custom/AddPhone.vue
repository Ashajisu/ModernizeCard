<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, type ComputedRef, ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import { ZoomPhoneDataTables } from "@/_mockApis/custom/RecordingData";
import type {ZoomPhoneItem} from "@/types/custom/DataTableTypes";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { watchDepartmentChange } from "@/data/teamOptions";

const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부', '연구개발'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '사용자명', name: 'username', type: 'search', value: '', searchObj:ZoomPhoneDataTables, view:false, required: false, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'text', value: '', required: false, disabled: false },
  { label: 'PSTN 번호', name: 'pstnNumber', type: 'search', value: '', searchObj:ZoomPhoneDataTables, view:false, required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'search', value: '', searchObj:ZoomPhoneDataTables, view:false, required: false, disabled: false },
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
  { label: '내선번호', name: 'mobile', type: 'search', value: '', searchObj:ZoomPhoneDataTables, view:false, required: true, disabled: false },
  { label: 'PSTN 번호', name: 'pstnNumber', type: 'search', value: '', searchObj:ZoomPhoneDataTables,view:false, required: false, disabled: false },
  { label: '당겨받기', name: 'pullNumber', type: 'search', value: '', searchObj:ZoomPhoneDataTables,view:false, required: false, disabled: false },
  { label: 'IP 전화기', name: 'ipPhone', type: 'search', value: 'Avaya J159_308152', searchObj:ZoomPhoneDataTables,view:false, required: false, disabled: false },
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: 'WorkplaceBiz', options: ['WorkplaceBiz', '...', 'Zoom Phone Basic '], required: true, disabled: false },
  { label: '통화녹음', name: 'callRecording', type: 'select', value: '미사용', options: ['미사용', '선택녹취','전수녹취'], required: false, disabled: false }
]);
const functionData = ref([
  { number: 1, key: 'Ext.8155', assignment: '입력란', displayName: '선택란', OBNumber: '', actions: '',},
  { number: 2, key: 'Ext.8156', assignment: '대표번호 발신', displayName: '0221778100', OBNumber: '', actions: '', },
  { number: 3, key: '단축다이얼', assignment: '010xxxxxxxx', displayName: '홍길동 휴대전화', OBNumber: '', actions: ''},
  { number: 4, key: '', assignment: '', displayName: '', OBNumber: '', actions: ''},
]);
const functionHeaders = ref<any[]>([
  { title: '라인', align: 'start', key: 'number', value: 'number', sortable: false , maxWidth: "40px"},
  { title: '키 유형', key: 'key', sortable: false, maxWidth: "200px"},
  { title: '할당', key: 'assignment', sortable: false, maxWidth: "200px"},
  { title: '표시이름', key: 'displayName', sortable: false, maxWidth: "200px"},
  { title: '발신번호', key: 'OBNumber', sortable: false, maxWidth: "200px"},
  { title: '', key: 'upDown', sortable: false, maxWidth: "150px"},
]);

// 부서명 변경 감지 팀명의 옵션 설정
watchDepartmentChange(formFields.value);
watchDepartmentChange(userFields.value);

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
        (!search.value.department || user.department.toLowerCase() === search.value.department.toLowerCase()) &&
        (!search.value.team || user.team.toLowerCase() === search.value.team.toLowerCase()) &&
        (!search.value.employmentStatus || user.employmentStatus.toLowerCase() === search.value.employmentStatus.toLowerCase()) &&
        (!search.value.employeeId || user.employeeId.toLowerCase().includes(search.value.employeeId.toLowerCase())) &&
        (!search.value.username || user.username.toLowerCase().includes(search.value.username.toLowerCase()))
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
    if(confirm(`${selectedName} 사용자가 ${selectedItem.length}명 검색되었습니다. 첫번째 사용자를 선택할까요? \n ${selectedItem[0].username} or ${selectedItem[1].username}`)){
      index = 1;
    }
  }

  userFields.value.forEach(field => {
    const key = field.name; // field.name 이 selectedItem 의 key 값과 일치
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
const keyOptions = ['Line','Speed Dial', 'Call Park', 'BLF', 'Intercom', 'Zoom Meeting'];

const isProLicense: ComputedRef<boolean> = computed(()=>{
  const license: FormField | undefined = userFields.value.find((user: any) => {
    return user.name.includes('zoomLicense');
  });
  const licenseName:string = license?.value as string;
  return licenseName.toLowerCase().includes('basic');
});

//마지막 down 버튼 찾기
const lastAssignedNumber = computed(() => {
  const reversedIndex = functionData.value
      .slice() // 원본 배열 유지
      .reverse()
      .findIndex(item => !!item.assignment); // assignment 값이 있는 첫 번째 요소 찾기

  return reversedIndex === -1 ? 1 : functionData.value.length - reversedIndex; // 실제 index+1 계산
});

//upDown 데이터 이동 : 현재위치 index, 이동범위 direction (+-n)
const moveItem = (number:number, direction:number) => {
  //index 값을 계산
  const index = number -1;
  const newIndex = index - direction;

  // 배열 범위를 벗어나지 않도록 제한
  if (newIndex < 0 || newIndex >= functionData.value.length) return;

  // 배열 요소 교환 (스왑)
  const temp = functionData.value[index];
  functionData.value[index] = functionData.value[newIndex];
  functionData.value[newIndex] = temp;

  // number 값을 다시 설정
  functionData.value.forEach((item, idx) => {
    item.number = idx + 1;
  });
};
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
                    <CustomSlotDialog title="기능키 관리" width="1500px" v-model:view="viewDialog">
                        <template v-slot:inCard>
                          <v-data-table :headers="functionHeaders" :items="functionData" item-value="username" hide-default-footer >
                              <!-- 필수값 표시하기 -->
                              <template v-slot:header.assignment="{ column }">
                                  {{ column.title }}
                                  <span v-if="column.title === '할당'" style="color: red;">*</span>
                              </template>

                              <!-- 필수값 assignment 이 있을때만 입력란 표시하기 -->
                              <template v-slot:item.key="{ item }">
                                <v-select v-if="!!item.assignment"
                                    v-model="item.key"
                                    :items="keyOptions"
                                    dense hide-details>
                                </v-select>
                                <div v-else>
                                  <v-btn variant="outlined" color="primary" class="font-weight-bold" @click="item.assignment='할당'">설정</v-btn>
                                </div>
                              </template>
                              <template v-slot:item.assignment="{ item }">
                                <v-text-field v-if="!!item.assignment" v-model="item.assignment" dense hide-details></v-text-field>
                              </template>
                              <template v-slot:item.displayName="{ item }">
                                <v-text-field v-if="!!item.assignment" v-model="item.displayName" dense hide-details></v-text-field>
                              </template>
                              <template v-slot:item.OBNumber="{ item }">
                                <v-select v-if="!!item.assignment"
                                    v-model="item.OBNumber"
                                    :items="keyOptions"
                                    dense hide-details>
                                </v-select>
                              </template>
                              <template  v-slot:item.actions="{ item }">
                                <v-btn flat variant="plain" v-if="!!item.assignment" :disabled="item.number === 1" @click="moveItem(item.number, 1)"><v-chip><v-icon icon="mdi-arrow-up-bold"/>up</v-chip></v-btn>
                                <v-btn flat variant="plain" v-if="!!item.assignment" :disabled="item.number === lastAssignedNumber" @click="moveItem(item.number, -1)"><v-chip><v-icon icon="mdi-arrow-down-bold"/>down</v-chip></v-btn>
                              </template>
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