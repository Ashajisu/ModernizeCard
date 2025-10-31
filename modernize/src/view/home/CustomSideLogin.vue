<script setup lang="ts">
import Logo from '@/layouts/full/logo/Logo.vue';
/* Login form */
import CustomLoginForm from '@/components/custom/CustomLoginForm.vue';
</script>
<style lang="scss">
.auth-image-wrapper {
    height: 100%;
    width: 100%;
    background-image: url('../../assets/images/backgrounds/mone_3215.png');
    background-repeat: no-repeat;  /* 반복 방지 */
    background-size: cover;
    background-position: left top;
}
</style>

<template>
    <div class="">
        <v-row class="h-100vh mh-100 auth">
            <v-col cols="12" lg="7" xl="8"
                class="d-lg-flex align-center justify-center authentication position-relative">
                <div class="auth-header pt-lg-6 pt-2 px-sm-6 px-3 pb-lg-6 pb-0">
                    <div class="position-relative">
                        <Logo />
                    </div>
                </div>
                <div class="position-relative d-none d-lg-flex auth-image-wrapper"/>
            </v-col>
            <v-col cols="12" lg="5" xl="4" class="d-flex align-center justify-center">
                <div class="mt-xl-0 mt-5 mw-100">
                    <h2 class="text-h3 font-weight-bold mb-2">로그인</h2>
                    <div class="text-subtitle-1 mb-6"></div>
                    <CustomLoginForm />
                    <h6 class="text-h6  d-flex align-center mt-6 font-weight-medium">
                        <v-btn class="pl-0 text-primary text-body-1 opacity-1 pl-2 font-weight-medium" height="auto"
                            to="/auth/register" variant="plain"></v-btn>
                    </h6>
                </div>
            </v-col>
        </v-row>
    </div>
</template>
