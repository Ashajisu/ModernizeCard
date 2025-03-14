const StatisticsRoutes = {
    path: '/main',
    meta: {
        requiresAuth: true
    },
    redirect: '/main',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: 'Code-App',
            path: '/manage/code',
            component: () => import('@/views/manage/CodeManage.vue')
        },
        {
            name: 'Menu-App',
            path: '/manage/menu',
            component: () => import('@/views/manage/MenuManage.vue')
        },
        {
            name: 'ZoomPhoneAdmin-App',
            path: '/zoomAdmin/ZoomPhoneAdmin',
            component: () => import('@/views/zoomAdmin/ZoomPhoneAdmin.vue')
        },
        {
            name: 'DeptPhonePayment-App',
            path: '/statistics/deptPayment',
            component: () => import('@/views/statistics/deptPayment.vue')
        },
        {
            name: 'DeptPhoneUsing-App',
            path: '/statistics/deptCall',
            component: () => import('@/views/statistics/DeptCall.vue')
        },
        {
            name: 'TeamPhoneUsing-App',
            path: '/statistics/teamCall',
            component: () => import('@/views/statistics/TeamCall.vue')
        },
        {
            name: 'TeamDailyPhoneUsing-App',
            path: '/statistics/teamDailyCall',
            component: () => import('@/views/statistics/TeamDailyCall.vue')
        },
        {
            name: 'TeamDailyPhoneUsing2-App',
            path: '/statistics/teamDailyCall2',
            component: () => import('@/views/statistics/TeamDailyCallSecond.vue')
        },
    ]
};

export default StatisticsRoutes;