import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import { router } from './router';
import vuetify from './plugins/vuetify';
import '@/scss/style.scss';
import PerfectScrollbar from 'vue3-perfect-scrollbar';
import VueApexCharts from 'vue3-apexcharts';
import VueTablerIcons from 'vue-tabler-icons';
import 'vue3-carousel/dist/carousel.css';
import Maska from 'maska';
import { createI18n } from 'vue-i18n';
import messages from '@/utils/locales/messages';
import VueScrollTo from 'vue-scrollto';
import VueEasyLightbox from 'vue-easy-lightbox';
import { useTabStore } from "@/stores/tabStore_renewal";

const i18n = createI18n({
    locale: 'en',
    messages: messages,
    silentTranslationWarn: true,
    silentFallbackWarn: true
});

const app = createApp(App);
app.use(router);

app.use(PerfectScrollbar);
app.use(VueTablerIcons);
app.use(i18n);
app.use(Maska);
app.use(VueApexCharts);
app.use(vuetify).mount('#app');
app.use(VueScrollTo, {
    duration: 1000,
    easing: "ease",
})
app.use(VueEasyLightbox)

//리뉴얼 탭
const pinia = createPinia();
app.use(pinia);
const tabStore = useTabStore();
router.isReady().then(() => {
    tabStore.initRouterWatcher(); // 라우터 완전히 준비된 후 탭 감시 시작
});