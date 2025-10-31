<script setup lang="ts">
import { ref, watch } from 'vue';
import { useCustomizerStore } from '@/stores/customizer';
// Icon Imports
import { SearchIcon, Menu2Icon } from 'vue-tabler-icons';
import Logo from '../logo/Logo.vue';
import RtlLogo from '../logo/RtlLogo.vue';

// dropdown imports
import CustomProfileDD from './CustomProfileDD.vue';
import Searchbar from '../vertical-header/Searchbar.vue';
import RightMobileSidebar from '../vertical-header/RightMobileSidebar.vue';
import ThemeToggler from '../vertical-header/ThemeToggler.vue';
import HeaderCards from "@/views/dashboard/dashboardComponents/modern/HeaderCards.vue";

const customizer = useCustomizerStore();
const showSearch = ref(false);
const appsdrawer = ref(false);
const priority = ref(customizer.setHorizontalLayout ? 0 : 0);
function searchbox() {
    showSearch.value = !showSearch.value;
}
watch(priority, (newPriority) => {
    // yes, console.log() is a side effect
    priority.value = newPriority;
});

// config iframe
import { useAuthStore } from "@/stores/auth";
import CustomTabs from "@/layouts/full/custom/CustomTabs_renewal.vue";
import config from "@/config";
const authStore = useAuthStore();
</script>

<template>
    <v-app-bar elevation="10" :priority="priority" height="70" class="horizontal-header">
        <div :class="customizer.boxed ? 'maxWidth v-toolbar__content' : 'v-toolbar__content px-6'">
            <v-locale-provider v-if="customizer.setRTLLayout" rtl>
                <div class="hidden-md-and-down">
                    <RtlLogo />
                </div>
            </v-locale-provider>
            <v-locale-provider v-else>
                <div class="hidden-md-and-down">
                    <Logo />
                </div>
            </v-locale-provider>

            <v-btn class="hidden-lg-and-up ms-3" icon rounded="sm" variant="flat" @click.stop="customizer.SET_SIDEBAR_DRAWER" size="small">
                <Menu2Icon size="20" stroke-width="1.5" />
            </v-btn>

            <!-- search mobile -->
            <v-btn class="hidden-lg-and-up ms-lg-3" icon rounded="sm" variant="flat" size="small" @click="searchbox" v-if="customizer.setSearchbar">
                <SearchIcon size="17" stroke-width="1.5" />
            </v-btn>

            <v-sheet v-if="showSearch && customizer.setSearchbar" class="search-sheet v-col-12">
                <Searchbar :closesearch="searchbox" />
            </v-sheet>

            <!-- ------------------------------------------------>
            <!-- Search part -->
            <!-- ------------------------------------------------>
            <v-sheet class="mx-2 d-none d-lg-block" v-if="customizer.setSearchbar">
                <Searchbar />
            </v-sheet>

            <!---/Search part -->

            <!-- ------------------------------------------------>
            <!-- Mega menu -->
            <!-- ------------------------------------------------>
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
            <!-- Notification -->
            <!-- ---------------------------------------------- -->
            <div class="me-2" v-if="customizer.setDashboard">
              <v-btn variant="plain" color="primary" :to="config.mainPage" class="custom-hover-primary" size="x-large">
                <HeaderCards />
              </v-btn>
            </div>
            <!-- ---------------------------------------------- -->
            <!-- ShoppingCart -->
            <!-- ---------------------------------------------- -->

            <!-- right sidebar -->

            <!-- ---------------------------------------------- -->
            <!-- User Profile -->
            <!-- ---------------------------------------------- -->
            <div class="ml-3 mr-sm-0 mr-3">
                <CustomProfileDD />
            </div>
        </div>
    </v-app-bar>
    <v-app-bar elevation="10" :priority="priority" height="50" class="horizontal-tabs">
        <CustomTabs/>
    </v-app-bar>

    <v-navigation-drawer v-model="appsdrawer" location="right" temporary>
        <RightMobileSidebar />
    </v-navigation-drawer>
</template>
