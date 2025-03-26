<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import {computed, ref} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import {searchSugg} from "@/_mockApis/headerData";
import CustomSearchCheckForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import {RecordingDataTables} from "@/_mockApis/custom/ZoomData";
import type {RecordingItem} from "@/types/custom/DataTableTypes";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import CustomTwoSlotDialog from "@/components/custom/dialog/CustomTwoSlotDialog.vue";
import Wave from "@/components/custom/Wave.vue";

const formFields = ref<FormField[]>([
  { label: '발신자 번호', name: 'sender', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '수신자 번호', name: 'receiver', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '부서', name: 'department', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '팀', name: 'team', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '사용자', name: 'username', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '통화시작시간', name: 'startTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: 'ID', name: 'id', type: 'search', value: '', searchObj:searchSugg, view:false, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['전수', '선택'], required: false, disabled: false },
  { label: '', name: '', type: 'none', value: '', required: false, disabled: true },
]);

const headers = ref<any[]>([
  { title: "유형", align: 'start', key: "type" },
  { title: "구분", align: 'start', key: "direction" },
  { title: "발신자 번호", align: 'start', key: "sender" },
  { title: "수신자 번호", align: 'start', key: "receiver" },
  { title: "부서", align: 'start', key: "department" },
  { title: "팀", align: 'start', key: "team" },
  { title: "사용자", align: 'start', key: "username" },
  { title: "통화 시작시간", align: 'start', key: "startTime" },
  { title: "통화 종료시간", align: 'start', key: "endTime" },
  { title: "통화 시간", align: 'start', key: "duration" },
  { title: "재생", align: 'start', key: "play"},
  { title: "다운로드", align: 'start', key: "download"},
  { title: "ID", align: 'start', key: "id"},
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
const dialogWidth = 700;
const viewDialog = ref<{ [key: string]: boolean }>({
  play: false,
  download: false,
  id: false,
});

const selectedItem = ref<RecordingItem>();

const handleDialog = (type: "play" | "download" | "id", item: RecordingItem) => {
  selectedItem.value = item;

  // 모든 다이얼로그 초기화 후 특정 다이얼로그 열기
  Object.keys(viewDialog.value).forEach((key) => {
    viewDialog.value[key] = false;
  });

  viewDialog.value[type] = true;
  console.log(`open ${type}`, viewDialog.value[type]);
};

</script>
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="녹취">
              <v-row class="mb-6 mx-2">
                <h6 class="text-subtitle-1">줌 폰 시스템에서 저장 된 녹취 내역 확인과 청취를 할 수 있습니다.</h6>
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
                  <template v-slot:item.play="{ item }">
                    <v-btn icon="mdi-eye" @click="handleDialog('play',item)" :disabled="!item.play">
                      <v-icon icon="mdi-play-circle-outline"/>
                    </v-btn>
                  </template>
                  <template v-slot:item.download="{ item }">
                    <v-btn icon="mdi-eye" @click="handleDialog('download',item)">
                      <v-icon icon="mdi-cloud-download" />
                    </v-btn>
                  </template>
                  <template v-slot:item.id="{ item }">
                    <v-btn icon="mdi-eye" @click="handleDialog('id',item)">
                      <FileDescriptionIcon/>
                    </v-btn>
                  </template>
                </v-data-table>
<!--              팝업 : -->
                <CustomTwoSlotDialog title="청취" title-sec="텍스트" v-model:view="viewDialog.play" :width="dialogWidth">
                    <template v-slot:top>
                        <v-card-title class="text-subtitle-1">
                          발신자 {{ selectedItem?.sender}} <v-icon icon="mdi-arrow-right-bold-circle" color="primary"/> 수신자 {{ selectedItem?.receiver}}
                        </v-card-title>
                        <v-card-text>
                            <Wave v-if="!!selectedItem?.play" :url="selectedItem?.play"/>
                        </v-card-text>
                    </template>
                    <template v-slot:bottom>
                        <v-row>
                          <v-col cols="6" class="text-subtitle-1 justify-start">발신자 {{ selectedItem?.sender}}</v-col>
                          <v-col cols="6" class="text-subtitle-1 justify-end">수신자 {{ selectedItem?.receiver}}</v-col>
                        </v-row>
                        <v-row>
                          <v-col cols="12" class="">
                            <span> {{ JSON.stringify(selectedItem) }} </span>
                          </v-col>
                        </v-row>
                    </template>
                </CustomTwoSlotDialog>
                <CustomSlotDialog title="다운로드" v-model:view="viewDialog.download" width="">
                  <template v-slot:inCard>
                    <v-row>
                      <v-col>
                        <span> 파일 다운로드 경로를 지정해 주세요. </span>
                      </v-col>
                    </v-row>
                    <v-row class="align-center">
                      <v-col cols="12">
                        <v-file-input></v-file-input>
                      </v-col>
                    </v-row>
                  </template>
                  <template v-slot:btn>
                      <v-btn color="primary" variant="flat">저장</v-btn>
                  </template>
                </CustomSlotDialog>
                <CustomSlotDialog title="콜 상세 정보" v-model:view="viewDialog.id" :width="dialogWidth">
                  <template v-slot:inCard>
                    <div v-for="(value, key) in selectedItem" :key="key">
                      <v-btn readonly><span class="">{{ key }} : {{ value }}</span></v-btn>
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