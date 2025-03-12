<script setup lang="ts">
import type { FormField } from '@/types/custom/InputTypes';
import {computed, ref} from "vue";
import CustomDialog from "@/components/custom/CustomDialog.vue";

const props = defineProps<{
  formFields: FormField[];
  colsPerRow: number;
  edit: boolean;
}>();

// props.edit 값을 반응형으로 유지
const isEditable = computed(() => props.edit);

// Dialog 열림 여부
const viewDialog = ref(false);
const openSearchDialog = () => {
  viewDialog.value = true;
};

</script>
<!--formFields 배열을 사용해 동적으로 입력 필드 정보를 관리 : 1줄에 4개-->
<!--"select" 타입이면 <v-autocomplete>을 사용하여 options 목록을 드롭다운으로 표시, -->
<!--"text" 타입이면 <v-text-field>을 사용하여 일반 입력 필드 표시-->
<template>
  <v-container>
    <v-row class="mb-1">
      <v-col :cols="12 / colsPerRow" v-for="(field, index) in formFields" :key="index">
        <v-row class="align-center">
          <!--                  라벨-->
          <v-col cols="3" sm="3" class="pb-sm-1 pb-0">
            <v-label class="font-weight-medium">{{ field.label }}
              <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
            </v-label>
          </v-col>
          <!--                  입력란-->
          <v-col cols="9" sm="9" class="pb-sm-1 pb-0 custom-height">
            <v-select v-if="field.type === 'select'" return-object single-line variant="outlined"
                      v-model="field.value as string"
                      :items="field.options"
                      :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                      :readonly="!isEditable || field.disabled"></v-select>
            <v-text-field v-else-if="field.type === 'search'"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"
                          @click="openSearchDialog">
              <template v-slot:append-inner>
                <v-icon icon="mdi-account-search" class="text-right"></v-icon>
              </template>
              <CustomDialog v-model:view="viewDialog" :title="field.label" :items="field.searchObj"  @update:selectedValue="(selectedValue : string) => { field.value = selectedValue}" />
            </v-text-field>
            <v-text-field v-else-if="field.type === 'password'" color="primary" variant="outlined" type="password"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"></v-text-field>
            <v-text-field v-else-if="field.type === 'date'" color="primary" variant="outlined" type="date"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"></v-text-field>
            <v-checkbox v-else-if="field.type === 'check'" color="primary" variant="outlined" type="text"
                        v-model="field.value as boolean"
                        :placeholder="field.placeholder"
                        :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                        :readonly="!isEditable || field.disabled"></v-checkbox>
            <v-switch v-else-if="field.type === 'switch'"  hide-details color="primary" inset
                      :label="field.placeholder"
                      v-model="field.value as boolean"
                      :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                      :readonly="!isEditable || field.disabled"></v-switch>
            <v-text-field v-else color="primary" variant="outlined" type="text"
                          v-model="field.value"
                          :placeholder="field.placeholder"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"></v-text-field>
          </v-col>
        </v-row>
      </v-col>
      <!--          한줄 버튼 슬롯-->
      <v-col :cols="12 / colsPerRow">
        <slot/>
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