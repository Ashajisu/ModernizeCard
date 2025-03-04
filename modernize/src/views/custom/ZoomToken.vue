<script setup lang="ts">

import BaseBreadcrumb from "@/components/shared/BaseBreadcrumb.vue";
import {ref} from "vue";
const breadcrumbs = ref([
  {
    text: 'Dashboard',
    disabled: false,
    href: '#'
  },
  {
    text: 'Account Setting',
    disabled: true,
    href: '#'
  }
]);
</script>

<template>
  <BaseBreadcrumb title="ZoomToken" :breadcrumbs="breadcrumbs"></BaseBreadcrumb>
  <iframe src="/zoom.html" width="100%" height="2000px"></iframe>

</template>

<style scoped lang="scss">

</style>