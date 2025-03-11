<!-- src/views/monitoring/DailyCallStats.vue -->
<template>
    <v-card class="pa-5">
        <v-card-title class="text-h4 font-weight-bold"> 활성 녹취 건수 </v-card-title>
        <v-card-title class="text-h6 font-weight-bold "> 💡줌 폰 시스템의 실시간 녹취 사용량을 모니터링 할 수 있습니다. </v-card-title>

        <v-row class="mt-5">
            <v-col cols="6">
                <div class="d-flex align-center">
                    <v-card-title class="text-body-1 font-weight-bold text-medium-emphasis">
                        최종 업데이트 시간 : {{ lastUpdatedTime }}
                    </v-card-title>
                    <v-btn size="large" @click="getcurrenttime" icon>
                        <IconRefresh size="30" />
                    </v-btn>
                </div>
            </v-col>
           
        </v-row>

        <!-- 날짜 선택 -->
        <v-row>
            <v-col cols="12" align="center">
                <div class="text-h6">활성 통화 건수</div>
            </v-col>
        </v-row>

        <v-row>
            <!-- 통계 카드 1 -->
            <v-col cols="12" md="3">
                <v-card elevation="10" class="pa-4" @click="() => { updateTableTitle('전체 통화 콜 수'); getTableData('전체 통화 콜 수'); }">
                    <div class="d-flex justify-space-between">
                        <div>
                            <div class="text-subtitle-1">전체 통화 콜 수</div>
                            <div class="text-h3 font-weight-bold">{{ totalCalls }}</div>
                        </div>
                        <v-avatar color="warning" size="56" class="elevation-6">
                            <v-icon color="white">mdi-phone</v-icon>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>

            <!-- 통계 카드 2 -->
            <v-col cols="12" md="3">
                <v-card elevation="10" class="pa-4" @click="() => { updateTableTitle('외부 통화'); getTableData('외부 통화'); }">
                    <div class="d-flex justify-space-between">
                        <div>
                            <div class="text-subtitle-1">외부 통화</div>
                            <div class="text-h3 font-weight-bold">{{ outboundCalls }}</div>
                            <div class="d-flex mt-2">
                                <div class="mr-4">
                                    <div class="text-caption">아웃바운드</div>
                                    <div class="text-subtitle-1 font-weight-bold">163</div>
                                </div>
                                <div>
                                    <div class="text-caption">인바운드</div>
                                    <div class="text-subtitle-1 font-weight-bold">308</div>
                                </div>
                            </div>
                        </div>
                        <v-avatar color="error" size="56" class="elevation-6">
                            <v-icon color="white">mdi-phone-outgoing</v-icon>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>
 
            <!-- 통계 카드 3 -->
            <v-col cols="12" md="3">
                <v-card elevation="10" class="pa-4" @click="() => { updateTableTitle('내선 통화'); getTableData('내선 통화'); }">
                    <div class="d-flex justify-space-between">
                        <div>
                            <div class="text-subtitle-1">내선 통화</div>
                            <div class="text-h3 font-weight-bold">{{ inboundCalls }}</div>
                        </div>
                        <v-avatar color="#D7CCC8" size="56" class="elevation-6">
                            <v-icon color="white">mdi-phone-incoming</v-icon>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>

            <!-- 도넛 차트 -->
            <v-col cols="12" md="3">
                <div class="text-center text-h6">활성통화</div>
                <UiChildCard title="Donut Chart">
                    <apexchart type="donut" height="300" :options="donutchartOptions" :series="donutChart.series"></apexchart>
                </UiChildCard>
            </v-col>
        </v-row>

        <!-- 테이블 -->
        <v-row>
            <v-col>
                <div class="text-h6 ml-5">활성통화 리스트  ( {{ selectedTableTitle }} )</div>
            </v-col>
            <v-col cols="12">
                <UiParentCard title="External Pagination">
                    <v-data-table
                        :items-per-page="itemsPerPage"
                        :headers="headers"
                        :items="tablesItems"
                        v-model:page="pagination"
                        hide-default-footer
                        class="border rounded-md"
                    >
                        <template v-slot:bottom>
                            <div class="text-center pt-2 mb-3 px-3">
                                <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
                                <v-text-field
                                    :model-value="itemsPerPage"
                                    class="pa-2"
                                    label="페이지당 테이블 행 수 (최대 15개)"
                                    type="number"
                                    min="-1"
                                    max="15"
                                    hide-details
                                    @update:model-value="itemsPerPage = parseInt($event, 10)"
                                ></v-text-field>
                            </div>
                        </template>
                    </v-data-table>
                </UiParentCard>
            </v-col>
        </v-row>
    </v-card>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useTheme } from 'vuetify';
import VueApexCharts from 'vue3-apexcharts';
import { IconRefresh } from '@tabler/icons-vue';
import {OutboundCallDatatables,CurrentCallDataTables,ExtensionCallDataTables} from '@/_mockApis/components/datatable/ActiveCallstatsDataTable';

// 상태 관리
const theme = useTheme();
const selectedDate = ref(new Date().toISOString().substr(0, 10));
const selectedTypes = ref(['outbound', 'inbound', 'internal']);
const totalCalls = ref(684);
const outboundCalls = ref(471);
const inboundCalls = ref(213);
const lastUpdatedTime = ref('2025-03-06 10:00:00');

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
                    size: '50px',
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
        colors: [theme.current.value.colors.error, theme.current.value.colors.warning, '#D7CCC8'],
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

// 데이터 조회 함수
const fetchData = () => {
    // API 호출 로직 구현
    console.log('데이터 조회:', selectedDate.value);
};
// 2025-03-06 10:00:00 포멧으로 현재날짜 가져오기기
const getcurrenttime = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = ('0' + (now.getMonth() + 1)).slice(-2);
    const day = ('0' + now.getDate()).slice(-2);
    const hours = ('0' + now.getHours()).slice(-2);
    const minutes = ('0' + now.getMinutes()).slice(-2);
    const seconds = ('0' + now.getSeconds()).slice(-2);

    lastUpdatedTime.value = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    console.log('현재시간: ', lastUpdatedTime.value);
};

/* 테이블 관련 */
/* 테이블 타이틀 */
const selectedTableTitle = ref('외부통화')
const updateTableTitle = (title: string) => {
    selectedTableTitle.value = title
}
/* 테이블 데이터 가져오기 */
const tablesItems = ref(OutboundCallDatatables);
const itemsDataCount = ref(OutboundCallDatatables.length);
const getTableData = (title: string) => {
    if (title === '외부 통화') {
        tablesItems.value = OutboundCallDatatables;
        itemsDataCount.value = OutboundCallDatatables.length;
    } else if (title === '전체 통화 콜 수') {
        tablesItems.value = CurrentCallDataTables;
        itemsDataCount.value = CurrentCallDataTables.length;
    } else if (title === '내선 통화') {
        tablesItems.value = ExtensionCallDataTables;
        itemsDataCount.value = ExtensionCallDataTables.length;
    }
    // 페이지네이션 초기화
    pagination.value = 1;
}
/* 테이블 헤더 데이터 */
const pagination = ref(1);
const itemsPerPage = ref(5);
const headers = ref([
    { title: 'No.', align: 'start' as const, key: 'id' },
    { title: '유형', align: 'start' as const, key: 'callType' },
    { title: '발신자', align: 'start' as const, key: 'caller' },
    { title: '수신자', align: 'start' as const, key: 'receiver' },
    { title: '통화시간', align: 'start' as const, key: 'callDuration' },
    { title: '통화녹음', align: 'center' as const, key: 'recordType', sortable: false },
])
/*페이지 카운트*/
const pageCount = computed(() => Math.ceil(itemsDataCount.value / itemsPerPage.value));

/* 테이블 관련 끝 */
onMounted(() => {
    fetchData();
    getcurrenttime();
});
</script>
