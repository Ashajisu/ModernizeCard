import type { RouteRecordRaw } from 'vue-router';

const SettingsRoutes: RouteRecordRaw = {
    path: '/settings',
    redirect: '/settings/code',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '코드 관리',
            path: 'code',
            component: () => import('@/view/settings/CodeManage.vue')
        },
        {
            name: '메뉴 관리',
            path: 'menu',
            component: () => import('@/view/settings/MenuManage.vue')
        },
    ]
};

export default SettingsRoutes;
