<script setup lang="ts">
import { computed } from 'vue';
import type { FormField } from '@/types/custom/InputTypes';

const props = defineProps<{
  formFields: FormField[];
  onSave: (formData : any) => void;
}>();

const formData = computed(()=> {
  const data: Record<string, any> = {};
  props.formFields.forEach(field => {
    data[field.name] = field.value;
  });
  return data;
});

const handleSave = () => {
  // 부모 컴포넌트로 computed 된 formData 를 전달
  console.log('Form Data:', formData.value); // formData.value 값 확인
  props.onSave(formData.value);
};
</script>
<!--formFields 배열을 사용해 동적으로 입력 필드 정보를 관리 : 1줄에 4개-->
<!--"select" 타입이면 <v-autocomplete>을 사용하여 options 목록을 드롭다운으로 표시, -->
<!--"text" 타입이면 <v-text-field>을 사용하여 일반 입력 필드 표시-->
<template>
    <v-container>
        <v-row>
            <v-col cols="12" sm="6" v-for="(field, index) in formFields" :key="index">
                <v-row class="align-center mb-3">
<!--                  라벨-->
                    <v-col cols="12" sm="3" class="pb-sm-3 pb-0">
                        <v-label class="font-weight-medium">{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </v-label>
                    </v-col>
<!--                  입력란-->
                    <v-col cols="12" sm="9" class="pb-sm-3 pb-0 custom-height">
                      <v-select v-if="field.type === 'select'" return-object single-line variant="outlined"
                                      v-model="field.value as string"
                                      :items="field.options"
                                      :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                      :readonly="field.disabled"></v-select>
                      <v-input v-else-if="field.type === 'search'"
                               v-model="field.value"
                               :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                               :readonly="field.disabled"></v-input>
                      <v-text-field v-else-if="field.type === 'password'" color="primary" variant="outlined" type="password"
                                    v-model="field.value"
                                    :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                      ></v-text-field>
                      <v-text-field v-else-if="field.type === 'date'" color="primary" variant="outlined" type="date"
                                    v-model="field.value"
                                    :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                    ></v-text-field>
                      <v-text-field v-else color="primary" variant="outlined" type="text"
                                    v-model="field.value"
                                    :placeholder="field.placeholder"
                                    :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                    :readonly="field.disabled"></v-text-field>
                    </v-col>
                </v-row>
            </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" sm="9" offset-sm="10">
          <v-btn color="primary" flat @click="handleSave()">수정</v-btn>
          <v-btn color="primary" flat @click="handleSave()">닫기</v-btn>
        </v-col>
      </v-row>
    </v-container>
</template>

<style scoped>
.custom-height {
  min-height: 50px !important;
  height: 60px !important;
}
</style>