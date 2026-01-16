import type { RouteRecordRaw } from 'vue-router';

const EntryRoutes: RouteRecordRaw = {
    path: '/entry',
    meta: {
        requiresAuth: true
    },
    redirect: '/entry/account',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        // 사용자 화면: 상대경로
        {
            name: '계정관리',
            path: 'account',
            component: () => import('@/view/entry/AddAccount.vue')
        }
        // {
        //     name: '전표조회',
        //     path: 'voucher',
        //     component: () => import('@/view/entry/VoucherList.vue')
        // },
        // {
        //     name: '보조원장',
        //     path: 'ledger/account',
        //     component: () => import('@/view/entry/LedgerAccount.vue')
        // },
        // {
        //     name: '전체원장',
        //     path: 'ledger/all',
        //     component: () => import('@/view/entry/LedgerAll.vue')
        // },
        // {
        //     name: '대차대조표',
        //     path: 'sheet/balance',
        //     component: () => import('@/view/entry/BalanceSheet.vue')
        // },
        // {
        //     name: '손익계산서',
        //     path: 'sheet/income',
        //     component: () => import('@/view/entry/IncomeStatement.vue')
        // }
    ]
};

export default EntryRoutes;
