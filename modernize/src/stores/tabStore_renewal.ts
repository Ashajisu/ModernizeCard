import { defineStore } from "pinia";
import { computed, nextTick, ref } from 'vue';
import config from "@/config";
import { router } from "@/router";

//pinia+vue 에 적합한 setup 방식으로 변경해 타입안정성 높임.
//keepAlive 캐시가 삭제되므로 탭목록도 남겨둘 필요가 없음. persist 사용안함.
export const useTabStore = defineStore("tab", () => {
    const activeTab = ref<{ name: string; path: string; cacheKey: string; cached: boolean;}>({
        name: "Dashboard",
        path: config.mainPage,
        cacheKey: "init",
        cached: true
    });
    const tabs = ref<{ name: string; path: string; cacheKey: string; cached: boolean; }[]>([]);
    const defaultTab = ref({ ...activeTab.value });
    const cachedTabs = computed(() => tabs.value.filter(t => t.cached === true).map(t => t.name));
    const activePath = computed(() => activeTab.value.path);
    function isActive(path: string) {
        return activePath.value === path;
    }
    const tabIgnoreList = [
        "/",                 // Landing Page
        "/auth/login"        // 로그인 페이지
    ];

    let routerWatcherReady = false;
    function initRouterWatcher() {
        //최초인입 설정
        if (routerWatcherReady){
            return;
        }else {
            routerWatcherReady = true;
            const cur = router.currentRoute.value;
            if (cur?.path && !tabIgnoreList.includes(cur.path)) {
                const name = (cur.name as string) || cur.path;
                addTab({ name, path: cur.path, cacheKey: Date.now().toString(), cached:true }, true);
            }
        }

        router.afterEach((to) => {
            if (!to?.path) return;
            const name = (to.name as string) || to.path;
            addTab({ name, path: to.path, cacheKey: Date.now().toString(), cached:true }, true);
        });
    }

    function setActiveTab(page: { name: string; path: string; cacheKey: string ; cached: boolean;}, routerWatcher = false) {
        if (!page.path || (activeTab.value.cacheKey === page.cacheKey && activeTab.value.path === page.path)) return;
        activeTab.value = { ...page };
        if (!routerWatcher) { router.push(page.path); }
    }

    function addTab(page: { name: string; path: string; cacheKey: string; cached:boolean }, routerWatcher = false) {
        if (tabIgnoreList.includes(page.path)) return;

        const existingTab = tabs.value.find(tab => tab.path === page.path);
        if (!existingTab) {
            tabs.value.push(page);
            setActiveTab(page, routerWatcher);
        }else {
            setActiveTab(existingTab, routerWatcher);
        }
    }

    function removeTab(path: string) {
        const idx = tabs.value.findIndex(t => t.path === path);
        if (idx > -1) {
            tabs.value.splice(idx, 1);
        }
        if (activeTab.value.path === path) {
            const fallback = tabs.value[idx - 1] || tabs.value[idx] || defaultTab.value;
            setActiveTab(fallback);
        }
    }

    /** key 값 변경으로 새 인스턴스 강제생성, cached= false 로 include 목록에서 제거, setTimeout 이후 cached= true 로 include 목록에 추가하여 다시 캐시대상이 됨**/
    function refreshTab(path: string) {
        const index = tabs.value.findIndex(tab => tab.path === path);
        if (index === -1) return;

        const tab = tabs.value[index];
        tab.cacheKey = Date.now().toString();
        tab.cached = false;
        tabs.value[index] = { ...tabs.value[index], ...tab };

        if (activeTab.value.path === path) {
            activeTab.value = { ...tab };
        }

        setTimeout(()=>{
            tab.cached = true;
            tabs.value[index] = { ...tabs.value[index], ...tab };
        },500)
    }

    function removeAllTabs() {
        tabs.value = [];
        addTab(defaultTab.value, false);
    }

    return {
        tabs,
        activeTab,
        defaultTab,
        initRouterWatcher,
        setActiveTab,
        addTab,
        removeTab,
        removeAllTabs,
        refreshTab,
        isActive,
        cachedTabs,
    };
}
// { persist: {
        // key: "tabStore",
        // storage: localStorage,
        // pick: ["activeTab"]
    // }}
);
