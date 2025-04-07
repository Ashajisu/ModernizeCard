import type { RouteRecordRaw } from 'vue-router';

const LicenseRoutes : RouteRecordRaw = {
    path: '/license',
    meta: {
        requiresAuth: true
    },
    redirect: '/license/status',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            path: 'status',
            name: '사용 현황',
            component: () => import('@/view/license/DailyLicenseStatus.vue')
        },
        {
            path: 'history',
            name: '사용 내역',
            component: () => import('@/view/license/MonthlyLicenseStatus.vue')
        }
    ]
};

export default LicenseRoutes;
