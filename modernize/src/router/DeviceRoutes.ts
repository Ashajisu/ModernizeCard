import type { RouteRecordRaw } from 'vue-router';

const DeviceRoutes: RouteRecordRaw = {
    path: '/device',
    meta: {
        requiresAuth: true
    },
    redirect: '/device/phone',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [

        {
            name: '폰 디바이스 현황',
            path: 'phone',
            component : () => import('@/view/device/Device.vue')
        },
        {
            name: 'SBC 현황',
            path: 'sbc',
            component : () => import('@/view/device/SbcStatus.vue')
        }
    ]
};

export default DeviceRoutes;