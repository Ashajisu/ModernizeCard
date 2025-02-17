import { defineStore } from "pinia";
import config from "@/config";
import {computed, nextTick, toRaw} from "vue";

export const useTabStore = defineStore({
  id: "tab",
  state: () => ({
    maxTabs: config.maxTabs,
    tabs: [] as { title: string; to: string;}[],
    activeTab: null as string | null
  }),
  actions: {
    setActiveTab(page: { title: string; to: string }){
      if (!page.to || this.activeTab === page.to) return;
      this.activeTab = page.to;
    },
    addTab(page: { title: string; to: string }) {
      const existingTab = this.tabs.find(tab => tab.to === page.to);
      if (existingTab) {
        this.setActiveTab(existingTab); // 이미 열려있다면 활성화
      } else {
        if (this.tabs.length >= this.maxTabs) {
          alert(`최대 ${this.maxTabs}개의 탭만 열 수 있습니다.`);
          if (this.tabs.length > 0) {
            this.setActiveTab(this.tabs[0]);
          }
        } else {
          this.tabs.push(page);
          // this.activeTab = page.to;
          this.setActiveTab(page);
        }
      }
    },
    removeTab(tabTo: string) {
      console.log(this.tabs.length);
      if(this.tabs.length == 1){
        alert(`최소 1개의 탭은 열려있어야 합니다.`);
        return;
      };

      const index = this.tabs.findIndex(tab => tab.to === tabTo);
      if (index === -1) return; // 이미 없는 탭이면 종료
      // 활성화된 탭이 삭제된 경우, 적절한 탭으로 이동
      if (this.activeTab === tabTo) {
        const newActiveTab = this.tabs[index - 1] || this.tabs[index + 1] || this.tabs[0];
        this.setActiveTab(newActiveTab);
      }
      // 배열에서 해당 탭 삭제
      this.tabs.splice(index, 1);
    },
    refreshTab() {
      const index = this.tabs.findIndex(tab => tab.to === this.activeTab);
      if (index === -1) return;

      const tab = this.tabs[index];
      this.removeTab(tab.to);

      setTimeout(()=>{
      this.addTab(tab);
      console.log("done", tab);
      });

      setTimeout(()=>{
      this.setActiveTab(tab);
      },5);
      //requestAnimationFrame, nextTick 은 제대로 동작하지 않음.
    }
  },
  getters: {
    isActive: (state) => (path: string) => {
      return state.activeTab === path;
    },
    cachedTabs: (state) => state.tabs.map(tab => tab.title),
  }
});