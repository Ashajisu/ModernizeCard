<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { computed, ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import { UserDataTables } from "@/_mockApis/custom/ZoomData";
import type {UserItem} from "@/types/custom/DataTableTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { watchDepartmentChange } from "@/_mockApis/custom/teamOptions";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";

//검색
const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: false, disabled: false },
  { label: '사원번호', name: 'employeeId',  type: 'search', value: '', searchObj:UserDataTables, view:false, required: false, disabled: false },
  { label: '사용자명', name: 'username',  type: 'search_list', value: '', searchObj:UserDataTables, view:false, required: false, disabled: false }
]);
//테이블헤더
const headers = ref<any[]>([
  { title: "부서명", align: "start", key: "department" },
  { title: "팀명", align: "start", key: "team" },
  { title: "사용자명", align: "start", key: "username" },
  { title: "사원번호", align: "start", key: "employeeId" },
  { title: "직위", align: "center", key: "position" },
  { title: "메일주소", align: "center", key: "email" },
  { title: "줌 라이센스", align: "end", key: "zoomLicense" },
  { title: "줌폰 라이센스", align: "end", key: "phoneLicense" },
]);
//상세정보
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '', placeholder: '이름 입력', required: true, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '', placeholder: '사원번호 입력', required: true, disabled: false },
  { label: '부서명', name: 'department',  type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], view:false, required: true, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], view:false, required: true, disabled: false },
  { label: '직위', name: 'position', type: 'text', value: '', placeholder: '직위 입력', required: true, disabled: false },
  { label: '메일주소', name: 'email', type: 'text', value: '', placeholder: 'example@domain.com', required: true, disabled: false },
  { label: '전화번호', name: 'phone', type: 'text', value: '', placeholder: '02-0000-0000', required: true, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'text', value: '', placeholder: '0000', required: false, disabled: false },
  { label: '입사일자', name: 'hireDate', type: 'date', value: '', placeholder: 'YYYY-MM-DD', required: true, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: true, disabled: false },
  { label: '사용자 권한', name: 'userRole', type: 'select', value: '', options: ['사용자', '관리자', '슈퍼 관리자'], required: true, disabled: false },
  { label: '사용유무', name: 'activeStatus', type: 'select', value: '', options: ['Y', 'N'], required: true, disabled: false },
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: '', options: ['WorkplaceBiz', 'Basic', '...'], required: true, disabled: false },
  { label: '줌폰 라이센스', name: 'phoneLicense', type: 'select', value: '', options: ['Phone Pro', 'Power', 'Phone Pro, Power'], required: false, disabled: false },
]);
//테이블데이터 반응형 배열로 선언
const userList = ref<UserItem[]>(UserDataTables);

//부서명 변경 감지 팀명의 옵션 설정
watchDepartmentChange(formFields.value);
watchDepartmentChange(userFields.value);

//검색기능
const search = ref();
const onSearch = async (validateForm:any) =>{
  const formData:UserItem|null = await validateForm(); // validateForm() 실행 후 결과 대기
  console.log('onSearch', formData);
  search.value = formData;
}
//검색 초기화
const resetSearch = ()=>{
  formFields.value.forEach(field => {
    field.value='';
  });
  search.value = null;
};
//검색어로 테이블 필터링
const filteredList = computed(() => {
  if (!search.value) return userList.value;

  let isAllEmpty = true;
  for (const key in search.value) {
    if (search.value[key] !== "") {
      isAllEmpty = false;
      break;
    }
  }
  if (isAllEmpty) return userList.value;

  return userList.value.filter((user: any) => {
    //배열일 수 있는 필드라면,
    const matchesUsername =
        (Array.isArray(search.value.username)
                ? search.value.username.length === 0 || search.value.username.some((val: string) => user.username.toLowerCase() === val.toLowerCase())
                : !search.value.username || user.username.toLowerCase().includes(search.value.username.toLowerCase())
        );

    return (
        matchesUsername && // username 조건을 포함
        (!search.value.department || user.department.toLowerCase() === search.value.department.toLowerCase()) &&
        (!search.value.team || user.team.toLowerCase() === search.value.team.toLowerCase()) &&
        (!search.value.activeStatus || user.activeStatus.toLowerCase() === search.value.activeStatus.toLowerCase()) &&
        (!search.value.employeeId || user.employeeId.toLowerCase().includes(search.value.employeeId.toLowerCase()))
    );
  });
});

//체크박스 선택
const selectedEmpId = ref<string[]>([]);
const onSelectionChange = () => {
  console.log("onSelectionChange", selectedEmpId.value);
  if(!!selectedEmpId.value){
    updateUserFields(selectedEmpId.value[0]);
  }
};

//사용자 상세정보 출력
const updateUserFields = (selectedEmpId: string) => {
  const selectedItem: UserItem[] | undefined = userList.value.filter((user) => {
    return !selectedEmpId || user.employeeId.includes(selectedEmpId);
  });

  let index = 0;
  userFields.value.forEach(field => {
    if (selectedItem[index][field.name] !== undefined) {
      field.value = selectedItem[index][field.name]; // 값 매칭
    } else {
      field.value = ''; // 매칭되지 않으면 빈 값 설정
    }
  });
};

//사용자 상세정보 편집 / 읽기 화면으로 변경
const edit = ref<boolean>(false);
const handleEdit = (bool : boolean) => {
  edit.value = bool;
  console.log('handleEdit', edit.value);
};

//사용자 생성
const onNew = () => {
  selectedEmpId.value=['new'];
  userFields.value.forEach(field => {
      field.value = ''; //빈 값 설정
  });
  edit.value = true;
}

//사용자 저장
const onSave = async (validateForm:any) => {
  const formData:UserItem|null = await validateForm(); // validateForm() 실행 후 결과 대기
  if(!!formData){
    console.log(formData);
    const existingUserIndex = userList.value.findIndex(user => user.employeeId === formData.employeeId);
    if(existingUserIndex === -1){
        userList.value.push({ ...formData });
        alert(`"${formData.username}" 사용자의 정보가 등록되었습니다.`);
    }else {
        userList.value[existingUserIndex] = { ...formData };
        alert(`"${formData.username}" 사용자의 정보가 업데이트되었습니다.`);
    }
    edit.value = false;
  }
}

//사용자 삭제
const onDelete = (selected : string) => {
  const existingUserIndex = userList.value.findIndex(user => user.employeeId === selected);
  if(confirm('줌 폰 사용자도 함께 삭제 됩니다.\n\n선택한 사용자를 삭제 하시겠습니까?')){
    const removedItems = userList.value.splice(existingUserIndex, 1);
    if(removedItems?.[0]){
      alert(`${removedItems?.[0].username} 이 삭제 되었습니다.`);
    }else {
      alert('삭제에 실패하였습니다. 다시 시도해주세요.')
    }
  }
}
</script>
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="사용자 관리">
              <v-row>
                <CustomSearchChecksForm :formFields="formFields" :colsPerRow="5" :edit="true">
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
                    <v-btn flat color="primary" variant="outlined" @click="onNew"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
                    <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0])"><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제 </v-btn>
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
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="employeeId"
                              select-strategy="single" show-select class="border rounded-md"
                              v-model="selectedEmpId"
                              @update:model-value="onSelectionChange"
                              >
                </v-data-table>
              </v-row>
            </UiParentCard>
            <br>
          <UiParentCard title="사용자 상세정보" :key="selectedEmpId[0]" v-if="!!selectedEmpId">
            <template v-slot:action></template>
            <template v-slot:default>
              <CustomSearchChecksForm :formFields="userFields" :colsPerRow="4" :edit="edit">
                <template v-slot:topBtn="{validateForm}">
                  <v-row>
                    <v-col cols="5">
                      <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                        <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)">편집</v-btn>
                        <v-btn flat color="error" variant="outlined" @click="handleEdit(false)">취소</v-btn>
                      </div>
                    </v-col>
                    <v-col cols="7">
                      <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                        <CustomSlotDialog ref="passwordDialog" title="패스워드초기화" :view="false" width=""/>
                        <v-btn color="grey" variant="outlined" @click="$refs.passwordDialog?.open()">패스워드초기화</v-btn>
                        <v-btn flat color="primary" variant="outlined" @click="onSave(validateForm)">저장</v-btn>
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

<style scoped>
</style>