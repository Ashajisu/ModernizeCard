import type { RouteRecordRaw } from 'vue-router';

const EnvManagementRoutes: RouteRecordRaw = {
    path: '/env',
    redirect: '/env/extension/list',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        //상대경로 설정.
        {
            name: '내선번호',
            path: 'extension/list',
            component: () => import('@/views/env-management/extention/ExtentionList.vue')
        },
        {
            name: 'PSTN',
            path: 'pstn/list',
            component: () => import('@/views/env-management/pstn/PstnList.vue')
        }
    ]
};

export default EnvManagementRoutes;
