<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, ref} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import {searchSugg} from "@/_mockApis/headerData";
import CustomSearchCheckForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import {RecordingDataTables} from "@/_mockApis/custom/RecordingData";
import type {RecordingItem} from "@/types/custom/DataTableTypes";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";

const formFields_admin = ref<FormField[]>([
  { label: '발신자 번호', name: 'sender', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '수신자 번호', name: 'receiver', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '부서', name: 'department', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '팀', name: 'team', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '사용자', name: 'username', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '통화시작시간', name: 'startTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: 'ID', name: 'id', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['국제전화', '모바일', '유선', '내선'], required: false, disabled: false },
  { label: '', name: '', type: 'none', value: '', required: false, disabled: true },
]);
const formFields_user = ref<FormField[]>([
  { label: '발신자 번호', name: 'sender', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '수신자 번호', name: 'receiver', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '통화시작시간', name: 'startTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: 'ID', name: 'id', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['국제전화', '모바일', '유선', '내선'], required: false, disabled: false },
]);

const headers = ref<any[]>([
  { title: '유형', align: 'start', key: 'type' },
  { title: '구분', align: 'start', key: 'direction' },
  { title: '발신자 번호', align: 'start', key: 'sender' },
  { title: '수신자 번호', align: 'start', key: 'receiver' },
  { title: '부서', align: 'start', key: 'department' },
  { title: '팀', align: 'start', key: 'team' },
  { title: '사용자', align: 'start', key: 'username' },
  { title: '통화시작시간', align: 'end', key: 'startTime' },
  { title: '통화종료시간', align: 'end', key: 'endTime' },
  { title: '통화시간', align: 'end', key: 'duration' },
  { title: '녹취', align: 'end', key: 'play' },
  { title: '결과', align: 'end', key: 'download' },
  { title: 'ID', align: 'end', key: 'id' },
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
  if (!search.value) return RecordingDataTables;

  // 모든 필드가 비어있는지 검사
  // es2017 이상 필요
  // const isAllEmpty = Object.values(search.value).every(val => val === "" || (Array.isArray(val) && val.length === 0));
  const isAllEmpty = search.value &&
      Object.keys(search.value).length > 0 &&
      Object.keys(search.value).every((key) => {
        const val = search.value[key];
        return val === "" || (Array.isArray(val) && (val.length === 0 || (val.length === 1 && val[0] === "")));
      });

  if (isAllEmpty) return RecordingDataTables;

  return RecordingDataTables.filter((record: any) => {
    return (
        (!search.value.sender || record.sender.toLowerCase().includes(search.value.sender.toLowerCase())) &&
        (!search.value.receiver || record.receiver.toLowerCase().includes(search.value.receiver.toLowerCase())) &&
        (!search.value.department || record.department.toLowerCase().includes(search.value.department.toLowerCase())) &&
        (!search.value.team || record.team.toLowerCase().includes(search.value.team.toLowerCase())) &&
        (!search.value.username || record.username.toLowerCase().includes(search.value.username.toLowerCase())) &&
        (!search.value.startTime || new Date(record.startTime) >= new Date(search.value.startTime)) &&
        (!search.value.endTime || new Date(record.endTime) <= new Date(search.value.endTime)) &&
        (!search.value.id || record.id.toString().includes(search.value.id)) &&
        (!search.value.direction || search.value.direction.length === 0 || search.value.direction.includes(record.direction)) &&
        (!search.value.type || search.value.type.length === 0 || search.value.type.includes(record.type))
    );
  });
});

/* 팝업 띄우기 */
const viewDialog = ref<boolean>(false);
const selectedItem = ref<RecordingItem>();
const onSelectionChange = (item : RecordingItem) => {
  console.log("onSelectionChange", item);
  selectedItem.value = item;
  handleDialog();
};
const handleDialog = () => {
  viewDialog.value = true;
  console.log('open',viewDialog.value);
}
/* 복사 */
function copyToClipboard(item : any) {
  const jsonString = JSON.stringify(item);
  navigator.clipboard.writeText(jsonString)
      .then(() => {
        alert('클립보드에 복사되었습니다!');
      })
      .catch((err: any) => {
        alert('복사 실패: ' + err);
      });
}
/* 출력포멧 */
// key를 포맷하는 함수 (첫 문자 대문자화)
const formatKey = (key: string) => {
  return key
      .replace(/([A-Z])/g, ' $1') // CamelCase로 되어 있는 경우 공백 추가
      .replace(/^./, str => str.toUpperCase()); // 첫 문자 대문자화
};

// 특정 키에 대한 값 포맷 (예: 날짜 포맷 변환)
const formatValue = (key: string, value: any) => {
  if (key === 'startTime' || key === 'endTime') {
    return value ? value.replace('T', ' ').slice(0, 16) : ''; // 날짜 형식 변환
  }
  if (key === 'duration') {
    return value; // 통화 시간 그대로 표시
  }
  return value;
};

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
                      <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                      <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                    </div>
                  </CustomSearchCheckForm>
                </v-col>
              </v-row>
              <v-row>
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="name"
                              select-strategy="single" class="border rounded-md"
                              >
                  <template v-slot:item.id="{ item }">
                    <v-btn icon="mdi-eye" variant="text" @click="onSelectionChange(item)">
                      <FileDescriptionIcon/>
                    </v-btn>
                  </template>
                </v-data-table>
<!--              팝업 : 콜 상세정보 -->
                <CustomSlotDialog title="콜 상세 정보" v-model:view="viewDialog" :item="selectedItem">
                  <template v-slot:inCard>
                    <div v-if="!!selectedItem">
                        <div v-for="(value, key) in selectedItem" :key="key">
                          <span>{{ formatKey(key) }} : {{ formatValue(key, value) }}</span>
                          <v-btn color="primary" variant="plain" @click="copyToClipboard(formatValue(key, value))" >복사</v-btn>
                        </div>
                    </div>
                  </template>
                </CustomSlotDialog>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>