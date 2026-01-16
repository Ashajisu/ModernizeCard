<script setup lang="ts">
import type { DateRange, FormField } from '@/types/custom/InputTypes';
import { type ComponentPublicInstance, computed, getCurrentInstance, ref, watch } from 'vue';
import CustomDialog from '@/components/custom/dialog/CustomSearchsDialog.vue';
import type { VForm } from 'vuetify/components';
import { getDeepValue, setDeepValue } from '@/utils/common';

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
// 클릭만으로는 안 열리고, 한 글자라도 입력되면 자동으로 팝업이 열리며, 그 입력값이 검색 키워드로 전달되는 구조
const { proxy } = getCurrentInstance()!;
const openSearchDialog = async (field: FormField, keyword: string) => {
    if (isEditable.value) {
        const rowDialog = proxy?.$refs[`dialog_${field.name}`] as ComponentPublicInstance<typeof CustomDialog>;
        const dialog = Array.isArray(rowDialog) ? rowDialog[0]?.$?.exposed : rowDialog?.$?.exposed;
        if (!!dialog) {
            dialog?.open(keyword);
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
        // ⬇ 날짜 필드는 포맷 변환
        if (field.type === 'datetime') {
            // setDeepValue(data, field.name, formatDateTimeString(field.value?.toString()));
            setDeepValue(data, field.name, field.value);
        } else {
            setDeepValue(data, field.name, field.value);
        }
    });
    return data;
});
// 외부에서 접근 가능하도록 노출
defineExpose({
    validateForm
});

// 반응형 폭
const colSettings: Record<number, { sm: number; md: number; lg: number }> = {
    1: { sm: 12, md: 12, lg: 12 },
    2: { sm: 12, md: 6, lg: 6 },
    3: { sm: 12, md: 6, lg: 4 },
    4: { sm: 12, md: 6, lg: 3 },
    5: { sm: 12, md: 6, lg: 2 }
};
function useButtonColSpan(props: { formFields: FormField[]; colsPerRow: number }) {
    const colSetting = colSettings[props.colsPerRow] || colSettings[1];

    const visibleFieldCount = computed(() => props.formFields.filter((f) => f.type !== 'hidden').length);

    return computed(() => {
        const result = { sm: 12, md: 12, lg: 12 }; // 기본값은 줄바꿈
        const fields = visibleFieldCount.value;

        const calcRemaining = (size: 'sm' | 'md' | 'lg') => {
            const used = fields * colSetting[size];
            const remaining = 12 - used;
            return remaining > 0 ? remaining : 12;
        };

        result.sm = calcRemaining('sm');
        result.md = calcRemaining('md');
        result.lg = calcRemaining('lg');

        return result;
    });
}

const rules = [(v: string) => !!v || '필수 입력 항목입니다.'];

// 필드 자동 동기화
watch(
    () => props.formFields.map((f) => f.value),
    () => {
        props.formFields.forEach((field) => {
            if (field.type === 'hidden' && field.linkTo) {
                const srcValue = getDeepValue(formData.value, field.linkTo);
                if (srcValue !== undefined) {
                    setDeepValue(formData.value, field.name, srcValue);
                    field.value = srcValue; // UI에도 반영
                }
            }
        });
    },
    { deep: true }
);
</script>
<!-- single-line 입력시 label 옵션 적용 안됨 주의! -->
<!-- 자동검증을 위한 form 태그 추가 : form 제출로 인한 페이지 새로고침을 막기위해 lineBtn 슬롯 내 버튼에는 type="submit"을 주지 말것-->
<!-- type 항목: select, search, search_list, password, date, datetime, check, switch, text, slot, hidden, 그 외는 공백처리-->
<!-- colsPerRow 값은 한줄에 나열하고 싶은 필드의 갯수 기준, btn은 포함하지 않음. -->
<!-- LineBtn 은 내부 버튼을 자동 정렬하므로 추가 <div>는 불필요함. -->
<template>
    <v-container>
        <slot name="topBtn" :validateForm="validateForm" />
        <v-form ref="formRef" lazy-validation="false">
            <v-row class="mb-3 align-center">
                <template v-for="field in formFields">
                    <v-col
                        v-bind="colSettings[colsPerRow > 5 ? 5 : colsPerRow]"
                        v-if="field.type !== 'hidden'"
                        class="pb-sm-1 pb-0 custom-height"
                    >
                        <v-select
                            v-if="field.type === 'select'"
                            :return-object="field.returnObject !== false"
                            variant="outlined"
                            v-model="field.value as string"
                            :items="field.options"
                            :item-title="field.itemTitle"
                            :item-value="field.itemValue"
                            :rules="edit && field.required ? rules : []"
                            :readonly="field.disabled || !isEditable"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            :label="field.label + (field.required ? ' *' : '')"
                        />

                        <div v-else-if="field.type === 'search' || field.type === 'search_list'">
                            <CustomDialog
                                :ref="`dialog_${field.name}`"
                                :title="field.label"
                                :single="field.type === 'search'"
                                :items="field.searchObj"
                                :searchField="field.itemValue ?? field.name"
                                :viewField="field.viewLabelMap"
                                :field="field"
                                @update:selectedValue="
                                    (selectedValue: string[] | string) => {
                                        field.value = selectedValue;
                                    }
                                "
                            />
                            <v-text-field
                                v-model="field.value"
                                :rules="edit && field.required ? rules : []"
                                :readonly="field.disabled || !isEditable"
                                :hide-details="hideDetails"
                                :item-title="field.itemTitle ?? field.name"
                                persistent-placeholder
                                :label="field.label + (field.required ? ' *' : '')"
                            >
                                <template v-slot:append-inner>
                                    <v-icon
                                        icon="mdi-account-search"
                                        class="text-right"
                                        @click.stop="openSearchDialog(field, String(field.value ?? '').trim())"
                                    ></v-icon>
                                </template>
                            </v-text-field>
                        </div>

                        <v-text-field
                            v-else-if="field.type === 'password'"
                            color="primary"
                            variant="outlined"
                            type="password"
                            v-model="field.value"
                            :item-title="field.itemTitle ?? field.name"
                            :rules="edit && field.required ? rules : []"
                            :readonly="field.disabled || !isEditable"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            :label="field.label + (field.required ? ' *' : '')"
                        >
                        </v-text-field>
                        <v-text-field
                            v-else-if="field.type === 'date'"
                            color="primary"
                            variant="outlined"
                            type="date"
                            v-model="field.value"
                            :item-title="field.itemTitle ?? field.name"
                            @update:model-value="(val) => (field.value = val === '' ? null : val)"
                            :rules="edit && field.required ? rules : []"
                            :readonly="field.disabled || !isEditable"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            :label="field.label + (field.required ? ' *' : '')"
                        >
                        </v-text-field>
                        <v-row v-else-if="field.type === 'due'">
                            <v-col cols="6">
                                <v-text-field
                                    type="date"
                                    v-model="(field.value as DateRange).startDate"
                                    :item-title="field.itemTitle ?? field.name"
                                    @update:model-value="(val) => (field.value = val === '' ? null : val)"
                                    :label="field.label + ' 시작'"
                                    :rules="edit && field.required ? rules : []"
                                    :readonly="field.disabled || !isEditable"
                                    :hide-details="hideDetails"
                                    persistent-placeholder
                                />
                            </v-col>

                            <v-col cols="6">
                                <v-text-field
                                    type="date"
                                    v-model="(field.value as DateRange).endDate"
                                    :item-title="field.itemTitle ?? field.name"
                                    @update:model-value="(val) => (field.value = val === '' ? null : val)"
                                    :label="field.label + ' 종료'"
                                    :rules="edit && field.required ? rules : []"
                                    :readonly="field.disabled || !isEditable"
                                    :hide-details="hideDetails"
                                    persistent-placeholder
                                />
                            </v-col>
                        </v-row>

                        <v-text-field
                            v-else-if="field.type === 'datetime'"
                            color="primary"
                            variant="outlined"
                            type="datetime-local"
                            v-model="field.value"
                            :item-title="field.itemTitle ?? field.name"
                            @update:model-value="(val) => (field.value = val === '' ? null : val)"
                            :rules="edit && field.required ? rules : []"
                            :readonly="field.disabled || !isEditable"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            style="min-width: 200px"
                            :label="field.label + (field.required ? ' *' : '')"
                        >
                        </v-text-field>

                        <div v-else-if="field.type === 'check'" class="outlined-checkbox">
                            <v-label class="checkbox-label"
                                >{{ field.label }}
                                <span style="color: red"> {{ field.required ? '&nbsp*' : '' }}</span>
                            </v-label>
                            <v-checkbox
                                v-for="(option, index) in field.options"
                                :key="index"
                                v-model="field.value"
                                :label="option.label"
                                :value="option.value"
                                :readonly="field.disabled || !isEditable"
                                multiple
                                density="compact"
                                hide-details
                                color="primary"
                                variant="outlined"
                                type="text"
                                :return-object="field.returnObject !== false"
                                :rules="edit && field.required ? rules : []"
                            >
                            </v-checkbox>
                        </div>
                        <v-switch
                            v-else-if="field.type === 'switch'"
                            hide-details
                            color="primary"
                            inset
                            v-model="field.value as boolean"
                            :item-title="field.itemTitle ?? field.name"
                            :rules="edit && field.required ? rules : []"
                            :readonly="field.disabled || !isEditable"
                            :label="field.label + (field.required ? ' *' : '')"
                        >
                        </v-switch>
                        <v-text-field
                            v-else-if="field.type === 'text'"
                            color="primary"
                            variant="outlined"
                            type="text"
                            v-model="field.value as string"
                            :item-title="field.itemTitle ?? field.name"
                            :rules="edit && field.required ? rules : []"
                            :readonly="field.disabled || !isEditable"
                            :hide-details="hideDetails"
                            persistent-placeholder
                            :label="field.label + (field.required ? ' *' : '')"
                        >
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
                    <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
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
</style>