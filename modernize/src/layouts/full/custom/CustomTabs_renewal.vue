<script setup lang="ts">
import { useTabStore } from '@/stores/tabStore_renewal';
import { router } from "@/router";

const tabStore = useTabStore();
import {useCustomizerStore} from "@/stores/customizer";
const customizer = useCustomizerStore();

// 다른페이지 탭 클릭 시 라우터 이동 -> router.afterEach()에서 addTab()이 자동 호출됨 → 탭 자동 활성화.
function activateTab(tab: any) {
  if (router.currentRoute.value.path !== tab.path) {
    router.push(tab.path)
  }
}

// 탭 리로드
function reloadTab(tab: any, e: Event) {
  e.stopPropagation();
  // 비활성 탭일 경우 무시
  if (tabStore.activeTab.path != tab.path) {
    return;
  }
  tabStore.refreshTab(tab.path);
}

// 탭 닫기
function closeTab(tab: any, e: Event) {
  e.stopPropagation();
  tabStore.removeTab(tab.path);
}

</script>
<template>
  <div :class="customizer.boxed ? 'maxWidth tab-container' : 'px-6 tab-container'">
    <div
        v-for="tab in tabStore.tabs"
        :key="tab.cacheKey"
        class="tab-item"
        :class="{ active: tabStore.isActive(tab.path) }"
        @click="activateTab(tab)"
    >
      <span>{{ tab.name }}</span>
      <span class="tab-btn tab-reload" @click="reloadTab(tab, $event)">⟳</span>
      <span class="tab-btn tab-close" @click="closeTab(tab, $event)">×</span>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.tab-container {
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  padding: 0 10px;
  overflow-x: auto;
  min-height: 45px;
}
.tab-item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  background: rgba(226, 232, 240, 0.53);
  border: solid 1px #e2e8f0;
  border-radius: 8px 8px 0 0;
  margin-right: 5px;
  cursor: pointer;
  white-space: nowrap;
  color: #64748b;
  font-size: 14px;
  transition: all 0.2s;
}
.tab-item:hover {
  background: #cbd5e1;
}
.tab-item.active {
  background: #ffffff;
  color: rgb(var(--v-theme-primary));
  font-weight: 600;
  border-bottom: 2px solid rgb(var(--v-theme-primary));
}
.tab-btn {
  margin-left: 10px;
  color: #94a3b8;
  font-size: 16px;
  line-height: 1;
  cursor: pointer;
}
.tab-reload:hover {
  color: #09268a;
}
.tab-close:hover {
  color: #a31b1b;
}
.tab-content {
  padding: 20px;
  background: #f8fafc;
  min-height: calc(100vh - 105px);
}
</style>