import type { RouteRecordRaw } from 'vue-router';

const MonitoringRoutes: RouteRecordRaw = {
    path: '/monitoring',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            path: 'active-call-stats',
            name: '활성통화',
            component: () => import('@/views/monitoring/ActiveCallStats.vue')
        },
        {
            path: 'daily-call-stats',
            name: '일일 누적 통화',
            component: () => import('@/views/monitoring/DailyCallStats.vue')
        },
        {
            path: 'daily-recording-stats',
            name: '일일 누적 녹취',
            component: () => import('@/views/monitoring/DailyRecordingStats.vue')
        },
        {
            path: 'active-recording-stats',
            name: '활성 녹취',
            component: () => import('@/views/monitoring/ActiveRecordingStats.vue')
        }
    ]
};

export default MonitoringRoutes;
