<script setup lang="ts">
import UiParentCard from "@/components/shared/UiParentCard.vue";
import { computed, type ComputedRef, ref } from "vue";
import type { FormField } from '@/types/custom/InputTypes';
import { ZoomPhoneDataTables } from "@/_mockApis/custom/ZoomData";
import type { ZoomPhoneItem } from "@/types/custom/DataTableTypes";
import CustomSlotDialog from "@/components/custom/dialog/CustomSlotDialog.vue";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import { useTableManager } from "@/common/useTableManager";

const formFields = ref<FormField[]>([
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부', '연구개발'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '사용자명', name: 'username', type: 'search_list', value: '', searchObj:ZoomPhoneDataTables, required: false, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'text', value: '', required: false, disabled: false },
  { label: 'PSTN 번호', name: 'pstnNumber', type: 'search', value: '', searchObj:ZoomPhoneDataTables, required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'search', value: '', searchObj:ZoomPhoneDataTables, required: false, disabled: false },
  { label: '', name: '_', type: 'none', value: '', required: false, disabled: false },
]);
const headers = ref<any[]>([
  { title: '부서명', align: 'start', key: 'department' },
  { title: '팀명', align: 'start', key: 'team' },
  { title: '사용자명', align: 'start', key: 'username' },
  { title: '사원번호', align: 'start', key: 'employeeId' },
  { title: '내선번호', align: 'center', key: 'mobile' },
  { title: 'PSTN 번호', align: 'center', key: 'pstnNumber' },
  { title: '...', align: 'center', key: 'pstnNumber' },
  { title: '통화녹음', align: 'end', key: 'callRecording' },
])
const userFields = ref<FormField[]>([
  { label: '사용자명', name: 'username', type: 'text', value: '', placeholder: '이름 입력', required: false, disabled: false },
  { label: '사원번호', name: 'employeeId', type: 'text', value: '', placeholder: '사원번호 입력', required: false, disabled: false },
  { label: '부서명', name: 'department', type: 'select', value: '', options: ['기술팀', '영업팀', '고객지원본부'], required: false, disabled: false },
  { label: '팀명', name: 'team', type: 'select', value: '', options: ['기술2팀', '기술1팀', '기술지원팀'], required: false, disabled: false },
  { label: '직위', name: 'position', type: 'select', value: '', options: ['사원', '대리', '과장', '차장', '부장'], required: false, disabled: false },
  { label: '내선번호', name: 'mobile', type: 'search_list', value: '', searchObj:ZoomPhoneDataTables, required: true, disabled: false },
  { label: 'PSTN 번호', name: 'pstnNumber', type: 'search', value: '', searchObj:ZoomPhoneDataTables,required: false, disabled: false },
  { label: '당겨받기', name: 'pullNumber', type: 'search_list', value: '', searchObj:ZoomPhoneDataTables,required: false, disabled: false },
  { label: 'IP 전화기', name: 'ipPhone', type: 'search', value: 'Avaya J159_308152', searchObj:ZoomPhoneDataTables,required: false, disabled: false },
  { label: '줌 라이센스', name: 'zoomLicense', type: 'select', value: 'WorkplaceBiz', options: ['WorkplaceBiz', '...', 'Zoom Phone Basic '], required: true, disabled: false },
  { label: '통화녹음', name: 'callRecording', type: 'select', value: '미사용', options: ['미사용', '선택녹취','전수녹취'], required: false, disabled: false }
]);
const functionData = ref([
  { number: 1, key: 'Ext.8155', assignment: '입력란', displayName: '선택란', OBNumber: ''},
  { number: 2, key: 'Ext.8156', assignment: '대표번호 발신', displayName: '0221778100', OBNumber: ''},
  { number: 3, key: '단축다이얼', assignment: '010xxxxxxxx', displayName: '홍길동 휴대전화', OBNumber: ''},
  { number: 4, key: '', assignment: '', displayName: '', OBNumber: ''},
]);
const functionHeaders = ref<any[]>([
  { title: '라인', align: 'start', key: 'number', value: 'number', sortable: false , maxWidth: "40px"},
  { title: '키 유형', key: 'key', sortable: false, maxWidth: "200px"},
  { title: '할당', key: 'assignment', sortable: false, maxWidth: "200px"},
  { title: '표시이름', key: 'displayName', sortable: false, maxWidth: "200px"},
  { title: '발신번호', key: 'OBNumber', sortable: false, maxWidth: "200px"},
  { title: '', key: 'upDown', sortable: false, maxWidth: "150px"},
]);

//모듈 호출
const {
  onSearch,
  resetSearch,
  filteredList,
  selectedEmpId,
  onSelectionChange,
  edit,
  handleEdit,
  onNew,
  onSave,
  onDelete,
} = useTableManager<ZoomPhoneItem>(ZoomPhoneDataTables, formFields, userFields);

//기능키 관리 : Zoom Phone Basic 사용자는 사용할 수 없음
const keyOptions = ['Line','Speed Dial', 'Call Park', 'BLF', 'Intercom', 'Zoom Meeting'];

const isProLicense: ComputedRef<boolean> = computed(()=>{
  const license: FormField | undefined = userFields.value.find((user: any) => {
    return user.name.includes('zoomLicense');
  });
  const licenseName:string = license?.value as string;
  return licenseName.toLowerCase().includes('basic');
});

//마지막 down 버튼 찾기
const lastAssignedNumber = computed(() => {
  const reversedIndex = functionData.value
      .slice() // 원본 배열 유지
      .reverse()
      .findIndex(item => !!item.assignment); // assignment 값이 있는 첫 번째 요소 찾기

  return reversedIndex === -1 ? 1 : functionData.value.length - reversedIndex; // 실제 index+1 계산
});

//upDown 데이터 이동 : 현재위치 index, 이동범위 direction (+-n)
const moveItem = (number:number, direction:number) => {
  //index 값을 계산
  const index = number -1;
  const newIndex = index - direction;

  // 배열 범위를 벗어나지 않도록 제한
  if (newIndex < 0 || newIndex >= functionData.value.length) return;

  // 배열 요소 교환 (스왑)
  const temp = functionData.value[index];
  functionData.value[index] = functionData.value[newIndex];
  functionData.value[newIndex] = temp;

  // number 값을 다시 설정
  functionData.value.forEach((item, idx) => {
    item.number = idx + 1;
  });
};
</script>
<!-- 각 버튼의 기능 구현 전 -->
<!-- 행이 아닌 체크박스만 동작함 -->
<template>
    <v-row>
        <v-col cols="12" md="12">
            <UiParentCard title="줌 폰 사용자 관리">
              <v-row>
                <CustomSearchChecksForm :formFields="formFields" :colsPerRow="4" :edit="true">
                  <template v-slot:lineBtn="{ validateForm }">
                    <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                      <v-btn color="primary" flat @click="onSearch(validateForm)">조회</v-btn>
                      <v-btn color="primary" variant="outlined" @click="resetSearch">초기화</v-btn>
                    </div>
                  </template>
                </CustomSearchChecksForm>
              </v-row>
              <v-row>
                <v-col>
                  <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn flat color="primary" variant="outlined" @click="onNew"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
                    <v-btn flat color="error" variant="outlined" @click="onDelete(selectedEmpId[0])"><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제 </v-btn>
                  </div>
                </v-col>
                <v-col>
                  <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn color="grey" variant="outlined" @click="">엑셀 다운로드</v-btn>
                    <v-btn color="grey" variant="outlined" @click="">엑셀 업로드</v-btn>
                  </div>
                </v-col>
              </v-row>
              <v-row>
                <v-data-table items-per-page="5" :headers="headers" :items="filteredList" item-value="employeeId"
                              select-strategy="single" show-select class="border rounded-md"
                              v-model="selectedEmpId"
                              @update:model-value="onSelectionChange"
                              >
                </v-data-table>
              </v-row>
            </UiParentCard>
            <UiParentCard title="사용자 상세정보" :key="selectedEmpId[0]" v-if="!!selectedEmpId">
              <template v-slot:default>
                <CustomSearchChecksForm :formFields="userFields" :colsPerRow="4" :edit="edit">
                  <template v-slot:topBtn="{validateForm}">
                    <v-row>
                      <v-col>
                        <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                          <v-btn flat color="primary" variant="outlined" @click="handleEdit(true)" >편집</v-btn>
                          <v-btn flat color="error" variant="outlined" @click="handleEdit(false)">취소</v-btn>
                        </div>
                      </v-col>
                      <v-col>
                        <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                          <CustomSlotDialog ref="functionDialog" title="기능키 관리" width="1500px">
                            <template v-slot:inCard>
                              <v-data-table :headers="functionHeaders" :items="functionData" item-value="username" hide-default-footer >
                                <!-- 필수값 표시하기 -->
                                <template v-slot:header.assignment="{ column }">
                                  {{ column.title }}
                                  <span v-if="column.title === '할당'" style="color: red;">*</span>
                                </template>

                                <!-- 필수값 assignment 이 있을때만 입력란 표시하기 -->
                                <template v-slot:item.key="{ item }">
                                  <v-select v-if="!!item.assignment"
                                            v-model="item.key"
                                            :items="keyOptions"
                                            dense hide-details>
                                  </v-select>
                                  <div v-else>
                                    <v-btn variant="outlined" color="primary" class="font-weight-bold" @click="item.assignment='할당'">설정</v-btn>
                                  </div>
                                </template>
                                <template v-slot:item.assignment="{ item }">
                                  <v-text-field v-if="!!item.assignment" v-model="item.assignment" dense hide-details></v-text-field>
                                </template>
                                <template v-slot:item.displayName="{ item }">
                                  <v-text-field v-if="!!item.assignment" v-model="item.displayName" dense hide-details></v-text-field>
                                </template>
                                <template v-slot:item.OBNumber="{ item }">
                                  <v-select v-if="!!item.assignment"
                                            v-model="item.OBNumber"
                                            :items="keyOptions"
                                            dense hide-details>
                                  </v-select>
                                </template>
                                <template  v-slot:item.upDown="{ item }">
                                  <v-btn flat variant="plain" v-if="!!item.assignment" :disabled="item.number === 1" @click="moveItem(item.number, 1)"><v-chip><v-icon icon="mdi-arrow-up-bold"/>up</v-chip></v-btn>
                                  <v-btn flat variant="plain" v-if="!!item.assignment" :disabled="item.number === lastAssignedNumber" @click="moveItem(item.number, -1)"><v-chip><v-icon icon="mdi-arrow-down-bold"/>down</v-chip></v-btn>
                                </template>
                              </v-data-table>
                            </template>
                          </CustomSlotDialog>
                          <v-btn color="grey" variant="outlined" @click="( $refs.functionDialog as any )?.open()" :disabled="isProLicense">사용자 전화 기능키 관리</v-btn>
                          <v-btn flat color="primary" variant="outlined" @click="onSave(validateForm)">저장</v-btn>
                        </div>
                      </v-col>
                    </v-row>
                  </template>
                </CustomSearchChecksForm>
              </template>
            </UiParentCard>
        </v-col>
    </v-row>
</template>

<style scoped>
</style>