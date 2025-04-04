import {createRouter, createWebHistory, type RouteRecordRaw} from 'vue-router';
import MainRoutes from './MainRoutes';
import AuthRoutes from './AuthRoutes';
import { useAuthStore } from '@/stores/auth';
import UserRoutes from "@/router/UserRoutes";
import MonitoringRoutes from './MonitoringRoutes';
import EnvManagementRoutes from '@/router/EnvManagementRoutes';
import GroupRoutes from "@/router/GroupRoutes";
import StatisticsRoutes from "@/router/StatisticsRoutes";

const routes = [
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/authentication/Error.vue')
    },
    MainRoutes, // 탭 기본 화면 dashboard 포함
    AuthRoutes, // home 경로 로그인화면 포함
    // 아래에 각자의 route 파일을 생성해 추가하시면 됩니다.
    // 필수: type 지정, redirect 설정, 상대경로 사용, 경로는 소문자+슬래시만 (하이픈/대문자 금지)
    UserRoutes,
    MonitoringRoutes,
    EnvManagementRoutes,
    GroupRoutes,
    StatisticsRoutes
]
// KeepAlive 에서 컴포넌트를 캐싱하려면 컴포넌트에 name 이 필요함.
// 비동기(import())로 호출된 컴포넌트는 기본적으로 name 이 없으므로,
// tabStore 에서 관리하는 탭 이름과도 일치하도록 route.name 값을 컴포넌트의 name 으로 설정해줌.
function setComponentNames(routes: RouteRecordRaw[]) {
    routes.forEach((route) => {
        if (route.children) {
            setComponentNames(route.children); // 🔄 재귀적으로 하위 라우트까지 적용
        }

        if (route.name && route.component) {
            const component = route.component as any;

            if (typeof component === "function") {
                component().then((mod: any) => {
                    if (mod.default && !mod.default.name) {
                        mod.default.name = route.name as string;
                    }
                });
            } else if (typeof component === "object" && !component.name) {
                component.name = route.name as string;
            }
        }
    });
}
setComponentNames(routes);

export const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: routes
});

router.beforeEach(async (to, from, next) => {
    // redirect to login page if not logged in and trying to access a restricted page
    const publicPages = ['/auth/login'];
    const authRequired = !publicPages.includes(to.path);
    const auth: any = useAuthStore();

    if (to.matched.some((record) => record.meta.requiresAuth)) {
        if (authRequired && !auth.user) {
            auth.returnUrl = to.fullPath;
            return next('/auth/login');
        } else next();
    } else {
        next();
    }
});
