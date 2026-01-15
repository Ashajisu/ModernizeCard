<template>
    <RouterView></RouterView>
    <GlobalAlert />
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router';
import GlobalAlert from '@/common/GlobalAlert.vue';
</script>
<style lang="scss">
.v-data-table th {
    white-space: nowrap;
}
</style>