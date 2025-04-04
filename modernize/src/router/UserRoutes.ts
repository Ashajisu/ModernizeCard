import type { RouteRecordRaw } from 'vue-router';

const UserRoutes : RouteRecordRaw = {
    path: '/user',
    meta: {
        requiresAuth: true
    },
    redirect: '/user/profile',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '사용자',
            path: 'profile',
            component: () => import('@/views/custom/AddUsers.vue')
        },
        {
            name: '조직',
            path: 'organization',
            component: () => import('@/views/custom/OrganizationM.vue')
        },
        {
            name: '폰 사용자',
            path: 'phone',
            component: () => import('@/views/custom/AddPhone.vue')
        },
        {
            name: '과금',
            path: 'billing',
            component: () => import('@/views/custom/Billing.vue')
        },
        {
            name: '녹취',
            path: 'recording',
            component: () => import('@/views/custom/Recording.vue')
        }
    ]
};

export default UserRoutes;
