<script setup lang="ts">

import { ref, onMounted } from "vue";
import Chart from "chart.js/auto";

const selectedTab = ref("tab1");

const tab = ref("system");

const tabs = ref([
  { value: 'system', icon: 'mdi-monitor', label: 'ArisysSBC  #1 본사에 설치되어 있는 SBC'}
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

          <!-- CPU & Memory Load 그래프 -->
          <v-col cols="10">
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

      </v-window-item>
    </v-window>
  </v-container>

</template>