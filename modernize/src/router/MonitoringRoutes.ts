import type { RouteRecordRaw } from 'vue-router';

const MonitoringRoutes: RouteRecordRaw = {
    path: '/monitoring',
    meta: {
        requiresAuth: true
    },
    redirect: '/monitoring/real/active/call',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            path: 'real/active/call',
            name: '활성통화',
            component: () => import('@/views/monitoring/real-time/ActiveCallStats.vue')
        },
        {
            path: 'real/daily/call',
            name: '일일 누적 통화',
            component: () => import('@/views/monitoring/real-time/DailyCallStats.vue')
        },
        {
            path: 'real/daily/recording',
            name: '일일 누적 녹취',
            component: () => import('@/views/monitoring/real-time/DailyRecordingStats.vue')
        },
        {
            path: 'real/active/recording',
            name: '활성 녹취',
            component: () => import('@/views/monitoring/real-time/ActiveRecordingStats.vue')
        },
        {
            path: 'license/daily',
            name: '사용 현황',
            component: () => import('@/views/monitoring/license/DailyLicenseStatus.vue')
        },
        {
            path: 'license/monthly',
            name: '사용 내역',
            component: () => import('@/views/monitoring/license/MonthlyLicenseStatus.vue')
        }
    ]
};

export default MonitoringRoutes;
