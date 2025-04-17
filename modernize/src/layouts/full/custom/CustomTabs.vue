<script setup lang="ts">
import { useTabStore } from '@/stores/tabStore';
const tabStore = useTabStore();
import { watch } from "vue";
import { router } from "@/router";
import {useCustomizerStore} from "@/stores/customizer";
const customizer = useCustomizerStore();

// 라우터 감지해서 경로 변경시, 자동으로 탭 추가.
watch(() => router.currentRoute.value, (newRoute) => {
  if (newRoute && newRoute.path) {
    const name = (newRoute.name as string) || newRoute.path || 'undefined'; // name이 없으면 path 사용
    tabStore.addTab({ path: newRoute.path, name }); // 기존 탭이 있으면 setActiveTab 실행됨
  }
}, { immediate: true });

// 탭 변경 감지해서 라우터 변경
watch(() => tabStore.activeTab,(newTab) => {
  if (newTab && decodeURIComponent(newTab.path) !== decodeURIComponent(router.currentRoute.value.path)) {
    router.push(newTab);
  }
});

</script>
<template>
  <div :class="customizer.boxed ? 'maxWidth v-toolbar__content' : 'px-6 v-toolbar__content'">
      <v-tabs show-arrows color="primary" v-model="tabStore.activeTab" class="custom-tabs" >
          <v-tab v-for="(tab) in tabStore.tabs" :key="tab.path" :value="tab">
            {{ tab.name}}
            <v-btn @click.stop="tabStore.removeTab(tab.path)" icon size="x-small" style="--v-btn-height: 8px;" class="ma-1">
              <XIcon stroke-width="1.5" width="12" />
            </v-btn>
            <v-btn @click.stop="tabStore.refreshTab()" icon size="x-small" style="--v-btn-height: 8px;" class="ma-1">
              <ReloadIcon stroke-width="1.5" width="12" />
            </v-btn>
          </v-tab>
      </v-tabs>
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
.custom-tabs .v-tabs-bar {
  justify-content: flex-start !important; /* 탭을 왼쪽 정렬 */
}
</style>