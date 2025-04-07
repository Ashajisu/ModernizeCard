import type { RouteRecordRaw } from 'vue-router';

const UserRoutes : RouteRecordRaw = {
    path: '/user',
    meta: {
        requiresAuth: true
    },
    redirect: '/user/profile',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        //탭 기본화면: 절대경로
        {
            name: 'Dashboard',
            path: '/home/dashboard',
            component: () => import('@/view/home/dashboard.vue')
        },
        // 사용자 화면: 상대경로
        {
            name: '사용자',
            path: 'profile',
            component: () => import('@/view/user/AddUsers.vue')
        },
        {
            name: '조직',
            path: 'organization',
            component: () => import('@/view/user/OrganizationM.vue')
        },
        {
            name: '폰 사용자',
            path: 'phone',
            component: () => import('@/view/user/AddPhone.vue')
        }
    ]
};

export default UserRoutes;
