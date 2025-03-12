import type {searchType} from "@/types/HeaderTypes";

export interface FormField {
    label: string;
    name: string;
    type: string; //text, select, search
    value: string | Date | boolean;
    options?: string[];
    placeholder?: string;
    required?: boolean;
    disabled?: boolean;
    view?: boolean; //dialog
    searchObj?: searchType[] | undefined; //dialog
}