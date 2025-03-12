<template>
    <v-card class="pa-5">
      <v-card-title class="text-h4 font-weight-bold">Extension 번호 관리</v-card-title>
      <v-card-title class="text-h6 font-weight-bold">
        💡 줌 폰에서 사용하는 Extension 번호를 관리 할 수 있습니다. Extension 번호에는 사용자 내선번호, 담겨받기, Shared Group Line, Call Queue 등이 있습니다.
      </v-card-title>
  
      <!-- 검색 영역 -->
      <v-row class="mt-5">
        <v-col cols="12" md="4">
          <div class="d-flex align-center">
            <div class="mr-2">Extension 번호</div>
            <v-text-field
              v-model="searchNumber"
              density="compact"
              variant="outlined"
              hide-details
              append-inner-icon="mdi-magnify"
              class="mx-2"
            ></v-text-field>
          </div>
        </v-col>
        <v-col cols="12" md="4">
          <div class="d-flex align-center">
            <div class="mr-2">할당</div>
            <v-select
              v-model="selectedAssignment"
              :items="assignmentTypes"
              density="compact"
              variant="outlined"
              hide-details
              class="mx-2"
            ></v-select>
          </div>
        </v-col>
        <v-col cols="12" md="4" class="d-flex justify-end">
          <v-btn color="primary" @click="fetchData">조회</v-btn>
          <v-btn class="ml-2" variant="outlined" @click="downloadExcel">엑셀 다운로드</v-btn>
        </v-col>
      </v-row>
  
      <!-- 데이터 테이블 -->
      <v-table class="mt-5">
        <thead>
          <tr>
            <th class="text-center bg-primary text-white">Extension 번호</th>
            <th class="text-center bg-primary text-white">표시이름</th>
            <th class="text-center bg-primary text-white">할당</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in extensionList" :key="item.number">
            <td class="text-center">{{ item.number }}</td>
            <td class="text-center">{{ item.provision }}</td>
            <td class="text-center">{{ item.assignment }}</td>
          </tr>
        </tbody>
      </v-table>
  
      <!-- 페이지네이션 -->
      <div class="d-flex align-center mt-4">
        <div>
          페이지당 항목 수
          <v-select
            v-model="itemsPerPage"
            :items="[15, 30, 50]"
            density="compact"
            variant="outlined"
            hide-details
            style="width: 80px"
            class="mx-2"
          ></v-select>
        </div>
        <v-spacer></v-spacer>
        <v-pagination
          v-model="page"
          :length="totalPages"
          :total-visible="7"
        ></v-pagination>
      </div>
  
 
    </v-card>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted } from 'vue';
  
  // 상태 관리
  const searchNumber = ref('');
  const selectedAssignment = ref('');
  const assignmentTypes = ['전체', 'Phone User', 'Group Call Pickup', 'Auto Receptionist', 'Call Queue', 'Paging', 'Shared Line Group'];
  const itemsPerPage = ref(15);
  const page = ref(1);
  const extensionList = ref([
    { number: '308152', provision: '박OO', assignment: 'Phone User' },
    { number: '308201', provision: '기술지원팀', assignment: 'Group Call Pickup' },
    { number: '308305', provision: '아리시스 대표번호', assignment: 'Auto Receptionist' },
    { number: '308403', provision: '고객지원부서 Queue', assignment: 'Call Queue' },
    { number: '308561', provision: '기술지원 1팀', assignment: 'Paging' },
    { number: '308612', provision: '경영지원팀', assignment: 'Shared Line Group' },
  ]);
  
  // 계산된 속성
  const totalPages = computed(() => {
    return Math.ceil(extensionList.value.length / itemsPerPage.value);
  });
  
  // 데이터 조회 함수
  const fetchData = () => {
    // API 호출 로직 구현
    console.log('데이터 조회:', {
      number: searchNumber.value,
      assignment: selectedAssignment.value,
      page: page.value,
      itemsPerPage: itemsPerPage.value
    });
  };
  
  // 엑셀 다운로드 함수
  const downloadExcel = () => {
    console.log('엑셀 다운로드');
    // 엑셀 다운로드 로직 구현
  };
  
  onMounted(() => {
    fetchData();
  });
  </script>