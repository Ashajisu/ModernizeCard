import type { RouteRecordRaw } from 'vue-router';

const JournalRoutes: RouteRecordRaw = {
    path: '/journal',
    meta: {
        requiresAuth: true
    },
    redirect: '/journal/balancesheet',
    component: () => import('@/layouts/full/FullLayout.vue'),
    children: [
        // 대차대조표
        {
            name: '대차대조표',
            path: 'balancesheet',
            component: () => import('@/view/journal/BalanceSheet.vue')
        },
        // 손익계산서
        {
            name: '손익계산서',
            path: 'incomestatement',
            component: () => import('@/view/journal/IncomeStatement.vue')
        },
        // 전표 수기입력
        {
            name: '전표 수기입력',
            path: 'manualentry',
            component: () => import('@/view/journal/ManualEntry.vue')
        },
        // 전표조회
        {
            name: '전표조회',
            path: 'list',
            component: () => import('@/view/journal/JournalList.vue')
        },
        // 초기잔액 등록
        {
            name: '초기잔액등록',
            path: 'openingbalance',
            component: () => import('@/view/journal/OpeningBalanceUpload.vue')
        }

        // 아래는 추후 구현 예정 화면 (컴포넌트 작성 후 주석 해제)
        // ,{
        //     name: '계정별 잔액',
        //     path: 'accountbalance',
        //     component: () => import('@/view/journal/AccountBalance.vue')
        // }
        // ,{
        //     name: '월별 예산/지출통계',
        //     path: 'budgetstats',
        //     component: () => import('@/view/journal/BudgetStats.vue')
        // }
    ]
};

export default JournalRoutes;
