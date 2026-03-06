export type usageChart = {
    label: string;
    cost: string;
    ratio: string;
};

export type StatsItem = {
    id: string;
    title: string;
    stat1: string;
    stat2: string;
    stat3: string;
    stat4: string;
    stat5: string;
    stat6: string;
    [key: string]: string;
};

export type UsageItem = {
    id: string;
    cardCompany: string;
    transactionDate: string;
    usedCard: string;
    merchantName: string;
    amount: string;
    purchaseType: string;
    usageType: string;
    currency: string;
    paymentDate: string;
    [key: string]: string;
};

export type SSCardItem = {
    id: string;
    transactionDate: string;
    usedCard: string;
    merchantName: string;
    amount: string;
    purchaseType: string;
    usageType: string;
    cardType: string;
    benefitType: string;
    benefitAmount: string;
    currency: string;
    paymentDate: string;
    installmentTotal: string;
    installmentMonths: string;
    installmentNumber: string;
    // [key: string]: string;
};

export type SHCardItem = {
    id: string;
    transactionDate: string;
    cardType: string;
    usedCard: string;
    merchantName: string;
    approvalNumber: string;
    amount: string;
    purchaseType: string;
    usageType: string;
    currency: string;
    paymentDate: string;
    [key: string]: string;
};

export type AccountItem = {
    id: string;
    accountName: string;
    accountType: string;
    categoryType: string;
    accountBalance: string;
};

export type VoucherItem = {
    id: string;
    voucherType: string;
    description: string;
    voucherDate: string;
    createdAt: string;
    updatedAt: string;
};

export const AccountTypeMapping = [
    { label: '자산', value: 'ASSET' },
    { label: '부채', value: 'LIABILITY' },
    { label: '기간수익', value: 'INCOME' },
    { label: '기간비용', value: 'EXPENSE' },
    { label: '순자산', value: 'EQUITY' }
];
export const CategoryTypeMapping = [
    { label: '계좌', value: 'BANK' },
    { label: '카드', value: 'CARD' },
    { label: '현금', value: 'CASH' },
    { label: '투자', value: 'INVEST' },
    { label: '대출', value: 'LOAN' },
    { label: '내가받을돈', value: 'RECEIVABLE' },
    { label: '기타', value: 'ETC' }
];
export const VoucherTypeMapping = [
    { label: '이체', value: 'TRANSFER' },
    { label: '카드', value: 'CARD' },
    { label: '현금', value: 'CASH' },
    { label: '카드대금', value: 'CARD_PAYMENT' },
    { label: '정산', value: 'ADJUST' }
];