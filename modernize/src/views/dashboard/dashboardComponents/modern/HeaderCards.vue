<script setup lang="ts">
import { topCardsData } from '@/_mockApis/components/dashboard/modernData';
</script>
<template>
    <v-row>
        <v-col cols="12" lg="2" md="2" sm="2" v-for="card in topCardsData" :key="card.title">
            <div
                :class="'text-decoration-none d-flex align-center justify-center text-center rounded-md pa-3  bg-' + card.bgcolor"
            >
                <div :class="'d-flex align-center gap-2 bg-' + card.bgcolor">
                    <img :src="card.img" alt="icon" style="width: 20px; height: 20px;"/>
<!--                    <div :class="'text-subtitle-1 text-capitalize font-weight-bold mt-1 text-' + card.textcolor" v-text="card.title"></div>-->
                    <span :class="'text-h6 mt-1 text-' + card.textcolor" v-text="card.number"></span>
                </div>
            </div>
        </v-col>
    </v-row>
</template>
