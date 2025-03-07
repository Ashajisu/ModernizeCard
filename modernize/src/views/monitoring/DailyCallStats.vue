<!-- src/views/monitoring/DailyCallStats.vue -->
<template>
    <v-card class="pa-5">
        <v-card-title class="text-h4 font-weight-bold"> 일일 누적 통화 건수 </v-card-title>
        <v-card-title class="text-h6 font-weight-bold"> 💡줌 폰 시스템의 실시간 전화 사용량을 모니터링 할 수 있습니다. </v-card-title>

        <!-- 날짜 선택 -->
        <div class="d-flex justify-end mb-4">
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
        </div>

        <v-row>
            <v-col cols="12" align="center">
                <div class="text-h6">일일 누적 통화 건수</div>
            </v-col>
        </v-row>

        <v-row>
            <!-- 통계 카드 1 -->
            <v-col cols="12" md="3">
                <v-card elevation="10" class="pa-4">
                    <div class="d-flex justify-space-between">
                        <div>
                            <div class="text-subtitle-1 text-grey">전체 통화 콜 수</div>
                            <div class="text-h3 font-weight-bold">{{ totalCalls }}</div>
                        </div>
                        <v-avatar color="primary" size="56" class="elevation-6">
                            <v-icon color="white">mdi-phone</v-icon>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>

            <!-- 통계 카드 2 -->
            <v-col cols="12" md="3">
                <v-card elevation="10" class="pa-4">
                    <div class="d-flex justify-space-between">
                        <div>
                            <div class="text-subtitle-1 text-grey">외부 통화</div>
                            <div class="text-h3 font-weight-bold">{{ outboundCalls }}</div>
                            <div class="d-flex mt-2">
                                <div class="mr-4">
                                    <div class="text-caption text-grey">아웃바운드</div>
                                    <div class="text-subtitle-1 font-weight-bold">163</div>
                                </div>
                                <div>
                                    <div class="text-caption text-grey">인바운드</div>
                                    <div class="text-subtitle-1 font-weight-bold">308</div>
                                </div>
                            </div>
                        </div>
                        <v-avatar color="info" size="56" class="elevation-6">
                            <v-icon color="white">mdi-phone-outgoing</v-icon>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>

            <!-- 통계 카드 3 -->
            <v-col cols="12" md="3">
                <v-card elevation="10" class="pa-4">
                    <div class="d-flex justify-space-between">
                        <div>
                            <div class="text-subtitle-1 text-grey">내선 통화</div>
                            <div class="text-h3 font-weight-bold">{{ inboundCalls }}</div>
                        </div>
                        <v-avatar color="success" size="56" class="elevation-6">
                            <v-icon color="white">mdi-phone-incoming</v-icon>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>

            <!-- 도넛 차트 -->
            <v-col cols="12" md="3">
                <div class="text-center text-h6">일일 누적 통화</div>
                <UiChildCard title="Donut Chart">
                    <apexchart type="donut" height="300" :options="donutchartOptions" :series="donutChart.series"></apexchart>
                </UiChildCard>
            </v-col>
        </v-row>

        <!-- 막대 차트 (기존 추적 차트 대체) -->
        <v-card elevation="10" class="mt-5 pa-4">
            <v-row>
                <v-col cols="12" align="center">
                    <div class="text-h6">일일 누적 통화 건수</div>
                </v-col>
            </v-row>
            
            <v-row class="mb-2">
                <v-col cols="12" class="d-flex justify-end">
                    <v-checkbox
                        v-model="selectedTypes"
                        label="아웃바운드"
                        value="outbound"
                        hide-details
                        density="compact"
                        class="mr-2"
                    ></v-checkbox>
                    <v-checkbox
                        v-model="selectedTypes"
                        label="인바운드"
                        value="inbound"
                        hide-details
                        density="compact"
                        class="mr-2"
                    ></v-checkbox>
                    <v-checkbox
                        v-model="selectedTypes"
                        label="내선통화"
                        value="internal"
                        hide-details
                        density="compact"
                    ></v-checkbox>
                </v-col>
            </v-row>
            
            <apexchart type="bar" height="350" :options="barChartOptions" :series="chartSeries"></apexchart>
        </v-card>
    </v-card>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useTheme } from 'vuetify';
import VueApexCharts from 'vue3-apexcharts';

// 상태 관리
const theme = useTheme();
const selectedDate = ref(new Date().toISOString().substr(0, 10));
const selectedTypes = ref(['outbound', 'inbound', 'internal']);
const totalCalls = ref(684);
const outboundCalls = ref(471);
const inboundCalls = ref(213);

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
            enabled: false
        },
        plotOptions: {
            pie: {
                donut: {
                    size: '70px'
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
