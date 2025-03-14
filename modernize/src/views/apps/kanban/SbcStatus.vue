<script setup lang="ts">

import { ref } from "vue";
import SystemView from './SystemView.vue';
import CallView from './CallView.vue';
import AlarmView from './AlarmView.vue';

// 모듈 데이터
const selectedTab = ref("tab1");

const tab = ref("system");

const tabs = ref([
  { value: 'system', icon: 'mdi-monitor', label: 'ArisysSBC  #1 본사에 설치되어 있는 SBC'}
]);

</script>

<style scoped>
/* 카드 제목 스타일 */
.v-card-title {
  font-weight: bold;
  text-align: center;
}

</style>

<template>

  <v-card class="pa-1">
    <v-card-title class="text-h4 font-weight-bold d-flex align-left"> SBC 현황 </v-card-title>
    <v-card-title class="text-h6 font-weight-bold d-flex align-left">줌 폰과 연동되어 있는 SBC 장비를 모니터링 할 수 있습니다.</v-card-title>

  </v-card>
  <br><br>
  <v-container>
    <v-tabs v-model="selectedTab">
      <v-tab v-for="tab in tabs" :key="tab.value" :value="tab.value">
        <v-icon size="40">{{tab.icon}}</v-icon>

      </v-tab>


      <v-window v-model="selectedTab">
        <v-window-item v-for="tab in tabs" :key="tab.value" :value="tab.value" class="d-flex justify-center">
          <p><b>Arisys SBC #1</b></p>
        </v-window-item>
      </v-window>
    </v-tabs>
  </v-container>
  <br>

  <v-container>
    <!-- 탭 목록 -->
    <v-tabs v-model="selectedTab">
      <v-tab value="system">
        시스템 현황
      </v-tab>
      <v-tab value="call">
        콜 현황
      </v-tab>
      <v-tab value="alarm">
        알람 내역
      </v-tab>
    </v-tabs>

    <!-- 탭 내용 표시 -->
    <v-window v-model="selectedTab">
      <v-window-item value="system">
        <SystemView />
      </v-window-item>
      <v-window-item value="call">
        <CallView />
      </v-window-item>
      <v-window-item value="alarm">
        <AlarmView />
      </v-window-item>
    </v-window>
  </v-container>

</template>