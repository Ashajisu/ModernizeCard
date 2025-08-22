<script setup lang="ts">
import { computed } from 'vue';
import { getPrimary, getSecondary } from '@/utils/UpdateColors';
import { format } from 'date-fns';

const now = new Date();
const month_before4 = format(new Date().setMonth(now.getMonth()-4), 'MMMM');
const month_before3 = format(new Date().setMonth(now.getMonth()-3), 'MMMM');
const month_before2 = format(new Date().setMonth(now.getMonth()-2), 'MMMM');
const month_before1 = format(new Date().setMonth(now.getMonth()-1), 'MMMM');
const month_now = format(now, 'MMMM');
const months = [month_before4, month_before3, month_before2, month_before1, month_now];

const chartOptions = computed(() => {
    return {
        chart: {
            height: 320,
            type: 'bar',
            fontFamily: `inherit`,
            foreColor: '#adb0bb',
            toolbar: {
                show: false
            },
            offsetX: -20,
            stacked: true
        },
        colors: [getPrimary.value, getSecondary.value],
        plotOptions: {
            bar: {
                horizontal: false,
                barHeight: '60%',
                columnWidth: '20%',
                borderRadius: [6],
                borderRadiusApplication: 'end',
                borderRadiusWhenStacked: 'all'
            }
        },
        stroke: {
            show: false
        },
        dataLabels: {
            enabled: false
        },
        legend: {
            show: false
        },
        grid: {
            show: false
        },
        yaxis: {
            min: -300,
            max: 300,
            tickAmount: 4
        },
        xaxis: {
            categories: months,
            axisTicks: {
                show: false
            }
        },
        tooltip: {
            theme: 'dark',
            x: {
                format: 'dd/MM/yy HH:mm'
            }
        }
    };
});
const seriescolumnchart = [
    {
        name: '수입',
        data: [255.3, 244.3, 233.2, 226.6, 251.9]
    },
    {
        name: '지출',
        data: [-200.8, -115.1, -300.0, -159.5, -159.9]
    }
];
</script>
<template>
    <v-card>
        <v-card-item>
            <v-card-title class="text-h5">Revenue Updates</v-card-title>
            <v-card-subtitle class="text-subtitle-1 textSecondary">Overview of Profit</v-card-subtitle>
            <div class="d-flex align-center mt-9 mb-3">
                <h6 class="text-subtitle-2 textSecondary d-flex align-center">
                    <v-icon icon="mdi mdi-checkbox-blank-circle" class="mr-2" size="10" color="primary"></v-icon> 수입
                </h6>
                <h6 class="text-subtitle-2 textSecondary pl-5 d-flex align-center">
                    <v-icon icon="mdi mdi-checkbox-blank-circle" class="mr-2" size="10" color="secondary"></v-icon> 지출
                </h6>
            </div>
            <apexchart class="pt-1 revenuechart" type="bar" height="320" :options="chartOptions" :series="seriescolumnchart"> </apexchart>
            <v-row class="mt-12"><v-col></v-col></v-row>
        </v-card-item>
    </v-card>
</template>


<style type="text/css">
.revenuechart .apexcharts-bar-series.apexcharts-plot-series .apexcharts-series path {
  clip-path: inset(0 0 5% 0 round 20px);
}
</style>