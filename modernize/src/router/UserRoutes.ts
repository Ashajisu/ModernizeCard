const UserRoutes = {
    path: '/main',
    meta: {
        requiresAuth: true
    },
    redirect: '/main',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '사용자 추가',
            path: '/apps/user/profile/add',
            component: () => import('@/views/custom/AddUsers.vue')
        },
        {
            name: '조직 관리',
            path: '/apps/organization/manage',
            component: () => import('@/views/custom/OrganizationM.vue')
        },
        {
            name: '줌 폰 사용자 관리',
            path: '/apps/user/zoom/phone',
            component: () => import('@/views/custom/AddPhone.vue')
        },
        {
            name: '과금',
            path: '/apps/user/zoom/charging',
            component: () => import('@/views/custom/Charging.vue')
        }
    ]
};

export default UserRoutes;
