<script setup lang="ts">
import { ref } from "vue";
import {MenuDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import type { Menu } from '@/types/apps/SampleType';

const page = ref({ title: '메뉴 관리' });
const headers = [
  { title: '메인 메뉴', align: 'center', key: 'main' },
  { title: '서브 메뉴', align: 'center', key: 'sub' },
] as const;
const headers2 = [
  { title: '순번', align: 'center', key: 'id' },
  { title: '메뉴 ID', align: 'center', key: 'menuid' },
  { title: '메뉴명', align: 'center', key: 'main' },
  { title: 'URL', align: 'center', key: 'url' },
  { title: '메뉴표시', align: 'center', key: 'sub' },
  { title: '이미지 파일명', align: 'center', key: 'imagename' },
  { title: '정렬', align: 'center', key: 'sortid' },
] as const;

const status = ref("");
const billFrom = ref();
const billFrom2 = ref();
const rules = ref([
  (v: string) => !!v || 'rule',
  (v: string) => (v && v.length <= 20) || 'rule must be less than 20 characters'
]);

const selectedItems = ref<Menu[]>([]);
const leftTableData = ref(MenuDatatables);
const itemsPerPage = ref(3);
const pagination = ref(1);
const pageCount = Math.ceil(MenuDatatables.length / itemsPerPage.value);
const rightUpTableData = ref<Menu[]>([
  {
    main: '사용자 및 조직',
    sub: '사용자',
    selectable: false,
    id: '1',
    menuid: 'menu1',
    url: '/ecommerce/addProduct2',
    imagename: 'image1.png',
    sortid: '1',
  },
  {
    main: '사용자 및 조직',
    sub: '폰 사용자',
    selectable: true,
    id: '2',
    menuid: 'menu2',
    url: '/ecommerce/editProduct2',
    imagename: 'image2.png',
    sortid: '2',
  },
]);
const rightDownTableData = ref<Menu[]>([
/*  {
    main: 'E-Commerce',
    sub: 'AddProduct3',
    selectable:false,
  },
  {
    main: 'E-Commerce',
    sub: 'EditProduct3',
    selectable:true,
  },*/
  {
    main: '환경설정',
    sub: '메뉴 관리',
    selectable: false,
    id: '1',
    menuid: 'menu1',
    url: '/ecommerce/addProduct3',
    imagename: 'image1.png',
    sortid: '1',
  },
  {
    main: '환경설정',
    sub: '코드관리',
    selectable: true,
    id: '2',
    menuid: 'menu2',
    url: '/ecommerce/editProduct3',
    imagename: 'image2.png',
    sortid: '2',
  },
]);

// 오른쪽 위 테이블로 데이터 이동
function moveDataToRight() {
  if (selectedItems.value.length > 0) {
    console.log("selectedItems = ", JSON.stringify(selectedItems.value));

    rightUpTableData.value.push(...selectedItems.value);
    leftTableData.value = leftTableData.value.filter(item => !selectedItems.value.includes(item));
    selectedItems.value = [];  // 선택 영역 초기화
  }
  else{
    console.log("selectedItems is empty");
  }
}

// 왼쪽 테이블로 데이터 이동
function moveDataToLeft() {
  if (selectedItems.value.length > 0) {
    console.log("selectedItems = ", JSON.stringify(selectedItems.value));

    leftTableData.value.push(...selectedItems.value);
    rightUpTableData.value = rightUpTableData.value.filter(item => !selectedItems.value.includes(item));
    selectedItems.value = [];  // 선택 영역 초기화
  }
  else{
    console.log("selectedItems is empty");
  }
}

// 오른쪽 아래 테이블로 데이터 이동
function moveDownDataToRight() {
  if (selectedItems.value.length > 0) {
    console.log("selectedItems = ", JSON.stringify(selectedItems.value));

    rightDownTableData.value.push(...selectedItems.value);
    leftTableData.value = leftTableData.value.filter(item => !selectedItems.value.includes(item));
    selectedItems.value = [];  // 선택 영역 초기화
  }
  else{
    console.log("selectedItems is empty");
  }
}

// 왼쪽 테이블로 데이터 이동
function moveDownDataToLeft() {
  if (selectedItems.value.length > 0) {
    console.log("selectedItems = ", JSON.stringify(selectedItems.value));

    leftTableData.value.push(...selectedItems.value);
    rightDownTableData.value = rightDownTableData.value.filter(item => !selectedItems.value.includes(item));
    selectedItems.value = [];  // 선택 영역 초기화
  }
  else{
    console.log("selectedItems is empty");
  }
}


const menuData = ref({
  menuid: "MENU001",
  menuname: "E-Commerce",
  parent: "shop",
  menulevel: "1",
  image: "sortid.png",
  url: "apps/menu",
  lastModified: "2025-03-12",
  lastuser: "홍길동",
  description: "테스트 메뉴입니다.",
});

const saveChanges = () => {
  console.log("변경된 데이터:", menuData.value);
};

</script>

<style scoped lang="scss">

</style>

<template>
  <br>
  <v-card class="mt-5 mb-3" elevation="10" style="max-width: 100%; margin-right: 0;">
    <v-card-item>
      <h5 class="text-20 mb-7">할당 메뉴 변경</h5>

      <v-row class="d-flex align-center flex-nowrap" no-gutters style="gap: 15px;">
        <!-- 대상그룹 -->
        <v-col cols="auto" class="d-flex align-center">
          <v-label class="font-weight-semibold">대상그룹</v-label>
        </v-col>
        <v-col cols="3">
          <v-select
              v-model="status"
              hide-details
              :items="leftTableData"
              required
          />
        </v-col>

        <!-- 사원번호 -->
        <v-col cols="auto" class="d-flex align-center">
          <v-label class="font-weight-semibold">사원번호</v-label>
        </v-col>
        <v-col cols="2">
          <v-text-field
              v-model="billFrom"
              :rules="rules"
              required
              hide-details
          />
        </v-col>

        <!-- 이름 -->
        <v-col cols="auto" class="d-flex align-center">
          <v-label class="font-weight-semibold">이름</v-label>
        </v-col>
        <v-col cols="2">
          <v-text-field
              v-model="billFrom2"
              :rules="rules"
              required
              hide-details
          />
        </v-col>

        <!-- 버튼들 -->
        <v-col cols="auto" class="d-flex align-center">
          <v-btn color="primary" flat class="mr-3">조회</v-btn>
          <v-btn color="primary" flat class="mr-3">취소</v-btn>
          <v-btn color="primary" flat>저장</v-btn>
        </v-col>
      </v-row>
    </v-card-item>
  </v-card>



  <br>
  <v-card class="mt-5 mb-3" elevation="0" style="max-width: 100%; margin-right: 0;">
    <v-row style="width: 100%; padding: 0;" align="center" justify="center">
      <v-col cols="12" lg="5">
        <v-data-table
            class="border rounded-md text-center light scrollable-card"
            style="height: 625px; width: 100%;"
            show-select
            v-model="selectedItems"
            return-object
            hide-default-footer
            fixed-header
            :headers="headers"
            :items="leftTableData"
            item-key="main"
            :sort-by="[{ key: 'main', order: 'asc' }]"
            :items-per-page="30"
        >
          <template v-slot:top>
            <v-toolbar flat>
              <v-toolbar-title>※ 할당된 메뉴</v-toolbar-title>
            </v-toolbar>
          </template>
        </v-data-table>
      </v-col>

      <v-col cols="12" md="1" class="d-flex flex-column justify-center align-center">
        <v-btn color="primary" icon @click="moveDataToRight" class="my-10 mt-16">
          <v-icon>mdi-chevron-right</v-icon>
        </v-btn>
        <v-btn color="primary" icon @click="moveDataToLeft" class="mb-16">
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>
        <v-btn color="primary" icon @click="moveDownDataToRight" class="my-10 mt-16">
          <v-icon>mdi-chevron-right</v-icon>
        </v-btn>
        <v-btn color="primary" icon @click="moveDownDataToLeft" class="mb-10">
          <v-icon>mdi-chevron-left</v-icon>
        </v-btn>
      </v-col>

      <v-col cols="12" lg="5">
        <v-data-table
            class="border rounded-md text-center light scrollable-card"
            style="height: 300px; width: 100%;"
            show-select
            v-model="selectedItems"
            return-object
            hide-default-footer
            fixed-header
            :headers="headers"
            :items="rightUpTableData"
            item-key="main"
            :sort-by="[{ key: 'main', order: 'asc' }]">
          <template v-slot:top>
            <v-toolbar flat>
              <v-toolbar-title>※ 기본 할당 메뉴</v-toolbar-title>
            </v-toolbar>
          </template>
        </v-data-table>
        <br>
        <v-data-table
            class="border rounded-md text-center light scrollable-card"
            style="height: 300px; width: 100%;"
            show-select
            v-model="selectedItems"
            return-object
            hide-default-footer
            :headers="headers"
            :items="rightDownTableData"
            item-key="main"
            :sort-by="[{ key: 'main', order: 'asc' }]">
          <template v-slot:top>
            <v-toolbar flat>
              <v-toolbar-title>※ 미 할당 메뉴</v-toolbar-title>
            </v-toolbar>
          </template>
        </v-data-table>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <h5>※ 메뉴 리스트</h5>
        <v-data-table class="border rounded-md text-center light scrollable-card"
                      style="height: 450px; width: 100%;"
                      hide-default-footer
                      fixed-header
                      :headers="headers2"
                      :items="MenuDatatables"
                      :sort-by="[{ key: 'id', order: 'asc' }]">
          <template v-slot:bottom>
            <div class="text-center pt-2 mb-3 px-3">
              <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
            </div>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-card>


  <br>
  <v-row>
    <v-col cols="12">
      <h5>※ 메뉴 정보</h5>
      <v-container class="mt-5">
        <v-card elevation="4" class="pa-5" max-width="800px" width="100%">
          <v-card-title class="text-h6">메뉴 수정</v-card-title>
          <v-divider class="mb-4"></v-divider>

          <v-row class="mb-3">
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">메뉴ID</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.menuid" variant="outlined" dense hide-details></v-text-field>
            </v-col>
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">메뉴명</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.menuname" variant="outlined" dense hide-details readonly></v-text-field>
            </v-col>
          </v-row>

          <v-row class="mb-3">
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">상위 메뉴</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.parent" variant="outlined" dense hide-details></v-text-field>
            </v-col>
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">메뉴레벨</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.menulevel" variant="outlined" dense hide-details readonly></v-text-field>
            </v-col>
          </v-row>

          <v-row class="mb-3">
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">이미지 파일명</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.image" variant="outlined" dense hide-details></v-text-field>
            </v-col>
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">URL</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.url" variant="outlined" dense hide-details readonly></v-text-field>
            </v-col>
          </v-row>

          <v-row class="mb-3">
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">내용</v-label>
            </v-col>
            <v-col cols="10">
              <v-textarea v-model="menuData.description" variant="outlined" dense hide-details></v-textarea>
            </v-col>
          </v-row>

          <v-row class="mb-3">
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">최종변경일자</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.lastModified" type="date" variant="outlined" dense hide-details></v-text-field>
            </v-col>
            <v-col cols="2" class="d-flex align-center justify-center">
              <v-label class="font-weight-medium">최종변경자</v-label>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="menuData.lastuser" variant="outlined" dense hide-details readonly></v-text-field>
            </v-col>
          </v-row>

          <v-row justify="center" class="mt-4">
            <v-btn color="primary" class="mx-2" @click="saveChanges">등록</v-btn>
            <v-btn color="error" class="mx-2" @click="saveChanges">삭제</v-btn>
            <v-btn color="warning" class="mx-2" @click="saveChanges">취소</v-btn>
            <v-btn color="info" class="mx-2" @click="saveChanges">저장</v-btn>
          </v-row>
        </v-card>
      </v-container>
    </v-col>
  </v-row>


</template>
