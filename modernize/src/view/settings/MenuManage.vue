<script setup lang="ts">
import {computed, nextTick, ref, watch} from 'vue';
import {MenuDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import {UserDataTables} from '@/_mockApis/custom/ZoomData';
import type {Menu} from '@/types/apps/SampleType';
import {useTableManager} from "@/common/useTableManager";
import type {FormField} from "@/types/custom/InputTypes";
import UiParentCard from "@/components/shared/UiParentCard.vue";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import {alert, confirm} from '@/common/alertService';
import PaginationControl from "@/components/custom/pagination/PaginationControl.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";

const formFields = ref<FormField[]>([
  {label: '대상그룹', name: "team", type: "search_list", value: "", searchObj: UserDataTables, required: false, disabled: false},
  {label: '사원번호', name: "employeeId", type: "search_list", value: "", searchObj: UserDataTables, required: false, disabled: false},
  {label: "이름", name: "username", type: "search_list", value: "", searchObj: UserDataTables,  required: false, disabled: false},
]);

const headers = ref<any[]>([
  {title: '메인 메뉴', align: 'center', key: 'main'},
  {title: '서브 메뉴', align: 'center', key: 'sub'},
]);

const headers2 = ref<any[]>([
  {title: '순번', align: 'center', key: 'id'},
  {title: '메뉴 ID', align: 'center', key: 'menuid'},
  {title: '메뉴명', align: 'center', key: 'main'},
  {title: 'URL', align: 'center', key: 'url'},
  {title: '메뉴표시', align: 'center', key: 'sub'},
  {title: '이미지 파일명', align: 'center', key: 'imagename'},
  {title: '정렬', align: 'center', key: 'sortid'},
  {title: '변경', value: 'actions', align: 'center',  sortable: false},
]);

const deviceFormFields = ref<FormField[]>([
  {label: "순번", name: "id", type: "text", value: "", required: false, disabled: false},
  {label: "메뉴 ID", name: "menuid", type: "text", value: "", required: false, disabled: false},
  {label: "메뉴명", name: "main", type: "text", value: "", required: false, disabled: false},
  {label: "URL", name: "url", type: "text", value: "", required: false, disabled: false},
  {label: "메뉴표시", name: "sub", type: "text", value: "", required: false, disabled: false},
  {label: "이미지 파일명", name: "imagename", type: "text", value: "", required: false, disabled: false},
  {label: "정렬", name: "sortid", type: "text", value: "", required: false, disabled: false},
]);

const identifierField: string = 'id';

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
} = useTableManager<Menu>(MenuDatatables, formFields, deviceFormFields, identifierField);


// ✅ edit 값이 바뀌면 다이얼로그 열기/닫기, 다이얼로그 닫히면 edit 값도 변경.
const detailDialog = ref<InstanceType<typeof CustomSlotDialog> | null>(null);
watch(edit, val => detailDialog.value && (detailDialog.value.showDialog = val));
watch(() => detailDialog.value?.showDialog, val => typeof val === 'boolean' && (edit.value = val));

const formRef = ref();
const onClickSave = async () => {
  console.log('onClickSave');
  const validateForm = await formRef.value?.validateForm;
  if (!!validateForm) {
    await onSave(validateForm);
  } else {
    await alert('저장에 실패하였습니다. 다시 시도해주세요');
  }
}

const editedIndex = ref(-1)

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
  const result = await confirm("메뉴를 삭제하시겠습니까?");

  if (index === -1) {
    alert('이미 삭제된 항목입니다.');
    return;
  } else if (result) {
    filteredList.value.splice(index, 1);
  }
}

const itemsPerPage = ref(5);
const pagination = ref(1);
const pageCount = computed(() => {
  return Math.ceil(filteredList.value.length / itemsPerPage.value);
});


///////////////////////////////////////////////////////////////////////////////
const status = ref("");
const billFrom = ref();
const billFrom2 = ref();
const rules = ref([
  (v: string) => !!v || 'rule',
  (v: string) => (v && v.length <= 20) || 'rule must be less than 20 characters'
]);

const selectedItems = ref<Menu[]>([]);
const leftTableData = ref(MenuDatatables);

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


//////////////////////////////////////////////////////////////////////////
// 오른쪽 위 테이블로 데이터 이동
function moveDataToRight() {
  if (selectedItems.value.length > 0) {
    console.log("selectedItems = ", JSON.stringify(selectedItems.value));

    rightUpTableData.value.push(...selectedItems.value);
    leftTableData.value = leftTableData.value.filter(item => !selectedItems.value.includes(item));
    selectedItems.value = [];  // 선택 영역 초기화
  } else {
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
  } else {
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
  } else {
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
  } else {
    console.log("selectedItems is empty");
  }
}

const saveChanges = () => {
  console.log("데이터:", menuData.value);
};

const cancleChanges = () => {
  menuData.value = null;
  isCardVisible.value = false;
};

const deleteChanges = () => {
  menuData.value = null;
  isCardVisible.value = false;
};


const isCardVisible = ref(false);  // 카드 표시 여부 상태
const menuData = ref<Menu | null>(null);

function selectMenu(item: any) {
  console.log("선택된 메뉴 데이터:", item);
  menuData.value = {
    id: item.id || "",
    menuid: item.menuid || "",
    main: item.main || "",
    url: item.url || "",
    sub: item.sub || "",
    imagename: item.imagename || "",
    sortid: item.sortid || "",
    selectable: item.selectable ?? false,
  };

  // 메뉴가 선택될 때 카드 표시
  isCardVisible.value = true;
}
</script>

<style scoped lang="scss">

</style>

<template>
  <br>
  <UiParentCard title="줌 폰 디바이스 관리">
    <h5 class="text-20 mb-7">할당 메뉴 변경</h5>

    <v-row>
      <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true" :hide-details="true">
        <template v-slot:lineBtn="{validateForm}">
          <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
          <v-btn color="primary" flat @click="onClickSave">저장</v-btn>
          <v-btn flat color="primary" variant="outlined" @click="()=>{ onNew(); }"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
        </template>
      </CustomSearchChecksForm>
    </v-row>
<!--
    <v-row class="d-flex align-center flex-nowrap" no-gutters style="gap: 15px;">
      &lt;!&ndash; 대상그룹 &ndash;&gt;
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

      &lt;!&ndash; 사원번호 &ndash;&gt;
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

      &lt;!&ndash; 이름 &ndash;&gt;
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

      &lt;!&ndash; 버튼들 &ndash;&gt;
      <v-col cols="auto" class="d-flex align-center">
        <v-btn color="primary" flat class="mr-3">조회</v-btn>
        <v-btn color="primary" flat class="mr-3">취소</v-btn>
        <v-btn color="primary" flat>저장</v-btn>
      </v-col>
    </v-row>-->


    <br>
    <v-card class="mt-5 mb-3" elevation="0" style="max-width: 100%; margin-right: 0;">
      <v-row style="width: 100%; padding: 0;" align="center" justify="center">
        <v-col cols="12" lg="5">
          <h5>※ 할당 메뉴</h5>
          <v-data-table
              class="border rounded-md text-center light scrollable-card mt-1"
              style="height: 625px;"
              show-select
              v-model="selectedItems"
              return-object
              hide-default-footer
              fixed-header
              :headers="headers"
              :items="leftTableData"
              item-key="main" W
              :sort-by="[{ key: 'main', order: 'asc' }]"
              :items-per-page="30"
          >
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
          <h5>※ 기본 할당 메뉴</h5>
          <v-data-table
              class="border rounded-md text-center light scrollable-card mt-1"
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
          </v-data-table>
          <br>
          <h5>※ 미할당 메뉴</h5>
          <v-data-table
              class="border rounded-md text-center light scrollable-card mt-1"
              style="height: 300px; width: 100%;"
              show-select
              v-model="selectedItems"
              return-object
              hide-default-footer
              fixed-header
              :headers="headers"
              :items="rightDownTableData"
              item-key="main"
              :sort-by="[{ key: 'main', order: 'asc' }]">
          </v-data-table>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12">
          <h5>※ 메뉴 리스트</h5>
          <!--        <v-data-table class="border rounded-md text-center light scrollable-card"
                                style="height: 450px; width: 100%;"
                                hide-default-footer
                                fixed-header
                                :headers="headers2"
                                :items="MenuDatatables"
                                :items-per-page="itemsPerPage"
                                v-model:page="pagination"
                                :sort-by="[{ key: 'id', order: 'asc' }]"
                                @click:row="(event: Event, item: any) => selectMenu(item.item)">
                    <template v-slot:bottom>
                      <div class="text-center pt-2 mb-3 px-3">
                        <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
                      </div>
                    </template>
                  </v-data-table>-->

          <v-data-table :headers="headers2" :items="filteredList" :item-value="identifierField"
                        select-strategy="single" class="border rounded-md"
                        v-model="selectedEmpId"
                        :items-per-page="itemsPerPage"
                        v-model:page="pagination"
                        @update:model-value="onSelectionChange"
                        @click:row="(event: Event, item: any) => selectMenu(item.item)"
          >
            <template v-slot:item.model="{ item }">
              <div :key="`${item.id}-${item.main}`">
                {{ item.id }} {{ item.main }}
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
                  :pagination="pagination"
                  :page-count="pageCount"
                  @update:itemsPerPage="(val: number) => itemsPerPage = val"
                  @update:pagination="(val: number) => pagination = val"
              />
            </template>
          </v-data-table>
        </v-col>
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
    </v-card>

<!--    <v-card title="※ 메뉴 정보" v-if="menuData" v-show="isCardVisible" class="mt-5">
      <v-container>
        &lt;!&ndash;      <v-card elevation="0" class="pa-5" max-width="800px" width="100%">&ndash;&gt;
        <v-card-title class="text-h6">메뉴 수정</v-card-title>
        <v-divider class="mb-4"></v-divider>

        <v-row class="mb-3">
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">메뉴ID</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field v-model="menuData.id" variant="outlined" dense hide-details></v-text-field>
          </v-col>
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">메뉴명</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field v-model="menuData.sub" variant="outlined" dense hide-details readonly></v-text-field>
          </v-col>
        </v-row>

        <v-row class="mb-3">
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">상위 메뉴</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field v-model="menuData.main" variant="outlined" dense hide-details></v-text-field>
          </v-col>
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">메뉴레벨</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field v-model="menuData.sortid" variant="outlined" dense hide-details readonly></v-text-field>
          </v-col>
        </v-row>

        <v-row class="mb-3">
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">이미지 파일명</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field v-model="menuData.imagename" variant="outlined" dense hide-details></v-text-field>
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
            <v-textarea variant="outlined" dense hide-details></v-textarea>
          </v-col>
        </v-row>

        <v-row class="mb-3">
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">최종변경일자</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field dense hide-details></v-text-field>
          </v-col>
          <v-col cols="2" class="d-flex align-center justify-center">
            <v-label class="font-weight-medium">최종변경자</v-label>
          </v-col>
          <v-col cols="4">
            <v-text-field variant="outlined" dense hide-details readonly></v-text-field>
          </v-col>
        </v-row>

        <v-row justify="center" class="mt-4">
          <v-btn color="primary" class="mx-2" @click="saveChanges">등록</v-btn>
          <v-btn color="error" class="mx-2" @click="deleteChanges">삭제</v-btn>
          <v-btn color="warning" class="mx-2" @click="cancleChanges">취소</v-btn>
          <v-btn color="info" class="mx-2" @click="saveChanges">저장</v-btn>
        </v-row>
        &lt;!&ndash;      </v-card>&ndash;&gt;
      </v-container>
    </v-card>-->
  </UiParentCard>
</template>
