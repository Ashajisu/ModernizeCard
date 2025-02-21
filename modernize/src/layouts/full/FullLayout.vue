<script setup lang="ts">
import { RouterView } from 'vue-router';
import VerticalSidebarVue from './vertical-sidebar/VerticalSidebar.vue';
import CustomVerticalHeader from './custom/CustomVerticalHeader.vue';
import CustomHorizontalHeader from './custom/CustomHorizontalHeader.vue';
import HorizontalSidebar from './horizontal-sidebar/HorizontalSidebar.vue';
import Customizer from './customizer/Customizer.vue';
import CustomTabs from "@/layouts/full/custom/CustomTabs.vue";
import { useCustomizerStore } from '@/stores/customizer';
const customizer = useCustomizerStore();

import { useTabStore } from '@/stores/tabStore';
const tabStore = useTabStore();
</script>
<!-- CustomVerticalHeader 확인받고, CustomHorizontalHeader 작성하기 2.21-->
<template>
    <!-----RTL LAYOUT------->
    <v-locale-provider  v-if="customizer.setRTLLayout"  rtl >
        <v-app
            :theme="customizer.actTheme"
            :class="[
                customizer.actTheme,
                customizer.mini_sidebar ? 'mini-sidebar' : '',
                customizer.setHorizontalLayout ? 'horizontalLayout' : 'verticalLayout',
                customizer.setBorderCard ? 'cardBordered' : ''
            ]"
        >
            <!---Customizer location left side--->
            <v-navigation-drawer app temporary elevation="10" location="left" v-model="customizer.Customizer_drawer" width="320" class="left-customizer">
            <Customizer />
            </v-navigation-drawer>
            <VerticalSidebarVue v-if="!customizer.setHorizontalLayout" />
            <CustomVerticalHeader v-if="!customizer.setHorizontalLayout" />
            <CustomHorizontalHeader v-if="customizer.setHorizontalLayout" />
            <HorizontalSidebar v-if="customizer.setHorizontalLayout" />

            <v-main>
                <CustomTabs>
                    <v-container fluid class="page-wrapper pb-sm-15 pb-10">
                        <div :class="customizer.boxed ? 'maxWidth' : ''">
                            <RouterView v-slot="{ Component }">
                              <keep-alive :include="tabStore.cachedTabs" :max="tabStore.maxTabs">
                                <component :is="Component" :key="tabStore.activeTab" />
                              </keep-alive>
                            </RouterView>
                            <v-btn
                                class="customizer-btn"
                                size="large"
                                icon
                                variant="flat"
                                color="primary"
                                @click.stop="customizer.SET_CUSTOMIZER_DRAWER(!customizer.Customizer_drawer)"
                            >
                                <SettingsIcon />
                            </v-btn>
                        </div>
                    </v-container>
                </CustomTabs>
            </v-main>
        </v-app>
    </v-locale-provider>

     <!-----LTR LAYOUT------->
    <v-locale-provider  v-else> 
        <v-app
            :theme="customizer.actTheme"
            :class="[
                customizer.actTheme,
                customizer.mini_sidebar ? 'mini-sidebar' : '',
                customizer.setHorizontalLayout ? 'horizontalLayout' : 'verticalLayout',
                customizer.setBorderCard ? 'cardBordered' : ''
            ]"
        >
            <!---Customizer location right side--->
            <v-navigation-drawer app temporary elevation="10" location="right" v-model="customizer.Customizer_drawer" width="320">
                <Customizer />
            </v-navigation-drawer>
            <VerticalSidebarVue v-if="!customizer.setHorizontalLayout" />
            <CustomVerticalHeader v-if="!customizer.setHorizontalLayout" />
            <CustomHorizontalHeader v-if="customizer.setHorizontalLayout" />
            <HorizontalSidebar v-if="customizer.setHorizontalLayout" />

            <v-main>
                <CustomTabs>
                <v-container fluid class="page-wrapper pb-sm-15 pb-10">
                    <div :class="customizer.boxed ? 'maxWidth' : ''">
                          <RouterView v-slot="{ Component }">
                            <keep-alive :include="tabStore.cachedTabs" :max="tabStore.maxTabs">
                                <component :is="Component" :key="tabStore.activeTab" />
                            </keep-alive>
                          </RouterView>
                        <v-btn
                            class="customizer-btn"
                            size="large"
                            icon
                            variant="flat"
                            color="primary"
                            @click.stop="customizer.SET_CUSTOMIZER_DRAWER(!customizer.Customizer_drawer)"
                        >
                            <SettingsIcon />
                        </v-btn>
                    </div>
                </v-container>
                </CustomTabs>
            </v-main>
        </v-app>
    </v-locale-provider>
</template>
