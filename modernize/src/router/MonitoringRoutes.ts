import type { RouteRecordRaw } from 'vue-router';

const MonitoringRoutes: RouteRecordRaw = {
    path: '/monitoring',
    meta: {
        requiresAuth: true
    },
    redirect: '/monitoring/active/call',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            path: 'daily/call',
            name: '일일 누적 통화',
            component: () => import('@/view/monitoring/DailyCallStats.vue')
        },
        {
            path: 'active/call',
            name: '활성통화',
            component: () => import('@/view/monitoring/ActiveCallStats.vue')
        },
        {
            path: 'daily/recording',
            name: '일일 누적 녹취',
            component: () => import('@/view/monitoring/DailyRecordingStats.vue')
        },
        {
            path: 'active/recording',
            name: '활성 녹취',
            component: () => import('@/view/monitoring/ActiveRecordingStats.vue')
        },
    ]
};

export default MonitoringRoutes;
