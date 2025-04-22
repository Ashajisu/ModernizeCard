import { defineStore } from "pinia";
import config from "@/config";

// F5에도 탭 유지 : @pinia-plugin-persistedstate 라이브러리를 설치하고, tabs 상태를 유지하도록 설정 : 불필요
// 탭전체삭제 : removeAllTabs: 불필요
// 탭 갯수제한 제거.
export const useTabStore = defineStore({
  id: "tab",
  state: () => ({
    tabs: [] as { name: string; path: string;}[],
    activeTab: { name: "Dashboard", path: config.mainPage },
    defaultTab: { name: "Dashboard", path: config.mainPage }
  }),
  actions: {
    setActiveTab(page: { name: string; path: string }){
      if (!page.path || this.activeTab.path === page.path) return;
      this.activeTab = { ...page }; // 새로운 객체 할당
    },
    addTab(page: { name: string; path: string }) {
      const existingTab = this.tabs.find(tab => tab.path === page.path);
      if (existingTab) {
        this.setActiveTab(existingTab); // 이미 열려있다면 활성화
      } else {
          this.tabs.push(page);
          this.setActiveTab(page);
      }
    },
    removeTab(path: string) {
      const index = this.tabs.findIndex(tab => tab.path === path);
      // 없는 탭인 경우 처리
      if (index === -1) return;

      // 현재 활성화된 탭을 닫을 경우 처리
      if (this.activeTab.path === path) {
        const newActiveTab = this.tabs[index - 1] || this.tabs[index + 1];
        this.setActiveTab(newActiveTab || this.defaultTab);
      }
      // 탭 삭제
      this.tabs.splice(index, 1);

      // 모든 탭이 닫히면 기본 탭 추가
      if (this.tabs.length === 0) {
        this.addTab(this.defaultTab);
      }
    },
    refreshTab(path: string) {
      const tab = this.tabs.find(t => t.path === path);
      if (!tab) return;

      this.removeTab(path);

      setTimeout(() => {
        this.addTab(tab);
        this.setActiveTab(tab); // 혹은 this.activeTab = tab;
      }, 10);
    },
    removeAllTabs(){
      this.tabs = [];
      this.addTab(this.defaultTab);
    }
  },
  getters: {
    isActive: (state) => (path: string) => state.activeTab.path === path,
    cachedTabs: (state) => state.tabs.map(tab => tab.name),
  },
});