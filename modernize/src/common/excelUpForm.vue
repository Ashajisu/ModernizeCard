<script setup lang="ts">
import { ref } from 'vue';
import { parseExcel } from '@/common/excelService';
import type {VForm} from "vuetify/components"; // 여기서 분리한 함수 사용

const formRef = ref<VForm | null>(null);
const file = ref<File | null>(null);
const errorMessage = ref<string>('');

// 제네릭 타입을 외부에서 주입받음
const onSave = async <T extends Record<string, any>>(): Promise<T[] | undefined> => {
    if (!file.value) {
        errorMessage.value = '파일을 입력하세요.';
        return;
    }else{
        errorMessage.value = '';
      console.log(file.value);
    }
    const data = await parseExcel<T>(file.value);
    if (!data || data.length === 0) {
        errorMessage.value = '유효한 데이터가 없습니다.';
        return;
    }

    errorMessage.value = ''; // 에러 초기화
    return data;
};

const validateForm = async (): Promise<File | null> => {
  const valid = await formRef.value?.validate();
  return valid?.valid && file.value ? file.value : null;
};

// 외부에서 접근 가능하도록 노출
defineExpose({
    onSave, validateForm
});
</script>
<!-- 아직 기능 미구현 -->
<template>
    <v-form ref="formRef" v-model="validateForm">
        <v-file-input v-model="file" accept=".xlsx, .xls" width="100%" dense label="엑셀 파일을 선택하세요"

                      :rules="[v => !!v || '파일을 선택하세요']"
                      :error-messages="errorMessage">
        </v-file-input>
    </v-form>
</template>

<style scoped lang="scss"></style>