
type Datatables = {
    id: number;
    group: string;
    department: string;
    groupExtensionNumber: string;
    groupNumbers: string;
    bellRingDelay: string;
    // 인덱스 시그니처 추가
    [key: string]: string|number;
};


export type { Datatables};