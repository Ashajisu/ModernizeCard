<script setup lang="ts">
import { ref } from 'vue';
import {DepartmentCallDatatables} from '@/_mockApis/components/datatable/SampleDataTable';

import type {departmentCall} from '@/types/apps/SampleType'; // 날짜 포맷을 쉽게 다룰 수 있는 라이브러리

const billFrom = ref(false);
const rules = ref([
  (v: string) => !!v || 'rule',
  (v: string) => (v && v.length <= 20) || 'rule must be less than 20 characters'
]);

interface Team {
  id: number;
  title: string;
  children?: Team[];
}

const headers = [
  // { title: '본부명', align: 'center', key: 'dept' },
  { title: '부서명', align: 'center', key: 'team'},
  { title: '사용일', align: 'center', key: 'date' },
  { title: '발신 건수', align: 'center', key: 'calltotal' },
  { title: '발신 통화시간', align: 'center', key: 'totaltime' },
  { title: '수신 건수', align: 'center', key: 'inCalltotal' },
  { title: '수신 통화시간', align: 'center', key: 'inTotaltime' },
] as const;

const itemsPerPage = ref(3);
const pagination = ref(1);
const pageCount = Math.ceil(DepartmentCallDatatables.length / itemsPerPage.value)
const PhoneStatus = ref(['시간대별','일별', '월별']);

const selectedItem = ref({
  date: "",
  // dept: "",
  team: "",
  phonecase: "",
  calltotal: "",
  totaltime: "",
});

const selectedItems = ref([]);

const departmentCallDatatables = ref<departmentCall[]>(DepartmentCallDatatables);

// 그룹 내 첫 번째 항목인지 확인
const isFirstInGroup = (item: departmentCall, index: number) => {
  const prevItem = departmentCallDatatables.value[index - 1];
  return !prevItem || prevItem.dept !== item.dept || prevItem.team !== item.team;
};

const selectedTeam: Team[] = [
  {
    id: 1,
    title: '아리시스',
    children: [
      { id: 2, title: '영업부',
        children: [
          { id: 3, title: '영업 1팀'},
          { id: 4, title: '영업 2팀'}
        ]
      },
      { id: 5, title: '경영지원부',
        children: [
          { id: 6, title: '경영지원팀'}
        ]
      },
      { id: 7, title: '고객지원부',
        children: [
          { id: 8, title: '고객지원팀'},
          { id: 9, title: 'PM팀'}
        ]
      }
    ]
  },
];

const icons = [
  'mdi-plus-box',
]

const tree = ref<Team[]>([]);

function getIcon () {
  return icons[Math.floor(Math.random() * icons.length)]
}

const onClickClose = (selection: Team) => {
  tree.value = tree.value.filter(item => item.id !== selection.id);
}

// 트리 구조를 평면 구조로 변환
function flattenTree(items: Team[]): Team[] {
  return items.reduce((acc: Team[], item: Team) => {
    acc.push(item);
    if (item.children) {
      acc = acc.concat(flattenTree(item.children));  // 자식 아이템을 재귀적으로 결합
    }
    return acc;
  }, []);
}

const menu = ref(false);// 메뉴 열기/닫기 상태
const selectedTitle = ref("");
function onTreeItemSelected(selection: unknown) {
  // selection이 Team[] 타입인지 확인
  const teamSelection = selection as Team[];

  if (teamSelection && teamSelection.length > 0) {
    // 선택된 아이템의 제목을 텍스트 필드에 표시
    selectedTitle.value = teamSelection[0].title;  // 선택된 첫 번째 아이템의 제목을 설정
  }
}

const currentDate = ref<string>(new Date().toISOString().slice(0, 10));
const time1 = ref('09:00');
const time2 = ref('10:00');

const periods = ['시간대별', '일별', '월별', '년별']; // 버튼에 표시할 텍스트 배열
const selected = ref<string | null>(null); // 현재 선택된 버튼을 추적하는 ref

function select(period : string) {
  selected.value = period;

  if (period === '일별') {
    currentDate.value = new Date().toISOString().slice(0, 10);
  }else if(period === '월별'){

  }else if(period === '년별'){

  }
}

</script>

<template>
  <br>
  <h5 class="text-20 mb-3">일자별 수발신 통계</h5>
  <h5 class="text-15 mb-7">※ 조회조건</h5>
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
  <v-card elevation="10" style="height: auto; max-height: 400px;">
    <v-card-item>
      <v-row class="d-flex align-center" no-gutters>

      </v-row>
      <v-row class="d-flex align-center" no-gutters>
        <!-- 조회일 레이블 -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-label class="font-weight-semibold pb-2">조회일</v-label>
        </v-col>

        <!-- 첫 번째 DatePicker -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-text-field variant="outlined" hide-details type="date" v-model="currentDate"></v-text-field>
        </v-col>

        <!-- 첫 번째 TimePicker -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-text-field variant="outlined" hide-details type="time"  v-model="time1"></v-text-field>
        </v-col>

        <!-- ~ 기호 -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-label>~</v-label>
        </v-col>

        <!-- 두 번째 DatePicker -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-text-field variant="outlined" hide-details type="date" v-model="currentDate"></v-text-field>
        </v-col>

        <!-- 두 번째 TimePicker -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-text-field variant="outlined" hide-details type="time" v-model="time2"></v-text-field>
        </v-col>

        <v-col cols="auto" class="d-flex align-center ma-2">
          <v-container style="display: block; margin-top: 23px;">
            <v-menu v-model="menu" transition="slide-x-reverse-transition" offset-y>
              <!-- activator slot에 on과 attrs를 v-bind, v-on으로 전달 -->
              <template v-slot:activator="{ props }">
                <v-text-field
                    v-bind="props"
                    label="부서 / 팀 선택"
                    v-model="selectedTitle"
                    style="min-width:120px; max-width:120px;"
                    readonly
                />
              </template>

              <!-- 트리뷰 컴포넌트 -->
              <v-treeview
                  v-model:selected="tree"
                  :items="selectedTeam"
                  item-title="title"
                  item-value="id"
                  return-object
                  select-strategy="classic"
                  activatable
                  hide-details
                  style="min-width:220px; max-width:220px;"
                  @update:selected="onTreeItemSelected"
              />
            </v-menu>
          </v-container>
        </v-col>

        <!-- 엑셀 다운로드 버튼 -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <!--          <v-btn block size="large" color="primary" to="/ecommerce/checkout"
                           style="max-width: 100px; min-width: 100px;">
                      엑셀 다운로드
                    </v-btn>-->
          <v-btn color="grey" variant="outlined" @click="" style="max-width: 100px; min-width: 100px; min-height: 40px;">엑셀 다운로드</v-btn>
        </v-col>

        <!-- 조회 버튼 -->
        <v-col cols="auto" class="d-flex align-center ma-2">
          <!--          <v-btn block size="large" color="primary" to="/ecommerce/checkout"
                           style="max-width: 60px; min-width: 60px;">
                      조회
                    </v-btn>-->
          <v-btn color="primary" @click="" style="max-width: 100px; min-width: 100px; min-height: 40px;">조회</v-btn>
        </v-col>
      </v-row>
    </v-card-item>
  </v-card>
  <br>
  <h5 class="text-15 mb-7">※ 팀별 전화 통계</h5>
  <v-data-table
      class="border rounded-md text-center light scrollable-card"
      :headers="headers"
      :items="departmentCallDatatables"
      item-key="dept"
      hide-default-footer
      style="overflow-x: hidden; max-width: 100%;"
  >
    <template v-slot:body="props">
      <tr v-for="(item, index) in props.items" :key="index">
        <!-- 그룹 내 첫 번째 항목에만 rowspan 적용 -->
        <td v-if="isFirstInGroup(item, index)" rowspan="4">{{ item.team }}</td>
        <td>{{ item.date }}</td>
        <td>{{ item.calltotal }}</td>
        <td>{{ item.totaltime }}</td>
        <td>{{ item.inCalltotal }}</td>
        <td>{{ item.inTotaltime }}</td>
      </tr>
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
</template>
