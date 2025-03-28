<script setup lang="ts">

import {onMounted, ref} from 'vue';
import { DeviceDatatables } from '@/_mockApis/components/datatable/DeviceTable';

const selectedDate = ref(new Date().toISOString().substr(0, 10));

const cards = ref([
  { title: "온라인 수량", value: 119 },
  { title: "오프라인 수량", value: 11 },
  { title: "사용자 할당", value: 117 },
  { title: "사용자 미할당", value: 13 }
]);

const headers = ref([
  { title: "표시이름", key: "deviceName" },
  { title: "사용자", key: "user" },
  { title: "내선번호", key: "extensionNumber" },
  { title: "PSTN 번호", key: "pstnNumber" },
  { title: "상태", key: "status" },
  { title: "모델", key: "model" },
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


const toggleDataTable = () => {
  showDataTable.value = !showDataTable.value;
}

// onMounted(()=>{
//   fetchData();
// });
</script>

<template>

  <v-card class="pa-1">
    <v-card-title class="text-h4 font-weight-bold"> 전체 디바이스 현황 </v-card-title>
    <v-card-title class="text-h6 font-weight-bold">줌 폰 시스템의 전체 디바이스 현황을 모니터링 할 수 있습니다.</v-card-title>


    <v-row class="mt-5">
      <v-col cols="6">
        <v-card-title class="text-body-1 font-weight-bold text-medium-emphasis"> 최종 업데이트 시간 : {{ selectedDate }} </v-card-title>
      </v-col>
      <v-col cols="6" class="d-flex align-center justify-end">
        <v-btn color="primary" class="ml-2" @click=fetchData> 조회 </v-btn>
      </v-col>
    </v-row>

    <template v-if="showDashboard">
      <v-row>
        <v-col cols="12" align="center">
          <div class="text-h5 " style="background-color: pink; height: 50px; display: flex; justify-content: center; align-items: center" >전체 디바이스 현황</div>
        </v-col>
      </v-row>
      <br>

      <!-- 전체 전화기 등록 수량-->
      <v-container>
        <v-row>
          <v-col cols="4">
            <v-card class="d-flex flex-column align-center justify-center pa-6 text-center" style="background-color: beige">
              <div class="text-grey-darken-2 text-subtitle1">전체 전화기 등록 수량</div>
              <div class="text-h3 font-weight-bold">130</div>
              <v-btn icon size = "large" variant="text" class="mt-2">
                <v-icon size = "large" color="primary">mdi-arrow-right-circle</v-icon>
              </v-btn>
            </v-card>
          </v-col>

          <!-- 제조사 별 현황 -->
          <v-col cols="8">
            <v-card class="pa-4" style="background-color: beige">
              <div class="text-center text-grey-darken-2 text-subtitle1">제조사 별 현황</div>
              <v-divider class="my-4"></v-divider>
              <v-row>
                <!-- Poly -->
                <v-col cols="5" >
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

                <!-- Avaya -->
                <v-col cols="5">
                  <div class="text-blue font-weight-bold">Avaya</div>
                  <div class="d-flex justify-space-between">
                    <span class="text-grey">J159</span>
                    <span class="text-orange font-weight-bold">37</span>
                  </div>
                  <div class="d-flex justify-space-between">
                    <span class="text-grey">J189</span>
                    <span class="text-orange font-weight-bold">3</span>
                  </div>
                </v-col>
              </v-row>
              <v-btn icon variant="text" class="position-absolute bottom-0 right-0 ma-2">
                <v-icon size = "large" color="primary">mdi-arrow-right-circle</v-icon>
              </v-btn>
            </v-card>
          </v-col>
        </v-row>
      </v-container>

      <!-- 온라인/오프라인/사용자 할당,미할당 수량-->
      <v-container>
        <v-row>
          <v-col v-for="(item, index) in cards" :key="index" cols="3" >
            <v-card class="d-flex flex-column align-center justify-center pa-6 text-center" style="background-color: beige">
              <div class="text-grey-darken-2 text-subtitle1">{{ item.title }}</div>
              <div class="text-h3 font-weight-bold">{{ item.value }}</div>
              <v-btn icon variant="text" class="position-absolute bottom-0, right-0 ma-2" @click="item.title === '오프라인 수량' ? toggleDataTable() : null" >
                <v-icon size="large" color="primary">mdi-arrow-right-circle</v-icon>
              </v-btn>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </template>

    <v-container v-if="showDataTable">
      <v-row>
        <v-col cols="12">
          <v-data-table class="border rounded-md text-center light scrollable-card"
                        v-model="selectedItem"
                        :headers="headers" :items="DeviceDatatables"
                        item-key="deviceName"
                        show-select
                        return-object
                        hide-default-footer>
            <template v-slot:bottom>
              <div class="text-center pt-2 mb-3 px-3">
                <v-text-field :model-value="itemsPerPage" class="pa-2 mr-auto" label="Items per page" type="number"
                              min="-1" max="15" hide-details style="max-width: 150px;"
                              @update:model-value="itemsPerPage = parseInt($event, 10)"></v-text-field>
                <v-pagination v-model="pagination"></v-pagination>
              </div>
            </template>
          </v-data-table>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>
