<script setup lang="ts">
// 다른사람 페이지를 컴포넌트로 수정할 수 있는지 확인중.
import {ref, watch, computed} from 'vue';
import { ZoomPhoneDatatables } from '@/_mockApis/components/datatable/SampleDataTable';
import type { ZoomPhone } from '@/types/apps/SampleType';
import { useTableManager } from "@/common/useTableManager";
import type {FormField} from "@/types/custom/InputTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import UiParentCard from "@/components/shared/UiParentCard.vue";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import { alert, confirm } from '@/common/alertService';
import ExcelUploadDialogBtn from "@/common/excel/ExcelUploadDialogBtn.vue";

const PhoneStatus = ref(['Online','Offline']);
const formFields = ref<FormField[]>([
  { label: "사용자명", name: "user", type: "text", value: "", required: false, disabled: false },
  { label: "내선번호", name: "phoneNum", type: "text", value: "", required: false, disabled: false },
  { label: "상태", name: "phoneStatus", type: "select", value: "", options: PhoneStatus.value, required: false, disabled: false },
  { label: "모델명", name: "modelName", type: "search_list", value: "", searchObj:ZoomPhoneDatatables, required: false, disabled: false },
  { label: "Mac", name: "macAddress", type: "search_list", value: "", searchObj:ZoomPhoneDatatables, required: false, disabled: false },
  { label: "Private IP", name: "privateAddress", type: "search_list", value: "", searchObj:ZoomPhoneDatatables, required: false, disabled: false },
]);
const headers = ref<any[]>([
  { title: '표시', align: 'center', key: 'phoneName' },
  { title: '사용자', align: 'center', key: 'user'},
  { title: '내선번호', align: 'center', key: 'phoneNum' },
  { title: 'PSTN 번호', align: 'center', key: 'pstnNum' },
  { title: '상태', align: 'center', key: 'phoneStatus' },
  { title: '모델', align: 'center', key: 'model' },
  { title: 'Mac 주소', align: 'center', key: 'macAddress' },
  { title: '펌웨어', align: 'center', key: 'firmware' },
  { title: 'Public IP 주소', align: 'center', key: 'publicAddress' },
  { title: 'Private IP 주소', align: 'center', key: 'privateAddress' }
]);
const deviceFormFields = ref<FormField[]>([
  { label: "표시", name: "phoneName", type: "text", value: "", required: true, disabled: false },
  { label: "MAC 주소", name: "macAddress", type: "text", value: "", required: true, disabled: false },
  { label: "제조사 ", name: "manufacturer", type: "select", value: "", options: ["Avaya", "Poly"], required: false, disabled: false },
  { label: "모델명", name: "modelName", type: "select", value: "", options: ["J139", "J159", "J179", "E350"], required: false, disabled: false },
  { label: "할당", name: "allocationType", type: "select", value: "", options: ["SIP", "H.323", "Cloud"], required: true, disabled: false },
  { label: "할당 값", name: "allocationValue", type: "text", value: "", required: false, disabled: false },
]);
const identifierField:string = 'phoneName';

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
  selectedEmpId,
  onSelectionChange,
  selectedItem,
  edit,
  handleEdit,
  onNew,
  onSave,
  onDelete,
  onExcelSave
} = useTableManager<ZoomPhone>(ZoomPhoneDatatables, formFields, deviceFormFields, identifierField);

// ✅ edit 값이 바뀌면 다이얼로그 열기/닫기, 다이얼로그 닫히면 edit 값도 변경.
const detailDialog = ref<InstanceType<typeof CustomSlotDialog> | null>(null);
watch(edit, val => detailDialog.value && (detailDialog.value.showDialog = val));
watch(() => detailDialog.value?.showDialog, val => typeof val === 'boolean' && (edit.value = val));

const deviceRestart = async () => {
  const ok = await confirm('선택한 디바이스가 재시작 됩니다.\n재시작 하시겠습니까?');
  if (ok) {
    await alert('디바이스가 재시작 되었습니다.');
  }
}

const deployment = async () => {
  const allocationTypeField = deviceFormFields.value.find(f => f.name === 'allocationType');
  const allocationValueField = deviceFormFields.value.find(f => f.name === 'allocationValue');
  if (allocationTypeField) allocationTypeField.value = '';
  if (allocationValueField) allocationValueField.value = '';
  await alert('할당해제 되었습니다.');
}

const formRef = ref();
const onClickSave = async ()=>{
  console.log('onClickSave');
  const validateForm = await formRef.value?.validateForm;
  if (!!validateForm) {
    await onSave(validateForm);
  }else {
    await alert('저장에 실패하였습니다. 다시 시도해주세요');
  }
}

const itemsPerPage = ref(5);
const pagination = ref(1);
const pageCount = computed(() => {
  return Math.ceil(ZoomPhoneDatatables.length / itemsPerPage.value);
});

</script>

<template>
  <v-row>
    <v-col cols="12" md="12">
      <UiParentCard title="줌 폰 디바이스 관리">
        <v-row class="mb-6 mx-2">
          <h6 class="text-subtitle-1">줌 폰의 디바이스 관리를 할 수 있습니다.</h6>
        </v-row>
        <v-row>
          <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true" :hide-details="true">
            <template v-slot:lineBtn="{validateForm}">
                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                <v-btn color="primary" variant="outlined" class="ml-2" @click="resetSearch">초기화</v-btn>
            </template>
          </CustomSearchChecksForm>
        </v-row>

        <v-row>
          <v-col>
            <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
              <CustomSlotDialog ref="detailDialog" title="디바이스" >
                <template v-slot:inCard>
                  <CustomSearchChecksForm ref="formRef" :form-fields="deviceFormFields" :cols-per-row="1" :edit="true">
                    <template v-slot:lineBtn>
                      <v-btn flat color="error" variant="flat" @click="deployment">할당해제</v-btn>
                    </template>
                  </CustomSearchChecksForm>
                </template>
                <template v-slot:btn>
                      <v-btn flat color="primary" variant="flat" @click="onClickSave">저장 </v-btn>
                </template>
              </CustomSlotDialog>
              <v-btn flat color="primary" variant="outlined" @click="()=>{ onNew(); }"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
              <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0])"><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제 </v-btn>
              <v-btn flat color="primary" variant="outlined" @click="()=>{ handleEdit(true); }">편집</v-btn>
              <v-btn flat color="error" variant="outlined" @click="deviceRestart">재시작</v-btn>
            </div>
          </v-col>
          <v-col>
            <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
              <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
              <ExcelUploadDialogBtn :save="onExcelSave" title="엑셀 업로드"/>
            </div>
          </v-col>
        </v-row>

        <v-row>
          <v-data-table :headers="headers" :items="filteredList" :item-value="identifierField"
                        select-strategy="single" show-select class="border rounded-md"
                        v-model="selectedEmpId"
                        :items-per-page="itemsPerPage"
                        v-model:page="pagination"
                        @update:model-value="onSelectionChange"
          >
            <template v-slot:item.model="{ item }">
              <div :key="`${item.manufacturer}-${item.modelName}`">
                {{ item.manufacturer }} {{ item.modelName }}
              </div>
            </template>
            <template v-slot:bottom>
              <v-row align="center" justify="space-between" class="pt-2 mt-3 px-3">
                <v-col cols="auto">
                  <v-text-field
                      :model-value="itemsPerPage"
                      class="pa-2"
                      label="페이지당 항목 수"
                      type="number"
                      min="-1"
                      max="15"
                      hide-details
                      style="max-width: 130px; min-width: 130px;"
                      @update:model-value="itemsPerPage = parseInt($event, 10)"
                  />
                </v-col>
                <v-col style="margin-right: 160px;">
                  <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
                </v-col>
              </v-row>
            </template>
          </v-data-table>
        </v-row>
      </UiParentCard>
    </v-col>
  </v-row>
</template>
