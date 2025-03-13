<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, ref} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import {BasicDatatables} from "@/_mockApis/components/datatable/dataTable";
import type {Datatables} from "@/types/components/datatables";
import {searchSugg} from "@/_mockApis/headerData";
import CustomSearchCheckForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import CustomDetailDialog from "@/components/custom/dialog/CustomDetailDialog.vue";

const formFields_admin = ref<FormField[]>([
  { label: '발신자 번호', name: 'destAdd', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '수신자 번호', name: 'callerAdd', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '부서', name: 'department', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '팀', name: 'team', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '사용자', name: 'username', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '통화시작시간', name: 'startCall', type: 'date', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endCall', type: 'date', value: '', required: false, disabled: false },
  { label: 'ID', name: 'employeeId', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['국제전화', '모바일', '유선', '내선'], required: false, disabled: false },
  { label: '', name: '', type: 'none', value: '', required: false, disabled: true },
]);
const formFields_user = ref<FormField[]>([
  { label: '발신자 번호', name: 'destAdd', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '수신자 번호', name: 'callerAdd', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '통화시작시간', name: 'startCall', type: 'date', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endCall', type: 'date', value: '', required: false, disabled: false },
  { label: 'ID', name: 'employeeId', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['국제전화', '모바일', '유선', '내선'], required: false, disabled: false },
]);

const headers = ref<any[]>([
  { title: '유형', align: 'start', key: 'status' },
  { title: '구분', align: 'start', key: 'status' },
  { title: '발신자 번호', align: 'start', key: 'status' },
  { title: '수신자 번호', align: 'start', key: 'status' },
  { title: '부서', align: 'start', key: 'post' },
  { title: '팀', align: 'start', key: 'project' },
  { title: '사용자', align: 'start', key: 'name' },
  { title: '통화시작시간', align: 'end', key: 'budget' },
  { title: '통화종료시간', align: 'end', key: 'budget' },
  { title: '통화시간', align: 'end', key: 'budget' },
  { title: '녹취', align: 'end', key: 'budget' },
  { title: '결과', align: 'end', key: 'budget' },
  { title: 'ID', align: 'end', key: 'actions' },
])

const auth = ref<string>('admin'); //authStore 에서 받아올 예정
/* 관리자 / 사용자 구분 */
const formFields = computed(()=>{
  return auth.value === 'admin' ? formFields_admin.value : formFields_user.value;
});
const authName = computed(()=>{
  return auth.value === 'admin' ? '관리자' : '사용자';
});

const test_changeAuth = () => {
  if(auth.value === 'admin'){
    auth.value = 'user';
  }else {
    auth.value = 'admin';
  }
}

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
        (!search.value.username || user.name.toLowerCase().includes(search.value.username.toLowerCase())) &&
        (!search.value.types || search.value.types.length === 0 || search.value.types.includes(user.type)) &&
        (!search.value.categories || search.value.categories.length === 0 || search.value.categories.includes(user.category))
    );
  });
});

/* 팝업 띄우기 */
const viewDialog = ref<boolean>(false);
const selectedItem = ref<Datatables>();
const onSelectionChange = (item : Datatables) => {
  console.log("onSelectionChange", item);
  selectedItem.value = item;
  handleDialog();
};
const handleDialog = () => {
  viewDialog.value = true;
  console.log('open',viewDialog.value);
}

</script>
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard :title="`과금 _ ${authName}`">
              <v-row class="mb-6 mx-2">
                <h6 class="text-subtitle-1">줌 폰 시스템에서 개인이 사용한 내선, 외부전화의 수신, 발신 및 미응답 전화 사용 내역을 확인할 수 있습니다.
                  (관리자와 사용자의 권한에 따라 조회 옵션이 다르게 출력됩니다.
                  <v-btn @click="test_changeAuth" variant="plain">권한변경</v-btn>)
                </h6>
              </v-row>
              <v-row>
                <v-col cols="12">
                  <CustomSearchCheckForm :formFields="formFields" :colsPerRow="4" :edit="true">
                    <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                      <v-btn color="primary" flat @click="onSearch">조회</v-btn>
                      <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                    </div>
                  </CustomSearchCheckForm>
                </v-col>
              </v-row>
              <v-row>
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="name"
                              select-strategy="single" class="border rounded-md"
                              >
                  <template v-slot:item.actions="{ item }">
                    <v-btn icon="mdi-eye" variant="text" @click="onSelectionChange(item)">
                      <FileDescriptionIcon/>
                    </v-btn>
                  </template>
                </v-data-table>
<!--              팝업 : 콜 상세정보 -->
                    <CustomDetailDialog title="콜 상세 정보" v-model:view="viewDialog" :item="selectedItem"/>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>