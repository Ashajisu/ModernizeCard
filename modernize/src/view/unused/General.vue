<script setup lang="ts">
import { ref } from 'vue';
import GeneralTab from '@/view/phone/GeneralTab.vue';
const tab = ref(null);
</script>
<template>
  <v-tabs v-model="tab" bg-color="transparent" height="70" color="primary" class="mb-8">
    <v-tab value="General" class="text-h6"> 당겨받기 그룹 리스트</v-tab>
  </v-tabs>
  <v-window v-model="tab">
    <v-window-item value="General"><GeneralTab /></v-window-item>
  </v-window>
</template>
