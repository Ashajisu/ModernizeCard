<script setup lang="ts">
import type { FormField } from '@/types/custom/InputTypes';
import {computed} from "vue";
import CustomDialog from "@/components/custom/dialog/CustomSearchDialog.vue";

const props = defineProps<{
  formFields: FormField[];
  colsPerRow: number;
  edit: boolean;
}>();

// props.edit 값을 반응형으로 유지
const isEditable = computed(() => props.edit);

// Dialog 열림 여부
const openSearchDialog = (field : FormField) => {
  if(isEditable.value){ field.view = true; }
  // console.log('done', isEditable.value, field.view); //두 값이 항상 같아야 함.
};

</script>
<!-- 다중 체크박스를 지원 -->
<!-- single-line 입력시 label 옵션 적용 안됨 주의! -->
<template>
  <v-container>
    <v-row class="mb-6">
      <v-col :cols="12 / colsPerRow" v-for="(field, index) in formFields" :key="index">
        <v-row class="align-center">
          <v-col cols="12" sm="12" class="pb-sm-1 pb-0 custom-height">
            <v-select v-if="field.type === 'select'" return-object variant="outlined"
                      v-model="field.value as string"
                      :items="field.options"
                      :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                      :readonly="!isEditable || field.disabled"
                      :label="field.label" persistent-placeholder/>
            <v-text-field v-else-if="field.type === 'search'"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"
                          @click="openSearchDialog(field)"
                          :label="field.label" persistent-placeholder>
              <template v-slot:append-inner>
                <v-icon icon="mdi-account-search" class="text-right"></v-icon>
              </template>
              <CustomDialog v-if="isEditable && field.view" v-model:view="field.view" :title="field.label" :items="field.searchObj" :searchField="field.name"  @update:selectedValue="(selectedValue : string) => { field.value = selectedValue}" />
            </v-text-field>
            <v-text-field v-else-if="field.type === 'password'" color="primary" variant="outlined" type="password"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"
                          :label="field.label"></v-text-field>
            <v-text-field v-else-if="field.type === 'date'" color="primary" variant="outlined" type="date"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"
                          :label="field.label"></v-text-field>
            <v-text-field v-else-if="field.type === 'datetime'" color="primary" variant="outlined" type="datetime-local"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"
                          :label="field.label"></v-text-field>
            <div v-else-if="field.type === 'check'" class="tiny-checkbox-container">
              <v-checkbox v-for="(option, index) in field.options" color="primary" variant="outlined" type="text" density="compact" hide-details
                          :key="index"
                          v-model="field.value" multiple
                          :value="option"
                          :readonly="!isEditable || field.disabled"
                          :label="field.label"></v-checkbox>
            </div>
            <v-switch v-else-if="field.type === 'switch'"  hide-details color="primary" inset
                      v-model="field.value as boolean"
                      :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                      :readonly="!isEditable || field.disabled"
                      :label="field.label"></v-switch>
            <v-text-field v-else-if="field.type === 'text'" color="primary" variant="outlined" type="text"
                          v-model="field.value"
                          :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                          :readonly="!isEditable || field.disabled"
                          :label="field.label" persistent-placeholder></v-text-field>
<!--            공백처리-->
            <div v-else></div>
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

.tiny-checkbox-container {
  display: flex; /* 가로로 나열 */
  flex-wrap: wrap; /* 공간이 부족하면 자동 줄바꿈 */
  gap: 2px; /* 체크박스 간격 */
  width: 100%; /* ✅ 부모 <v-col> 내에서만 출력 */
  height: 100%; /* ✅ 부모 <v-col> 내에서만 출력 */
}
</style>