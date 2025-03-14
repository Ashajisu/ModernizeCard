import BasicRoutes from "@/router/BasicRoutes";

const GroupRoutes = {
    path: '/main',
    meta: {
        requiresAuth: true
    },
    redirect: '/main',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '당겨받기 관리',
            path: '/apps/General',
            component: () => import('@/views/apps/eCommerce/GeneralTab.vue')
        },
        {
            name: '줌 폰 디바이스 현황',
            path: '/apps/DeviceStatus',
            component : () => import('@/views/apps/kanban/Device.vue')
        },
        {
            name: 'SBC 현황',
            path: '/apps/SbcStatus',
            component : () => import('@/views/apps/kanban/SbcStatus.vue')
        }
    ]
};

export default GroupRoutes;