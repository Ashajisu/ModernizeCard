const NameRoutes = {
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
        //아래로 컴포넌트 추가하시면 됩니다.




    ]
};

export default NameRoutes;
