<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import { getPrimary, getSecondary, getLightPrimary, getLightSecondary, getLightError, getLightWarning } from '@/utils/UpdateColors';
import { format } from 'date-fns';
import { formatMoney } from '@/utils/common';
import { apiClient } from '@/data/Axios';
import type { usageChart } from '@/types/custom/DataTableTypes';

//날짜선택 : 당월, 전월, 전년도
const month_before = format(new Date().setMonth(new Date().getMonth() - 1), 'MMMM yyyy');
const select = ref(month_before);
// 차트 데이터

const statsData = ref<Record<string, usageChart[]>>({});
const currentData = computed<usageChart[]>(() => statsData.value[select.value] ?? []);

const selectedLabels = ref<string[]>([]);
const filteredData = computed(() =>
    currentData.value.filter(item => selectedLabels.value.includes(item.label))
);
const label = computed(() => filteredData.value.map(item => item.label));
const cost = computed(() => filteredData.value.map(item => item.cost));
const totalCost = computed(() =>
    formatMoney(filteredData.value.reduce((sum, item) => sum + item.cost, 0))
);

const items = computed(() => Object.keys(statsData.value));

watch(currentData, (list) => {
    selectedLabels.value = list.map(item => item.label);
}, { immediate: true });
onMounted(async () => {
    // 초기화 또는 초기 작업 수행
    try {
        const response = await apiClient.get('/card/dash/chart1');
        statsData.value = response.list;
        statsData.value = JSON.parse(response.list);
        console.log(statsData.value);
    } catch (e) {
        console.error('데이터 로드 중 오류 발생:', e);
    }
});

/* Chart */
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
                            show: true,
                            formatter: (val: number) => {
                                return formatMoney(val);
                            }
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
            <apexchart class="mt-6" type="donut" height="275" :options="chartOptions" :series="cost"> </apexchart>
            <v-row class="mt-5">
                <v-col cols="4" v-for="(item) in currentData">
                    <div class="d-flex align-center mt-md-6 mt-3">
                        <v-checkbox v-model="selectedLabels" :value="item.label" density="compact" hide-details color="primary"/>
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
