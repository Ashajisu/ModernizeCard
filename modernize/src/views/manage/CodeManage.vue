<script setup lang="ts">
import { computed, nextTick, ref, watch } from 'vue';
import {SearchIcon} from 'vue-tabler-icons';
import {CodeDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import type { Code } from '@/types/apps/SampleType';

const page = ref({ title: '코드 관리' });

const dialog = ref(false)
const dialogDelete = ref(false)

const codeDatatables = ref(CodeDatatables)

const billFrom = ref();
const codeName = ref();
const codeCase = ref();
const notUse = ref();
const codeid = ref();

const rules = ref([
  (v: string) => !!v || 'rule',
  (v: string) => (v && v.length <= 20) || 'rule must be less than 20 characters'
]);
const headers1 = [
  { title: '분류', align: 'center', key: 'id' },
  { title: '코드 ID', align: 'center', key: 'maincode'},
  { title: '코드명', align: 'center', key: 'codename' },
  { title: '변경 여부', align: 'center', key: 'changecode' },
  { title: '사용', align: 'center', key: 'usingcode' },
  // { title: '기능', align: 'center', key: 'actions' },
] as const;
const headers2 = [
  { title: '메인 코드', align: 'center', sortable: false, key: 'maincode',},
  { title: '기타코드 #1', align: 'center',  key: 'etccode1' },
  { title: '기타코드 #2', align: 'center',key: 'etrcode2' },
  { title: '코드명', align: 'center',key: 'codename' },
  { title: '정렬', align: 'center',key: 'id' },
  { title: '사용', align: 'center', key: 'usingcode' },
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

const itemsPerPage = ref(50);
const pagination = ref(1);
const pageCount = Math.ceil(CodeDatatables.length / itemsPerPage.value);

const formTitle = computed(() => {
  return editedIndex.value === -1 ? 'New Item' : 'Edit Item'
})


//취소
function close() {
  dialog.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
}
function closeDelete() {
  dialogDelete.value = false
  nextTick(() => {
    editedItem.value = Object.assign({}, defaultItem.value)
    editedIndex.value = -1
  })
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
        <v-col cols="12" lg="5" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px;">코드명</v-label>
          <v-text-field
              hide-details
              label="Search Code"
              v-model="codeName"
              :rules="rules"
              required>
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
        <v-col cols="12" lg="2" class="d-flex align-center" >
          <v-checkbox-btn :model-value="false" color='primary'></v-checkbox-btn>
          <v-label class="font-weight-semibold pb-2" style="margin-right: 70px;">미사용</v-label>
        </v-col>

        <!-- 코드 ID -->
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px; margin-left: -50px;">코드 ID</v-label>
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
        <v-col cols="12" lg="1" class="d-flex align-center">
          <v-btn color="primary" flat to="/apps/invoice/create"
                 style="min-height: 44px; min-width: 80px; margin-left: -10px;">조 회</v-btn>
        </v-col>
      </v-row>
    </v-card-item>
  </v-card>

  <br>
  <v-card elevation="10">
  <v-row class="d-flex align-center ma-2">
    <v-col cols="12" lg="5" class="d-flex align-center">
      <v-label style="margin-left:60px; text-align: center; ">총 건수</v-label>
      <v-text-field class="pa-2 text-center custom-text-field"
                    hide-details
                    value="12"
                    style="text-align:center; max-width: 130px; min-width: 130px;"></v-text-field>
      <v-label>건</v-label>

      <v-label style="margin-left: 50px;  ">조회</v-label>
      <v-text-field
          :model-value="itemsPerPage"
          class="pa-2"
          type="number"
          min="10"
          max="50"
          hide-details
          style="text-align: center; max-width: 100px; min-width: 100px;"
          @update:model-value="itemsPerPage = parseInt($event, 10)"
      />
    </v-col>
    <v-col cols="12" lg="7" class="d-flex align-center">
      <v-btn color="primary" flat to="/apps/invoice/create" style="margin-left: 70px; min-height: 44px; min-width: 100px; ">등 록</v-btn>
      <v-btn color="secondary" flat to="/apps/invoice/create" style="margin-left: 10px; min-height: 44px; min-width: 100px; ">수 정</v-btn>
      <v-btn color="error" flat to="/apps/invoice/create" style="margin-left: 10px; min-height: 44px; min-width: 100px; ">삭 제</v-btn>
      <v-btn color="warning" flat to="/apps/invoice/create" style="margin-left: 10px; min-height: 44px; min-width: 100px; ">취 소</v-btn>
      <v-btn color="info" flat to="/apps/invoice/create" style="margin-left: 10px; min-height: 44px; min-width: 100px; ">저 장</v-btn>
    </v-col>
  </v-row>
  </v-card>
  <v-row>
    <!--        <v-card class="d-flex align-center">-->

    <v-col cols="12" lg="5">
      <v-table class="border rounded-md text-center light" style="min-height: 130px; font-size: 13px;">
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
            <v-text-field class="mt-2"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-3" style="min-width:70px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-4" style="min-width:100px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-5"  style="min-width:50px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-7" style="min-width:50px;"></v-text-field>
          </td>
        </tr>
        </tbody>
      </v-table>
    </v-col>

    <v-col cols="12" lg="7">
      <v-table class="border rounded-md text-center light" style="min-height: 130px; font-size: 13px;">
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
            <v-text-field class="mt-2" value="0001" style="min-width:70px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-3" value="0001" style="min-width:70px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-4" value="001" style="min-width:70px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-5" value="영업본부" style="min-width:100px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field type="number" value="1" class="mt-6" style="max-width:70px;"></v-text-field>
          </td>
          <td class="text-center">
            <v-text-field class="mt-7" value="Y" style="min-width:70px;"></v-text-field>
          </td>
        </tr>
        </tbody>
      </v-table>
    </v-col>


    <v-col cols="12" lg="5">
        <v-data-table class="border rounded-md text-center light scrollable-card" style="height: 400px;"
                      hide-default-footer  :headers="headers1" :items="CodeDatatables" :sort-by="[{ key: 'id', order: 'asc' }]">
          <template v-slot:bottom>
            <div class="text-center pt-2 mb-3 px-3">
              <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
            </div>
          </template>
        </v-data-table>
    </v-col>

    <v-col cols="12" lg="7">
      <v-data-table class="border rounded-md text-center light"  style="height: 400px;"
                    :headers="headers2" item-value="name" :items="CodeDatatables" :sort-by="[{ key: 'id', order: 'asc' }]">

         <template v-slot:top>

        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon color="info" size="small" class="me-2">  // @click="editItem(item)"
            mdi-pencil
          </v-icon>
          <v-icon color="error" size="small" > //@click="deleteItem(item)"
            mdi-delete
          </v-icon>
        </template>
        <template v-slot:no-data>

        </template>
        <template v-slot:bottom>
          <div class="text-center pt-2 mb-3 px-3">
            <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
          </div>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>