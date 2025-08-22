<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { getPrimary, getSecondary, getLightPrimary, getLightSecondary, getLightError, getLightWarning } from '@/utils/UpdateColors';
import { format } from 'date-fns';
import { formatMoney } from '@/utils/common';
import { apiClient } from '@/data/Axios';
import type { usageChart } from '@/types/custom/DataTableTypes';

//날짜선택 : 당월, 전월, 전년도
const now = new Date();
const year_before = format(new Date().setFullYear(now.getFullYear() - 1), 'MMMM yyyy');
const month_before = format(new Date().setMonth(now.getMonth() - 1), 'MMMM yyyy');
const month_now = format(now, 'MMMM yyyy');
const select = ref(month_now);
const items = ref([month_now, month_before, year_before]);

// 차트 데이터
const statsData = ref<Record<string, usageChart[]>>({});
const currentData = computed<usageChart[]>(() => statsData.value[select.value] ?? []);
const label = computed(() => currentData.value.map((item: any) => item.label));
const seriescolumnchart = computed(() => currentData.value.map((item: any) => item.ratio));
const totalCost = computed(() => {
    return formatMoney(currentData.value.reduce((sum: number, item: any) => sum + item.cost, 0));
});

onMounted(async () => {
    // 초기화 또는 초기 작업 수행
    try {
        const response = await apiClient.get('/card/dash/chart1');
        statsData.value = response.list;
        statsData.value = JSON.parse(response.list);
    } catch (e) {
        console.error('데이터 로드 중 오류 발생:', e);
    }
});

/* Chart */
const colorText = [
    'bg-lightsecondary text-secondary',
    'bg-lightprimary text-primary',
    'bg-secondary text-secondary',
    'bg-lighterror text-error',
    'bg-lightsecondary text-secondary',
    'bg-lightwarning text-warning'
];
const chartOptions = computed(() => {
    return {
        labels: label.value,
        chart: {
            height: 275,
            type: 'donut',
            foreColor: '#adb0bb',
            fontFamily: `inherit`,
            toolbar: {
                show: false
            }
        },
        colors: [
            getLightSecondary.value,
            getPrimary.value,
            getSecondary.value,
            getLightError.value,
            getLightPrimary.value,
            getLightWarning.value
        ],
        plotOptions: {
            pie: {
                donut: {
                    size: '89%',
                    background: 'transparent',

                    labels: {
                        show: true,
                        name: {
                            show: true,
                            offsetY: 7
                        },
                        value: {
                            show: false
                        },
                        total: {
                            show: true,
                            fontSize: '20px',
                            fontWeight: '600',
                            label: totalCost.value
                        }
                    }
                }
            }
        },
        dataLabels: {
            enabled: false
        },
        stroke: {
            show: false
        },
        legend: {
            show: false
        },
        tooltip: {
            theme: 'dark',
            fillSeriesColor: false,
            x: {
                format: 'dd/MM/yy HH:mm'
            }
        }
    };
});
</script>
<template>
    <v-card>
        <v-card-item>
            <div class="d-sm-flex align-center justify-space-between pt-sm-2">
                <div><v-card-title class="text-h5">지출 요약</v-card-title></div>
                <div class="my-sm-0 my-2">
                    <v-select v-model="select" :items="items" variant="outlined" density="compact" hide-details></v-select>
                </div>
            </div>
            <apexchart class="mt-6" type="donut" height="275" :options="chartOptions" :series="seriescolumnchart"> </apexchart>
            <v-row class="mt-5">
                <v-col cols="4" v-for="(item, index) in currentData">
                    <div class="d-flex align-center mt-md-6 mt-3">
                        <v-avatar :class="'rounded-md ' + colorText[index % colorText.length]">
                            <GridDotsIcon size="22" />
                        </v-avatar>
                        <div class="pl-4">
                            <h3 class="text-h6">{{ formatMoney(item.cost) }}</h3>
                            <h6 class="text-subtitle-1 textSecondary">{{ item.label }}</h6>
                        </div>
                    </div>
                </v-col>
            </v-row>
        </v-card-item>
    </v-card>
</template>
