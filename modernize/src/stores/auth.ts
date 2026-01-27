import { defineStore } from 'pinia';
import { router } from '@/router';
import { apiClient } from "@/data/Axios";

export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({
        // initialize state from local storage to enable user to stay logged in
        // @ts-ignore
        user: JSON.parse(localStorage.getItem('user')),
        token: localStorage.getItem('token'),
        returnUrl: null,
        config : { iframe : false }
    }),
    actions: {
        async login(username: string, password: string) {
            await apiClient.post(`/auth/login`, { username, password }).then( async response => {
                const { token, user } = response;
                this.token = token;
                this.user = user;
                localStorage.setItem('token', token);
                localStorage.setItem('user', JSON.stringify(this.user));
                await router.push(this.returnUrl || '/home/dashboard');
            });
        },
        logout() {
            this.user = null;
            localStorage.removeItem('user');
            router.push('/');
        },
        async loginCheckCookie() {
            //서버 에러시 쿠키로그인 실패처리
            try {
                return await apiClient.get(`/auth/me`).then( async response => {
                    if(response.loggedIn){
                        const user = response.user;
                        localStorage.setItem('user', JSON.stringify(user));
                    }else {
                        console.debug("loginCheckCookie loggedIn not found ");
                    }
                    return response.loggedIn;
                });
            }catch (error) {
                console.debug("loginCheckCookie error: ", error);
                return false;
            }
        },
        async fetchOptions() {
            try {
                await apiClient.get('/api/base/option').then(res => {
                    // this.deptOptions = res.dept;
                    // this.authOptions = res.userType;
                    // this.licenseOptions = res.license;
                    // console.log("selectOption", this.deptOptions, this.authOptions, this.licenseOptions)
                });
            } catch (error) {
                alert(error);
                console.log(error);
            }
        },
    }
});
