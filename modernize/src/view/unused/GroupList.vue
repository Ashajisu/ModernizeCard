<script setup>
import { ref } from 'vue';
import BaseBreadcrumb from '@/components/shared/BaseBreadcrumb.vue';
import LeftSide from '@/components/apps/ecommerce/addproduct/General.vue';
import GeneralTab from "@/view/phone/GeneralTab.vue";
import RightSide from '@/components/apps/ecommerce/addproduct/RightSide.vue';

const page = ref({ title: '당겨받기 관리' });
const breadcrumbs = ref([
  {
    text: '줌 폰 시스템에서 당겨받기 그룹 및 구성원 관리를 할 수 있습니다.',
    disabled: true,
    href: '#'
  }
]);
</script>
<template>
  <BaseBreadcrumb :title="page.title" :breadcrumbs="breadcrumbs"></BaseBreadcrumb>
  <v-row>
    <v-col cols="12" lg="9" md="8">
      <LeftSide/>
    </v-col>

  </v-row>
</template>