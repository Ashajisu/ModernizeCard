import apiClient from '@/data/Axios';
// 화면 출력용 포맷
export function formatMoney(value: any) {
    if (value === null || value === undefined || value === '') return '';
    const number = parseInt(value.toString().replace(/[^0-9\-]/g, ''));
    const result = isNaN(number) ? '' : number.toLocaleString(); // 12,345 형식
    return result;
}

// 백엔드 호출용 함수
export const saveToServer = async (validateForm: any, url: string) => {
    const formData = await validateForm();
    console.log('서버 저장:', formData);
    if (!formData) return;
    try {
        const response = await apiClient.post(url, formData);
        console.log('서버 저장:', response);
        return response;
    } catch (error) {
        console.error('서버 저장 실패:', error);
        throw error;
    }
};

export const deleteToServer = async (id: string, url: string) => {
    try {
        const response = await apiClient.get(url);
        return response.status >= 200 && response.status < 300;  // boolean 보장
    } catch (error) {
        console.error('서버 저장 실패:', error);
        throw error;
    }
};

export const excelSaveToServer = async (data: any, url: string) => {
    const list = Array.isArray(data) ? data : [data];
    try {
        const response = await apiClient.post(url, list);
        console.log('excel 서버 저장:');
        return response;
    } catch (error) {
        console.error('excel 서버 저장 실패:', error);
        throw error;
    }
};

// 중첩 JSON 데이터 관리용
export function parsePath(path: string): string[] {
    return path
        .replace(/\[(\w+)\]/g, '.$1')
        .replace(/^\./, '')
        .split('.');
}

export function getDeepValue(obj: any, path: string, fallback: any = "") {
    const keys = parsePath(path);
    let cur = obj;
    for (const k of keys) {
        if (cur == null) return fallback;
        cur = cur[k];
    }
    return cur ?? fallback;
}

export function setDeepValue(obj: any, path: string, value: any) {
    const keys = parsePath(path);
    let cur = obj;
    for (let i = 0; i < keys.length - 1; i++) {
        const k = keys[i];
        if (cur[k] == null) cur[k] = {};
        cur = cur[k];
    }
    cur[keys[keys.length - 1]] = value;
}

export function deepMerge(target: any, source: any): any {
    for (const key in source) {
        if (
            source[key] &&
            typeof source[key] === 'object' &&
            !Array.isArray(source[key])
        ) {
            if (!target[key] || typeof target[key] !== 'object') {
                target[key] = {};
            }
            deepMerge(target[key], source[key]);
        } else {
            // primitive → 직접 덮어쓰기
            target[key] = source[key];
        }
    }
    return target;
}

import { toRaw } from "vue";
export function deepClone(obj: any) {
    return JSON.parse(JSON.stringify(toRaw(obj)));
}

export function objectToReadableText(obj: any, indent: number = 0): string {
    if (obj == null) return "";

    var result: string[] = [];
    var space = Array(indent + 1).join("  ");

    for (var key in obj) {
        if (!Object.prototype.hasOwnProperty.call(obj, key)) continue;

        var value = obj[key];

        // null, undefined, 빈 객체 제외
        if (value === null || value === undefined) continue;

        // 객체인 경우 (배열 포함)
        if (typeof value === "object") {
            var nested = objectToReadableText(value, indent + 1);
            if (nested) {
                result.push(space + key + ":\n" + nested);
            }
        } else {
            result.push(space + key + ": " + value);
        }
    }

    return result.join("\n");
}

// label, value 고정
export function getLabelByValue(
    value: string | string[],
    options: { label: string; value: string }[]
) {
    if (options == undefined){
        return '';
    }
    if (Array.isArray(value)) {
        return value
            .map(v => options.find(o => o.value === v)?.label)
            .filter(Boolean)
            .join(', ');
    }
    return options.find(o => o.value === value)?.label ?? value;
}

// labelKey, valueKey 지정가능
export function getLabelByValueFromObject<
    T extends Record<string, any>,
    L extends keyof T
>(
    value: string | number | Array<string | number>,
    options: T[],
    valueKey: keyof T,
    labelKey: L & (T[L] extends string ? L : never)
): string {
    if (!options?.length) return '';

    const findLabel = (v: string | number): string | undefined =>
        options.find(o => o[valueKey] === v)?.[labelKey] as string | undefined;

    if (Array.isArray(value)) {
        return value
            .map(findLabel)
            .filter(Boolean)
            .join(', ');
    }

    return findLabel(value) ?? String(value);
}

export function formatPhoneNumber(raw: string): string {
    if (!raw) return raw;

    // 1. 숫자와 +만 남김
    const normalized = raw.replace(/[^0-9+]/g, '');

    // 2. 길이 체크
    if (normalized.length < 6) return normalized;
    let num = normalized;

    // 3. +82 또는 82 → 0 치환
    if (num.startsWith('+82')) {
        num = '0' + num.slice(3);
    } else if (num.startsWith('82')) {
        num = '0' + num.slice(2);
    }

    // 4. 정상 전화번호 검증
    const validPrefix = /^(01[016789]|02|0[3-6][1-5])/;
    if (!validPrefix.test(num)) {
        return raw;
    }

    // 5. 뒤에서 4글자씩 끊기
    const chunks: string[] = [];
    for (let i = num.length; i > 0; i -= 4) {
        const start = Math.max(i - 4, 0);
        chunks.unshift(num.slice(start, i));
    }

    return chunks.join('-');
}
