<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { onMounted, ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import type { UserItem } from "@/types/custom/DataTableTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import { useTableManager } from "@/common/useTableManager";
import ExcelUploadDialogBtn from "@/common/excel/ExcelUploadDialogBtn.vue";
import { apiClient } from "@/data/Axios";

//검색
const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], required: false, disabled: false },
  { label: '사원번호', name: 'employeeId',  type: 'search', value: '',searchObj:[], required: false, disabled: false },
  { label: '사용자명', name: 'username',  type: 'search_list', value: '', searchObj:[], required: false, disabled: false }
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
  { label: '부서명', name: 'department',  type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], placeholder: undefined, required: true, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], placeholder: undefined, required: true, disabled: false },
  { label: '직위', name: 'position', type: 'text', value: '', placeholder: '직위 입력', required: true, disabled: false },
  { label: '메일주소', name: 'email', type: 'text', value: '', placeholder: 'example@domain.com', required: true, disabled: false },
  { label: '전화번호', name: 'phone', type: 'text', value: '', placeholder: '02-0000-0000', required: true, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'text', value: '', placeholder: '0000', required: false, disabled: false },
  { label: '입사일자', name: 'hireDate', type: 'date', value: '', placeholder: 'YYYY-MM-DD', required: true, disabled: false },
  { label: '재직상태', name: 'employmentStatus', type: 'select', value: '', options: ['재직', '퇴사', '휴직'], placeholder: undefined, required: true, disabled: false },
  { label: '사용자 권한', name: 'userRole', type: 'select', value: '', options: ['사용자', '관리자', '슈퍼 관리자'], placeholder: undefined, required: true, disabled: false },
  { label: '사용유무', name: 'activeStatus', type: 'select', value: '', options: ['Y', 'N'], placeholder: undefined, required: true, disabled: false },
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: '', options: ['WorkplaceBiz', 'Basic', '...'], placeholder: undefined, required: true, disabled: false },
  { label: '줌폰 라이센스', name: 'phoneLicense', type: 'select', value: '', options: ['Phone Pro', 'Power', 'Phone Pro, Power'], placeholder: undefined, required: false, disabled: false },
]);

const setUsers = (userList: UserItem[])=>{
  users.value = userList;
  console.log("사용자 데이터:", users.value);
}
// mockApi 로 데이터 불러오기.
const users = ref<UserItem[]>([]); // 사용자 데이터를 저장할 변수
onMounted(async () => {
  // 초기화 또는 초기 작업 수행
  try {
    setUsers(await apiClient.get("/zoom/users",{userId:""}))
  }catch (e){
    console.error("데이터 로드 중 오류 발생:", e);
  }
});
// `users` 값을 동적으로 반영하도록 useTableManager 를 수정하였습니다.
//모듈 호출 : 기존코드 동일
const {
  onSearch,
  resetSearch,
  filteredList,
  selectedEmpId,
  onSelectionChange,
  edit,
  handleEdit,
  onNew,
  onSave,
  onDelete,
  onExcelSave
} = useTableManager<UserItem>(users, formFields, userFields);
</script>
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="사용자 관리">
<!--                <v-row>-->
<!--                    <v-btn @click="setUsers(users.slice(0, 2))">test</v-btn>-->
<!--                    <p>{{ JSON.stringify(users) }}</p>-->
<!--                    <p>{{ JSON.stringify(filteredList) }}</p>-->
<!--                </v-row>-->
                <v-row>
                    <CustomSearchChecksForm :formFields="formFields" :colsPerRow="5" :edit="true" :hide-details="true">
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
                            <v-btn flat color="primary" variant="outlined" @click="onNew"
                                ><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록
                            </v-btn>
                            <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0])"
                                ><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제
                            </v-btn>
                        </div>
                    </v-col>
                    <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                            <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                            <ExcelUploadDialogBtn :save="onExcelSave" title="엑셀 업로드" />
                        </div>
                    </v-col>
                </v-row>
                <v-row>
                    <v-data-table
                        items-per-page="5"
                        :headers="headers"
                        :items="filteredList"
                        item-value="employeeId"
                        select-strategy="single"
                        show-select
                        class="border rounded-md"
                        v-model="selectedEmpId"
                        @update:model-value="onSelectionChange"
                    >
                    </v-data-table>
                </v-row>
            </UiParentCard>
            <br />
            <UiParentCard title="사용자 상세정보" :key="selectedEmpId[0]" v-if="selectedEmpId[0]">
                <template v-slot:action></template>
                <template v-slot:default>
                    <CustomSearchChecksForm :formFields="userFields" :colsPerRow="4" :edit="edit">
                        <template v-slot:topBtn="{ validateForm }">
                            <v-row>
                                <v-col cols="5">
                                    <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                        <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)">편집</v-btn>
                                        <v-btn flat color="error" variant="outlined" @click="handleEdit(false)">취소</v-btn>
                                    </div>
                                </v-col>
                                <v-col cols="7">
                                    <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                                        <CustomSlotDialog ref="passwordDialog" title="패스워드초기화" />
                                        <v-btn color="grey" variant="outlined" @click="($refs.passwordDialog as any)?.open()"
                                            >패스워드초기화</v-btn
                                        >
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