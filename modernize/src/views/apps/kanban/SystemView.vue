<script setup lang="ts">

import { ref, onMounted } from "vue";
import {Chart} from "chart.js";

const selectedTab = ref("tab1");

const tab = ref("system");

const tabs = ref([
  { value: 'system', icon: 'mdi-monitor', label: 'ArisysSBC  #1 본사에 설치되어 있는 SBC'}
]);
// 모듈 데이터
const modules = ref([
  { location: "Main Board", type: "Main Board", moduleStatus: "Up", serviceStatus: "Up" },
  { location: "DSP Module #1", type: "MSPD C349 DSP", moduleStatus: "Up", serviceStatus: "Up" },
  { location: "DSP Module #2", type: "MSPD C349 DSP", moduleStatus: "Up", serviceStatus: "Up" },
  { location: "Telco Module #1", type: "DS1 PRI Port #1", moduleStatus: "Up", serviceStatus: "Up" },
  { location: "Telco Module #2", type: "DS1 PRI Port #2", moduleStatus: "Up", serviceStatus: "Down" },
  { location: "Telco Module #3", type: "FXS Port #1", moduleStatus: "Up", serviceStatus: "Up" },
  { location: "Telco Module #4", type: "FXS Port #2", moduleStatus: "Up", serviceStatus: "Down" }
]);

// 테이블 헤더
const headers = ref([
  { title: "Location", key: "location" },
  { title: "Type", key: "type" },
  { title: "Module Status", key: "moduleStatus" },
  { title: "Service Status", key: "serviceStatus" }
]);

const cpuChart = ref(null);
const memoryChart = ref(null);


// Chart.js 그래프 생성
onMounted(() => {
  if(!cpuChart.value) return ;

  new Chart(cpuChart.value, {
    type: "line",
    data: {
      labels: ["0:00", "2:00", "4:00", "6:00", "8:00", "10:00", "12:00"],
      datasets: [
        {
          label: "CPU Load(%)",
          data: [15, 13, 12, 14, 16, 18, 17],
          borderColor: "lightgreen",
          fill: false
        }
      ]
    },
    options:{
      responsive : false,
      maintainAspectRatio: false,
    }
  });
  if(!memoryChart.value) return;
  new Chart(memoryChart.value, {
    type: "line",
    data: {
      labels: ["0:00", "2:00", "4:00", "6:00", "8:00", "10:00", "12:00"],
      datasets: [
        {
          label: "Memory Load(%)",
          data: [35, 34, 33, 32, 36, 38, 37],
          borderColor: "blue",
          fill: false
        }
      ]
    },
    options:{
      responsive : false,
      maintainAspectRatio : false,
    }
  });
});

</script>

<style scoped>
/* 카드 제목 스타일 */
.v-card-title {
  font-weight: bold;
  text-align: center;
}

</style>

<template>

  <v-container>
    <v-tabs v-model="selectedTab">
      <v-tab v-for="tab in tabs" :key="tab.value" :value="tab.value">
        <v-icon size="30">{{tab.icon}}</v-icon>
        {{tab.label}}
      </v-tab>
    </v-tabs>

    <v-window v-model="selectedTab">
      <v-window-item v-for="tab in tabs" :key="tab.value" :value="tab.value">
      </v-window-item>
    </v-window>
  </v-container>
  <v-container fluid>
    <!-- 네비게이션 탭 -->
    <v-tabs v-model="tab" class="bg-grey-darken-3 text-white">
      <v-tab value="system">시스템 현황</v-tab>
      <v-tab value="call">콜 현황</v-tab>
      <v-tab value="alarm">알람 내역</v-tab>
    </v-tabs>

    <v-window v-model="tab">
      <v-window-item value="system">
        <v-row>
          <!-- 시스템 정보 카드 -->
          <v-col cols="4">
            <v-card class="pa-8">
              <v-card-title class="text-h4">시스템 현황</v-card-title>
              <v-card-text>
                <p class="text-h6"><strong>Name:</strong> ArisysSBC #1</p>
                <p class="text-h6"><strong>IP:</strong> 192.168.1.90</p>
                <p class="text-h6"><strong>Model:</strong> Ribbon SBC 1000</p>
                <p class="text-h6"><strong>Software Version:</strong> 12.1.1</p>
                <p class="text-h6"><strong>Serial Number:</strong> S4093126**********</p>
              </v-card-text>
            </v-card>
          </v-col>

          <!-- CPU & Memory Load 그래프 -->
          <v-col cols="8">
            <v-row>
              <v-col cols="6">
                <v-card>
                  <v-card-title>CPU Load (%)</v-card-title>
                  <v-card-text>
                    <canvas ref="cpuChart"></canvas>
                  </v-card-text>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card>
                  <v-card-title>Memory Load (%)</v-card-title>
                  <v-card-text>
                    <canvas ref="memoryChart"></canvas>
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </v-col>
        </v-row>

        <!-- 모듈 상태 테이블 -->
        <v-card class="mt-5">
          <v-data-table :headers="headers" :items="modules" density="compact">
            <template v-slot:item.moduleStatus="{ item }">
              <v-chip :color="item.moduleStatus === 'Up' ? 'lightgreen' : 'red'" dark>
                {{ item.moduleStatus === 'Up' ? '⇧ Up' : '⇩ Down' }}
              </v-chip>
            </template>
            <template v-slot:item.serviceStatus="{ item }">
              <v-chip :color="item.serviceStatus === 'Up' ? 'lightgreen' : 'red'" dark>
                {{ item.serviceStatus === 'Up' ? '⇧ Up' : '⇩ Down' }}
              </v-chip>
            </template>


          </v-data-table>
        </v-card>
      </v-window-item>
    </v-window>
  </v-container>

</template>