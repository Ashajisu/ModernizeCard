<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {ZoomPhoneDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import {SearchIcon, ArrowBigDownFilledIcon, ArrowBigUpFilledIcon} from 'vue-tabler-icons';
import type { ZoomPhone } from '@/types/apps/SampleType';
import { useContactStore } from '@/stores/apps/contact';

const store = useContactStore();

const TriangleUpComponent = ArrowBigUpFilledIcon;
const TriangleDownComponent = ArrowBigDownFilledIcon;

onMounted(() => {
  store.fetchContacts();
});
const getContacts: any = computed(() => {
  return store.contacts;
});

const page = ref({ title: '줌 폰 디바이스 관리' });
const dialog1 = ref(false);
const dialog2 = ref(false);
const dialog3 = ref(false);
const dialog4 = ref(false);
const dialog5 = ref(false);
const dialog6 = ref(false);
const dialog7 = ref(false);
const dialog8 = ref(false);
const dialog9 = ref(false);
const dialogUpload = ref(false);
const user = ref();
const phoneNum = ref();
const phoneStatus = ref();
const model = ref();
const mac= ref()
const ip= ref();
const rules = ref([
  (v: string) => !!v || 'rule',
  (v: string) => (v && v.length <= 20) || 'rule must be less than 20 characters'
]);

const button1 = ref(false);

const editDevice = () => {
  console.log("편집 버튼 클릭", selectedItem.value);
};

const restartDevice = () => {
  console.log("재시작 버튼 클릭", selectedItem.value);
};

const selectDevice = (item: ZoomPhone | null | ZoomPhone[]) => {
  console.log("item : " + JSON.stringify(item));

  // 배열일 경우 첫 번째 요소만 선택
  if (Array.isArray(item)) {
    item = item.length > 0 ? item[0] : null;
  }

  // 현재 선택된 값과 동일하면 선택 해제
  if (JSON.stringify(modifytItem.value) === JSON.stringify(item)) {
    modifytItem.value = null;
    button1.value = false; // 선택 해제 시 버튼 숨김
    console.log("button1.value : " + button1.value);
    return;
  }

  // item이 null이거나 빈 배열일 경우 초기화
  if (!item) {
    modifytItem.value = null;
    button1.value = false;
    console.log("button1.value : " + button1.value);
    return;
  }

  // 새로운 아이템 선택
  modifytItem.value = { ...item };
  button1.value = true;
  console.log("modifytItem 2 : " + JSON.stringify(modifytItem.value));
  console.log("button1.value : " + button1.value);
};

const headers = [
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
] as const;

const itemsPerPage = ref(3);
const pagination = ref(1);
const pageCount = Math.ceil(ZoomPhoneDatatables.length / itemsPerPage.value)
const PhoneStatus = ref(['Online','Offline']);

const selectedItem = ref<ZoomPhone | null>(null);
const modifytItem = ref<ZoomPhone | null>(null);

const makeExcelFile4 = () => {
  let tab_text = '<html xmlns:x="urn:schemas-microsoft-com:office:excel">';
  tab_text += '<head><meta http-equiv="content-type" content="application/vnd.ms-excel; charset=UTF-8">';
  tab_text += '<xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet>';
  tab_text += '<x:Name>Test Sheet</x:Name>';
  tab_text += '<x:WorksheetOptions><x:Panes></x:Panes></x:WorksheetOptions></x:ExcelWorksheet>';
  tab_text += '</x:ExcelWorksheets></x:ExcelWorkbook></xml></head><body>';
  tab_text += "<table border='1'>";

  // 테이블 헤더 생성
  tab_text += "<thead><tr>";
  headers.forEach((header) => {
    tab_text += `<th>${header.title}</th>`;
  });
  tab_text += "</tr></thead>";

  // 테이블 데이터 생성
  tab_text += "<tbody>";
  ZoomPhoneDatatables.forEach((item) => {
    tab_text += "<tr>";
    headers.forEach((header) => {
      const key = header.key as keyof ZoomPhone;
      tab_text += `<td>${item[key]}</td>`;
    });
    tab_text += "</tr>";
  });
  tab_text += "</tbody>";

  tab_text += "</table></body></html>";

  // Blob을 생성하여 다운로드 트리거
  const fileName = 'ZoomPhoneData.xls';
  const a_tag = document.createElement('a');
  const blob = new Blob([tab_text], { type: 'application/vnd.ms-excel;charset=utf-8;' });
  a_tag.href = window.URL.createObjectURL(blob);
  a_tag.download = fileName;
  a_tag.click();  // 다운로드 시작
}

const onDeleteDialog = () => {
  if (selectedItem.value) {
    console.log("selectedItem : " + JSON.stringify(selectedItem.value));
    dialog3.value = true; // 삭제 모달 열기
  } else {
    alert('항목을 선택하세요!'); // 항목을 선택하지 않으면 메시지 표시
  }
};


const excelUpload = () => {
  dialogUpload.value = true;
}


const excelUploadStart = () => {
  dialogUpload.value = false;
}

const excelUploadCancle = () => {
  dialogUpload.value = false;
}

function deleteItem(item: any) {
  if (!item || Object.keys(item).length === 0) {
    alert("삭제할 항목을 선택해주세요.");
    return;
  }

  console.log("삭제할 항목 :", JSON.stringify(item));

  editedIndex.value = zoomPhone.value.indexOf(item);
  console.log("index : ", editedIndex.value);
  editedItem.value = Object.assign({}, item);
  zoomPhone.value.splice(editedIndex.value, 1);
  dialog4.value = true;
}

function save() {
  if (editedIndex.value > -1) {
    Object.assign(zoomPhone.value[editedIndex.value], editedItem.value);
  } else {
    zoomPhone.value.push(editedItem.value);
    console.log("zoomPhone :" + JSON.stringify(zoomPhone.value));
  }
  dialog1.value = false;
  dialog2.value = false;
  // close();
}

const zoomPhone = ref(ZoomPhoneDatatables);
const editedIndex = ref(-1);
const editedItem = ref({
  phoneName: '',
  user: '',
  phoneNum: '',
  pstnNum: '',
  phoneStatus: '',
  manufacturer: '',
  modelName: '',
  macAddress: '',
  firmware: '',
  publicAddress: '',
  privateAddress: '',
  assignmentType: '',
  assignmentTarget: ''
});

const defaultItem = ref({
  phoneName: '',
  user: '',
  phoneNum: '',
  pstnNum: '',
  phoneStatus: '',
  model: '',
  macAddress: '',
  firmware: '',
  publicAddress: '',
  privateAddress: '',
});

</script>

<template>
  <!--    <BaseBreadcrumb :title="page.title" :breadcrumbs="breadcrumbs"></BaseBreadcrumb>-->
  <br>
  <v-card elevation="10" class="pa-5">
    <v-card-title class="text-h4 font-weight-bold mt-2"> 줌 폰 디바이스 관리</v-card-title>
    <v-card-title class="text-h6 font-weight-bold d-flex align-items-center justify-space-between">
      줌 폰의 디바이스 관리를 할 수 있습니다.
      <div class="d-flex flex-row align-items-center">
        <v-btn color="primary" flat @click="">조회</v-btn>
        <v-btn color="primary" variant="outlined" class="ml-2" @click="">초기화</v-btn>
      </div>
    </v-card-title>
    <v-card-item>
      <v-row class="d-flex align-center">
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2 mt-2" style="margin-right: 8px;">사용자명</v-label>
          <v-text-field
              density="compact"
              hide-details
              class="mt-2"
              label="사용자명 입력"
              v-model="user"
              :rules="rules"
              required>
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>

        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2 mt-2" style="margin-right: 8px;">내선번호</v-label>
          <v-text-field
              density="compact"
              hide-details
              class="mt-2"
              label="내선번호 입력"
              v-model="phoneNum"
              :rules="rules"
              required
              style=""
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>

        <!-- 코드유형 -->
        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2 mt-2" style="margin-right: 8px;">상태</v-label>
          <v-select
              density="compact"
              hide-details
              class="mt-2"
              v-model="phoneStatus"
              :items="PhoneStatus"
              required
          />
        </v-col>

        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2 mt-2" style="margin-right: 8px; margin-left: 0px;">모델</v-label>
          <v-text-field
              label="모델 입력"
              v-model="model"
              :rules="rules"
              required
              density="compact"
              hide-details
              class="mt-2"
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>

        <v-col cols="12" lg="2" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2 mt-2" style="margin-right: 8px; margin-left: 0px;">Mac</v-label>
          <v-text-field
              label="Mac 주소 입력"
              v-model="mac"
              :rules="rules"
              required
              density="compact"
              hide-details
              class="mt-2"
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>
        <v-col cols="12" lg="2" class="d-flex align-center ">
          <v-label class="font-weight-semibold pb-2 mt-2" style="margin-right: 8px; margin-left: 0px;">Private IP</v-label>
          <v-text-field
              label="IP 입력"
              v-model="ip"
              :rules="rules"
              required
              density="compact"
              hide-details
              class="mt-2"
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>
      </v-row>
    </v-card-item>
  </v-card>


  <br>
  <v-row>
    <!-- 왼쪽 정렬: 신규 등록, 삭제, 편집, 재시작 -->
    <v-col cols="6">
      <div class="d-flex gap-3 justify-start flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
        <v-dialog v-model="dialog1" persistent class="dialog-mw">
          <template v-slot:activator="{ props }">
            <v-btn flat color="primary" variant="outlined" v-bind="props"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
          </template>
          <v-card style="height: 660px; width: 500px;" class="overflow-auto">
            <v-container>
              <v-card-title class="pa-5">
                <span class="text-h5">디바이스 추가</span>
              </v-card-title>
              <v-row>
                <v-card-text>
                  <v-row class="mb-3">
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">표시이름</v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-text-field  v-model="editedItem.phoneName"></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row class="mb-3">
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">MAC 주소</v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-text-field v-model="editedItem.macAddress"></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">디바이스 유형</v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-select
                          :items="['Avaya', 'CISCO', 'ZOOM', 'Genesys']"
                          label="제조사 선택"
                          required
                          v-model="editedItem.user"
                      ></v-select>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium"></v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-select
                          :items="['J139', 'J159', 'J179']"
                          label="모델 선택"
                          required
                          v-model="editedItem.modelName"
                      ></v-select>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">할당</v-label>
                    </v-col>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-select
                          :items="['SIP', 'H.323', 'Cloud']"
                          label="선택"
                          required
                      ></v-select>
                    </v-col>
                    <v-col cols="5" class="d-flex align-center justify-center">
                      <v-text-field v-model="editedItem.privateAddress"></v-text-field>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-row>
              <v-row class="d-flex justify-end">
                <v-card-actions>
                  <v-btn color="primary" variant="text"  @click="save" flat>
                    저장
                  </v-btn>
                  <v-btn color="error" variant="text" @click="dialog1 = false" flat style="margin-right: 30px;">
                    취소
                  </v-btn>
                </v-card-actions>
              </v-row>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog2" class="dialog-mw" persistent>
          <v-card class="pa-6" style="height: 150px; width: 300px; margin: auto;">
            <v-card-title>
              <span>저장 되었습니다.</span>
            </v-card-title>
            <v-card-actions>
              <v-btn color="primary" variant="text" @click="dialog2 = false; dialog1 = false;" flat> 확인 </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog3" persistent class="dialog-mw">
          <template v-slot:activator="{ props }">
            <v-btn flat color="error" variant="outlined" v-bind="props" @click="onDeleteDialog">
              <v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2"/>
              삭제
            </v-btn>
          </template>
          <v-card style="height: 190px; width: 470px;" class="overflow-auto">
            <v-container>
              <v-row class="mt-2" style="display: flex; justify-content: center;">
                <v-card-title style="font-size: 16px;">
                  <span>선택하신 디바이스가 삭제됩니다.</span>
                  <br> <br>
                  <span>삭제 하시겠습니까?</span>
                </v-card-title>
              </v-row>
              <v-row class="d-flex justify-center mt-5">
                <v-card-actions>
                  <v-btn color="primary" variant="text" @click="deleteItem(selectedItem)" flat>
                    확인
                  </v-btn>
                  <v-spacer></v-spacer>
                  <v-btn color="error" variant="text" @click="dialog3 = false" flat style="margin-right: 30px;">
                    취소
                  </v-btn>
                </v-card-actions>
              </v-row>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog4" class="dialog-mw" persistent>
          <v-card class="pa-6" style="height: 150px; width: 300px; margin: auto;">
            <v-card-title>
              <span>삭제 되었습니다.</span>
            </v-card-title>
            <v-card-actions>
              <v-btn color="primary" variant="text" @click="dialog4 = false; dialog3 = false;" flat> 확인 </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>


        <v-dialog v-model="dialog5" persistent class="dialog-mw">
          <template v-slot:activator="{ props }">
            <v-btn v-if="button1"  color="info" v-bind="props" flat>편집</v-btn>
          </template>
          <v-card style="height: 660px; width: 500px;" class="overflow-auto">
            <v-container>
              <v-card-title class="pa-5">
                <span class="text-h5">디바이스 추가</span>
              </v-card-title>
              <v-row>
                <v-card-text>
                  <v-row class="mb-3">
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">표시이름</v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-text-field></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row class="mb-3">
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">MAC 주소</v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-text-field></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">디바이스 유형</v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-select
                          :items="['Avaya', 'CISCO', 'ZOOM', 'Genesys']"
                          label="제조사 선택"
                          required
                      ></v-select>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium"></v-label>
                    </v-col>
                    <v-col cols="8" class="d-flex align-center justify-center">
                      <v-select
                          :items="['J139', 'J159', 'J179']"
                          label="모델 선택"
                          required
                      ></v-select>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-label class="font-weight-medium" style="margin-top: -23px;">할당</v-label>
                    </v-col>
                    <v-col cols="3" class="d-flex align-center justify-center">
                      <v-select
                          :items="['SIP', 'H.323', 'Cloud']"
                          label="선택"
                          required
                      ></v-select>
                    </v-col>
                    <v-col cols="5" class="d-flex align-center justify-center">
                      <v-text-field></v-text-field>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-row>
              <v-row class="d-flex justify-end">
                <v-card-actions>
                  <v-btn color="error" variant="text"  @click="dialog9 = !dialog9" flat style="margin-right: 200px; margin-top: -70px;">
                    할당해제
                  </v-btn>
                  <v-btn color="primary" variant="text"  @click="dialog6 = !dialog6" flat>
                    저장
                  </v-btn>
                  <v-btn color="error" variant="text" @click="dialog5 = false" flat style="margin-right: 30px;">
                    취소
                  </v-btn>
                </v-card-actions>
              </v-row>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog9" class="dialog-mw" persistent>
          <v-card class="pa-6" style="height: 150px; width: 300px; margin: auto;">
            <v-card-title>
              <span>할당해제 되었습니다.</span>
            </v-card-title>
            <v-card-actions>
              <v-btn color="primary" variant="text" @click="dialog9 = false; dialog5 = false;" flat> 확인 </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog6" class="dialog-mw" persistent>
          <v-card class="pa-6" style="height: 150px; width: 300px; margin: auto;">
            <v-card-title>
              <span>저장 되었습니다.</span>
            </v-card-title>
            <v-card-actions>
              <v-btn color="primary" variant="text" @click="dialog6 = false; dialog5 = false;" flat> 확인 </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog7" persistent class="dialog-mw">
          <template v-slot:activator="{ props }">
            <v-btn v-if="button1"  color="info" v-bind="props" flat>재시작</v-btn>
          </template>
          <v-card style="height: 190px; width: 400px;" class="overflow-auto">
            <v-container>
              <v-row class="mt-2" style="display: flex; justify-content: center;">
                <v-card-title style="font-size: 16px;">
                  <span>선택하신 디바이스가 재시작됩니다.</span>
                  <br> <br>
                  <span>재시작 하시겠습니까?</span>
                </v-card-title>
              </v-row>
              <v-row class="d-flex justify-center mt-5">
                <v-card-actions>
                  <v-btn color="primary" variant="text"  @click="dialog8 = !dialog8" flat>
                    확인
                  </v-btn>
                  <v-spacer></v-spacer><v-spacer></v-spacer><v-spacer></v-spacer><v-spacer></v-spacer>
                  <v-btn color="error" variant="text" @click="dialog7 = false" flat style="margin-right: 30px;">
                    취소
                  </v-btn>
                </v-card-actions>
              </v-row>
            </v-container>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog8" class="dialog-mw" persistent>
          <v-card class="pa-6" style="height: 180px; width: 300px; margin: auto;">
            <v-card-title style="font-size: 18px;">
              <span>디바이스가</span>
              <br>
              <span>재시작 되었습니다.</span>
            </v-card-title>
            <v-card-actions>
              <v-btn class="mt-3" color="primary" variant="text" @click="dialog8 = false; dialog7 = false;" flat> 확인 </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </v-col>

    <!-- 오른쪽 정렬: 엑셀 다운로드, 엑셀 업로드 -->
    <v-col cols="6">
      <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
        <v-btn color="grey" variant="outlined" @click="makeExcelFile4">엑셀 다운로드</v-btn>
        <v-btn color="grey" variant="outlined" @click="excelUpload">엑셀 업로드</v-btn>
        <v-dialog v-model="dialogUpload" class="dialog-mw" persistent>
          <v-card class="pa-6" style="height: 250px; width: 400px; margin: auto;">
            <v-card-title>
              <span>엑셀 업로드</span>
            </v-card-title>
            <v-card-actions>
              <v-container>
                <v-file-input
                    label="엑셀 파일을 선택하세요"
                    accept=".xlsx, .xls"
                    dense
                    style="width: 100%;"
                ></v-file-input>
              </v-container>
            </v-card-actions>
            <!-- 버튼을 flex로 나란히 정렬 -->
            <v-row class="d-flex gap-4 justify-center">
              <v-btn color="primary" variant="outlined" @click="excelUploadStart" class="mr-2">업로드</v-btn>
              <v-btn color="error" variant="outlined" @click="excelUploadCancle">취소</v-btn>
            </v-row>
          </v-card>
        </v-dialog>
      </div>
    </v-col>
  </v-row>
  <v-row>
    <v-col cols="12">
      <v-data-table class="border rounded-md text-center light scrollable-card"
                    v-model="selectedItem"
                    :headers="headers"
                    :items="zoomPhone"
                    item-key="phoneName"
                    show-select
                    return-object
                    fixed-header
                    hide-default-footer
                    :sort-asc-icon="TriangleUpComponent"
                    :sort-desc-icon="TriangleDownComponent"
                    :items-per-page="itemsPerPage"
                    v-model:page="pagination"
                    @click:row="selectDevice"
                    @update:modelValue="selectDevice">
        <template v-slot:bottom>
          <div class="text-center pt-2 mt-2 px-3">
            <v-text-field
                :model-value="itemsPerPage"
                class="pa-2 mr-auto elevation-1"
                label="페이지당 항목 수"
                type="number"
                min="-1" max="15"
                hide-details
                style="max-width: 150px;"
                @update:model-value="itemsPerPage = parseInt($event, 10)">
            </v-text-field>
            <v-pagination v-model="pagination" :length="pageCount"></v-pagination>
          </div>
        </template>
      </v-data-table>
    </v-col>
  </v-row>
</template>
