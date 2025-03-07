<script setup lang="ts">
import { useTabStore } from '@/stores/tabStore';
const tabStore = useTabStore();
import { watch } from "vue";
import { router } from "@/router";
import {useCustomizerStore} from "@/stores/customizer";
const customizer = useCustomizerStore();

//  라우터 감지해서 경로 변경시, 자동으로 탭 추가.
watch(() => router.currentRoute.value, (newRoute) => {
  if (newRoute && newRoute.path) {
    tabStore.addTab({ to: newRoute.path, title: (newRoute.name as string) || 'New Tab' });
  }
}, { immediate: true });

// 탭 변경 감지해서 라우터 변경
watch(() => tabStore.activeTab,(newTab) => {
  if (newTab && newTab !== router.currentRoute.value.path) {
    router.push(newTab);
  }
});

</script>
<template>
    <div :class="customizer.boxed ? 'boxed-customTab' : ''">
      <v-tabs color="primary" v-model="tabStore.activeTab" class="customTab">
          <v-tab v-for="(tab) in tabStore.tabs" :key="tab.to" :value="tab.to">
            {{ tab.title}}
            <v-btn @click.stop="tabStore.removeTab(tab.to)" icon size="x-small" style="--v-btn-height: 8px;" class="ma-1">
              <XIcon stroke-width="1.5" width="12" />
            </v-btn>
          </v-tab>
          <v-tab>
            <v-btn class="px-0 py-0 align-center text-center" @click.stop="tabStore.refreshTab()"><v-icon>mdi-refresh</v-icon></v-btn>
          </v-tab>
      </v-tabs>
      <v-divider></v-divider>
      <slot/>
    </div>
</template>
<style scoped lang="scss">
:deep(.v-tab__slider) {
  color: rgb(var(--v-theme-primary)) !important;
}
button.v-tab--selected {
  :deep(.v-btn__content) {
    color: rgb(var(--v-theme-primary)) !important;
  }
}
</style>