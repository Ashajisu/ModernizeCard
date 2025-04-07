<template>
  <RouterView></RouterView>
  <GlobalAlert/>
</template>

<script setup lang="ts">
import { RouterView } from "vue-router";
import GlobalAlert from '@/common/GlobalAlert.vue';
</script>