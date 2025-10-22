<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { Form } from 'vee-validate';
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";

/*Social icons*/
const checkbox = ref(false);
const valid = ref(false);
const password = ref('');
const username = ref('info@wrappixel.com');
const passwordRules = ref([
    (v: string) => !!v || 'Password is required',
    (v: string) => (v && v.length <= 10) || 'Password must be less than 10 characters'
]);
const emailRules = ref([(v: string) => !!v || 'E-mail is required', (v: string) => /.+@.+\..+/.test(v) || 'E-mail must be valid']);

function validate(values: any, { setErrors }: any) {
    const authStore = useAuthStore();
    return authStore.login(username.value, password.value).catch((error) => setErrors({ apiError: error }));
}

</script>
<!-- 환경설정 팝업, 비밀번호 변경 링크 수정해야 함-->
<template>
    <div class="d-flex align-center text-center mb-6">
        <div class="text-h6 w-100 px-5 font-weight-regular auth-divider position-relative">
        </div>
    </div>
    <Form @submit="validate" v-slot="{ errors, isSubmitting }" class="mt-5">
        <v-label class="text-subtitle-1 font-weight-semibold pb-2 text-lightText">아이디</v-label>
        <VTextField
            v-model="username"
            :rules="emailRules"
            class="mb-8"
            required
            hide-details="auto"
        ></VTextField>
        <v-label class="text-subtitle-1 font-weight-semibold pb-2 text-lightText">패스워드</v-label>
        <VTextField
            v-model="password"
            :rules="passwordRules"
            required
            hide-details="auto"
            type="password"
            class="pwdInput"
        ></VTextField>
        <div class="d-flex flex-wrap align-center my-3 ml-n2" style="min-width: 400px">
            <v-checkbox v-model="checkbox" :rules="[(v:any) => !!v || 'You must agree to continue!']" required hide-details color="primary">
                <template v-slot:label class="">로그인 상태를 유지합니다.</template>
            </v-checkbox>
        </div>
        <v-btn size="large" :loading="isSubmitting" color="primary" :disabled="valid" block type="submit" flat>Sign In</v-btn>
        <div v-if="errors.apiError" class="mt-2">
            <v-alert color="error">{{ errors.apiError }}</v-alert>
        </div>
        <v-row class="d-flex flex-wrap align-baseline mb-3 mt-3">
            <v-col cols="6" sm="6" >
                <CustomSlotDialog ref="settingDialog" title="환경설정">
                  <template v-slot:inCard>
                  </template>
                </CustomSlotDialog>
                <v-btn variant="outlined" size="large" class="border text-subtitle-1" block @click="( $refs.settingDialog as any )?.open()">
                    <SettingsIcon /><span class="d-sm-flex d-none mr-1"> 환경설정</span>
                </v-btn>
            </v-col>
            <v-col cols="6" sm="6">
                <RouterLink to="" class="text-primary text-decoration-none text-body-1 opacity-1 font-weight-medium"
                    > 비밀번호를 잊어버렸나요 ?</RouterLink
                >
            </v-col>
        </v-row>
    </Form>
</template>
