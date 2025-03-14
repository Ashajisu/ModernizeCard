const StatisticsRoutes = {
    path: '/main',
    meta: {
        requiresAuth: true
    },
    redirect: '/main',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '코드 관리',
            path: '/manage/code',
            component: () => import('@/views/manage/CodeManage.vue')
        },
        {
            name: '메뉴 관리',
            path: '/manage/menu',
            component: () => import('@/views/manage/MenuManage.vue')
        },
        {
            name: '디바이스',
            path: '/zoomAdmin/ZoomPhoneAdmin',
            component: () => import('@/views/zoomAdmin/ZoomPhoneAdmin.vue')
        },
        {
            name: '부서별 과금 통계',
            path: '/statistics/deptPayment',
            component: () => import('@/views/statistics/deptPayment.vue')
        },
        {
            name: '부서별 유형 통계',
            path: '/statistics/deptCall',
            component: () => import('@/views/statistics/DeptCall.vue')
        },
        {
            name: 'TeamPhoneUsing-App',
            path: '/statistics/teamCall',
            component: () => import('@/views/statistics/TeamCall.vue')
        },
        {
            name: '일자별 수발신 통계',
            path: '/statistics/teamDailyCall',
            component: () => import('@/views/statistics/TeamDailyCall.vue')
        },
        {
            name: '일자별 수발신 유형별 통계',
            path: '/statistics/teamDailyCall2',
            component: () => import('@/views/statistics/TeamDailyCallSecond.vue')
        },
    ]
};

export default StatisticsRoutes;