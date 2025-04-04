import type { RouteRecordRaw } from 'vue-router';

const StatisticsRoutes: RouteRecordRaw = {
    path: '/statistics',
    meta: {
        requiresAuth: true
    },
    redirect: '/statistics/dept/billing',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '코드 관리',
            path: 'manage/code',
            component: () => import('@/views/manage/CodeManage.vue')
        },
        {
            name: '메뉴 관리',
            path: 'manage/menu',
            component: () => import('@/views/manage/MenuManage.vue')
        },
        {
            name: '디바이스',
            path: 'device',
            component: () => import('@/views/zoomAdmin/ZoomPhoneAdmin.vue')
        },
        {
            name: '부서별 과금 통계',
            path: 'dept/billing',
            component: () => import('@/views/statistics/deptPayment.vue')
        },
        {
            name: '부서별 유형 통계',
            path: 'dept/call',
            component: () => import('@/views/statistics/DeptCall.vue')
        },
        {
            name: '일자별 수발신 통계',
            path: 'daily/call',
            component: () => import('@/views/statistics/TeamDailyCall.vue')
        },
        {
            name: '일자별 수발신 유형별 통계',
            path: 'daily/call/type',
            component: () => import('@/views/statistics/TeamDailyCallSecond.vue')
        },
        {
            name: '사용량 통계',
            path: 'usage',
            component: () => import('@/views/statistics/UsedStatistics.vue')
        },
        {
            name: '전화 통화 이력',
            path: 'call/history',
            component: () => import('@/views/statistics/CallStaticsRecord.vue')
        },
        {
            name: '전화 최대 사용 이력',
            path: 'call/history/max',
            component: () => import('@/views/statistics/CallMaxStaticsRecord.vue')
        },
    ]
};

export default StatisticsRoutes;