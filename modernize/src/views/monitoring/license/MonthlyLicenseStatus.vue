<!-- src/views/monitoring/license/MonthlyLicenseStatus.vue -->
<template>
  <v-card class="pa-5">
    <v-card-title class="text-h4 font-weight-bold"> 줌 라이센스 사용 현황 </v-card-title>
    <v-card-title class="text-h6 font-weight-bold"> 💡줌 시스템의 라이센스 사용 현황을 모니터링 할 수 있습니다. </v-card-title>

    <v-row class="mt-5">
      <v-col cols="6">
        <v-card-title class="text-body-1 font-weight-bold text-medium-emphasis"> 최종 업데이트 시간 : {{ lastUpdatedTime }} </v-card-title>
      </v-col>
      <v-col cols="6" class="d-flex align-center justify-end">
        <v-select
            v-model="selectedOption"
            :items="options"
            label="제품 선택"
            density="compact"
            variant="outlined"
            hide-details
            class="mr-2"
            style="max-width: 200px"
        ></v-select>
        <v-text-field
            type="date"
            v-model="startDate"
            density="compact"
            variant="outlined"
            hide-details
            class="date-picker mr-2"
            style="max-width: 200px"
            label="조회 시작일"
        ></v-text-field>
        <v-text-field
            type="date"
            v-model="endDate"
            density="compact"
            variant="outlined"
            hide-details
            class="date-picker"
            style="max-width: 200px"
            label="조회 종료일"
        ></v-text-field>
        <v-btn color="primary" class="ml-2" @click="getLicenseData"> 조회 </v-btn>
      </v-col>
    </v-row>

    <!-- 라이센스 사용 현황 타이틀 -->
    <v-row>
      <v-col cols="12">
        <div class="text-h6 mb-4">Workplace 라이센스 사용 현황 (조회기간 2024.01 ~ 2024.12)</div>
      </v-col>
    </v-row>

    <!-- 라이센스 사용 현황 테이블 -->
    <v-table class="license-table">
      <thead>
      <tr>
        <th class="text-center">년도</th>
        <th class="text-center">월</th>
        <th class="text-center">Enterprise</th>
        <th class="text-center">Business</th>
        <th class="text-center">Basic</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item, index) in licenseData" :key="index" :class="{ 'grey lighten-4': index % 2 === 0 }">
        <td class="text-center">{{ item.year }}</td>
        <td class="text-center">{{ item.month }}</td>
        <td class="text-center">{{ item.enterprise }}</td>
        <td class="text-center">{{ item.business }}</td>
        <td class="text-center">{{ item.basic }}</td>
      </tr>
      </tbody>
    </v-table>


  </v-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useTheme } from 'vuetify';

// 상태 관리
const theme = useTheme();
const startDate = ref(new Date(new Date().getFullYear(), 0, 1).toISOString().slice(0, 10)); // 현재 년도의 1월 1일
const endDate = ref(new Date().toISOString().slice(0, 10)); // 오늘 날짜
const lastUpdatedTime = ref('2025.01.24 10:00:00');
const selectedOption = ref('Workplace');
const options = ref(['Workplace', 'Phone', 'Rooms', 'Webinar']);

// 라이센스 데이터
const licenseData = ref([
  { year: 2024, month: '01', enterprise: '46/50 (2024.01.13)', business: '120/130 (2024.01.19)', basic: '15 (2024.01.22)' },
  { year: 2024, month: '02', enterprise: '48/50 (2024.02.03)', business: '123/130 (2024.02.13)', basic: '13 (2024.02.08)' },
  { year: 2024, month: '03', enterprise: '48/50 (2024.03.23)', business: '125/130 (2024.03.21)', basic: '17 (2024.03.07)' },
  { year: 2024, month: '04', enterprise: '50/50 (2024.04.07)', business: '121/130 (2024.04.28)', basic: '20 (2024.04.28)' },
  { year: 2024, month: '05', enterprise: '49/50 (2024.05.15)', business: '126/130 (2024.05.22)', basic: '18 (2024.05.10)' },
  { year: 2024, month: '06', enterprise: '50/50 (2024.06.18)', business: '128/130 (2024.06.25)', basic: '22 (2024.06.14)' },
  { year: 2024, month: '07', enterprise: '47/50 (2024.07.05)', business: '124/130 (2024.07.11)', basic: '19 (2024.07.19)' },
  { year: 2024, month: '08', enterprise: '48/50 (2024.08.22)', business: '127/130 (2024.08.30)', basic: '21 (2024.08.15)' }
]);

// 데이터 조회 함수
const fetchData = () => {
  // API 호출 로직 구현
  console.log('데이터 조회:', startDate.value, endDate.value, selectedOption.value);
  // 여기서 실제 API 호출 후 licenseData 를 업데이트
};
onMounted(() => {
  fetchData();
});

const getLicenseData = () => {
  // API 호출 로직 구현
  console.log('데이터 조회:', startDate.value, endDate.value, selectedOption.value);
  // 여기서 실제 API 호출 후 licenseData 를 업데이트
  window.alert('@@@ 데이터 조회 기능 버튼 구현 예정입니다.');
};
</script>

<style scoped>
.license-table {
  border: 1px solid #e0e0e0;
}

.license-info li {
  margin-bottom: 8px;
  list-style-type: none;
}

.license-info li::before {
  content: "•";
  color: #1976d2;
  font-weight: bold;
  display: inline-block;
  width: 1em;
  margin-left: -1em;
}
</style>
