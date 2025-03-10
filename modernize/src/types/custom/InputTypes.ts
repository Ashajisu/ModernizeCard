export interface FormField {
    label: string;
    name: string;
    type: string; //text, select
    value: string | Date | boolean;
    options?: string[];
    placeholder?: string;
    required?: boolean;
    disabled?: boolean;
}