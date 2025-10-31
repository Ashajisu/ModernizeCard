import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import { router } from './router';
import vuetify from './plugins/vuetify';
import '@/scss/style.scss';
import PerfectScrollbar from 'vue3-perfect-scrollbar';
import VueApexCharts from 'vue3-apexcharts';
import VueTablerIcons from 'vue-tabler-icons';

import { fakeBackend } from '@/utils/helpers/fake-backend';
import 'vue3-carousel/dist/carousel.css';
//Mock Api data
import './_mockApis';


import Maska from 'maska';

//i18
import { createI18n } from 'vue-i18n';
import messages from '@/utils/locales/messages';

//ScrollTop
import VueScrollTo from 'vue-scrollto';

//LightBox
import VueEasyLightbox from 'vue-easy-lightbox';

// tui-tree : 플러그인 제공되지 않음.
import 'tui-tree/dist/tui-tree.css';

// 탭 새로고침
import { useTabStore } from "@/stores/tabStore_renewal";
import piniaPersistedState from "pinia-plugin-persistedstate";


const i18n = createI18n({
    locale: 'en',
    messages: messages,
    silentTranslationWarn: true,
    silentFallbackWarn: true
});

const app = createApp(App);
fakeBackend();
app.use(router);

app.use(PerfectScrollbar);
// app.use(VueDragscroll);

app.use(VueTablerIcons);

app.use(i18n);
app.use(Maska);
app.use(VueApexCharts);
app.use(vuetify).mount('#app');

//ScrollTop Use
// app.use(VueScrollTo);
app.use(VueScrollTo, {
    duration: 1000,
    easing: "ease",
})
//Lightbox
app.use(VueEasyLightbox)

//리뉴얼 탭
const pinia = createPinia();
// pinia.use(piniaPersistedState); // keepAlive 캐시가 삭제되므로 탭목록도 남겨둘 필요가 없음.
app.use(pinia);
const tabStore = useTabStore();
router.isReady().then(() => {
    tabStore.initRouterWatcher(); // 라우터 완전히 준비된 후 탭 감시 시작
});