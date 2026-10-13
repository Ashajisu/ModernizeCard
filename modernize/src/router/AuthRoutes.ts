const AuthRoutes = {
    path: '/auth',
    component: () => import('@/layouts/blank/BlankLayout.vue'),
    meta: {
        requiresAuth: false
    },
    children: [
        // home 경로 로그인화면
        {
            name: 'Landing Page',
            path: '/',
            component: () => import('@/view/home/CustomSideLogin.vue')
        },
        {
            name: 'Error',
            path: '/auth/404',
            component: () => import('@/views/authentication/Error.vue')
        },
    ]
};

export default AuthRoutes;
