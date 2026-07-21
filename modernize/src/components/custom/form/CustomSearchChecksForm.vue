<script setup lang="ts">
import type { DateRange, FormField } from '@/types/custom/InputTypes';
import { type ComponentPublicInstance, computed, getCurrentInstance, ref } from 'vue';
import CustomDialog from '@/components/custom/dialog/CustomSearchsDialog.vue';
import type { VForm } from 'vuetify/components';
import { useDisplay } from 'vuetify'
const { mobile } = useDisplay()

const props = defineProps<{
    formFields: FormField[];
    colsPerRow: number;
    edit: boolean;
    hideDetails?: boolean;
}>();

// props.edit 값을 반응형으로 유지
const isEditable = computed(() => props.edit);
const hideDetails = props.hideDetails ?? false; // props.hideDetails가 undefined이면 false로 설정

// Dialog 열림 여부
const { proxy } = getCurrentInstance()!;
const openSearchDialog = async (field: FormField) => {
    if (isEditable.value) {
        const rowDialog = proxy?.$refs[`dialog_${field.name}`] as ComponentPublicInstance<typeof CustomDialog>;
        const dialog = Array.isArray(rowDialog) ? rowDialog[0]?.$?.exposed : rowDialog?.$?.exposed;
        if (!!dialog) {
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
const formData = computed(() => {
    const data: Record<string, any> = {};
    props.formFields.forEach((field) => {
        data[field.name] = field.value;
    });
    return data;
});
// 외부에서 접근 가능하도록 노출
defineExpose({
    validateForm
});

// 반응형 폭
const colSettings: Record<number, { cols: number; sm: number; md: number; lg: number }> = {
    1: { cols: 12, sm: 12, md: 12, lg: 12 },
    2: { cols: 6, sm: 6,  md: 6,  lg: 6 },
    3: { cols: 6, sm: 6,  md: 4,  lg: 4 },
    4: { cols: 12, sm: 12, md: 6,  lg: 3 },
    5: { cols: 12, sm: 12, md: 6,  lg: 2 }
};
function useButtonColSpan(props: { formFields: FormField[]; colsPerRow: number }) {
    const colSetting = colSettings[props.colsPerRow] || colSettings[1];

    const visibleFieldCount = computed(() => props.formFields.filter((f) => f.type !== 'hidden').length);

    return computed(() => {
        const result = { cols:12, sm: 12, md: 12, lg: 12 }; // 기본값은 줄바꿈
        const fields = visibleFieldCount.value;

        const calcRemaining = (size: 'cols' | 'sm' | 'md' | 'lg') => {
            const used = fields * colSetting[size];
            const remaining = 12 - used;
            return remaining > 0 ? remaining : 12;
        };

        result.cols = calcRemaining('cols');
        result.sm = calcRemaining('sm');
        result.md = calcRemaining('md');
        result.lg = calcRemaining('lg');

        return result;
    });
}

const rules = [(v: string) => !!v || '필수 입력 항목입니다.'];
</script>
<!-- single-line 입력시 label 옵션 적용 안됨 주의! -->
<!-- 자동검증을 위한 form 태그 추가 : form 제출로 인한 페이지 새로고침을 막기위해 lineBtn 슬롯 내 버튼에는 type="submit"을 주지 말것-->
<!-- type 항목: select, search, search_list, password, date, datetime, check, switch, text, slot, hidden, 그 외는 공백처리-->
<!-- colsPerRow 값은 한줄에 나열하고 싶은 필드의 갯수 기준, btn은 포함하지 않음. -->
<!-- LineBtn 은 내부 버튼을 자동 정렬하므로 추가 <div>는 불필요함. -->
<template>
    <v-container fluid class="pa-2 pa-sm-4">
        <slot name="topBtn" :validateForm="validateForm" />
        <v-form ref="formRef" lazy-validation="false">
            <v-row dense>
                <template v-for="field in formFields">
                    <v-col
                        v-bind="colSettings[colsPerRow > 5 ? 5 : colsPerRow]"
                        v-if="field.type !== 'hidden'"
                        class="pb-sm-1 pb-0 custom-height"
                    >
                        <v-select
                            v-if="field.type === 'select'"
                            return-object
                            variant="outlined"
                            v-model="field.value as string"
                            :items="field.options"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                            :hide-details="hideDetails"
                            persistent-placeholder
                        >
                            <template v-slot:label>
                                <span
                                    >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-select>

                        <div v-else-if="field.type === 'search' || field.type === 'search_list'">
                            <CustomDialog
                                :ref="`dialog_${field.name}`"
                                :title="field.label"
                                :single="field.type === 'search'"
                                :items="field.searchObj"
                                :searchField="field.name"
                                @update:selectedValue="
                                    (selectedValue: string[] | string) => {
                                        field.value = selectedValue;
                                    }
                                "
                            />
                            <v-text-field
                                v-model="field.value"
                                :rules="edit && field.required ? rules : []"
                                :readonly="!isEditable || field.disabled"
                                :hide-details="hideDetails"
                                @click="openSearchDialog(field)"
                                persistent-placeholder
                                density="compact"
                            >
                                <template v-slot:append-inner>
                                    <v-icon icon="mdi-account-search" class="text-right"></v-icon>
                                </template>
                                <template v-slot:label>
                                    <span
                                        >{{ field.label }}
                                        <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                    </span>
                                </template>
                            </v-text-field>
                        </div>

                        <v-text-field
                            v-else-if="field.type === 'password'"
                            color="primary"
                            variant="outlined"
                            type="password"
                            v-model="field.value"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            density="compact"
                        >
                            <template v-slot:label>
                                <span
                                    >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-text-field>
                        <v-text-field
                            v-else-if="field.type === 'date'"
                            color="primary"
                            variant="outlined"
                            type="date"
                            v-model="field.value"
                            @update:model-value="val => field.value = val === '' ? null : val"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            density="compact"
                        >
                            <template v-slot:label>
                                <span
                                    >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-text-field>
                        <v-row v-else-if="field.type === 'due'">
                            <v-col cols="12" sm="6">
                                <v-text-field
                                    type="date"
                                    v-model="(field.value as DateRange).startDate"
                                    @update:model-value="val => field.value = val === '' ? null : val"
                                    :label="field.label + ' 시작'"
                                    :rules="edit && field.required ? rules : []"
                                    :readonly="!isEditable || field.disabled"
                                    :hide-details="hideDetails"
                                    persistent-placeholder
                                    density="compact"
                                />
                            </v-col>

                            <v-col cols="12" sm="6">
                                <v-text-field
                                    type="date"
                                    v-model="(field.value as DateRange).endDate"
                                    @update:model-value="val => field.value = val === '' ? null : val"
                                    :label="field.label + ' 종료'"
                                    :rules="edit && field.required ? rules : []"
                                    :readonly="!isEditable || field.disabled"
                                    :hide-details="hideDetails"
                                    persistent-placeholder
                                    density="compact"
                                />
                            </v-col>
                        </v-row>
                        <v-text-field
                            v-else-if="field.type === 'month'"
                            color="primary"
                            variant="outlined"
                            type="month"
                            v-model="field.value"
                            @update:model-value="val => field.value = val === '' ? null : val"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            density="compact"
                        >
                            <template v-slot:label>
                                <span
                                >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-text-field>
                        <v-text-field
                            v-else-if="field.type === 'datetime'"
                            color="primary"
                            variant="outlined"
                            type="datetime-local"
                            v-model="field.value"
                            @update:model-value="val => field.value = val === '' ? null : val"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            class="w-100"
                            density="compact"
                        >
                            <template v-slot:label>
                                <span
                                    >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-text-field>

                        <div v-else-if="field.type === 'check'" class="outlined-checkbox">
                            <v-label class="checkbox-label"
                                >{{ field.label }}
                                <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                            </v-label>
                            <v-checkbox
                                v-for="(option, index) in field.options"
                                color="primary"
                                variant="outlined"
                                type="text"
                                density="compact"
                                hide-details
                                :key="index"
                                v-model="field.value"
                                multiple
                                :value="option"
                                :rules="edit && field.required ? rules : []"
                                :readonly="!isEditable || field.disabled"
                                :label="option"
                            >
                            </v-checkbox>
                        </div>
                        <v-switch
                            v-else-if="field.type === 'switch'"
                            hide-details
                            color="primary"
                            inset
                            v-model="field.value as boolean"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                        >
                            <template v-slot:label>
                                <span
                                    >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-switch>
                        <v-text-field
                            v-else-if="field.type === 'text'"
                            color="primary"
                            variant="outlined"
                            type="text"
                            v-model="field.value as string"
                            :rules="edit && field.required ? rules : []"
                            :readonly="!isEditable || field.disabled"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            density="compact"
                        >
                            <template v-slot:label>
                                <span
                                    >{{ field.label }}
                                    <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                                </span>
                            </template>
                        </v-text-field>
                        <template v-else-if="field.type === `slot`">
                            <slot :name="field.name" :field="field" />
                        </template>
                        <!--            공백처리-->
                        <div v-else-if="field.type === 'hidden'"></div>
                        <div v-else></div>
                    </v-col>
                </template>
                <!--          한줄 버튼 슬롯 : 줄 맞춤을 위한 col 폭 반응형-->
                <v-col v-bind="useButtonColSpan">
                    <div class="d-flex gap-2 justify-end flex-column flex-sm-row fill-height align-stretch w-100">
                        <slot name="lineBtn" :validateForm="validateForm" />
                    </div>
                </v-col>
            </v-row>
        </v-form>
    </v-container>
</template>

<style lang="scss" scoped>
.checkbox-label {
    position: absolute;
    top: -12px; /* outline 위로 위치 */
    left: 5%;
    //background-color: white; /* 배경색 */
    padding: 0 5px; /* 여백 추가 */
    font-size: x-small;
    color: rgb(var(--v-theme-textPrimary));
    background-color: rgb(var(--v-theme-surface));
}
.outlined-checkbox {
    outline: 1px solid rgb(var(--v-theme-inputBorder));
    border-radius: 6px;
    position: relative;
    padding: 5px;
    display: flex; /* 가로로 나열 */
    flex-wrap: wrap; /* 공간이 부족하면 자동 줄바꿈 */
}
@media (max-width:600px){
    .outlined-checkbox{
        flex-direction:column;
    }
}
:deep(.v-field-label){
    white-space:normal;
}
</style>
