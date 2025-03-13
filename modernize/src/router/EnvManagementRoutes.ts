import type { RouteRecordRaw } from 'vue-router';

const EnvManagementRoutes: RouteRecordRaw = {
    path: '/env-management',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: 'Extension 번호 관리',
            path: '/env-management/extension-list',
            component: () => import('@/views/env-management/extention/ExtentionList.vue')
        },
        {
            name: 'PSTN 번호 관리',
            path: '/env-management/pstn-list',
            component: () => import('@/views/env-management/pstn/PstnList.vue')
        }
    ]
};

export default EnvManagementRoutes;
