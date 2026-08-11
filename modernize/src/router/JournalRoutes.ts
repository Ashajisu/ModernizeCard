import type { RouteRecordRaw } from 'vue-router';

const JournalRoutes: RouteRecordRaw = {
    path: '/journal',
    meta: {
        requiresAuth: true
    },
    redirect: '/journal/balancesheet',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        {
            name: '대차대조표',
            path: 'balancesheet',
            component: () => import('@/view/journal/BalanceSheet.vue')
        },
        {
            name: '손익계산서',
            path: 'incomestatement',
            component: () => import('@/view/journal/IncomeStatement.vue')
        },
        {
            name: '전표 수기입력',
            path: 'manualentry',
            component: () => import('@/view/journal/ManualEntry.vue')
        },
        {
            name: '전표조회',
            path: 'list',
            component: () => import('@/view/journal/JournalList.vue')
        },
        {
            name: '초기잔액등록',
            path: 'openingbalance',
            component: () => import('@/view/journal/OpeningBalanceUpload.vue')
        },
        {
            name: '계정과목관리',
            path: 'accountmanagement',
            component: () => import('@/view/journal/AccountManagement.vue')
        },
        {
            name: '계정별잔액',
            path: 'accountbalance',
            component: () => import('@/view/journal/AccountBalance.vue')
        }
        ,{
            name: '월별 예산/지출통계',
            path: 'budgetstats',
            component: () => import('@/view/journal/BudgetStats.vue')
        }
        ,{
            name: '현금흐름표',
            path: 'cashflow',
            component: () => import('@/view/journal/CashFlow.vue')
        }
    ]
};

export default JournalRoutes;
