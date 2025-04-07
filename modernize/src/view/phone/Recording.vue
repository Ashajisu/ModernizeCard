<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import CustomSearchCheckForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { RecordingDataTables } from "@/_mockApis/custom/ZoomData";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import CustomTwoSlotDialog from "@/components/custom/dialog/CustomTwoSlotDialog.vue";
import Wave from "@/components/custom/Wave.vue";
import RecordChat from "@/components/apps/chats/RecordChat.vue";
import { useTableManager } from "@/common/useTableManager";
import type { RecordingItem } from "@/types/custom/DataTableTypes";

const formFields = ref<FormField[]>([
  { label: '발신자 번호', name: 'sender', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '수신자 번호', name: 'receiver', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '부서', name: 'department', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '팀', name: 'team', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '사용자', name: 'username', type: 'search_list', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '통화시작시간', name: 'startTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: '통화종료시간', name: 'endTime', type: 'datetime', value: '', required: false, disabled: false },
  { label: 'ID', name: 'id', type: 'search', value: '', searchObj:RecordingDataTables, required: false, disabled: false },
  { label: '구분', name: 'direction', type: 'check', value: '',options: ['인바운드', '아웃바운드'], required: false, disabled: false },
  { label: '유형', name: 'type', type: 'check', value: '',options: ['유선', '내선', '모바일'], required: false, disabled: false },
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

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList
} = useTableManager<RecordingItem>(RecordingDataTables, formFields);

/*  audio 시간 상태를 전달 : Wave -> RecordChat */
const audioTime = ref(0);

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
                    <template v-slot:lineBtn="{ validateForm }">
                      <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                        <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                        <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                        <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                      </div>
                    </template>
                  </CustomSearchCheckForm>
                </v-col>
              </v-row>
              <v-row>
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="name"
                              select-strategy="single" class="border rounded-md"
                              >
                  <!--                  청취 -->
                  <template v-slot:item.play="{ item }">
                    <v-btn icon="mdi-eye" @click="( ($refs as Record<string, any>)[`playDialog-${item.id}`]?.open() )" :disabled="!item.play">
                      <v-icon icon="mdi-play-circle-outline"/>
                    </v-btn>
                    <CustomTwoSlotDialog :ref="`playDialog-${item.id}`" title="청취" title-sec="텍스트" :view="false">
                      <template v-slot:top>
                        <v-card-title class="text-subtitle-1">
                          발신자 {{ item?.sender}} <v-icon icon="mdi-arrow-right-bold-circle" color="primary"/> 수신자 {{ item?.receiver}}
                        </v-card-title>
                        <v-card-text>
                          <Wave v-if="!!item?.play" :url="item?.play" v-model:audioTime="audioTime"/>
                        </v-card-text>
                      </template>
                      <template v-slot:bottom>
                        <v-row>
                          <v-col cols="6" class="text-subtitle-1 justify-start">발신자 {{ item?.sender}}</v-col>
                          <v-col cols="6" class="text-subtitle-1 justify-end text-end">수신자 {{ item?.receiver}}</v-col>
                        </v-row>
                        <v-row>
                          <v-col cols="12" class="">
                            <RecordChat :audioTime="audioTime"/>
                          </v-col>
                        </v-row>
                      </template>
                    </CustomTwoSlotDialog>
                  </template>

                  <!--                  다운로드 -->
                  <template v-slot:item.download="{ item }">
                    <v-btn icon="mdi-eye" @click="( ($refs as Record<string, any>)[`downloadDialog-${item.id}`]?.open() )">
                      <v-icon icon="mdi-cloud-download" />
                    </v-btn>
                    <CustomSlotDialog :ref="`downloadDialog-${item.id}`" title="다운로드">
                      <template v-slot:inCard>
                        <v-row>
                          <v-col>
                            <span>파일 다운로드 경로를 지정해 주세요. </span>
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
                  </template>

                  <!--                  상세정보 -->
                  <template v-slot:item.id="{ item }">
                    <v-btn icon="mdi-eye" @click="( ($refs as Record<string, any>)[`detailDialog-${item.id}`]?.open() )">
                      <FileDescriptionIcon/>
                    </v-btn>
                    <CustomSlotDialog :ref="`detailDialog-${item.id}`" title="콜 상세 정보">
                      <template v-slot:inCard>
                        <div v-for="(value, key) in item" :key="key">
                          <v-btn readonly><span class="">{{ key }} : {{ value }}</span></v-btn>
                        </div>
                      </template>
                    </CustomSlotDialog>
                  </template>
                </v-data-table>
              </v-row>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>