<!-- src/views/monitoring/DailyCallStats.vue -->
<template>
  <v-card class="pa-5">
    <v-card-title class="text-h4 font-weight-bold"> 줌 라이센스 사용 현황 </v-card-title>
    <v-card-title class="text-h6 font-weight-bold"> 💡줌 시스템의 라이센스 사용 현황을 모니터링 할 수 있습니다. </v-card-title>

    <v-row class="mt-5">
      <v-col cols="6">
        <v-card-title class="text-body-1 font-weight-bold text-medium-emphasis"> 최종 업데이트 시간 : {{ lastUpdatedTime }} </v-card-title>
      </v-col>
      <v-col cols="6" class="d-flex align-center justify-end">
        <v-text-field
            type="date"
            v-model="selectedDate"
            density="compact"
            variant="outlined"
            hide-details
            class="date-picker"
            style="max-width: 200px"
        />
        <v-btn color="primary" class="ml-2" @click="getDailyLicenseStatus"> 조회 </v-btn>
      </v-col>
    </v-row>


    <!-- 라이센스 사용 현황 카드 영역 1 - Workplace -->
    <v-card class="mb-5">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>Workplace 라이센스</span>
        <v-btn
          icon
          variant="text"
          @click="toggleChartSection('workplace')"
          :color="activeChartSections.workplace ? 'primary' : ''"
        >
          <v-icon>mdi-chart-line</v-icon>
        </v-btn>
      </v-card-title>
      
      <!-- 퍼센티지 색상 설명 -->
      <v-row class="px-4 mb-2">
        <v-col cols="12">
          <div class="d-flex align-center">
            <span class="text-caption text-medium-emphasis mr-2">사용률:</span>
            <v-chip size="small" color="error" class="mr-2">30% 이하</v-chip>
            <v-chip size="small" color="warning" class="mr-2">30-50%</v-chip>
            <v-chip size="small" color="success">50% 초과</v-chip>
          </div>
        </v-col>
      </v-row>
      
      <v-row class="px-4">
        <!-- 라이센스 사용 현황 카드 1 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Total Workplace</div>
            <div class="text-h3"> 192 / 200</div>
            <div class="text-subtitle-1" :class="getPercentageColor(192, 200)">{{ calculatePercentage(192, 200) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(192, 200)"
              :color="getPercentageColor(192, 200).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 2 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Workplace Enterprise</div>
            <div class="text-h3"> 47 / 50</div>
            <div class="text-subtitle-1" :class="getPercentageColor(47, 50)">{{ calculatePercentage(47, 50) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(47, 50)"
              :color="getPercentageColor(47, 50).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 3 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Workplace Business</div>
            <div class="text-h3"> 125 / 130</div>
            <div class="text-subtitle-1" :class="getPercentageColor(125, 130)">{{ calculatePercentage(125, 130) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(125, 130)"
              :color="getPercentageColor(125, 130).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 4 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Workplace Basic</div>
            <div class="text-h3"> 20</div>
            <div class="text-subtitle-1">-</div>
            <v-progress-linear
              model-value="0"
              color="grey"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>
     
      </v-row>

      <!-- 차트 영역 -->
      <v-expand-transition>
        <div v-if="activeChartSections.workplace" class="pa-4">
          <v-card>
            <v-card-text>
              <apexchart
                type="line"
                height="350"
                :options="getWorkplaceChartOptions()"
                :series="workplaceChartSeries"
              ></apexchart>
            </v-card-text>
          </v-card>
        </div>
      </v-expand-transition>
    </v-card>

    <!-- 라이센스 사용 현황 카드 영역 2 - Phone -->
    <v-card class="mb-5">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>Zoom Phone 라이센스</span>
        <v-btn
          icon
          variant="text"
          @click="toggleChartSection('phone')"
          :color="activeChartSections.phone ? 'primary' : ''"
        >
          <v-icon>mdi-chart-line</v-icon>
        </v-btn>
      </v-card-title>
      
      <v-row class="px-4">
        <!-- 라이센스 사용 현황 카드 1 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Phone</div>
            <div class="text-h3"> 135 / 140</div>
            <div class="text-subtitle-1" :class="getPercentageColor(135, 140)">{{ calculatePercentage(135, 140) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(135, 140)"
              :color="getPercentageColor(135, 140).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 2 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Phone Pro</div>
            <div class="text-h3"> 125 / 130</div>
            <div class="text-subtitle-1" :class="getPercentageColor(125, 130)">{{ calculatePercentage(125, 130) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(125, 130)"
              :color="getPercentageColor(125, 130).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 3 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Phone Basic</div>
            <div class="text-h3">10</div>
            <div class="text-subtitle-1">-</div>
            <v-progress-linear
              model-value="0"
              color="grey"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 4 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Power Pack</div>
            <div class="text-h3"> 2 / 3</div>
            <div class="text-subtitle-1" :class="getPercentageColor(2, 3)">{{ calculatePercentage(2, 3) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(2, 3)"
              :color="getPercentageColor(2, 3).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>
      </v-row>

      <!-- 차트 영역 -->
      <v-expand-transition>
        <div v-if="activeChartSections.phone" class="pa-4">
          <v-card>
            <v-card-text>
              <apexchart
                type="line"
                height="350"
                :options="getPhoneChartOptions()"
                :series="phoneChartSeries"
              ></apexchart>
            </v-card-text>
          </v-card>
        </div>
      </v-expand-transition>
    </v-card>

    <!-- 라이센스 사용 현황 카드 영역 3 - Rooms -->
    <v-card class="mb-5">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>Rooms 라이센스</span>
        <v-btn
          icon
          variant="text"
          @click="toggleChartSection('rooms')"
          :color="activeChartSections.rooms ? 'primary' : ''"
        >
          <v-icon>mdi-chart-line</v-icon>
        </v-btn>
      </v-card-title>
      
      <v-row class="px-4">
        <!-- 라이센스 사용 현황 카드 1 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Rooms</div>
            <div class="text-h3"> 1 / 1</div>
            <div class="text-subtitle-1" :class="getPercentageColor(1, 1)">{{ calculatePercentage(1, 1) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(1, 1)"
              :color="getPercentageColor(1, 1).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>

        <!-- 라이센스 사용 현황 카드 2 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Rooms Connector</div>
            <div class="text-h3"> 0 / 1</div>
            <div class="text-subtitle-1" :class="getPercentageColor(0, 1)">{{ calculatePercentage(0, 1) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(0, 1)"
              :color="getPercentageColor(0, 1).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>
      </v-row>

      <!-- 차트 영역 -->
      <v-expand-transition>
        <div v-if="activeChartSections.rooms" class="pa-4">
          <v-card>
            <v-card-text>
              <apexchart
                type="line"
                height="350"
                :options="getRoomsChartOptions()"
                :series="roomsChartSeries"
              ></apexchart>
            </v-card-text>
          </v-card>
        </div>
      </v-expand-transition>
    </v-card>

    <!-- 라이센스 사용 현황 카드 영역 4 - Storage -->
    <v-card class="mb-5">
      <v-card-title class="d-flex justify-space-between align-center">
        <span>CloudStorage 라이센스</span>
        <v-btn
          icon
          variant="text"
          @click="toggleChartSection('storage')"
          :color="activeChartSections.storage ? 'primary' : ''"
        >
          <v-icon>mdi-chart-line</v-icon>
        </v-btn>
      </v-card-title>
      
      <v-row class="px-4">
        <!-- 라이센스 사용 현황 카드 1 -->
        <v-col cols="12" md="3">
          <v-card elevation="10" class="pa-4 text-center">
            <div class="text-h6 mb-3">Cloud Storage (GB)</div>
            <div class="text-h3"> 1.03 / 101</div>
            <div class="text-subtitle-1" :class="getPercentageColor(1.03, 101)">{{ calculatePercentage(1.03, 101) }}%</div>
            <v-progress-linear
              :model-value="calculatePercentage(1.03, 101)"
              :color="getPercentageColor(1.03, 101).replace('text-', '')"
              height="8"
              class="mt-2"
            ></v-progress-linear>
          </v-card>
        </v-col>
      </v-row>

      <!-- 차트 영역 -->
      <v-expand-transition>
        <div v-if="activeChartSections.storage" class="pa-4">
          <v-card>
            <v-card-text>
              <apexchart
                type="line"
                height="350"
                :options="getStorageChartOptions()"
                :series="storageChartSeries"
              ></apexchart>
            </v-card-text>
          </v-card>
        </div>
      </v-expand-transition>
    </v-card>
  </v-card>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive } from 'vue';
import { useTheme } from 'vuetify';
import VueApexCharts from 'vue3-apexcharts';

// 상태 관리
const theme = useTheme();
const selectedDate = ref(new Date().toISOString().slice(0, 10));
const selectedTypes = ref(['outbound', 'inbound', 'internal']);
const lastUpdatedTime = ref('2025-03-06 10:00:00');

// 퍼센티지 계산 함수
const calculatePercentage = (used: number, total: number) => {
  if (!total) return '-';
  return Math.round((used / total) * 100);
};

// 퍼센티지에 따른 색상 클래스 반환
const getPercentageColor = (used: number, total: number) => {
  if (!total) return '';
  
  const percentage = (used / total) * 100;
  
  if (percentage <= 30) {
    return 'text-error';
  } else if (percentage <= 50) {
    return 'text-warning';
  } else {
    return 'text-success';
  }
};

// 차트 섹션 토글 상태 관리
const activeChartSections = reactive({
  workplace: false,
  phone: false,
  rooms: false,
  storage: false
});

// 차트 섹션 토글 함수
const toggleChartSection = (sectionId: 'workplace' | 'phone' | 'rooms' | 'storage') => {
  activeChartSections[sectionId] = !activeChartSections[sectionId];
};

// 날짜 데이터 생성 (최근 30일)
const generateDates = () => {
  const dates = [];
  const today = new Date();
  for (let i = 29; i >= 0; i--) {
    const date = new Date();
    date.setDate(today.getDate() - i);
    dates.push(date.getTime());
  }
  return dates;
};

const dates = generateDates();
const categories = dates.map(date => {
  const d = new Date(date);
  return `${d.getMonth() + 1}/${d.getDate()}`;
});

// Workplace 차트 시리즈
const workplaceChartSeries = [
  {
    name: 'Total Workplace',
    data: Array.from({ length: 30 }, () => Math.floor(170 + Math.random() * 30))
  },
  {
    name: 'Workplace Enterprise',
    data: Array.from({ length: 30 }, () => Math.floor(40 + Math.random() * 10))
  },
  {
    name: 'Workplace Business',
    data: Array.from({ length: 30 }, () => Math.floor(110 + Math.random() * 20))
  },
  {
    name: 'Workplace Basic',
    data: Array.from({ length: 30 }, () => Math.floor(15 + Math.random() * 5))
  }
];

// Phone 차트 시리즈
const phoneChartSeries = [
  {
    name: 'Phone',
    data: Array.from({ length: 30 }, () => Math.floor(125 + Math.random() * 15))
  },
  {
    name: 'Phone Pro',
    data: Array.from({ length: 30 }, () => Math.floor(115 + Math.random() * 15))
  },
  {
    name: 'Phone Basic',
    data: Array.from({ length: 30 }, () => Math.floor(8 + Math.random() * 3))
  },
  {
    name: 'Power Pack',
    data: Array.from({ length: 30 }, () => Math.floor(1 + Math.random() * 2))
  }
];

// Rooms 차트 시리즈
const roomsChartSeries = [
  {
    name: 'Rooms',
    data: Array.from({ length: 30 }, () => 1)
  },
  {
    name: 'Rooms Connector',
    data: Array.from({ length: 30 }, () => Math.random() > 0.7 ? 1 : 0)
  }
];

// Storage 차트 시리즈
const storageChartSeries = [
  {
    name: 'Cloud Storage (GB)',
    data: Array.from({ length: 30 }, () => parseFloat((0.8 + Math.random() * 0.5).toFixed(2)))
  }
];

// Workplace 차트 옵션
const getWorkplaceChartOptions = () => {
  return {
    chart: {
      height: 350,
      type: 'line',
      fontFamily: 'inherit',
      zoom: {
        type: 'x',
        enabled: true
      },
      toolbar: {
        show: false
      },
      shadow: {
        enabled: true,
        color: '#000',
        top: 18,
        left: 7,
        blur: 10,
        opacity: 0.2
      }
    },
    colors: [
      theme.current.value.colors.primary,
      theme.current.value.colors.error,
      theme.current.value.colors.success,
      theme.current.value.colors.warning
    ],
    markers: {
      size: 4
    },
    xaxis: {
      categories: categories,
      title: {
        text: '날짜'
      }
    },
    yaxis: {
      title: {
        text: '사용량'
      }
    },
    grid: {
      show: true,
      borderColor: 'rgba(0,0,0,0.1)'
    },
    dataLabels: {
      enabled: false
    },
    stroke: {
      curve: 'smooth',
      width: 2
    },
    legend: {
      position: 'top',
      horizontalAlign: 'right',
      floating: true,
      offsetY: -25,
      offsetX: -5
    },
    tooltip: {
      theme: 'light',
      x: {
        format: 'MM/dd'
      }
    },
    title: {
      text: 'Workplace 라이센스 사용 추이',
      align: 'left',
      style: {
        fontSize: '16px',
        fontWeight: 'bold'
      }
    }
  };
};

// Phone 차트 옵션
const getPhoneChartOptions = () => {
  return {
    ...getWorkplaceChartOptions(),
    colors: [
      theme.current.value.colors.error,
      theme.current.value.colors.info,
      theme.current.value.colors.success,
      theme.current.value.colors.warning
    ],
    title: {
      text: 'Phone 라이센스 사용 추이',
      align: 'left',
      style: {
        fontSize: '16px',
        fontWeight: 'bold'
      }
    }
  };
};

// Rooms 차트 옵션
const getRoomsChartOptions = () => {
  return {
    ...getWorkplaceChartOptions(),
    colors: [
      theme.current.value.colors.success,
      theme.current.value.colors.info
    ],
    title: {
      text: 'Rooms 라이센스 사용 추이',
      align: 'left',
      style: {
        fontSize: '16px',
        fontWeight: 'bold'
      }
    }
  };
};

// Storage 차트 옵션
const getStorageChartOptions = () => {
  return {
    ...getWorkplaceChartOptions(),
    colors: [
      theme.current.value.colors.primary
    ],
    title: {
      text: 'Cloud Storage 사용 추이',
      align: 'left',
      style: {
        fontSize: '16px',
        fontWeight: 'bold'
      }
    }
  };
};

// 데이터 조회 함수
const fetchData = () => {
  // API 호출 로직 구현
  console.log('데이터 조회:', selectedDate.value);
};

const getDailyLicenseStatus = () => {
  // API 호출 로직 구현
  console.log('데이터 조회:', selectedDate.value);
  window.alert("@@@ 조회 버튼은 아직 API 연동이 안되었습니다.")
}

onMounted(() => {
  fetchData();
});
</script>

<style scoped>
.v-card {
  transition: all 0.3s ease;
}
</style>
