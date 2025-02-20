import { defineStore } from 'pinia';
import { router } from '@/router';
import { fetchWrapper } from '@/utils/helpers/fetch-wrapper';
import {getAuthData} from "@/data/Axios";

const baseUrl = `${import.meta.env.VITE_API_URL}/users`;
const apiUrl = `/api`;

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
            const user = await fetchWrapper.post(`${baseUrl}/authenticate`, { username, password });

            // update pinia state
            this.user = user;
            // store user details and jwt in local storage to keep user logged in between page refreshes
            localStorage.setItem('user', JSON.stringify(user));
            // redirect to previous url or default to home page
            router.push(this.returnUrl || '/dashboards/modern');
        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');
            router.push('/');
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
