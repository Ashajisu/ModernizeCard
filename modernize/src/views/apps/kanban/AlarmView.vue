<script setup lang="ts">

import { ref, onMounted } from "vue";

const selectedTab = ref("tab1");

const tab = ref("system");

const tabs = ref([
  { value: 'system', icon: 'mdi-monitor', label: 'ArisysSBC  #1 본사에 설치되어 있는 SBC'}
]);
// 모듈 데이터
const modules = ref([
  { status: "발생", alarmGrade: "Critical", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131074", alarmExplanation:"The Card will remain out-of-service."},
  { status: "발생", alarmGrade: "Minor", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131076", alarmExplanation:"The SBC Edge has lost Power Supply redun" },
  { status: "발생", alarmGrade: "Warning", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131078", alarmExplanation:"Check the System tab to find the Fan ID of failed" },
  { status: "발생", alarmGrade: "Warning", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131077", alarmExplanation:"The Card will remain out-of-service." },
  { status: "발생", alarmGrade: "Critical", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131074", alarmExplanation:"Check the System tab to find the Fan ID of failed" },
  { status: "발생", alarmGrade: "Minor", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131076" , alarmExplanation:"Check the System tab to find the Fan ID of failed"},
  { status: "발생", alarmGrade: "Warning", deviceName: "ArisysSBC #1", occurrenceTime: "2025/01/15 09:38:23", alarmCode:"1.3.6.1.4.1.177.15.2.131079", alarmExplanation:"The SBC Edge has lost Power Supply redun" }
]);

// 테이블 헤더
const headers = ref([
  { title: "상태", key: "status" },
  { title: "알림 등급", key: "alarmGrade" },
  { title: "장비명", key: "deviceName" },
  { title: "발생시간", key: "occurrenceTime" },
  { title: "알람 코드", key: "alarmCode" },
  { title: "알람 설명", key: "alarmExplanation" }
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

        <!-- 모듈 상태 테이블 -->
        <v-card class="mt-5">
          <v-data-table :headers="headers" :items="modules" density="compact">
            <template v-slot:item.moduleStatus="{ item }">
            </template>
            <template v-slot:item.serviceStatus="{ item }">
            </template>
          </v-data-table>
        </v-card>
      </v-window-item>
    </v-window>
  </v-container>

</template>