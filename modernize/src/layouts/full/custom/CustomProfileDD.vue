<script setup lang="ts">
import { MailIcon } from 'vue-tabler-icons';
import { profileDD } from '@/_mockApis/headerData';
import {computed} from "vue";

import { useAuthStore } from '@/stores/auth';
const authStore = useAuthStore();
import config from "@/config";

const userName = computed(()=>{
  return authStore.user.username.split('@')[0] || 'username';
})
const userFiles = import.meta.glob<{ default : string }>("@/assets/images/profile/*.jpg", { eager: true });
const defaultPath = '/src/assets/images/profile/default_user.jpg';

const userSrc = computed(() => {
  const userPath = `/src/assets/images/profile/${config.siteName}_${userName.value}.jpg`;
  // const userPath ='';
  // console.log(authStore.user);
  return userFiles[userPath]?.default || userFiles[defaultPath]?.default || '';
});

const dynamicMaxHeight = computed(()=>{
  return Math.min(profileDD.length * 80, 240);
})
</script>
<!--@/assets/images/profile/회사명_사원아이디.jpg-->
<!--@/store/auth 에서 user.username 에 사원아이디@이메일이 저장되어 있어야함.-->
<!--@/assets/images/profile/default_user.jpg 있어야함.-->

<!--런타임 시점에 정적인 패턴을 기반으로 경로 내 일치하는 패턴의 이미지를 모두 가져와 객체로 저장-->
<!--이후 siteName, userName, defaultPath 값에 따라 반응형으로 이미지를 조회하여 출력함-->
<!--이미지를 특정파일만 호출하려면 await, eager : false 를 사용해 비동기처리해야함-->

<!--profileDD 배열갯수가 1-2개라면 팝업크기 조절, 3개 이상부터는 고정사이즈에 스크롤 추가-->
<!--뷰포트 크기가 줄어들면 그에맞게 조절하고 스크롤 추가-->
<template>
    <!-- ---------------------------------------------- -->
    <!-- notifications DD -->
    <!-- ---------------------------------------------- -->
    <v-menu :close-on-content-click="false">
        <template v-slot:activator="{ props }">
            <v-btn class="custom-hover-primary" variant="text" v-bind="props" icon>
                <v-avatar size="35">
                    <img :src="userSrc" width="35" :alt="userName" />
                </v-avatar>
            </v-btn>
        </template>
        <v-sheet rounded="md" width="360" elevation="10">
            <div class="px-8 pt-6">
                <h6 class="text-h5 font-weight-medium"></h6>
                <div class="d-flex align-center mt-4 pb-6">
                    <v-avatar size="80">
                        <img :src="userSrc" width="80" :alt="userName"/>
                    </v-avatar>
                    <div class="ml-3">
                        <h6 class="text-h6 mb-n1">{{ authStore.user.firstName }}{{ authStore.user.lastName }}</h6>
                        <span class="text-subtitle-1 font-weight-regular textSecondary">&nbsp</span>
                        <div class="d-flex align-center mt-1">
                            <MailIcon size="18" stroke-width="1.5" />
                            <span class="text-subtitle-1 font-weight-regular textSecondary ml-2">{{ authStore.user.username }}</span>
                        </div>
                    </div>
                </div>
                <v-divider></v-divider>
            </div>
            <perfect-scrollbar :style="{ height: `calc(100vh - ${dynamicMaxHeight}px`, maxHeight: `${dynamicMaxHeight}px`}">
                <v-list class="py-0 theme-list" lines="two">
                    <v-list-item v-for="item in profileDD" :key="item.title" class="py-4 px-8 custom-text-primary" :to="item.href">
                        <template v-slot:prepend>
                            <v-avatar size="48" color="lightprimary" class="mr-3" rounded="md">
                                <img :src="item.avatar" width="24" height="24" :alt="item.avatar" />
                            </v-avatar>
                        </template>
                        <div>
                            <h6 class="text-subtitle-1 font-weight-bold mb-2 custom-title">{{ item.title }}</h6>
                        </div>
                        <p class="text-subtitle-1 font-weight-regular textSecondary">{{ item.subtitle }}</p>
                    </v-list-item>
                </v-list>
            </perfect-scrollbar>
            <div class="pt-4 pb-6 px-8 text-center">
                <v-btn color="primary" variant="outlined" block @click="authStore.logout()">Logout</v-btn>
            </div>
        </v-sheet>
    </v-menu>
</template>
