const UmjisuRoutes = {
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
        }
    ]
};

export default UmjisuRoutes;
