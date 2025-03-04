export interface FormField {
    label: string;
    name: string;
    type: string; //text, select
    value: string | Date;
    options?: string[];
    placeholder?: string;
    required?: boolean;
    disabled?: boolean;
}