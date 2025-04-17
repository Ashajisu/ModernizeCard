<script setup lang="ts">

import {ref} from 'vue';
import { DeviceDatatables } from '@/_mockApis/components/datatable/DeviceTable';
import UiParentCard from "@/components/shared/UiParentCard.vue";

const selectedDate = ref(new Date().toISOString().substr(0, 10));

const cards = ref([
  { title: "온라인 수량", value: 119 },
  { title: "오프라인 수량", value: 11 },
  { title: "사용자 할당", value: 117 },
  { title: "사용자 미할당", value: 13 }
]);

const headers = ref([
  {title:"표시 이름", key: "deviceName"},
  {title:"사용자", key: "user"},
  {title:"내선번호", key: "extensionNumber"},
  {title:"PSTN 번호", key: "pstnNumber"},
  {title:"상태", key: "status"},
  {title:"모델", key: "model"},
  { title: "MAC 주소", key: "macAddress" },
  { title: "펌웨어", key: "firmware" },
  { title: "Public IP 주소", key: "publicAddress" },
  { title: "Private IP 주소", key: "privateAddress" }
]);


const selectedItem = ref({
  deviceName: "",
  user: "",
  extensionNumber: "",
  pstnNumber: "",
  status: "",
  model: "",
  macAddress: "",
  firmware: "",
  publicAddress: "",
  privateAddress: ""
})

const itemsPerPage = ref(3);
const pagination = ref(1);

const showDashboard = ref(false);
const showDataTable = ref(false);

const fetchData = () => {
  // API 호출 로직 구현
  console.log('데이터 조회:', selectedDate.value);
  showDashboard.value = true;
};


const toggleDataTable = (title:string) => {
    if(title === '오프라인 수량'){
      showDataTable.value = !showDataTable.value;
    }
};

</script>

<template>
  <UiParentCard title="줌 폰 디바이스 현황">
    <v-row class="mb-6 mx-2">
      <h6 class="text-subtitle-1">줌 폰 시스템의 전체 디바이스 현황을 모니터링 할 수 있습니다.</h6>
    </v-row>
    <v-row>
      <v-col cols="6">
        <div class="text-body-1 font-weight-bold text-medium-emphasis">
          최종 업데이트 시간 : {{ selectedDate }}
        </div>
      </v-col>
      <v-col cols="6" class="d-flex justify-end">
        <v-btn color="primary" @click="fetchData">조회</v-btn>
      </v-col>
    </v-row>
      <v-divider class="my-4"></v-divider>
      <template v-if="showDashboard">
        <v-row>
          <v-col cols="12">
            <div class="text-h5 text-center font-weight-bold mb-2">
              전체 디바이스 현황
            </div>
          </v-col>
        </v-row>

        <!--전체 전화기 등록 수량 -->
        <v-row class="mb-4">
          <v-col cols="4">
            <v-card class="pa-6 text-center">
              <div class="text-grey-darken-2 text-subtitle-1">전체 전화기 등록 수량</div>
              <div class="text-h3 font-weight-bold">130</div>
              <v-btn icon variant="text" class="mt-2">
                <v-icon size="large" color="primary">mdi-arrow-right-circle</v-icon>
              </v-btn>
            </v-card>
          </v-col>

          <!--제조사별 현황 -->
          <v-col cols="8">
            <v-card class="pa-4">
              <div class="text-center text-grey-darken-2 text-subtitle-1">제조사 별 현황</div>
              <v-divider></v-divider>
              <v-row>
                <v-col cols="6">
                  <div class="text-blue font-weight-bold">Poly</div>
                  <div class="d-flex justify-space-between">
                    <span class="text-grey">E350</span>
                    <span class="text-orange font-weight-bold">65</span>
                  </div>
                  <div class="d-flex justify-space-between">
                    <span class="text-grey">E450</span>
                    <span class="text-orange font-weight-bold">25</span>
                  </div>
                </v-col>
                <v-col cols="6">
                  <div class="text-blue font-weight-bold">Avaya</div>
                  <div class="d-flex justify-space-between">
                    <span class="text-grey">J159</span>
                    <span class="text-orange font-weight-bold">37</span>
                  </div>
                  <div class="d-flex justify-space-between">
                    <span class="text-grey">J189</span>
                    <span class="text-orange font-weight-bold">3</span>
                  </div>
                  <v-btn icon variant="text" class="mt-2">
                    <v-icon size="large" color="primary">mdi-arrow-right-circle</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
            </v-card>
          </v-col>
        </v-row>

        <v-row>
          <v-col v-for="(item, idx) in cards" :key="idx" cols="3">
            <v-card class="d-flex flex-column align-center justify-center pa-6 text-center">
              <div class="text-grey-darken-2 text-subtitle-1">{{ item.title }}</div>
              <div class="text-h3 font-weight-bold">{{ item.value }}</div>
              <v-btn icon variant="text" class="mt-4" @click="toggleDataTable(item.title)">
                <v-icon size="large" color="primary">mdi-arrow-right-circle</v-icon>
              </v-btn>
            </v-card>
          </v-col>
        </v-row>
      </template>

      <v-row v-if="showDataTable">
        <v-col cols="12">
          <v-data-table :headers="headers" :items="DeviceDatatables" item-key="deviceName"
                        class="border rounded-md text-center mt-6" hide-default-footer>
          </v-data-table>
        </v-col>
      </v-row>
  </UiParentCard>
</template>