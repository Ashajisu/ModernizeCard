export interface FormField {
    label: string;
    name: string;
    type: string; //text, select, search, checks
    value: string | Date | boolean | string[];
    options?: string[];
    placeholder?: string;
    required?: boolean;
    disabled?: boolean;
    view?: boolean; //dialog
    searchObj?: any[] | undefined; //dialog 를 출력하기위해 FormField.name 의 필드가 포함된 객체의 배열을 담아야함/
}