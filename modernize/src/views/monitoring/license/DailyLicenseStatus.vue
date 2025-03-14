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
        <v-btn color="primary" class="ml-2" @click="fetchData"> 조회 </v-btn>
      </v-col>
    </v-row>

    <!-- 라이센스 사용 현황 타이틀 -->
    <v-row>
      <v-col cols="12" class="align-center">
        <div class="text-h6">줌 라이센스 사용 현황</div>
      </v-col>
    </v-row>

    <!-- 라이센스 사용 현황 카드 영역 1 -->
    <v-row>

      <!-- 라이센스 사용 현황 카드 1 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">Total Workplace</div>
          <div class="text-h3 "> 192 / 200</div>
        </v-card>
      </v-col>

      <!-- 라이센스 사용 현황 카드 2 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">Total Workplace</div>
          <div class="text-h3 "> 47 / 50</div>
        </v-card>
      </v-col>

      <!-- 라이센스 사용 현황 카드 3 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">Total Workplace</div>
          <div class="text-h3 "> 125 / 130</div>
        </v-card>
      </v-col>


      <!-- 라이센스 사용 현황 카드 4 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">Total Workplace</div>
          <div class="text-h3 "> 20</div>
        </v-card>
      </v-col>

    </v-row>

    <!-- 라이센스 사용 현황 카드 영역 2 -->
    <v-row>

      <!-- 라이센스 사용 현황 카드 2 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">Phone</div>
          <div class="text-h3 "> 135 / 140</div>
        </v-card>
      </v-col>

      <!-- 라이센스 사용 현황 카드 2 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">phone Pro</div>
          <div class="text-h3 "> 125 / 130</div>
        </v-card>
      </v-col>

      <!-- 라이센스 사용 현황 카드 3 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">phone Basic</div>
          <div class="text-h3 ">10</div>
        </v-card>
      </v-col>


      <!-- 라이센스 사용 현황 카드 4 -->
      <v-col cols="12" md="3">
        <v-card elevation="10" class="pa-4 text-center">
          <div class="text-h6 mb-3">Power Pack</div>
          <div class="text-h3 "> 2/3</div>
        </v-card>
      </v-col>

    </v-row>


  </v-card>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useTheme } from 'vuetify';

// 상태 관리
const theme = useTheme();
const selectedDate = ref(new Date().toISOString().slice(0, 10));
const selectedTypes = ref(['outbound', 'inbound', 'internal']);
// const totalCalls = ref(684);
// const outboundCalls = ref(471);
// const inboundCalls = ref(213);
const lastUpdatedTime = ref('2025-03-06 10:00:00');

// 바 차트 데이터
const timeLabels = ['06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00'];

/* 바 차트트 목 데이터 */
const chartData = {
  outbound: [2, 5, 22, 38, 45, 45, 25, 3, 28],
  inbound: [6, 23, 48, 63, 58, 42, 5, 8, 42],
  internal: [3, 17, 35, 40, 38, 18, 5, 6, 46]
};

// 바 차트 시리즈
const chartSeries = computed(() => {
  const series = [];

  if (selectedTypes.value.includes('outbound')) {
    series.push({
      name: '아웃바운드',
      data: chartData.outbound
    });
  }

  if (selectedTypes.value.includes('inbound')) {
    series.push({
      name: '인바운드',
      data: chartData.inbound
    });
  }

  if (selectedTypes.value.includes('internal')) {
    series.push({
      name: '내선통화',
      data: chartData.internal
    });
  }

  return series;
});
/* 도넛 차트 옵션 */
const donutchartOptions = computed(() => {
  return {
    chart: {
      type: 'donut',
      height: 300,
      fontFamily: `inherit`,
      foreColor: '#adb0bb'
    },
    labels: ['아웃바운드', '인바운드', '내선통화'],
    dataLabels: {
      enabled: true
    },
    plotOptions: {
      pie: {
        donut: {
          size: '50px'
        }
      }
    },
    stroke: {
      width: 2,
      colors: 'rgba(var(--v-theme-surface))'
    },
    legend: {
      show: true,
      position: 'bottom',
      width: '50px'
    },
    colors: [theme.current.value.colors.success, theme.current.value.colors.primary, theme.current.value.colors.lighterror],
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: {
            width: 200
          },
          legend: {
            position: 'bottom'
          }
        }
      }
    ]
  };
});
/* 도넛 차트 목데이터터 */
const donutChart = {
  series: [44, 55, 41]
};

// 차트 옵션 (기존 chartOptions를 barChartOptions로 변경)
const barChartOptions = computed(() => ({
  chart: {
    type: 'bar',
    toolbar: {
      show: false
    }
  },
  plotOptions: {
    bar: {
      horizontal: false,
      columnWidth: '55%',
      endingShape: 'rounded'
    }
  },
  dataLabels: {
    enabled: false
  },
  stroke: {
    show: true,
    width: 2,
    colors: ['transparent']
  },
  colors: [theme.current.value.colors.success, theme.current.value.colors.primary, theme.current.value.colors.lighterror],
  xaxis: {
    categories: timeLabels.map((time, index) => {
      if (index < timeLabels.length - 1) {
        return `${time} ~ ${timeLabels[index + 1]}`;
      }
      return `${time} ~ 15:00`;
    }),
    labels: {
      style: {
        fontSize: '12px'
      }
    }
  },
  yaxis: {
    title: {
      text: '통화 건수'
    }
  },
  fill: {
    opacity: 1
  },
  tooltip: {
    y: {
      formatter: function (val: number) {
        return val + ' 건';
      }
    }
  },
  grid: {
    borderColor: 'rgba(0,0,0,0.1)'
  }
}));

// 데이터 조회 함수
const fetchData = () => {
  // API 호출 로직 구현
  console.log('데이터 조회:', selectedDate.value);
};

onMounted(() => {
  fetchData();
});
</script>
