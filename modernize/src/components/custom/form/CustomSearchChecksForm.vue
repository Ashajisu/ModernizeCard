<script setup lang="ts">
import type { FormField } from '@/types/custom/InputTypes';
import {type ComponentPublicInstance, computed, getCurrentInstance, ref} from "vue";
import CustomDialog from "@/components/custom/dialog/CustomSearchsDialog.vue";
import type {VForm} from "vuetify/components";

const props = defineProps<{
  formFields: FormField[];
  colsPerRow: number;
  edit: boolean;
}>();

// props.edit 값을 반응형으로 유지
const isEditable = computed(() => props.edit);

// Dialog 열림 여부
const { proxy } = getCurrentInstance()!;
const openSearchDialog = async (field: FormField) => {
  if (isEditable.value) {
    const rowDialog = proxy?.$refs[`dialog_${field.name}`] as ComponentPublicInstance<typeof CustomDialog>;
    const dialog = Array.isArray(rowDialog) ? rowDialog[0]?.$?.exposed : rowDialog?.$?.exposed;
    if(!!dialog){
      dialog?.open();
    }
  }
};

const formRef = ref<VForm | null>(null);
// 유효성 검사 함수
const validateForm = async () => {
  if (formRef.value) {
    const isValid = await formRef.value.validate();
    return isValid.valid ? formData.value : null;
  }
  return null;
};
const formData = computed(()=> {
  const data: Record<string, any> = {};
    props.formFields.forEach(field => {
    data[field.name] = field.value;
  });
  return data;
});
// 외부에서 접근 가능하도록 노출
defineExpose({
  validateForm
});
</script>
<!-- single-line 입력시 label 옵션 적용 안됨 주의! -->
<!-- 자동검증을 위한 form 태그 추가 : form 제출로 인한 페이지 새로고침을 막기위해 lineBtn 슬롯 내 버튼에는 type="submit"을 주지 말것-->
<!-- type 항목: select, search, search_list, password, date, datetime, check, switch, text, slot, hidden, 그 외는 공백처리-->
<template>
  <v-container>
      <slot name="topBtn" :validateForm="validateForm"/>
      <v-form ref="formRef" lazy-validation="false">
          <v-row class="mb-6">
            <template v-for="(field) in formFields">
              <v-col :cols="12 / colsPerRow" v-if="field.type!=='hidden'">
                <v-row class="align-center">
                  <v-col cols="12" sm="12" class="pb-sm-1 pb-0 custom-height">
                    <v-select v-if="field.type === 'select'" return-object variant="outlined"
                              v-model="field.value as string"
                              :items="field.options"
                              :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                              :readonly="!isEditable || field.disabled"
                              persistent-placeholder>
                      <template v-slot:label>
                        <span>{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </span>
                      </template>
                    </v-select>

                    <div v-else-if="(field.type === 'search' || field.type === 'search_list')">
                      <CustomDialog :ref="`dialog_${field.name}`"
                                    :title="field.label" :single="field.type === 'search'" :items="field.searchObj" :searchField="field.name"  @update:selectedValue="(selectedValue : string[] | string) => { field.value = selectedValue}" />
                      <v-text-field
                                    v-model="field.value"
                                    :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                    :readonly="!isEditable || field.disabled"
                                    @click="openSearchDialog(field)"
                                    persistent-placeholder>
                        <template v-slot:append-inner>
                          <v-icon icon="mdi-account-search" class="text-right"></v-icon>
                        </template>
                        <template v-slot:label>
                          <span>{{ field.label }}
                            <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                          </span>
                        </template>
                      </v-text-field>
                    </div>

                    <v-text-field v-else-if="field.type === 'password'" color="primary" variant="outlined" type="password"
                                  v-model="field.value"
                                  :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                  :readonly="!isEditable || field.disabled" persistent-placeholder>
                      <template v-slot:label>
                        <span>{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </span>
                      </template>
                    </v-text-field>
                    <v-text-field v-else-if="field.type === 'date'" color="primary" variant="outlined" type="date"
                                  v-model="field.value"
                                  :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                  :readonly="!isEditable || field.disabled" persistent-placeholder>
                      <template v-slot:label>
                        <span>{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </span>
                      </template>
                    </v-text-field>
                    <v-text-field v-else-if="field.type === 'datetime'" color="primary" variant="outlined" type="datetime-local"
                                  v-model="field.value"
                                  :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                  :readonly="!isEditable || field.disabled" persistent-placeholder hide-details style="min-width: 200px;">
                      <template v-slot:label>
                        <span>{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </span>
                      </template>
                    </v-text-field>
                    <div v-else-if="field.type === 'check'" class="outlined-checkbox">
                      <v-label class="checkbox-label">{{ field.label }}
                        <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                      </v-label>
                      <v-checkbox v-for="(option, index) in field.options" color="primary" variant="outlined" type="text" density="compact" hide-details
                                  :key="index"
                                  v-model="field.value" multiple
                                  :value="option"
                                  :readonly="!isEditable || field.disabled"
                                  :label="option">
                      </v-checkbox>
                    </div>
                    <v-switch v-else-if="field.type === 'switch'"  hide-details color="primary" inset
                              v-model="field.value as boolean"
                              :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                              :readonly="!isEditable || field.disabled">
                      <template v-slot:label>
                        <span>{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </span>
                      </template>
                    </v-switch>
                    <v-text-field v-else-if="field.type === 'text'" color="primary" variant="outlined" type="text"
                                  v-model="field.value as boolean"
                                  :rules="field.required ? [v => !!v || '필수 입력 항목입니다.'] : []"
                                  :readonly="!isEditable || field.disabled" persistent-placeholder>
                      <template v-slot:label>
                        <span>{{ field.label }}
                          <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                        </span>
                      </template>
                    </v-text-field>
                    <template v-else-if="field.type === `slot`">
                      <slot :name="field.name" :field="field" />
                    </template>
                    <!--            공백처리-->
                    <div v-else-if="field.type==='hidden'"></div>
                    <div v-else></div>
                  </v-col>
                </v-row>
              </v-col>
            </template>
              <!--          한줄 버튼 슬롯-->
              <v-col :cols="12 / colsPerRow">
                <slot name="lineBtn" :validateForm="validateForm"/>
              </v-col>
          </v-row>
      </v-form>
  </v-container>
</template>

<style scoped>
.checkbox-label{
  color: rgb(42, 53, 71);
  position: absolute;
  top: -12px; /* outline 위로 위치 */
  left: 5%;
  background-color: white; /* 배경색 */
  padding: 0 5px; /* 여백 추가 */
  font-size: x-small;
}
.outlined-checkbox {
  outline: 1px solid #dfe5ef;
  border-radius: 6px;
  position: relative;
  padding: 5px;
  display: flex; /* 가로로 나열 */
  flex-wrap: wrap; /* 공간이 부족하면 자동 줄바꿈 */
}
.custom-height {
  min-height: 50px !important;
  height: 60px !important;
}
</style>