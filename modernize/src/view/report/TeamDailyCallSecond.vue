<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import {DepartmentCallDatatables} from '@/_mockApis/components/datatable/SampleDataTable';
import { useTableManager } from "@/common/useTableManager";
import type {FormField} from "@/types/custom/InputTypes";
import CustomSearchChecksForm from "@/components/custom/form/CustomSearchChecksForm.vue";
import UiParentCard from "@/components/shared/UiParentCard.vue";
import type {departmentCall} from '@/types/apps/SampleType';
import PaginationControl from '@/components/custom/pagination/PaginationControl.vue';

const formFields = ref<FormField[]>([
  { label: '시작일', name:"starttime", type: "datetime", value: "", required: false, disabled: false },
  { label: '종료일', name:"endtime", type: "datetime", value: "", required: false, disabled: false },
  { label: "부서명", name: "dept", type: "search_list", value: "", searchObj:DepartmentCallDatatables, required: false, disabled: false },
  { label: "팀명", name: "team", type: "search_list", value: "",  searchObj:DepartmentCallDatatables, required: false, disabled: false },
]);

const headers = ref<any[]>([
  { title: '팀명', align: 'center', key: 'team'},
  { title: '사용일', align: 'center', key: 'date'},
  { title: '발신 건수', align: 'center', key: 'calltotal' },
  { title: '발신 통화시간', align: 'center', key: 'totaltime' },
  { title: '수신 건수', align: 'center', key: 'inCalltotal' },
  { title: '수신 통화시간', align: 'center', key: 'inTotaltime' },
]);

const deviceFormFields = ref<FormField[]>([
  { label: "팀명", name: "team", type: "text", value: "", required: false, disabled: false },
  { label: "사용일", name: "date", type: "select", value: "", required: false, disabled: false },
  { label: "발신 건수", name: "calltotal", type: "search_list", value: "",  required: false, disabled: false },
  { label: "발신 통화시간", name: "totaltime", type: "search_list", value: "", required: false, disabled: false },
  { label: "수신 건수", name: "inCalltotal", type: "search_list", value: "", required: false, disabled: false },
  { label: "수신 통화시간", name: "inTotaltime", type: "search_list", value: "", required: false, disabled: false },
]);

const identifierField:string = 'deptName';

//모듈 호출
const {
  onSearch,
  filteredList,
} = useTableManager<departmentCall>(DepartmentCallDatatables, formFields, deviceFormFields, identifierField);

const periods = ['시간대별', '일별', '월별', '년별']; // 버튼에 표시할 텍스트 배열
const selected = ref<string | null>(null); // 현재 선택된 버튼을 추적하는 ref


function zeroPad(num: number): string {
  return (num < 10 ? '0' : '') + num;
}

function select(period: string) {
  selected.value = period;

  const now = new Date();
  const year = now.getFullYear();
  const month = zeroPad(now.getMonth() + 1);
  const nextmonth = zeroPad(now.getMonth() + 2);
  const date = zeroPad(now.getDate());
  const hours = zeroPad(now.getHours());
  const nexthours = zeroPad(now.getHours() + 1);
  const minutes = zeroPad(now.getMinutes())

  let startField = formFields.value.find(f => f.name === 'starttime');
  let endField = formFields.value.find(f => f.name === 'endtime');

  if (startField && endField) {
    let startvalue = '';
    let endvalue= '';
    if (period === "시간대별") {
      startField.type = 'datetime'; // ✅ 여기서 type 변경!
      endField.type = 'datetime';
      startvalue = `${year}-${month}-${date} ${hours}:00`;
      endvalue = `${year}-${month}-${date} ${nexthours}:00`;
    } else if (period === "일별") {
      startField.type = 'date'; // ✅ 여기서 type 변경!
      endField.type = 'date';
      startvalue = `${year}-${month}-${date}`;
      endvalue = `${year}-${month}-${date}`;
    } else if (period === "월별") {
      startField.type = 'date'; // ✅ 여기서 type 변경!
      endField.type = 'date';
      startvalue = `${year}-${month}-01`;
      endvalue = `${year}-${nextmonth}-01`;
    } else if (period === "년별") {
      startField.type = 'date'; // ✅ 여기서 type 변경!
      endField.type = 'date';
      startvalue = `${year}-01-01`;
      endvalue = `${year}-12-31`;
    }

    startField.value = startvalue;
    endField.value = endvalue;
  }
}

// 그룹 내 첫 번째 항목인지 확인
const isFirstInGroup = (item: departmentCall, index: number) => {
  const prevItem = filteredList.value[index - 1];
  return !prevItem || prevItem.dept !== item.dept || prevItem.team !== item.team;
};

const itemsPerPage = ref(10);
const pagination = ref(1);
const pageCount = computed(() => {
  return Math.ceil(filteredList.value.length / itemsPerPage.value);
});
</script>

<template>
  <v-row>
    <v-col cols="12" md="12">
      <UiParentCard title="일자별 수발신 유형별 통계">
        <v-row>
          <v-btn
              v-for="(period, index) in periods"
              :key="index"
              :value="period"
              v-model="selected"
              :class="{'v-btn--active': selected === period}"
              @click="select(period)"
          >
            {{ period }}
          </v-btn>
        </v-row>
        <v-row>
          <v-col>
            <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
              <CustomSearchChecksForm :formFields="formFields" :colsPerRow="7" :edit="true">
                <template v-slot:lineBtn="{ validateForm }">
                  <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
                    <v-btn color="grey" variant="outlined" @click=""
                           style="max-width: 100px; min-width: 100px; min-height: 40px;">엑셀 다운로드
                    </v-btn>
                    <v-btn color="primary" flat @click="onSearch(validateForm)"
                           style="max-width: 100px; min-width: 100px; min-height: 40px;">조회
                    </v-btn>
                  </div>
                </template>
              </CustomSearchChecksForm>
            </div>
          </v-col>
        </v-row>
        <v-row>
          <h5 class="text-15 mb-2">※ 팀별 전화 통계</h5>
          <v-data-table
              class="border rounded-md text-center light scrollable-card"
              :headers="headers"
              :items="filteredList"
              item-key="dept"
              hide-default-footer
              fixed-header
              return-object
              style="overflow-x: hidden; max-width: 100%;"
              :items-per-page="itemsPerPage"
              v-model:page="pagination"
          >
            <template v-slot:body="props">
              <tr v-for="(item, index) in props.items" :key="index">
                <td v-if="isFirstInGroup(item, index)" rowspan="4">{{ item.team }}</td>
                <td v-if="isFirstInGroup(item, index)" rowspan="4">{{ item.date }}</td>
                <td>{{ item.calltotal }}</td>
                <td>{{ item.totaltime }}</td>
                <td>{{ item.inCalltotal }}</td>
                <td>{{ item.inTotaltime }}</td>
              </tr>
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
        </v-row>
      </UiParentCard>
    </v-col>
  </v-row>
</template>
