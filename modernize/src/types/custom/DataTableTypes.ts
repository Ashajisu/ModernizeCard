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

