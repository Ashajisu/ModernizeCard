<script setup lang="ts">
import { GroupDatatables} from '@/_mockApis/components/datatable/GroupTable';
import { ref, computed } from 'vue';
import axios from 'axios';
import UiParentCard from "@/components/shared/UiParentCard.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import ExcelUploadDialogBtn from "@/common/excel/ExcelUploadDialogBtn.vue";
import type { FormField } from '@/types/custom/InputTypes';
import { useTableManager } from "@/common/useTableManager";
import type {Datatables} from "@/types/components/datatables/groupIndex";
import { alert, confirm } from '@/common/alertService';

interface Group {
  group: string;
  department: string;
  groupExtensionNumber: string;
  bellRingDelay: string;
  groupNumbers: string;
}

const userList = ref([
  {id : 1, username: '박OO', extension: '101', group: '경영지원팀'},
  {id : 2, username: '최OO', extension: '102', group: '영업1팀'},
  {id : 3, username: '김OO', extension: '103', group: '영업2팀'},
  {id : 4, username: '이OO', extension: '104', group: '기술지원1팀'},
]);
//부서 목록
const formFields = ref<FormField[]>([
  { label: '당겨받기 그룹', name: 'group', type: 'search_list', value: '', searchObj:GroupDatatables, required: false, disabled: false },
  { label: '그룹 내선번호', name: 'groupExtensionNumber', type: 'text', value: '', required: false, disabled: false },
  { label: '사용자', name: 'username', type: 'search_list', value: '', searchObj:userList.value, required: false, disabled: false },
  { label: '사용자 내선번호', name: 'mobile', type: 'text', value: '', required: false, disabled: false },
]);
const headers = ref<any[]>([
  { title: '당겨받기 그룹', align: 'center', key: 'group' },
  { title: '부서명', align: 'center', key: 'department' },
  { title: '그룹 내선번호', align: 'center', key: 'groupExtensionNumber' },
  { title: '구성원 수', align: 'center', key: 'groupNumbers' },
  { title: '벨울림 지연', align: 'center', key: 'bellRingDelay' },
]);
const groupFields = ref<FormField[]>([
  { label: 'id', name: 'id', type: 'hidden', value: '', placeholder: '이름 입력', required: false, disabled: false },
  { label: '그룹명', name: 'group', type: 'text', value: '', placeholder: '이름 입력', required: false, disabled: false },
  { label: '부서명', name: 'department', type: 'select', value: '', options:[], required: false, disabled: false },
  { label: '그룹 내선번호', name: 'groupExtensionNumber', type: 'search', value: '', searchObj:GroupDatatables, required: false, disabled: false },
  { label: '벨울림 지연', name: 'bellRingDelay', type: 'select', value: '', options: ['0초', '5초', '10초'], required: false, disabled: false },
  { label: '그룹 구성원', name: 'groupNumbers', type: 'text', value: '', required: true, disabled: true },
  { label: 'manage', name: 'manage', type: 'slot', value: '', required: false, disabled: false }
]);

const identifierField:string = 'id';

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
  selectedEmpId,
  selectedItem,
  onSelectionChange,
  edit,
  handleEdit,
  onNew,
  onSave,
  onDelete,
  onExcelSave,
  updateUserFields
} = useTableManager<Datatables>(GroupDatatables, formFields, groupFields, identifierField);

// 엑셀 다운로드 처리 : ?? 사용자 안내창 없음.
const downloadExcel = async () => {
  try {
    const response = await axios({
      url: 'http://localhost:5173/api/excel/download',
      method: 'GET',
      responseType: 'blob'
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'group_data.xlsx');
    document.body.appendChild(link);
    link.click();
  }catch(error){
    console.error("엑셀 다운로드 실패:", error);
  }
};

// 엑셀 업로드 처리 : ?? 업로드인데 파일입력창이 안뜸.
const selectedFile = ref(null);
const uploadExcel = async () => {
  if(!selectedFile.value){
    alert("파일을 선택하세요.");
    return;
  }

  const formData = new FormData();
  formData.append("file", selectedFile.value);

  try{
    const response = await axios.post("http://localhost:5173/api/excel/upload", formData, {
      headers: {"Content-Type" : "multipart/form-data"}
    });

    console.log("업로드 성공 : ", response.data);
    alert("엑셀 업로드 성공!");
  } catch(error){
    console.error("엑셀 업로드 실패 : ", error);
    alert("엑셀 업로드 실패!");
  }
}

//그룹 추가
const manageDialog = ref(false);
const userSearchTerm = ref('');

//구성원 관리 저장: 성공여부 반환
const selectedUser = ref([]);
const confirmManage = async () => {
  const count = selectedUser.value.length;
  console.log('선택된 사용자들:', selectedUser.value);
  console.log('구성원 수:', count);
  if (!!count) {
    const groupNumbersField = groupFields.value.find(f => f.name === 'groupNumbers');
    if (groupNumbersField) {
      groupNumbersField.value = String(count);
    }
    return true;
  } else {
    await alert('저장에 실패하였습니다. 잠시후 다시 시도해 주세요.');
    return false;
  }
}

const filteredUserList = computed(() => {
  return userList.value.filter(user => {
    return (
        user.username.includes(userSearchTerm.value) ||
        user.extension.includes(userSearchTerm.value)
    );
  });
})
</script>
<template>
  <v-row>
    <v-col cols="12" md="12">
      <UiParentCard title="당겨받기 관리">
        <v-row class="mb-6 mx-2">
          <h6 class="text-subtitle-1">줌 폰 시스템에서 당겨받기 그룹 및 구성원 관리를 할 수 있습니다.</h6>
        </v-row>
        <v-row>
          <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true">
            <template v-slot:lineBtn="{ validateForm }">
                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
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
              <v-btn color="grey" variant="outlined" @click="downloadExcel">엑셀 다운로드</v-btn>
              <ExcelUploadDialogBtn :save="onExcelSave" title="엑셀 업로드"/>
            </div>
          </v-col>
        </v-row>
        <v-row>
          <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="id"
                        select-strategy="single" show-select class="border rounded-md"
                        v-model="selectedEmpId"
                        @update:model-value="onSelectionChange"
          >
          </v-data-table>
        </v-row>
      </UiParentCard>
      <UiParentCard title="당겨받기 그룹 상세정보" :key="selectedEmpId[0]" v-if="selectedEmpId[0]">
        <template v-slot:default>
          <CustomSearchChecksForm :formFields="groupFields" :colsPerRow="3" :edit="edit">
            <template v-slot:topBtn="{validateForm}">
              <v-row>
                <v-col>
                  <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)" >편집</v-btn>
                    <v-btn flat color="error" variant="outlined" @click="handleEdit(false)">취소</v-btn>
                  </div>
                </v-col>
                <v-col>
                  <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn flat color="primary" variant="outlined" @click="onSave(validateForm)">저장</v-btn>
                  </div>
                </v-col>
              </v-row>
            </template>

            <!-- 관리 버튼 및 모달 -->
            <template v-slot:manage="{field}">
              <v-btn flat color="warning" variant="outlined" :disabled="!edit" @click="( $refs.manageDialog as any )?.open()">관리</v-btn>
              <CustomSlotDialog ref="manageDialog" title="당겨받기 구성원 관리">
                <template v-slot:inCard>
                  <v-text-field
                      v-model="userSearchTerm"
                      label="사용자명 또는 내선번호"
                      prepend-inner-icon="mdi-magnify"
                      clearable></v-text-field>
                  <v-data-table
                      :headers="[
                                    { title :'사용자명', value:'username'},
                                    { title :'내선번호', value:'extension'},
                                    { title : '당겨받기 그룹', value:'group'},
                                ]"
                      v-model="selectedUser" :items="filteredUserList"
                      return-object
                      item-value="id"
                      show-select item-key="id">
                  </v-data-table>
                </template>
                <template v-slot:btn>
                  <v-btn color="primary" @click="async () => { if (await confirmManage()) ($refs.manageDialog as any)?.close(); }">저장</v-btn>
                </template>
              </CustomSlotDialog>
            </template>
          </CustomSearchChecksForm>
        </template>
      </UiParentCard>
    </v-col>
  </v-row>
</template>

