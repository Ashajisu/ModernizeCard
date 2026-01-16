export interface FormField {
    label: string;
    name: string;
    itemTitle?: string;
    itemValue?: string;
    type: string; //text, select, search, checks
    value: string | Date | boolean | string[] | DateRange | null | Record<string, any> | number;
    options?: any[] | SelectOptionObject[];
    placeholder?: string;
    required?: boolean;
    disabled?: boolean;
    searchApi?: string; //자동완성 키워드로 호출할 백엔드 엔드포인트
    searchObj?: any[] | undefined; //FormField.name 의 필드가 포함된 객체의 배열 OR 검색된 데이터 목록
    viewLabelMap?: Record<string, string>; //검색 dialog 에 출력할 필드매핑
    action?: Promise<any> | undefined;
    linkTo?: string;
    excludedFromSearch?: boolean; // 검색 제외 (api 호출에 사용)
    returnObject?: boolean; //객체가 아닌 값으로만 받고싶다면 true 옵션값 지정
    defaultValue?: string | Date | boolean | string[] | DateRange | null | Record<string, any> | number; //초기화시 기본값
}

export type DateRange = {
    startDate: string;
    endDate: string;
};
export type SelectOptionObject = {
    label: string;
    value: string;
};