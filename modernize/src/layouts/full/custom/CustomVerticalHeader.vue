<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { useCustomizerStore } from '@/stores/customizer';
import { Menu2Icon } from 'vue-tabler-icons';
import CustomProfileDD from './CustomProfileDD.vue';
import Searchbar from '../vertical-header/Searchbar.vue';
import RightMobileSidebar from '../vertical-header/RightMobileSidebar.vue';
import ThemeToggler from '../vertical-header/ThemeToggler.vue';
import HeaderCards from "@/views/dashboard/dashboardComponents/modern/HeaderCards.vue";
import {useChatStore} from "@/stores/apps/chat";

const customizer = useCustomizerStore();
const appsdrawer = ref(false);
const priority = ref(customizer.setHorizontalLayout ? 0 : 0);
watch(priority, (newPriority) => {
    priority.value = newPriority;
});

// store 정보를 출력하고, 해당 상세페이지로 이동
const store = useChatStore();
const getChat = computed(() => {
  const lastChatName = store.countRecentChats[0]?.name;
  const lastChatHistory = store.countRecentChats[0]?.chatHistory;
  if(!lastChatHistory){
    return '';
  }
  const lastIndex = lastChatHistory?.length > 0 ? lastChatHistory.length -1 : 0;
  let lastChatMsg = lastChatHistory[lastIndex]?.msg;
  if(lastChatMsg.length > 15){
    lastChatMsg = lastChatMsg.slice(15) + '...';
  }
  return`${lastChatName} : ${lastChatMsg}`;
});

const clickChat = (()=>{
  const lastChatId = store.countRecentChats[0]?.id;
  store.SelectChat(lastChatId);
});

// config iframe
import { useAuthStore } from "@/stores/auth";
import CustomTabs from "@/layouts/full/custom/CustomTabs_renewal.vue";
import config from "@/config";
const authStore = useAuthStore();
</script>
<!--chat.ts 에서 최근 메세지 개수를 가져와 배지에 띄움-->
<!--최근 메세지의 보낸이, 메세지내용(15자 제한) 으로 가져와 출력함-->
<!--알림창을 클릭하면 해당 메세지 상세페이지로 바로 이동함-->
<!--fetchUserConfig : 랜더링 시점에 관리자정의한 기능버튼영역의 config 값을 불러옴.-->
<template>
    <v-app-bar elevation="0" :priority="priority" height="70" id="top" class="">
        <v-btn
            class="hidden-md-and-down"
            icon
            color="primary"
            variant="text"
            @click.stop="customizer.SET_MINI_SIDEBAR(!customizer.mini_sidebar)"
        >
            <Menu2Icon size="20" stroke-width="1.5" />
        </v-btn>
        <v-btn class="hidden-lg-and-up" icon variant="flat" @click.stop="customizer.SET_SIDEBAR_DRAWER" size="small">
            <Menu2Icon size="20" stroke-width="1.5" />
        </v-btn>

        <!-- ---------------------------------------------- -->
        <!-- Search part -->
        <!-- ---------------------------------------------- -->
        <v-sheet v-if="customizer.setSearchbar">
            <Searchbar />
        </v-sheet>

        <!---/Search part -->

        <!-- ---------------------------------------------- -->
        <!-- Mega menu -->
        <!-- ---------------------------------------------- -->
        <div class="me-2" v-if="authStore.config.iframe">
            <iframe :src="`https://www.youtube.com/embed/-ePIj-6autg?si=9OK35ecy_LVZPsFC`"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    width="100%" height="60"
                    allowfullscreen>
            </iframe>
        </div>
        <v-spacer />
        <!-- ---------------------------------------------- -->
        <!---right part -->
        <!-- ---------------------------------------------- -->
        <div class="me-2" v-if="customizer.setThemeToggle">
            <ThemeToggler />
        </div>
        <!-- ---------------------------------------------- -->
        <!-- translate -->
        <!-- ---------------------------------------------- -->

        <!-- ---------------------------------------------- -->
        <!-- ShoppingCart -->
        <!-- ---------------------------------------------- -->
        <div class="me-2" v-if="customizer.setDashboard">
            <v-btn variant="plain" color="primary" :to="config.mainPage" class="custom-hover-primary" size="x-large">
              <HeaderCards />
            </v-btn>
        </div>
        <!-- ---------------------------------------------- -->
        <!-- Notification -->
        <!-- ---------------------------------------------- -->

        <!-- right sidebar -->
<!--        <div class="me-2" v-if="customizer.setDashboard">-->
<!--          <v-badge color="error" :content="store.countRecentChats?.length">-->
<!--            <v-btn variant="text" color="primary" to="/apps/chats" @click.stop="clickChat" class="custom-hover-primary" size="small">-->
<!--              &lt;!&ndash;                      <DashboardIcon/><span>요약 Dashboard</span>&ndash;&gt;-->
<!--              <span> {{getChat || ''}} </span>-->
<!--            </v-btn>-->
<!--          </v-badge>-->
<!--        </div>-->

        <!-- ---------------------------------------------- -->
        <!-- User Profile -->
        <!-- ---------------------------------------------- -->
        <div class="ml-2">
            <CustomProfileDD />
        </div>
    </v-app-bar>
    <v-app-bar elevation="0" :priority="priority" height="50" id="top" tag="div">
        <CustomTabs/>
    </v-app-bar>

    <!-- ---------------------------------------------- -->
    <!-- Right Sidebar -->
    <!-- ---------------------------------------------- -->
    <v-navigation-drawer v-model="appsdrawer" location="right" temporary>
        <RightMobileSidebar />
    </v-navigation-drawer>
</template>
