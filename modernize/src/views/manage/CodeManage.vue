<script setup lang="ts">
import {computed, nextTick, ref, watch} from 'vue';
import {SearchIcon} from 'vue-tabler-icons';
import {CodeDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import type {Code, Menu} from '@/types/apps/SampleType';

const page = ref({title: '코드 관리'});

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

const selectedItem = ref({
  id: "",
  codename: "",
  maincode: "",
  etccode1: "",
  etccode2: "",
  changecode: "",
  usingcode: ""
});
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

const itemsPerPage = ref(3);
const pagination1 = ref(1);
const pagination2 = ref(1);
const pageCount1 = Math.ceil(CodeDatatables.length / itemsPerPage.value);
const pageCount2 = Math.ceil(CodeDatatables.length / itemsPerPage.value);




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

/*  if(editedItem2.value.changecode == 'N'  || editedItem2.value.changecode == 'n'){
    isReadOnly2.value = true;
  }else if(editedItem2.value.changecode == 'Y' || editedItem2.value.changecode == 'y'){
    isReadOnly2.value = false;
  }*/
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

function deleteItem(){
  cancleChanges();
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

// initialize()
</script>

<template>
  <!--    <BaseBreadcrumb :title="page.title" :breadcrumbs="breadcrumbs"></BaseBreadcrumb>-->
  <br>
  <v-card elevation="10">
    <v-card-item>
      <h5 class="text-20 mb-7">코드관리</h5>

      <v-row class="d-flex align-center">
        <!-- 코드명 -->
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

        <!-- 코드유형 -->
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 15px;">코드유형</v-label>
          <v-select
              hide-details
              v-model="codeCase"
              :items="['메인', 'sub', '기타']"
              required
          />
        </v-col>

        <!-- 미사용 체크박스 -->
        <v-col cols="12" lg="2" class="d-flex align-center">
          <div class="d-flex align-center">
            <v-checkbox-btn :model-value="false" color="primary"></v-checkbox-btn>
            <v-label class="font-weight-semibold ml-2">미사용</v-label>
          </div>
        </v-col>

        <!-- 코드 ID -->
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

        <!-- 조회 버튼 -->
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
    <!-- 첫 번째 카드 -->
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

    <!-- 두 번째 카드 -->
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
          <div class="text-center pt-2 mb-3 px-3">
            <v-pagination v-model="pagination1" :length="pageCount1"></v-pagination>
          </div>
        </template>
      </v-data-table>
    </v-col>

    <v-col cols="12" lg="7">
      <v-data-table class="border rounded-md text-center light"
                    style="height: 400px; font-size: 12px; --easy-table-header-font-size: 12px;"
                    hide-default-footer
                    fixed-header
                    :headers="headers2"
                    :items="CodeDatatables"
                    :items-per-page="itemsPerPage"
                    v-model:page="pagination2"
                    @click:row="(event: Event, item: any) => selectMenu2(item.item)"
                    :sort-by="[{ key: 'id', order: 'asc' }]">
        <template v-slot:bottom>
          <div class="text-center pt-2 mb-3 px-3">
            <v-pagination v-model="pagination2" :length="pageCount2"></v-pagination>
          </div>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>

