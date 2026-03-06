//서버와 통신하여 DB 목록을 가져옵니다. 선택 옵션을 생성해 반환합니다.
import apiClient from '@/data/Axios';

const STORAGE_KEY = 'accounts';

//App.vue 초기 로딩 시
export async function loadAccounts() {
    const res = await apiClient.get('/entry/accounts');
    localStorage.setItem(STORAGE_KEY, JSON.stringify(res.data));
    return res.data;
}

export function getAccountsFromStorage() {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : [];
}

//선택박스 화면에서 사용
export function getAccountOptions(accountType?: string) {
    const accounts = getAccountsFromStorage();

    return accounts
        .filter((a: any) => !accountType || a.accountType === accountType)
        .map((a: any) => ({
            label: a.accountName,
            value: a.id
        }));
}