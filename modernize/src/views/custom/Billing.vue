<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { computed, ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import CustomSearchsCheckForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { RecordingDataTables } from "@/_mockApis/custom/ZoomData";
import type { RecordingItem } from "@/types/custom/DataTableTypes";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import { useTableManager } from "@/common/useTableManager";

const formFields_admin = ref<FormField[]>([
  { label: '발신자 번호', name: 'sender', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '수신자 번호', name: 'receiver', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '부서', name: 'department', type: 'select', value: '', options:['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀', name: 'team', type: 'select', value: '', options:[], required: false, disabled: false },
  { label: '사용자', name: 'username', type: 'search_list', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '통화시작시간', name: 'startTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: 'ID', name: 'id', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['국제전화', '모바일', '유선', '내선'], required: false, disabled: false },
  { label: '', name: '', type: 'none', value: '', required: false, disabled: true },
]);
const formFields_user = ref<FormField[]>([
  { label: '발신자 번호', name: 'sender', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '수신자 번호', name: 'receiver', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '통화시작시간', name: 'startTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: 'ID', name: 'id', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
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

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
} = useTableManager<RecordingItem>(RecordingDataTables, formFields);


/* 복사 */
function copyToClipboard(item : any) {
  navigator.clipboard.writeText(item)
      .then(() => {
        alert('클립보드에 복사되었습니다!');
      })
      .catch((err: any) => {
        alert('복사 실패: ' + err);
      });
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
                  <CustomSearchsCheckForm :formFields="formFields" :colsPerRow="4" :edit="true">
                    <template v-slot:lineBtn="{ validateForm }">
                      <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                        <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                        <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                        <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                      </div>
                    </template>
                  </CustomSearchsCheckForm>
                </v-col>
              </v-row>
              <v-row>
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="name"
                              select-strategy="single" class="border rounded-md"
                              >
                  <template v-slot:item.id="{ item }">
                    <v-btn icon="mdi-eye" variant="text" @click="( ($refs as Record<string, any>)[`detailDialog-${item.id}`]?.open() )">
                      <FileDescriptionIcon/>
      <!--              팝업 : 콜 상세정보 -->
                      <CustomSlotDialog :ref="`detailDialog-${item.id}`" title="콜 상세 정보" width="500">
                        <template v-slot:inCard>
                          <div v-if="!!item">
                              <div v-for="(value, key) in item" :key="key">
                                <v-btn readonly><span class="">{{ key }} : {{ value }}</span></v-btn>
                                <v-btn color="primary" variant="plain" @click="copyToClipboard(value)" >복사</v-btn>
                              </div>
                          </div>
                        </template>
                      </CustomSlotDialog>
                    </v-btn>
                  </template>
                </v-data-table>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>