import type { RouteRecordRaw } from 'vue-router';

const CardRoutes: RouteRecordRaw = {
    path: '/user',
    meta: {
        requiresAuth: true
    },
    redirect: '/user/profile',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        //탭 기본화면: 절대경로
        {
            name: 'Dashboard',
            path: '/home/dashboard',
            component: () => import('@/view/home/chartDashboard.vue')
        },
        // 사용자 화면: 상대경로
        {
            name: '신한카드',
            path: 'profile',
            component: () => import('@/view/card/AddSH.vue')
        },
        {
            name: '삼성카드',
            path: 'samsung',
            component: () => import('@/view/card/AddSS.vue')
        },
        {
            name: '국민카드',
            path: 'kookmin',
            component: () => import('@/view/card/AddKB.vue')
        },
        {
            name: '우리카드',
            path: 'woori',
            component: () => import('@/view/card/AddWO.vue')
        },
        {
            name: '농협카드',
            path: 'nh',
            component: () => import('@/view/card/AddNH.vue')
        },
        {
            name: '지출전체',
            path: 'usage',
            component: () => import('@/view/card/AddUsage.vue')
        }
    ]
};

export default CardRoutes;
