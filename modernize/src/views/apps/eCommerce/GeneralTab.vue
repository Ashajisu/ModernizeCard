<script setup lang="ts">
import { useEditor, EditorContent } from '@tiptap/vue-3';
import { GroupDatatables} from '@/_mockApis/components/datatable/GroupTable';
import StarterKit from '@tiptap/starter-kit';
import { ref, computed } from 'vue';
import { SearchIcon} from 'vue-tabler-icons';
import { watch } from 'vue';

const editor = useEditor({
  extensions: [StarterKit]
});
const discount = ref('no-discount');
const select = ref('Select an option');

//그룹 추가
const newGroupDialog = ref(false);
const deleteConfirmDialog = ref(false);
const deleteSuccessDialog = ref(false);
const saveSuccessDialog = ref(false);
const manageDialog = ref(false);
const userSearchTerm = ref('');

const userList = ref([
  {id : 1, name: '박OO', extension: '101', group: '경영지원팀'},
  {id : 2, name: '최OO', extension: '102', group: '영업1팀'},
  {id : 3, name: '김OO', extension: '103', group: '영업2팀'},
  {id : 4, name: '이OO', extension: '104', group: '기술지원1팀'},
]);
const selectedUser = ref([]);

//관리 모달 창
const openManageDialog = () => {
  manageDialog.value = true;
}
//관리 모달 창 취소버튼
const cancelManage = () => {
  manageDialog.value = false;
};
//관리 모달 창 확인버튼
const confirmManage = () => {
  console.log('선택된 사용자들:', selectedUser.value);
  manageDialog.value = false;
}

const filteredUserList = computed(() => {
  return userList.value.filter(user => {
    return (
        user.name.includes(userSearchTerm.value) ||
        user.extension.includes(userSearchTerm.value)
    );
  });
})


// new 그룹 추가 시 필요한 데이터
const newGroupData = ref({
  group: "",
  department:"",
  groupExtensionNumber: "",
  bellRingDelay:"",
  groupNumbers: "",
})

//부서 목록
const departments = ref([
  "경영지원부", "영업본부", "고객지원본부", "개발연구소"
]);

interface Group {
  group: string;
  department: string;
  groupExtensionNumber: string;
  bellRingDelay: string;
  groupNumbers: string;
}

// props로 GroupDatatables를 받아옵니다
const props = defineProps({
  GroupDatatables: {
    type: Array as () => Group[],
    default: () => []
  }
});
// 새로운 그룹을 추가하는 함수
const addNewGroup = () => {
  if (Array.isArray(props.GroupDatatables)) {
    props.GroupDatatables.push({
      group: newGroupData.value.group,
      department: newGroupData.value.department,
      groupExtensionNumber: newGroupData.value.groupExtensionNumber,
      bellRingDelay: newGroupData.value.bellRingDelay,
      groupNumbers: newGroupData.value.groupNumbers,
    });

    // 새 그룹을 추가한 후 다이얼로그 닫기
    newGroupDialog.value = false;

    // 입력 필드 초기화
    newGroupData.value = {
      group: "",
      department: "",
      groupExtensionNumber: "",
      bellRingDelay: "",
      groupNumbers: "",
    };
  }else{
    console.error("GroupDatatables is not an array!")};
}

const deleteGroup = () => {
  if (selectedItem.value.length > 0) {
    deleteGroupData.value = selectedItem.value[0];
    deleteConfirmDialog.value = true;         //모달 열기
  } else {
    console.log('그룹 삭제');
  }
};

//취소 모달 닫기
const cancelDelete = () => {
  deleteConfirmDialog.value = false; //모달 닫기
}

const confirmDelete = () => {
  selectedItem.value.forEach((item: Group) => {  // Group 타입 명시
    const index = props.GroupDatatables.findIndex(group => group.group === item.group);
    if (index !== -1) {
      props.GroupDatatables.splice(index, 1);
    }
  });
  deleteConfirmDialog.value = false;
  deleteSuccessDialog.value = true;
};

const closeSuccessDialog = () => {
  deleteSuccessDialog.value = false;
};

const closeSaveSuccessDialog = () => {
  saveSuccessDialog.value = false;
}


const page = ref({ title: '당겨받기 그룹 관리' });
// `items`는 외부에서 주어진 데이터

const headers = [
  { title: '당겨받기 그룹', align: 'center', key: 'group' },
  { title: '부서명', align: 'center', key: 'department' },
  { title: '그룹 내선번호', align: 'center', key: 'groupExtensionNumber' },
  { title: '구성원 수', align: 'center', key: 'groupNumbers' },
  { title: '벨울림 지연', align: 'center', key: 'bellRingDelay' },
] as const;


const itemsPerPage = ref(3);
const pagination = ref(1);
const PhoneStatus = ref(['Online','Offline']);
const selectedItem = ref([]);
const deleteGroupData = ref(null);


// 선택된 그룹 정보
const selectedGroup = ref<{
  group?: string;
  department?: string;
  groupExtensionNumber?: string;
  groupNumbers?: number;
  bellRingDelay?: string[];
} | null>(null);


const editDialog = ref(false);
// 편집할 그룹 데이터 (선택적 속성으로 변경 가능)
const editGroupData = ref<{
  group: string;
  department: string;
  groupExtensionNumber: string;
  bellRingDelay: string;
}>({
  group: "",
  department:"",
  groupExtensionNumber: "",
  bellRingDelay: "",
});



//"보기" 버튼 클릭 시 상세조회
const showDetails = () => {
  if(Array.isArray(selectedItem.value) && selectedItem.value.length ===1){
    selectedGroup.value = selectedItem.value[0];      //한개만 선택 시 상세 정보 표시
  }else {
    selectedGroup.value = null;    //여러개 선택/해제 시 상세정보 숨김처리
  }
};

/// "편집" 버튼 클릭 시 모달 오픈
const editGroup = () => {
  if (selectedGroup.value) {
    editGroupData.value = {
      group: selectedGroup.value.group ?? "",
      department: selectedGroup.value.department ?? "",
      groupExtensionNumber: selectedGroup.value.groupExtensionNumber ?? "",
      // Ensure bellRingDelay is treated as an array
      bellRingDelay: Array.isArray(selectedGroup.value.bellRingDelay)
          ? selectedGroup.value.bellRingDelay.join(", ") // If it's an array, join it
          : selectedGroup.value.bellRingDelay ?? "", // Otherwise, default to an empty string
    };
    editDialog.value = true;
  }
};

// "수정 내용 저장" 버튼 클릭 시 업데이트
const saveEdit = () => {
  if (selectedGroup.value) {
    selectedGroup.value = {
      ...selectedGroup.value, // 기존 속성 유지
      ...editGroupData.value, // 새로운 값 덮어쓰기
      bellRingDelay: editGroupData.value.bellRingDelay?.split(", "), // 배열로 변환
    };
  }
  editDialog.value = false;
  saveSuccessDialog.value = true;
};

watch(selectedItem, (newValue) => {
  if (!newValue || newValue.length !== 1) {
    selectedGroup.value = null                  //체크박스 해제 시 숨김처리
  }
});


</script>


<template>
  <!--    <div class="pa-1">-->
  <!-- General -->
  <v-card elevation="10" class="pa-5">
    <v-card-title class="text-h4 font-weight-bold"> 당겨받기 관리 </v-card-title>
    <v-card-title class="text-h6 font-weight-bold">줌 폰 시스템에서 당겨받기 그룹 및 구성원 관리를 할 수 있습니다.</v-card-title>
    <v-card-item>
      <!--      <h5 class="text-h5 mb-7">당겨받기 그룹 리스트</h5>-->

      <v-row>
        <v-col>
          <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
            <v-btn color="primary" flat @click="editGroup">조회</v-btn>
            <v-btn color="primary" variant="outlined" @click="">초기화</v-btn>
          </div>
        </v-col>
      </v-row>
      <v-row class="d-flex align-center">
        <v-col cols="12" lg="3" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px;">당겨받기 그룹</v-label>
          <v-text-field
              hide-details
              label="이름 입력"
              required>
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>
        <v-col cols="12" lg="3" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px;">그룹 내선번호</v-label>
          <v-text-field
              hide-details
              label="번호 입력"
              required
              style=""
          >
          </v-text-field>
        </v-col>
        <v-col cols="12" lg="3" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px; margin-left: 0px;">사용자</v-label>
          <v-text-field
              label="사용자 입력"
              required
              hide-details
          >
            <template v-slot:prepend-inner>
              <SearchIcon size="16"/>
            </template>
          </v-text-field>
        </v-col>
        <v-col cols="12" lg="3" class="d-flex align-center">
          <v-label class="font-weight-semibold pb-2" style="margin-right: 8px; margin-left: 0px;">사용자 내선번호</v-label>
          <v-text-field
              label="사용자 입력"
              required
              hide-details
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
  <br>
  <v-row>
    <v-col>
      <div class="d-flex gap-3 flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
        <v-btn flat color="primary" variant="outlined"  @click="newGroupDialog = true"><v-icon icon="mdi-plus" stroke-width="1.5" size="18" class="mr-2" />신규등록 </v-btn>
        <v-btn flat color="error" variant="outlined" @click="deleteGroup"><v-icon icon="mdi-minus" stroke-width="1.5" size="18" class="mr-2" />삭제 </v-btn>
      </div>
    </v-col>
    <v-col>
      <div class="d-flex gap-3 justify-end flex-column flex-wrap flex-xl-nowrap flex-sm-row fill-height">
        <v-btn color="grey" variant="outlined" to="/ecommerce/checkout">엑셀 다운로드</v-btn>
        <v-btn color="grey" variant="outlined" @click="">엑셀 업로드</v-btn>
      </div>
    </v-col>
  </v-row>


  <!--신규등록 모달 -->
  <v-dialog v-model="newGroupDialog" max-width="500px">
    <v-card>
      <v-card-title>새 그룹 등록</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <v-text-field v-model="newGroupData.group" label="당겨받기 그룹명" class="large-font"></v-text-field>
        <v-select
            v-model="newGroupData.department"
            :items="departments"
            label="부서명"
            class="large-font"></v-select>

        <v-text-field
            v-model="newGroupData.groupExtensionNumber"
            label="그룹 내선번호"
        ></v-text-field>

        <v-select
            v-model="newGroupData.bellRingDelay"
            :items="['0초', '5초', '10초']"
            label="벨울림 지연"
            class="large-font"
        ></v-select>

        <v-text-field
            v-model="newGroupData.groupNumbers"
            label="그룹 구성원"
            type="number"
        ></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="addNewGroup">저장</v-btn>
        <v-btn color="secondary" @click="newGroupDialog = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="deleteConfirmDialog" max-width="400px">
    <v-card>
      <v-card-title>삭제 확인</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <p>선택하신 당겨받기 그룹이 삭제됩니다. <br>삭제 하시겠습니까?</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="confirmDelete">확인</v-btn>
        <v-btn color="secondary" @click="deleteConfirmDialog = false">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="deleteSuccessDialog" max-width="400px">
    <v-card>
      <v-card-title>삭제 완료</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <p>삭제 되었습니다.</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="closeSuccessDialog">확인</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 성공 모달 -->
  <v-dialog v-model="saveSuccessDialog" max-width="400px">
    <v-card>
      <v-card-title>저장 완료</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <p>저장 되었습니다.</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="closeSaveSuccessDialog">확인</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-row>
    <v-col cols="12">
      <v-data-table class="border rounded-md text-center light scrollable-card"
                    v-model="selectedItem"
                    :headers="headers"  :items="GroupDatatables"
                    show-select
                    return-object
                    hide-default-footer
      >
        <template v-slot:bottom>
          <div class="text-center pt-2 mb-3 px-3 d-flex justify-space-between">
            <v-btn
                color="primary"
                @click="showDetails"
                :disabled="!Array.isArray(selectedItem) || selectedItem.length !== 1">보기</v-btn>
            <v-text-field :model-value="itemsPerPage" class="pa-2 mr-auto" label="Items per page" type="number"
                          min="-1" max="15" hide-details style="max-width: 150px;"
                          @update:model-value="itemsPerPage = parseInt($event, 10)"></v-text-field>
            <v-pagination v-model="pagination" ></v-pagination>
          </div>
        </template>
      </v-data-table>

      <!--선택된 행 상세정보-->
      <v-card v-if="selectedGroup" class="mt-4 pa-4">
        <v-card-title>
          <v-row class="d-flex align-center" no-gutters>
            <v-col cols="auto">
              <h5>당겨받기 그룹 상세정보</h5>
            </v-col>
            <v-col cols="auto" class="ml-auto">
              <v-btn color="primary" @click="editGroup">편집</v-btn>
            </v-col>
          </v-row>
        </v-card-title>
        <v-divider></v-divider>
        <v-card-text>
          <v-row align="center">
            <!-- 그룹명 -->
            <v-col cols="3">
              <v-label class="font-weight-bold">그룹명</v-label>
              <v-text-field v-model="selectedGroup.group" readonly class="bg-grey-lighten-3"></v-text-field>
            </v-col>

            <!--부서명 -->
            <v-col cols="3">
              <v-label class="font-weight-bold">부서명</v-label>
              <v-text-field v-model="selectedGroup.department" readonly class="bg-grey-lighten-3">
                <template v-slot:append-inner>
                  <v-icon>mdi-magnify</v-icon>
                </template>
              </v-text-field>
            </v-col>

            <!-- 그룹 내선번호 -->
            <v-col cols="3">
              <v-label class="font-weight-bold">그룹 내선번호</v-label>
              <v-text-field v-model="selectedGroup.groupExtensionNumber" readonly class="bg-grey-lighten-3">
                <template v-slot:append-inner>
                  <v-icon>mdi-magnify</v-icon>
                </template>
              </v-text-field>
            </v-col>

            <!-- 벨울림 지연 -->
            <v-col cols="3">
              <v-label class="font-weight-bold">벨울림 지연</v-label>
              <!-- 선택 -->
              <v-select
                  v-model="selectedGroup.bellRingDelay"
                  :items="['0초','5초','10초']"
                  multiple
                  class="bg-grey-lighten-3"></v-select>
            </v-col>

            <!-- 구성원 수 -->
            <v-col cols="3">
              <v-label class="font-weight-bold">그룹 구성원</v-label>
              <v-text-field v-model="selectedGroup.groupNumbers" readonly class="bg-grey-lighten-3"></v-text-field>
            </v-col>

            <!-- 관리 버튼 -->
            <v-col cols="auto">
              <v-btn color="warning" @click="openManageDialog">관리</v-btn>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>



      <!-- 편집 모달 -->
      <v-dialog v-model="editDialog" max-width="500px">
        <v-card>
          <v-card-title>당겨받기 그룹 수정</v-card-title>
          <v-divider></v-divider>
          <v-card-text>
            <v-text-field v-model="editGroupData.group" label="그룹명" class="large-font"></v-text-field>
            <v-text-field v-model="editGroupData.department" label="부서명" class="large-font"></v-text-field>
            <v-text-field v-model="editGroupData.groupExtensionNumber" label="그룹 내선번호"></v-text-field>
            <v-text-field v-model="editGroupData.bellRingDelay" label="벨울림 지연"></v-text-field>
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" @click="saveEdit">저장</v-btn>
            <v-btn color="secondary" @click="editDialog = false" >취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>


      <!-- 관리 창 모달 -->
      <v-dialog v-model="manageDialog" max-width="500px">
        <v-card>
          <v-card-title>
            <h3>당겨받기 구성원 관리</h3>
          </v-card-title>
          <v-divider></v-divider>
          <v-card-text>
            <v-text-field
                v-model="userSearchTerm"
                label="사용자명 또는 내선번호"
                prepend-inner-icon="mdi-magnify"
                clearable></v-text-field>

            <v-data-table
                :headers="[
                  { title :'사용자명', value:'name'},
                  { title :'내선번호', value:'extension'},
                  {title : '당겨받기 그룹', value:'group'},
              ]"
                :items="filteredUserList"
                item-value="id"
                v-model:select="selectedUser"
                show-select
                item-key="id"
                class="elevation-1">
              <template v-slot:item.extension="{ item }">
                {{ item.extension }}
              </template>
            </v-data-table>
          </v-card-text>
          <v-divider></v-divider>
          <v-card-actions>
            <v-btn color="primary" @click="confirmManage">확인</v-btn>
            <v-btn color="secondary" @click="cancelManage">취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-col>
  </v-row>


  <!--    </div>-->
</template>
