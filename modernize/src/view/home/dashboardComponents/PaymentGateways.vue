<script setup lang="ts">
// import { paymentGateways } from '@/_mockApis/components/dashboard/ecommerceData';
import icon1 from '@/assets/images/svgs/icon-paypal.svg';
import icon2 from '@/assets/images/svgs/icon-office-bag.svg';
import icon3 from '@/assets/images/svgs/icon-master-card.svg';
import icon4 from '@/assets/images/svgs/icon-pie.svg';
import { onMounted, ref } from 'vue';
import apiClient from '@/data/Axios';
import type { paymentGateway } from '@/types/components/dashboard/ecommerceDashboard';
export type IconKey = 'icon1' | 'icon2' | 'icon3' | 'icon4';
const ICON_MAP: Record<IconKey, string> = {
    icon1: icon1,
    icon2: icon2,
    icon3: icon3,
    icon4: icon4
};
const paymentGateways = ref<paymentGateway[]>([]);
onMounted(async () => {
    // 초기화 또는 초기 작업 수행
    paymentGateways.value = await apiClient.get('/card/payment/gateway/list').then((res) => res.list);
});
function getIconByKey(key?: string): string {
    if (!key) return icon1; // 기본 아이콘
    return ICON_MAP[key as IconKey] ?? icon1;
}
</script>
<template>
    <v-card>
        <v-card-item>
            <v-card-title class="text-h5">계좌별 잔액</v-card-title>
            <v-card-subtitle class="text-subtitle-1 textSecondary">Platform For Income</v-card-subtitle>
            <div class="mt-sm-10 mt-5">
                <div class="d-flex align-center mt-6" v-for="list in paymentGateways" :key="list.title">
                    <v-avatar :class="'rounded-md bg-light' + list.bgcolor" size="40">
                        <img :src="getIconByKey(list.img)" :alt="getIconByKey(list.img)" width="25" />
                    </v-avatar>
                    <div class="pl-4 mt-n1">
                        <h5 class="text-h6">{{ list.title }}</h5>
                        <h6 class="text-subtitle-1 textSecondary mt-1">{{ list.subtitle }}</h6>
                    </div>
                    <div :class="'ml-auto font-weight-bold text-subtitle-1 text-medium-' + list.disable">{{ list.rank }}</div>
                </div>
                <v-btn class="mt-sm-15 mt-10" block variant="outlined" color="primary" href="/ModernizeCard/user/usage"
                    >View all transactions</v-btn
                >
            </div>
        </v-card-item>
    </v-card>
</template>
