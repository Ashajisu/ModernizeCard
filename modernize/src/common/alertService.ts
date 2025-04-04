// services/alertService.ts
import { ref } from 'vue';

export const alertState = ref<{ message: string; type: 'alert' | 'confirm'; resolve: (value: boolean) => void } | null>(null);

export function alert(message: string): Promise<void> {
    return new Promise((resolve) => {
        alertState.value = { message, type: 'alert', resolve: () => resolve() };
    });
}

export function confirm(message: string): Promise<boolean> {
    return new Promise((resolve) => {
        alertState.value = { message, type: 'confirm', resolve };
    });
}

export function closeAlert(result: boolean = true) {
    alertState.value?.resolve(result);
    alertState.value = null;
}

