<script setup lang="ts">
// import ContactTitle from '@/views/dashboard/dashboardComponents/ecommerce/ContactTitle.vue';
// import CallsOffered from '@/views/dashboard/dashboardComponents/ecommerce/CallsOffered.vue';
// import ProductSold from '@/views/dashboard/dashboardComponents/ecommerce/ProductSold.vue';
// import TotalEarning from '@/views/dashboard/dashboardComponents/ecommerce/TotalEarning.vue';
// import SalesProfit from '@/views/dashboard/dashboardComponents/ecommerce/SalesProfit.vue';
// import MonthlyEarnings from '@/views/dashboard/dashboardComponents/ecommerce/MonthlyEarnings.vue';
// import WeeklyStats from '@/views/dashboard/dashboardComponents/modern/WeeklyStats.vue';
// import YearlySales from '@/views/dashboard/dashboardComponents/ecommerce/YearlySales.vue';
// import ProductPerformance from '@/views/dashboard/dashboardComponents/ecommerce/ProductPerformance.vue';
import RevenueUpdates from '@/view/home/dashboardComponents/RevenueUpdates.vue';
import SalesOverview from '@/view/home/dashboardComponents/SalesOverview.vue';
import PaymentGateways from '@/view/home/dashboardComponents/PaymentGateways.vue';
import RecentTransactions from '@/view/home/dashboardComponents/RecentTransactions.vue';
</script>
<template>
    <!--    <v-row>-->
    <!-- Welcome Card -->
    <!--        <v-col cols="12" xl="12" lg="12">-->
    <!--            <ContactTitle />-->
    <!--        </v-col>-->
    <!--    </v-row>-->
    <v-row>
        <!-- Payments -->
        <!--        <v-col cols="12" lg="2" sm="6">-->
        <!--            <CallsOffered />-->
        <!--        </v-col>-->
        <!-- Products Sold -->
        <!--        <v-col cols="12" lg="2" sm="6">-->
        <!--            <ProductSold />-->
        <!--        </v-col>-->
        <!-- Revenue Updates -->
        <v-col cols="12" lg="6" sm="6">
            <SalesOverview />
        </v-col>

        <!--        <v-col cols="12" lg="4" sm="6">-->
        <!--            <v-row>-->
        <!--                <v-col cols="12" sm="6">-->
        <!--                    &lt;!&ndash;Total Earning &ndash;&gt;-->
        <!--                    <TotalEarning />-->
        <!--                </v-col>-->
        <!--                <v-col cols="12" sm="6">-->
        <!--                    &lt;!&ndash;sales profit &ndash;&gt;-->
        <!--                    <SalesProfit />-->
        <!--                </v-col>-->
        <!--            </v-row>-->
        <!--            &lt;!&ndash;Monthly Earning &ndash;&gt;-->
        <!--            <MonthlyEarnings />-->
        <!--        </v-col>-->
        <!-- Weekly Stats-->
        <!--        <v-col cols="12" lg="4" sm="6">-->
        <!--            <WeeklyStats />-->
        <!--        </v-col>-->
        <!--        &lt;!&ndash; Yearly Stats&ndash;&gt;-->
        <!--        <v-col cols="12" lg="4" md="6">-->
        <!--            <YearlySales />-->
        <!--        </v-col>-->
        <!-- Payment Gateways-->
        <v-col cols="12" lg="4" md="6">
            <PaymentGateways />
        </v-col>
        <!-- Sales Overview -->
        <v-col cols="12" lg="6" sm="6">
            <RevenueUpdates />
        </v-col>
        <!-- Recent Transactions -->
        <v-col cols="12" lg="4" sm="12">
            <RecentTransactions />
        </v-col>
        <!-- Product Performance -->
        <!--        <v-col cols="12" lg="8" sm="12">-->
        <!--            <ProductPerformance />-->
        <!--        </v-col>-->
    </v-row>
</template>
