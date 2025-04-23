// api-client.ts (Axios.ts 와 FetchWrapper.ts 통합)
import axios, { type AxiosResponse} from "axios";
import {router} from "@/router";
import {useAuthStore} from "@/stores/auth";
import { alert } from "@/common/alertService";
import {mockData} from "@/_mockApis/mockData";

const isBackendReady = false; // 백엔드 준비 여부를 나타내는 플래그 (임시)

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

function handleRedirect(response:AxiosResponse<any,any>): Promise<void> {
    const redirectLocation = response.status === 302 ? response.headers.location : null;
    if (redirectLocation.includes('/login')) {
            router.push('/auth/login');
            return Promise.reject(new Error('로그인 페이지로 이동합니다.'));
    }
    if (redirectLocation) {
        // 리다이렉션 URL로 push
        router.push(redirectLocation);
        return Promise.reject(new Error(`302 상태 코드로 리다이렉트: ${redirectLocation}`));
    }
    return Promise.resolve();
}
function handleHtmlResponse(response: AxiosResponse<any, any>): Promise<void> {
    const isHtmlResponse = typeof response.data === 'string' && response.data.trim().startsWith('<!DOCTYPE');
    const isLoginPage = router.currentRoute.value.path === '/auth/login'; // 리다이렉트 반복하지 않도록 로직 추가
    if (isHtmlResponse && !isLoginPage) {
        router.push('/auth/login');
        return Promise.reject(new Error('세션이 만료되었습니다. 다시 로그인하세요.'));
    }
    return Promise.resolve();
}
async function handleDataNull(response: AxiosResponse<any,any>): Promise<void> {
    const errorMessage = '데이터를 불러올 수 없습니다.';
    const isNullOrEmpty = response.data === null || (typeof response.data === "object" && Object.keys(response.data).length === 0);
    if (isNullOrEmpty) {
        await alert(errorMessage);
        return Promise.reject(new Error(errorMessage));
    }
    return Promise.resolve();
}
async function handleUnauthorized(): Promise<void> {
    await alert('세션이 만료되었습니다. 다시 로그인하세요.');
    router.push('/auth/login'); // 로그인 화면으로 리다이렉트
    return Promise.reject(new Error('인증이 필요합니다. 로그인 페이지로 이동합니다.'));
}
async function handleForbidden(): Promise<void> {
    await alert('이 작업을 수행할 권한이 없습니다.');
    return Promise.reject(new Error('권한이 없습니다.'));
}
async function handleNotFound(): Promise<void> {
    router.push('/not-found');
    return Promise.reject(new Error('페이지를 찾을 수 없습니다.'));
}
async function handleServerError(): Promise<void> {
    await alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
    return Promise.reject(new Error('서버 오류'));
}


// 요청 인터셉터: Authorization 헤더 추가
api.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    if (config.url?.includes('/auth/login')) {
        // 로그인 요청(Content-Type 동적 설정)
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded';
        if (config.data && typeof config.data === 'object') {
            // JSON 데이터를 URL-Encoded 데이터로 변환 - Spring Security 6의 기본 폼 데이터를 기반으로 한 로그인 요청에 사용함. 중첩 객체 및 배열 지원하지 않음. key-value 구조만 처리가능.
            config.data = new URLSearchParams(config.data).toString();
        }
    }

    if (authStore.user?.token) {
        config.headers.Authorization = `Bearer ${authStore.user.token}`;
    }
    console.log(`[Request]${config.method?.toUpperCase()}: ${config.url}`);
    return config;
});

// 응답 인터셉터: 에러 및 리다이렉트 처리
axios.interceptors.response.use(
    async response => {
        console.log('[Response]', response);
        // 백엔드가 준비되지 않았을 때 mockData로 응답 대체
        response = await fetchMockData(response) ?? response;

        // 응답데이터 검증 핸들러 병렬 호출 : 하나라도 예외상황인 경우 `Promise.reject` 호출, 즉시 요청 흐름은 중단 됨.
        await Promise.all([
            handleRedirect(response),   // 리다이렉트 검사
            handleHtmlResponse(response),// HTML 응답 검사
            handleDataNull(response),   //데이터 무결성 검사
        ]);
        return response;
    },
    async error => {
        console.error('[Error]', error);
        const authStore = useAuthStore();
            //예상되는 에러 직접 처리
            const status = error.response?.status;
            if (status === 401) await handleUnauthorized();
            else if (status === 403) await handleForbidden();
            else if (status === 404) await handleNotFound();
            else if (status === 500) await handleServerError();

        return Promise.reject(error);// 예기치 못한 에러
    }
);

// API Client 헬퍼 메서드
// body 값 주의 : undefined: body 자체가 생략됨. null: 명시적으로 body 에 null 이 전송됨. {}: body 에 빈 객체 전송
// 인터셉터에 의해 headers 자동 설정 됨.
export const apiClient = {
    get: async (url: string, params?: any) => {
        const response = await api.get(url, { params });
        return response.data;
    },
    post: async (url: string, body: any, headers = {}) => {
        const response = await api.post(url, body, {
            headers,
        });
        return response.data;
    },
    put: async (url: string, body: any) => {
        const response = await api.put(url, body);
        return response.data;
    },
    patch: async (url: string, body: any) => {
        const response = await api.patch(url, body);
        return response.data;
    },
    delete: async (url: string) => {
        const response = await api.delete(url);
        return response.data;
    },
};


async function fetchMockData(response:AxiosResponse<any,any>): Promise<AxiosResponse<any, any> | undefined> {
    // mockData에서 데이터 매핑
    if (!isBackendReady) {
        const method = response.config.method?.toUpperCase() as "GET" | "POST" | "PUT" | "PATCH" | "DELETE"?? "GET"; //인식불가 method 명은 GET 요청.
        const url = response.config.url??"";
        const mockResponse = mockData[url!]?.[method!];
        if(mockResponse){
            console.log(`[Mock Response] ${method} ${url}:`, mockResponse);
            return { ...response,  status: 200,  data: mockResponse };
        }else{
            return { ...response,  status: 400,  data: null };
        }
    }
    return undefined;
}

export default apiClient;