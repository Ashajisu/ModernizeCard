const CustomRoutes = {
    path: '/main',
    meta: {
        requiresAuth: true
    },
    redirect: '/main',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: 'Dashboard',
            path: '/dashboards/modern',
            component: () => import('@/views/dashboard/modern/index.vue')
        },
        {
            name: '사용자 추가',
            path: '/apps/user/profile/add',
            component: () => import('@/views/custom/AddUsers.vue')
        },{
            name: '시스템 설정',
            path: '/apps/zoom/token',
            component: () => import('@/views/custom/ZoomToken.vue')
        }
    ]
};

export default CustomRoutes;
