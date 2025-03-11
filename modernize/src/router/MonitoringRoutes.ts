const MonitoringRoutes = {
    path: '/monitoring',
    //component: () => import('@/views/monitoring/MonitoringLayout.vue'),
    meta: {
        requiresAuth: false
    },
    children: [
        {
            path: 'active-call-stats',
            component: () => import('@/views/monitoring/ActiveCallStats.vue')
        },
        {
            path: 'daily-call-stats',
            component: () => import('@/views/monitoring/DailyCallStats.vue')
        },
        {
            path: 'daily-recording-stats',
            component: () => import('@/views/monitoring/DailyRecordingStats.vue')
        }
    ]
};

export default MonitoringRoutes;
