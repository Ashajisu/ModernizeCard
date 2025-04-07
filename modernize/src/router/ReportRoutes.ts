import type { RouteRecordRaw } from 'vue-router';

const ReportRoutes: RouteRecordRaw = {
    path: '/report',
    meta: {
        requiresAuth: true
    },
    redirect: '/report/dept/billing',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '부서별 과금 통계',
            path: 'dept/billing',
            component: () => import('@/view/report/deptPayment.vue')
        },
        {
            name: '부서별 유형 통계',
            path: 'dept/call',
            component: () => import('@/view/report/DeptCall.vue')
        },
        {
            name: '일자별 수발신 통계',
            path: 'daily/call',
            component: () => import('@/view/report/TeamDailyCall.vue')
        },
        {
            name: '일자별 수발신 유형별 통계',
            path: 'daily/call/type',
            component: () => import('@/view/report/TeamDailyCallSecond.vue')
        },
        {
            name: '사용량 통계',
            path: 'usage',
            component: () => import('@/view/report/UsedStatistics.vue')
        },
        {
            name: '전화 통화 이력',
            path: 'call/history',
            component: () => import('@/view/report/CallStaticsRecord.vue')
        },
        {
            name: '전화 최대 사용 이력',
            path: 'call/history/maximum',
            component: () => import('@/view/report/CallMaxStaticsRecord.vue')
        },
    ]
};

export default ReportRoutes;