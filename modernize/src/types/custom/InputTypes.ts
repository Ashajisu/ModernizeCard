import type {searchType} from "@/types/HeaderTypes";

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
    searchObj?: searchType[] | undefined; //dialog
}