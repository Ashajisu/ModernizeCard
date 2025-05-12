import { defineStore } from 'pinia';
import { router } from '@/router';
import { fetchWrapper } from '@/utils/helpers/fetch-wrapper';
import { apiClient } from "@/data/Axios";

const baseUrl = `${import.meta.env.VITE_API_URL}`;
// const apiUrl = `/api`;

export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        // @ts-ignore
        user: JSON.parse(localStorage.getItem('user')),
        returnUrl: null,
        config : { iframe : false }
    }),
    actions: {
        async login(username: string, password: string) {

            const user = await fetchWrapper.post(`${baseUrl}/users/authenticate`, { username, password });
            // update pinia state
            this.user = user;

            //test JWT
            await apiClient.post(`${baseUrl}/auth/login`, { username, password }).then( response => {
                console.log(response);
                const token = response?.token; // JWT 토큰 추출
                if (token) {
                    // 로컬 저장소에 토큰 저장
                    this.user.token = token // 사용자 상태에 토큰 추가
                    console.log(token);
                }
            });

            // store user details and jwt in local storage to keep user logged in between page refreshes
            localStorage.setItem('user', JSON.stringify(user));
            // redirect to previous url or default to home page
            await router.push(this.returnUrl || '/home/dashboard');
        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');
            router.push('/');
        },
        // JWT 토큰 존재 여부 확인
        getToken() {
            return this.user?.token || null;
        },
        // 인증 상태 확인
        isLoggedIn() {
            return !!this.getToken();
        },
        async fetchUserConfig() {
            try {
                //시스템 관리자가 개인별/부서별/본부별 부여한 기능버튼영역 config 정보 호출 : 미구현
                // const data = await axios.get('/api/config/user');
                // this.config = data.data;
            } catch (error) {
                alert(error);
                console.log(error);
            }
        },
    }
});
