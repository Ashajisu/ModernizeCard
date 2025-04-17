<script setup lang="ts">
import {computed, nextTick, ref, watch} from 'vue';
import {SearchIcon} from 'vue-tabler-icons';
import {CodeDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import type {Code} from '@/types/apps/SampleType';
import { useTableManager } from "@/common/useTableManager";
import type {FormField} from "@/types/custom/InputTypes";
import UiParentCard from "@/components/shared/UiParentCard.vue";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import { alert, confirm } from '@/common/alertService';
import PaginationControl from "@/components/custom/pagination/PaginationControl.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";

const formFields = ref<FormField[]>([
  { label: '코드명', name:"codename", type: "text", value: "", required: false, disabled: false },
  { label: '코드유형', name:"id", type: "select", value: "", options: ["메인", "서브", "기타"] , required: false, disabled: false },
  { label: "코드 ID", name: "maincode", type: "text", value: "",  searchObj:CodeDatatables, required: false, disabled: false },
  { label: "사용 여부", name: "usingcode", type: "check", value: [], options: ["사용", "미사용"] },
]);

const headers = ref<any[]>([
  { title: '분류', align: 'center', key: 'id' },
  { title: '코드 ID', align: 'center', key: 'maincode'},
  { title: '코드명', align: 'center', key: 'codename'},
  { title: '변경 여부', align: 'center', key: 'changecode' },
  { title: '사용', align: 'center', key: 'usingcode' },
  { title: '기타코드 #1', align: 'center', key: 'etccode1' },
  { title: '기타코드 #2', align: 'center', key: 'etccode2' },
  { title: '수정', value: 'actions', align: 'center',  sortable: false },
]);

const deviceFormFields = ref<FormField[]>([
  { label: "분류", name: "id", type: "text", value: "", required: false, disabled: false },
  { label: "코드 ID", name: "maincode", type: "text", value: "", required: false, disabled: false },
  { label: "코드명", name: "codename", type: "text", value: "", required: false, disabled: false },
  { label: "변경 여부", name: "changecode", type: "select", value: "",options: ["Y", "N"] , required: false, disabled: false },
  { label: "사용", name: "usingcode", type: "text", value: "", required: false, disabled: false },
  { label: "기타코드 #1", name: "etccode1", type: "text", value: "", required: false, disabled: false },
  { label: "기타코드 #2", name: "etccode2", type: "text", value: "", required: false, disabled: false },
]);

const identifierField:string = 'codename';

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
  selectedEmpId,
  selectedItem,
  onSelectionChange,
  edit,
  handleEdit,
  onNew,
  onSave,
  onDelete,
} = useTableManager<Code>(CodeDatatables, formFields, deviceFormFields, identifierField);

const dialog = ref(false)
const dialogDelete = ref(false)

const codeName = ref();
const codeCase = ref();
const codeid = ref();

const rules = ref([
  (v: string) => !!v || 'rule',
  (v: string) => (v && v.length <= 20) || 'rule must be less than 20 characters'
]);
const headers1 = [
  {title: '분류', align: 'center', key: 'id'},
  {title: '코드 ID', align: 'center', key: 'maincode'},
  {title: '코드명', align: 'center', key: 'codename'},
  {title: '변경 여부', align: 'center', key: 'changecode'},
  {title: '사용', align: 'center', key: 'usingcode'},
  // { title: '기능', align: 'center', key: 'actions' },
] as const;
const headers2 = [
  {title: '메인 코드', align: 'center', sortable: false, key: 'maincode',},
  {title: '기타코드 #1', align: 'center', key: 'etccode1'},
  {title: '기타코드 #2', align: 'center', key: 'etccode2'},
  {title: '코드명', align: 'center', key: 'codename'},
  {title: '정렬', align: 'center', key: 'id'},
  {title: '사용', align: 'center', key: 'usingcode'},
] as const;

const editedIndex = ref(-1)
const editedItem = ref({
  id: "",
  codename: "",
  maincode: "",
  etccode1: "",
  etccode2: "",
  changecode: "",
  usingcode: ""
});

const defaultItem = ref({
  id: "",
  codename: "",
  maincode: "",
  etccode1: "",
  etccode2: "",
  changecode: "",
  usingcode: ""
});


const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'New Item' : 'Edit Item'
})


function closeDelete() {
  dialogDelete.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
}

//수정
function changeEnable(){
  if(editedItem1.value.changecode == 'N' || editedItem1.value.changecode == 'n'){
    isReadOnly1.value = true;
  }else if(editedItem1.value.changecode == 'Y' || editedItem1.value.changecode == 'y'){
    isReadOnly1.value = false;
  }
}

//취소
function cancleChanges() {
  editedItem1.value = {
    id: "",
    codename: "",
    maincode: "",
    etccode1: "",
    etccode2: "",
    changecode: "",
    usingcode: "",
  };
  editedItem2.value = {
    id: "",
    codename: "",
    maincode: "",
    etccode1: "",
    etccode2: "",
    changecode: "",
    usingcode: "",
  };

  isReadOnly1.value = true;
  isReadOnly2.value = true;
}
const editedItem1 = ref({
  id: "",
  codename: "",
  maincode: "",
  etccode1: "",
  etccode2: "",
  changecode: "",
  usingcode: ""
});
const editedItem2 = ref({
  id: "",
  codename: "",
  maincode: "",
  etccode1: "",
  etccode2: "",
  changecode: "",
  usingcode: ""
});
const isReadOnly1 = ref(true);
const isReadOnly2 = ref(true);
const codeData1 = ref<Code | null>(null);
const codeData2 = ref<Code | null>(null);

function selectMenu1(item: any) {
  console.log("선택된 데이터1:", item);
  codeData1.value = {
    id: item.id || "",
    codename: item.codename || "",
    maincode: item.maincode || "",
    etccode1: item.etccode1 || "",
    etccode2: item.etccode2 || "",
    changecode: item.changecode || "",
    usingcode: item.usingcode || "",
  };

  editedItem1.value = codeData1.value;
}

function selectMenu2(item: any) {
  console.log("선택된 데이터2:", item);
  codeData2.value = {
    id: item.id || "",
    codename: item.codename || "",
    maincode: item.maincode || "",
    etccode1: item.etccode1 || "",
    etccode2: item.etccode2 || "",
    changecode: item.changecode || "",
    usingcode: item.usingcode || "",
  };

  editedItem2.value = codeData2.value;
}

watch(dialog, val => {
  val || close()
})
watch(dialogDelete, val => {
  val || closeDelete()
})

// ✅ edit 값이 바뀌면 다이얼로그 열기/닫기, 다이얼로그 닫히면 edit 값도 변경.
const detailDialog = ref<InstanceType<typeof CustomSlotDialog> | null>(null);
watch(edit, val => detailDialog.value && (detailDialog.value.showDialog = val));
watch(() => detailDialog.value?.showDialog, val => typeof val === 'boolean' && (edit.value = val));

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

function editItem(item: any) {
  editedIndex.value = filteredList.value.indexOf(item);
  console.log("editIndex : " +  editedIndex.value);

  // deviceFormFields의 각 항목의 name과 item의 속성을 비교하여 value를 업데이트
  deviceFormFields.value.forEach((field) => {
    if (item.hasOwnProperty(field.name)) {
      field.value = item[field.name];
    }
  });
  edit.value = true;
}

async function deleteItem(item: any) {
  const index = filteredList.value.indexOf(item);
  const result = await confirm("코드를 삭제하시겠습니까?");

  if (index === -1) {
    alert('이미 삭제된 항목입니다.');
    return;
  } else if (result) {
    filteredList.value.splice(index, 1);
  }
}

watch(() => selectedEmpId, (val) => {
  console.log('선택된 값:', val);
});

const itemsPerPage = ref(5);
const pagination1 = ref(1);
const pageCount1 = computed(() => {
  return Math.ceil(filteredList.value.length  / itemsPerPage.value);
});

</script>

<template>
  <!--    <BaseBreadcrumb :title="page.title" :breadcrumbs="breadcrumbs"></BaseBreadcrumb>-->
  <br>
<!--  <v-card elevation="10">
    <v-card-item>
      <h5 class="text-20 mb-7">코드관리</h5>

      <v-row class="d-flex align-center">
        &lt;!&ndash; 코드명 &ndash;&gt;
        <v-col cols="12" lg="4" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px;">코드명</v-label>
          <v-text-field
              hide-details
              label="Search Code"
              v-model="codeName"
              :rules="rules"
              required
              style="max-width: 400px;"
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="18"/>
            </template>
          </v-text-field>
        </v-col>

        &lt;!&ndash; 코드유형 &ndash;&gt;
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 15px;">코드유형</v-label>
          <v-select
              hide-details
              v-model="codeCase"
              :items="['메인', 'sub', '기타']"
              required
          />
        </v-col>

        &lt;!&ndash; 미사용 체크박스 &ndash;&gt;
        <v-col cols="12" lg="2" class="d-flex align-center">
          <div class="d-flex align-center">
            <v-checkbox-btn :model-value="false" color="primary"></v-checkbox-btn>
            <v-label class="font-weight-semibold ml-2">미사용</v-label>
          </div>
        </v-col>

        &lt;!&ndash; 코드 ID &ndash;&gt;
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px; margin-left: -70px;">코드 ID</v-label>
          <v-text-field
              v-model="codeid"
              :rules="rules"
              required
              hide-details
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="18"/>
            </template>
          </v-text-field>
        </v-col>

        &lt;!&ndash; 조회 버튼 &ndash;&gt;
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-btn color="primary" flat to="/apps/invoice/create"
                 style="min-height: 44px; min-width: 80px; margin-left: 40px;">조 회
          </v-btn>
        </v-col>
      </v-row>
    </v-card-item>
  </v-card>

  <br>
    <v-row class="d-flex align-stretch">
      &lt;!&ndash; 첫 번째 카드 &ndash;&gt;
      <v-col cols="12" lg="5">
        <v-card elevation="9" class="pa-4 d-flex flex-column justify-center flex-grow-1" style="min-height: 80px;">
          <v-row
              class="align-center no-gutters"
              style="display: flex; justify-content: center; align-items: center; gap: 12px;"
          >
            <v-label class="text-center">총 건수</v-label>
            <v-text-field
                class="text-center custom-text-field"
                hide-details
                v-model="CodeDatatables.length"
                style="max-width: 130px; min-width: 130px;"
            />
            <v-label>건</v-label>
            <v-label>조회</v-label>
            <v-text-field
                v-model="itemsPerPage"
                class="text-center"
                type="number"
                min="1"
                max="50"
                hide-details
                style="max-width: 100px; min-width: 100px;"
            />
          </v-row>
        </v-card>
      </v-col>

      &lt;!&ndash; 두 번째 카드 &ndash;&gt;
      <v-col cols="12" lg="7">
        <v-card elevation="9" class="pa-4 d-flex justify-center flex-grow-1" style="min-height: 80px;">
          <v-btn color="primary" @click="" class="mx-2">등 록</v-btn>
          <v-btn color="secondary" @click="changeEnable"  class="mx-2">수 정</v-btn>
          <v-btn color="error" @click="deleteItem"  class="mx-2">삭 제</v-btn>
          <v-btn color="warning" @click="cancleChanges"  class="mx-2">취 소</v-btn>
          <v-btn color="info" @click=""  class="mx-2">저 장</v-btn>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12" lg="5">
        <v-table class="border rounded-md text-center light" v-if="editedItem1" style="min-height: 130px; font-size: 13px;">
          <thead>
          <tr>
            <th v-for="header in headers1" :key="header.key" class="text-center">
              {{ header.title }}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-if="CodeDatatables.length > 0">
            <td class="text-center">
              <v-text-field :readonly="isReadOnly1" v-model="editedItem1.id" class="mt-2"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly1"  v-model="editedItem1.maincode" class="mt-3" style="min-width:70px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly1"  v-model="editedItem1.codename" class="mt-4" style="min-width:100px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly1"  v-model="editedItem1.changecode" class="mt-5" style="min-width:50px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly1"  v-model="editedItem1.usingcode" class="mt-7" style="min-width:50px;"></v-text-field>
            </td>
          </tr>
          </tbody>
        </v-table>
      </v-col>

      <v-col cols="12" lg="7">
        <v-table class="border rounded-md text-center light" v-if="editedItem2" style="min-height: 130px; font-size: 13px;">
          <thead>
          <tr>
            <th v-for="header in headers2" :key="header.key" class="text-center">
              {{ header.title }}
            </th>
          </tr>
          </thead>
          <tbody>
          <tr v-if="CodeDatatables.length > 0">
            <td class="text-center">
              <v-text-field :readonly="isReadOnly2"  class="mt-2" v-model="editedItem2.maincode" style="min-width:70px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly2"  class="mt-3" v-model="editedItem2.etccode1" style="min-width:70px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly2"  class="mt-4" v-model="editedItem2.etccode2" style="min-width:70px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly2"  class="mt-5" v-model="editedItem2.codename" style="min-width:100px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly2"  type="number" v-model="editedItem2.id" class="mt-6" style="max-width:70px;"></v-text-field>
            </td>
            <td class="text-center">
              <v-text-field :readonly="isReadOnly2"  class="mt-7" v-model="editedItem2.usingcode" style="min-width:70px;"></v-text-field>
            </td>
          </tr>
          </tbody>
        </v-table>
      </v-col>
    </v-row>

  <v-row>
    <v-col cols="12" lg="5">
      <v-data-table class="border rounded-md text-center light scrollable-card"
                    style="height: 400px; font-size: 12px;"
                    fixed-header
                    hide-default-footer
                    :items-per-page="itemsPerPage"
                    v-model:page="pagination1"
                    :headers="headers1"
                    :items="CodeDatatables"
                    @click:row="(event: Event, item: any) => selectMenu1(item.item)"
                    :sort-by="[{ key: 'id', order: 'asc' }]">
        <template v-slot:bottom>
          <PaginationControl
              :items-per-page="itemsPerPage"
              :pagination="pagination1"
              :page-count="pageCount1"
              @update:itemsPerPage="(val: number) => itemsPerPage = val"
              @update:pagination="(val: number) => pagination1 = val"
          />
        </template>
      </v-data-table>
    </v-col>

    <v-col cols="12" lg="7">
      <v-data-table class="border rounded-md text-center light"
                    style="height: 400px; font-size: 12px; &#45;&#45;easy-table-header-font-size: 12px;"
                    hide-default-footer
                    fixed-header
                    :headers="headers2"
                    :items="CodeDatatables"
                    :items-per-page="itemsPerPage"
                    v-model:page="pagination2"
                    @click:row="(event: Event, item: any) => selectMenu2(item.item)"
                    :sort-by="[{ key: 'id', order: 'asc' }]">
        <template v-slot:bottom>
          <PaginationControl
              :items-per-page="itemsPerPage"
              :pagination="pagination2"
              :page-count="pageCount2"
              @update:itemsPerPage="(val: number) => itemsPerPage = val"
              @update:pagination="(val: number) => pagination2 = val"
          />
        </template>
      </v-data-table>
    </v-col>
  </v-row>-->

  <v-row>
    <v-col cols="12" md="12">
      <UiParentCard title="코드관리">
<!--        <v-row class="mb-6 mx-2">
          <h6 class="text-subtitle-1">줌 폰의 디바이스 관리를 할 수 있습니다.</h6>
        </v-row>-->
<!--        <v-row cols="auto" class="d-flex align-center">
            &lt;!&ndash; 코드명 &ndash;&gt;
            <v-col cols="12" lg="3" class="d-flex align-center">
              <v-text-field
                  hide-details
                  label="코드명"
                  v-model="codeName"
                  :rules="rules"
                  persistent-placeholder
                  style="max-width: 400px;"
              >
                <template v-slot:prepend-inner>
                  <SearchIcon size="18"/>
                </template>
              </v-text-field>
            </v-col>

            &lt;!&ndash; 코드유형 &ndash;&gt;
            <v-col cols="12" lg="3" class="d-flex align-center">
              <v-select
                  hide-details
                  label="코드유형"
                  v-model="codeCase"
                  :items="['메인', 'sub', '기타']"
                  persistent-placeholder
              />
            </v-col>

            &lt;!&ndash; 코드 ID &ndash;&gt;
            <v-col cols="12" lg="3" class="d-flex align-center">
              <v-text-field
                  label="코드 ID"
                  v-model="codeid"
                  :rules="rules"
                  persistent-placeholder
                  hide-details
              >
                <template v-slot:prepend-inner>
                  <SearchIcon size="18"/>
                </template>
              </v-text-field>
            </v-col>

            &lt;!&ndash; 미사용 체크박스 &ndash;&gt;
            <v-col cols="12" lg="1" class="d-flex align-center">
              <div class="d-flex align-center">
                <v-checkbox-btn :model-value="false" color="primary"></v-checkbox-btn>
                <v-label class="font-weight-semibold ml-2">미사용</v-label>
              </div>
            </v-col>

            &lt;!&ndash; 조회 버튼 &ndash;&gt;
            <v-col cols="12" lg="2" class="d-flex align-center">
              <v-btn color="primary" flat to="/apps/invoice/create"
                     style="min-height: 44px; min-width: 80px; margin-left: 40px;">조 회
              </v-btn> &lt;!&ndash;@click="onSearch()"&ndash;&gt;
            </v-col>
        </v-row>-->
        <v-row>
          <CustomSearchChecksForm :formFields="formFields" :colsPerRow="5" :edit="true" :hide-details="true">
            <template v-slot:lineBtn="{validateForm}">
              <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
            </template>
          </CustomSearchChecksForm>
        </v-row>
        <v-row>
          <v-data-table :headers="headers" :items="filteredList" :item-value="identifierField"
                        select-strategy="single" class="border rounded-md"
                        v-model="selectedEmpId"
                        :items-per-page="itemsPerPage"
                        v-model:page="pagination1"
                        @update:model-value="onSelectionChange"
          >
            <template v-slot:item.model="{ item }">
              <div :key="`${item.id}-${item.codename}`">
                {{ item.id }} {{ item.codename }}
              </div>
            </template>

            <!-- 버튼 2개가 들어가는 수정 컬럼 슬롯 -->
            <template v-slot:item.actions="{ item }">
              <v-tooltip text="Edit">
                <template v-slot:activator="{ props }">
                  <v-btn icon flat @click="()=>{ editItem(item); }"  v-bind="props"
                  ><PencilIcon stroke-width="1.5" size="20" class="text-primary"
                  /></v-btn>
                </template>
              </v-tooltip>
              <v-tooltip text="Delete">
                <template v-slot:activator="{ props }">
                  <v-btn icon flat @click="deleteItem(item)" v-bind="props"
                  ><TrashIcon stroke-width="1.5" size="20" class="text-error"
                  /></v-btn>
                </template>
              </v-tooltip>
            </template>

            <template v-slot:bottom>
              <PaginationControl
                  :items-per-page="itemsPerPage"
                  :pagination="pagination1"
                  :page-count="pageCount1"
                  @update:itemsPerPage="(val: number) => itemsPerPage = val"
                  @update:pagination="(val: number) => pagination1 = val"
              />
            </template>
          </v-data-table>
        </v-row>
        <v-row>
          <v-col>
            <CustomSlotDialog ref="detailDialog" title="Code" >
              <template v-slot:inCard>
                <CustomSearchChecksForm ref="formRef" :form-fields="deviceFormFields" :cols-per-row="1" :edit="true">
                  <template v-slot:lineBtn>
                  </template>
                </CustomSearchChecksForm>
              </template>
              <template v-slot:btn>
                <v-btn flat color="primary" variant="flat" @click="onClickSave">저장 </v-btn>
              </template>
            </CustomSlotDialog>
          </v-col>
        </v-row>
      </UiParentCard>
    </v-col>
  </v-row>
</template>

