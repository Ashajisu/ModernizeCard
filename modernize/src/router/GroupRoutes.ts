import type { RouteRecordRaw } from 'vue-router';

const GroupRoutes: RouteRecordRaw = {
    path: '/group',
    meta: {
        requiresAuth: true
    },
    redirect: '/group/general',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '당겨받기 그룹',
            path: 'general',
            component: () => import('@/views/apps/eCommerce/GeneralTab.vue')
        },
        {
            name: '폰 디바이스 현황',
            path: 'device',
            component : () => import('@/views/apps/kanban/Device.vue')
        },
        {
            name: 'SBC 현황',
            path: 'sbc/status',
            component : () => import('@/views/apps/kanban/SbcStatus.vue')
        }
    ]
};

export default GroupRoutes;