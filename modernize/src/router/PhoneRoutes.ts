import type { RouteRecordRaw } from 'vue-router';

const PhoneRoutes : RouteRecordRaw = {
    path: '/phone',
    meta: {
        requiresAuth: true
    },
    redirect: '/phone/device',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '디바이스',
            path: 'device',
            component: () => import('@/view/phone/Device.vue')
        },
        {
            name: '내선번호',
            path: 'extension',
            component: () => import('@/view/phone/ExtensionNew.vue')
        },
        {
            name: 'PSTN',
            path: 'pstn',
            component: () => import('@/view/phone/PstnNew.vue')
        },
        {
            name: '당겨받기 그룹',
            path: 'pickup/group',
            component: () => import('@/view/phone/PickupGroup.vue')
        },
        {
            name: '과금',
            path: 'billing',
            component: () => import('@/view/phone/Billing.vue')
        },
        {
            name: '녹취',
            path: 'recording',
            component: () => import('@/view/phone/Recording.vue')
        }
    ]
};

export default PhoneRoutes;
