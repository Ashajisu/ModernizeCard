<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { ref, watch} from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import type { PstnItem } from "@/types/custom/DataTableTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import { useTableManager } from "@/common/useTableManager";
import ExcelUploadDialogBtn from "@/common/excel/ExcelUploadDialogBtn.vue";
import { alert, confirm } from '@/common/alertService';

//데이터 정보
const PstnList:PstnItem[] = [
  { id: '1', number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '2', number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용'},
  { id: '3', number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단'},
  { id: '4', number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '5', number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '6', number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용'},
  { id: '7', number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단'},
  { id: '8', number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '9', number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '10', number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용'},
  { id: '11', number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단'},
  { id: '12', number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '13', number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '14', number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용'},
  { id: '15', number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단'},
  { id: '16', number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '17', number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '18', number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용'},
  { id: '19', number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단'},
  { id: '20', number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '21', number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
  { id: '22', number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용'},
  { id: '23', number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단'},
  { id: '24', number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용'},
];
const identifierField:string = 'id'; //테이블 식별자 필드명
const productTypes = ['줌 폰', '줌 폰 프로'];
const sipGroupTypes = ['Phone SIP Route Group', '어카운트에 설정된 SIP Group'];
const incomingTypes = ['허용', '차단'];
//검색
const formFields = ref<FormField[]>([
  { label: 'PSTN 번호', name: 'number', type: 'search_list', value: '', searchObj:PstnList},
  { label: '제품', name: 'product', type: 'select', value: '', options:productTypes},
  { label: '사용자', name: 'user', type: 'search_list', value: '' , searchObj:PstnList},
  { label: 'SIP Group', name: 'sipGroup', type: 'select', value: '', options:sipGroupTypes},
  { label: '수신', name: 'incoming', type: 'select', value: '', options:incomingTypes},
  { label: '발신', name: 'outgoing', type: 'select', value: '', options:incomingTypes},
]);
//테이블헤더
const headers = ref<any[]>([
  { title: "PSTN 번호", align: "center", key: "number" },
  { title: "제품", align: "center", key: "product" },
  { title: "사용자", align: "center", key: "user" },
  { title: "SIP Group", align: "center", key: "sipGroup" },
  { title: "지역", align: "center", country: "assignment" },
  { title: "수신", align: "center", key: "incoming" },
  { title: "발신", align: "center", key: "outgoing" },
]);
//상세정보
const newFields = ref<FormField[]>([
  { label: '제품', name: 'product', type: 'select', value: '', options:productTypes, required: true, disabled: false },
  { label: 'PSTN 번호', name: 'number', type: 'slot', value: '', placeholder: '사원번호 입력', required: true, disabled: false },
  { label: 'id', name: 'id', type: 'none', value: '',required: false, disabled: true },
  { label: 'SIP Group', name: 'sipGroup',  type: 'select', value: '', options:sipGroupTypes, required: true, disabled: false },
]);
const pstnFields = ref<FormField[]>([
  { label: 'id', name: 'id', type: 'hidden', value: '',required: false, disabled: true },
  { label: '제품', name: 'product', type: 'select', value: '', options:productTypes, required: true, disabled: false },
  { label: '사용자', name: 'user', type: 'search', value: '' , searchObj:PstnList},
  { label: 'SIP Group', name: 'sipGroup', type: 'select', value: '', options:sipGroupTypes},
  { label: '수신', name: 'incoming', type: 'select', value: '', options:incomingTypes},
  { label: '발신', name: 'outgoing', type: 'select', value: '', options:incomingTypes},
]);

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
  selectedItem,
  selectedEmpId,
  onSelectionChange,
  edit,
  handleEdit,
  onNew,
  onSave,
  onDelete,
  onExcelSave
} = useTableManager<PstnItem>(PstnList, formFields,pstnFields,identifierField);

const formRef = ref();
// 신규 다이얼로그 열기
const newDialog = ref<InstanceType<typeof CustomSlotDialog> | null>(null);
const openNewDialog = ()=>{
  selectedEmpId.value = [];
  newDialog.value?.open();
}

// ✅ edit 값이 바뀌면 편집 다이얼로그 열기/닫기, 다이얼로그 닫히면 edit 값도 변경.
const detailDialog = ref<InstanceType<typeof CustomSlotDialog> | null>(null);
watch(edit, val => detailDialog.value && (detailDialog.value.showDialog = val));
watch(() => detailDialog.value?.showDialog, val => typeof val === 'boolean' && (edit.value = val));

const onClickSave = async ()=>{
  console.log('onClickSave:');
  const validateForm = await formRef.value?.validateForm;
  if (!!validateForm) {
    await onSave(validateForm);
  }else {
    await alert('저장에 실패하였습니다. 다시 시도해주세요');
  }
}

const deleteCheckConfirm = async ()=>{
  const selected:PstnItem|null = selectedItem.value;

  if(selected?.user){
    const isDeleteUser = await confirm(`선택하신 ${selected?.number} 번호가 \n 사용자 ${selected?.user} 로부터 해제되고 삭제됩니다. \n 삭제를 계속하시겠습니까?`);
    if(isDeleteUser){
    //사용자 해제 API 호출
      console.log('isDeleteUser');
    }
  }

  const isDeleteNumber = await confirm(`선택하신 ${selected?.number} 번호가 삭제됩니다. \n 삭제를 계속하시겠습니까?`);
  if(isDeleteNumber){
    //PSTN 번호 삭제 호출
    await onDelete(selectedEmpId.value[0]);
  }

}
</script>
<!-- ArrNumber : 배열 갯수제한 필요, number 가 배열이면 save 시점에 분해해서 등록해야함.-->
<template>
  <v-row>
    <v-col cols="12" md="12">
      <UiParentCard title="PSTN 번호 관리">
        <v-row class="mb-6 mx-2">
          <h6 class="text-subtitle-1">💡줌 폰에서 사용되는 PSTN 번호를 관리 할 수 있습니다. </h6>
        </v-row>
        <v-row>
          <CustomSearchChecksForm :formFields="formFields" :colsPerRow="7" :edit="true">
            <template v-slot:lineBtn="{ validateForm }">
              <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
              </div>
            </template>
          </CustomSearchChecksForm>
        </v-row>

      <!-- 버튼 영역 -->
        <v-row>
          <v-col>
            <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">

      <!-- PSTN 번호 신규 다이얼로그 -->
              <CustomSlotDialog ref="newDialog" title="PSTN 번호 추가" >
                <template v-slot:inCard>
                  <CustomSearchChecksForm ref="formRef" :form-fields="newFields" :cols-per-row="1" :edit="true">
                    <template v-slot:number="{ field }">
                          <ArrNumber :field="field"/>
                    </template>
                  </CustomSearchChecksForm>
                </template>
                <template v-slot:btn>
                  <v-btn flat color="primary" variant="flat" @click="onClickSave">저장 </v-btn>
                </template>
              </CustomSlotDialog>

              <v-btn flat color="primary" variant="outlined" @click="openNewDialog"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
              <v-btn flat color="error" variant="outlined" @click="deleteCheckConfirm"><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제 </v-btn>

              <!-- PSTN 번호 편집 다이얼로그 -->
              <CustomSlotDialog ref="detailDialog" title="PSTN 번호 편집" >
                <template v-slot:inCard>
                  <CustomSearchChecksForm ref="formRef" :form-fields="pstnFields" :cols-per-row="1" :edit="true">
                    <template v-slot:number="{ field }">
                      <ArrNumber :field="field"/>
                    </template>
                  </CustomSearchChecksForm>
                </template>
                <template v-slot:btn>
                  <v-btn flat color="primary" variant="flat" @click="onClickSave">저장 </v-btn>
                </template>
              </CustomSlotDialog>
              <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)">편집 </v-btn>
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
          <v-data-table items-per-page="5" :headers="headers" :items="filteredList" :item-value="identifierField"
                        select-strategy="single" show-select class="border rounded-md"
                        v-model="selectedEmpId"
                        @update:model-value="onSelectionChange"
          >
          </v-data-table>
        </v-row>

      <!-- PSTN 번호 삭제 확인 다이얼로그 -->
<!--      <v-dialog v-model="deleteDialog" max-width="500px">-->
<!--        <v-card>-->
<!--          <v-card-title class="text-h5 bg-error text-white">-->
<!--            PSTN 번호 삭제-->
<!--          </v-card-title>-->
<!--          <v-card-text class="pt-4">-->
<!--            <p v-if="selectedItems.length === 1">-->
<!--              선택하신 {{ selectedItems[0].number }} 번호가 삭제됩니다.<br>-->
<!--              사용자 {{ selectedItems[0].user }}로부터 해제되고 삭제됩니다.-->
<!--            </p>-->
<!--            <p v-else>-->
<!--              선택하신 {{ selectedItems.length }}개의 번호가 삭제됩니다.<br>-->
<!--              해당 번호들이 사용자로부터 해제되고 삭제됩니다.-->
<!--            </p>-->
<!--            <p class="mt-2">삭제를 계속 하시겠습니까?</p>-->
<!--          </v-card-text>-->
<!--          <v-card-actions>-->
<!--            <v-spacer></v-spacer>-->
<!--            <v-btn color="primary" @click="deleteDialog = false">취소</v-btn>-->
<!--            <v-btn color="error" @click="deletePstnNumbers">확인</v-btn>-->
<!--          </v-card-actions>-->
<!--        </v-card>-->
<!--      </v-dialog>-->

      </UiParentCard>
      </v-col>
      </v-row>
  </template>