// /src/utils/dateUtil.ts
// padStart()는 ES2017부터 지원
const pad = (n: number) => ('0' + n).slice(-2)
const now = new Date();
/**
 * 로컬 기준 날짜 → yyyy-MM-dd'T'HH:mm
 */
export function formatLocalToT(date: Date): string {
    const y = date.getFullYear()
    const m = pad(date.getMonth() + 1)
    const d = pad(date.getDate())
    const h = pad(date.getHours())
    const mi = pad(date.getMinutes())
    return `${y}-${m}-${d}T${h}:${mi}`
}

/**
 * yyyy-MM-dd (날짜만)
 */
export function formatDate(date: Date): string {
    const y = date.getFullYear()
    const m = pad(date.getMonth() + 1)
    const d = pad(date.getDate())
    return `${y}-${m}-${d}`
}

/**
 * yyyy-MM-dd HH:mm:ss
 */
export function formatDateTime(date: Date): string {
    const y = date.getFullYear()
    const m = pad(date.getMonth() + 1)
    const d = pad(date.getDate())
    const h = pad(date.getHours())
    const mi = pad(date.getMinutes())
    const s = pad(date.getSeconds())
    return `${y}-${m}-${d} ${h}:${mi}:${s}`
}

export function formatDateTimeString(dateString: string | null | undefined): string {
    if (!dateString) return ""; // 빈값 처리
    const date = new Date(dateString);
    const y = date.getFullYear();
    const m = pad(date.getMonth() + 1);
    const d = pad(date.getDate());
    const h = pad(date.getHours());
    const mi = pad(date.getMinutes());
    const s = pad(date.getSeconds());
    return `${y}-${m}-${d} ${h}:${mi}:${s}`;
}

export function todayFormatDate():string {
    const string = formatDate(now);
    console.log('todayFormatDate:',string);
    return string;
}

export function firstDay():string {
    return formatLocalToT(new Date(now.getFullYear(), now.getMonth(), 1));
}
export function lastDay():string {
    return formatLocalToT(new Date(now.getFullYear(), now.getMonth() +1, 0));
}
export function beforeYearFirstDay():string {
    return formatLocalToT(new Date(now.getFullYear() -1, 0, 1));
}