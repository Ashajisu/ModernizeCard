export interface AccountBalanceItem {
    accountId: number;
    code: string;
    name: string;
    parentId: number | null;
    depth: number;
    confirmedAmount: number;
    unconfirmedAmount: number;
    totalAmount: number;
    leaf: boolean;
}

export interface BalanceSheetResponse {
    asOfDate: string;
    assets: AccountBalanceItem[];
    liabilities: AccountBalanceItem[];
    totalAssets: number;
    totalLiabilities: number;
    equity: number;
    totalAssetsConfirmedOnly: number;
    totalLiabilitiesConfirmedOnly: number;
    equityConfirmedOnly: number;
    balanced: boolean;
}

export interface IncomeStatementResponse {
    fromDate: string;
    toDate: string;
    revenues: AccountBalanceItem[];
    expenses: AccountBalanceItem[];
    totalRevenue: number;
    totalExpense: number;
    netIncome: number;
}
