<template>
    <v-card class="pa-5">
      <v-card-title class="text-h4 font-weight-bold">PSTN 번호 관리</v-card-title>
      <v-card-title class="text-h6 font-weight-bold">
        💡 줌 폰에서 사용되는 PSTN 번호를 관리 할 수 있습니다.
      </v-card-title>
  
      <!-- 검색 영역 -->
      <v-row class="mt-5">
        <v-col cols="12" md="2">
          <div class="d-flex align-center">
            <div class="mr-2">PSTN 번호</div>
            <v-text-field
              v-model="searchNumber"
              density="compact"
              variant="outlined"
              hide-details
              append-inner-icon="mdi-magnify"
              class="mx-2"
            ></v-text-field>
          </div>
        </v-col>
        <v-col cols="12" md="2">
          <div class="d-flex align-center">
            <div class="mr-2">제품</div>
            <v-select
              v-model="selectedProduct"
              :items="productTypes"
              density="compact"
              variant="outlined"
              hide-details
              class="mx-2"
            ></v-select>
          </div>
        </v-col>
        <v-col cols="12" md="2">
          <div class="d-flex align-center">
            <div class="mr-2">사용자</div>
            <v-text-field
              v-model="searchUser"
              density="compact"
              variant="outlined"
              hide-details
              append-inner-icon="mdi-magnify"
              class="mx-2"
            ></v-text-field>
          </div>
        </v-col>
        <v-col cols="12" md="2">
          <div class="d-flex align-center">
            <div class="mr-2">SIP Group</div>
            <v-select
              v-model="selectedSipGroup"
              :items="sipGroupTypes"
              density="compact"
              variant="outlined"
              hide-details
              class="mx-2"
            ></v-select>
          </div>
        </v-col>
        <v-col cols="12" md="2">
          <div class="d-flex align-center">
            <div class="mr-2">수신</div>
            <v-select
              v-model="selectedIncoming"
              :items="incomingTypes"
              density="compact"
              variant="outlined"
              hide-details
              class="mx-2"
            ></v-select>
          </div>
        </v-col>
        <v-col cols="12" md="2">
          <div class="d-flex align-center">
            <div class="mr-2">발신</div>
            <v-select
              v-model="selectedOutgoing"
              :items="outgoingTypes"
              density="compact"
              variant="outlined"
              hide-details
              class="mx-2"
            ></v-select>
          </div>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12" class="d-flex justify-end">
          <v-btn color="primary" @click="getPstnList">조회</v-btn>
          <v-btn class="ml-2" variant="outlined" @click="downloadExcel">엑셀 다운로드</v-btn>
          <v-btn class="ml-2" variant="outlined" @click="uploadExcel">엑셀 업로드</v-btn>
        </v-col>
      </v-row>
  
      <!-- 버튼 영역 -->
      <v-row class="mt-3">
        <v-col cols="12" class="d-flex">
          <v-btn color="primary" prepend-icon="mdi-plus" @click="openAddDialog">신규등록</v-btn>
          <v-btn class="ml-2" color="error" prepend-icon="mdi-delete" @click="openDeleteDialog">삭제</v-btn>
          <v-btn class="ml-2" color="warning" prepend-icon="mdi-pencil" @click="openEditDialog">편집</v-btn>
        </v-col>
      </v-row>
  
      <!-- 데이터 테이블 -->
      <v-table class="mt-3">
        <thead>
          <tr>
            <th class="text-center  bg-primary text-white" style="width: 50px;">
              <v-checkbox
                v-model="selectAll"
                @click="toggleSelectAll"
                hide-details
              ></v-checkbox>
            </th>
            <th class="text-center bg-primary text-white">PSTN 번호</th>
            <th class="text-center bg-primary text-white">제품</th>
            <th class="text-center bg-primary text-white">사용자</th>
            <th class="text-center bg-primary text-white">SIP Group</th>
            <th class="text-center bg-primary text-white">지역</th>
            <th class="text-center bg-primary text-white">수신</th>
            <th class="text-center bg-primary text-white">발신</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in displayedItems" :key="item.id">
            <td class="text-center">
              <v-checkbox
                v-model="item.selected"
                hide-details
                @change="updateSelectAll"
              ></v-checkbox>
            </td>
            <td class="text-center">{{ item.number }}</td>
            <td class="text-center">{{ item.product }}</td>
            <td class="text-center">{{ item.user }}</td>
            <td class="text-center">{{ item.sipGroup }}</td>
            <td class="text-center">{{ item.country }}</td>
            <td class="text-center">{{ item.incoming }}</td>
            <td class="text-center">{{ item.outgoing }}</td>
          </tr>
        </tbody>
      </v-table>
  
      <!-- 페이지네이션 -->
      <div class="d-flex align-center mt-4">
        <div>
          페이지당 항목 수
          <v-select
            v-model="itemsPerPage"
            :items="[15, 30, 50]"
            density="compact"
            variant="outlined"
            hide-details
            style="width: 80px"
            class="mx-2"
            @update:model-value="fetchData"
          ></v-select>
        </div>
        <v-spacer></v-spacer>
        <div class="text-center">
          <v-pagination
            v-model="page"
            :length="totalPages"
            :total-visible="7"
            @update:model-value="fetchData"
          ></v-pagination>
        </div>
        <v-spacer></v-spacer>
        <div class="text-right">
          전체 ({{ totalItems }}) 건 / {{ page }}/{{ totalPages }}
        </div>
      </div>
  
      <!-- PSTN 번호 추가 다이얼로그 -->
      <v-dialog v-model="addDialog" max-width="600px">
        <v-card>
          <v-card-title class="text-h5 bg-primary text-white">
            PSTN 번호 추가
          </v-card-title>
          <v-card-text class="pt-4">
            <v-form ref="addForm">
              <v-row>
                <v-col cols="12">
                  <v-select
                    v-model="newItem.product"
                    :items="productTypes"
                    label="제품"
                    required
                    variant="outlined"
                    density="compact"
                  ></v-select>
                </v-col>
                <v-col cols="12">
                  <v-radio-group v-model="addMethod" inline>
                    <v-radio label="개별 입력 (여러 번호의 경우 쉼표(,)로 구분)" value="individual"></v-radio>
                    <v-radio label="범위 입력 (최대 100개 번호까지)" value="range"></v-radio>
                  </v-radio-group>
                </v-col>
                <v-col cols="12" v-if="addMethod === 'individual'">
                  <v-text-field
                    v-model="newItem.numbers"
                    label="PSTN 번호 (쉼표로 구분)"
                    required
                    variant="outlined"
                    density="compact"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" v-if="addMethod === 'range'">
                  <v-row>
                    <v-col cols="5">
                      <v-text-field
                        v-model="rangeStart"
                        label="시작 번호"
                        required
                        variant="outlined"
                        density="compact"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="2" class="d-flex align-center justify-center">
                      <span class="text-h5">~</span>
                    </v-col>
                    <v-col cols="5">
                      <v-text-field
                        v-model="rangeEnd"
                        label="끝 번호"
                        required
                        variant="outlined"
                        density="compact"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-col>
                <v-col cols="12">
                  <v-select
                    v-model="newItem.sipGroup"
                    :items="sipGroupTypes"
                    label="SIP Group"
                    required
                    variant="outlined"
                    density="compact"
                  ></v-select>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error" @click="addDialog = false">취소</v-btn>
            <v-btn color="primary" @click="addPstnNumber">저장</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- PSTN 번호 편집 다이얼로그 -->
      <v-dialog v-model="editDialog" max-width="600px">
        <v-card>
          <v-card-title class="text-h5 bg-primary text-white">
            PSTN 번호 편집
          </v-card-title>
          <v-card-text class="pt-4">
            <v-form ref="editForm">
              <v-row>
                <v-col cols="12">
                  <v-select
                    v-model="editItem.product"
                    :items="productTypes"
                    label="제품"
                    variant="outlined"
                    density="compact"
                    disabled
                  ></v-select>
                </v-col>
                <!-- 통화허용 -->
                <v-col cols="12">
                  <div class="d-flex">
                    <div class="pt-3">통화허용:</div>
                    <v-checkbox v-model="editItem.incomingEnabled" label="수신" class="mr-4"></v-checkbox>
                    <v-checkbox v-model="editItem.outgoingEnabled" label="발신"></v-checkbox>
                  </div>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="editItem.user"
                    label="사용자"
                    variant="outlined"
                    density="compact"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-select
                    v-model="editItem.sipGroup"
                    :items="sipGroupTypes"
                    label="SIP Group"
                    variant="outlined"
                    density="compact"
                  ></v-select>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error" @click="editDialog = false">취소</v-btn>
            <v-btn color="primary" @click="updatePstnNumber">저장</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- PSTN 번호 삭제 확인 다이얼로그 -->
      <v-dialog v-model="deleteDialog" max-width="500px">
        <v-card>
          <v-card-title class="text-h5 bg-error text-white">
            PSTN 번호 삭제
          </v-card-title>
          <v-card-text class="pt-4">
            <p v-if="selectedItems.length === 1">
              선택하신 {{ selectedItems[0].number }} 번호가 삭제됩니다.<br>
              사용자 {{ selectedItems[0].user }}로부터 해제되고 삭제됩니다.
            </p>
            <p v-else>
              선택하신 {{ selectedItems.length }}개의 번호가 삭제됩니다.<br>
              해당 번호들이 사용자로부터 해제되고 삭제됩니다.
            </p>
            <p class="mt-2">삭제를 계속 하시겠습니까?</p>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" @click="deleteDialog = false">취소</v-btn>
            <v-btn color="error" @click="deletePstnNumbers">확인</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted, watch } from 'vue';

  // 상태 관리
  const searchNumber = ref('');
  const searchUser = ref('');
  const selectedProduct = ref('');
  const selectedSipGroup = ref('');
  const selectedIncoming = ref('');
  const selectedOutgoing = ref('');
  const productTypes = ['전체', '줌 폰', '줌 폰 프로'];
  const sipGroupTypes = ['전체', 'Phone SIP Route Group', '어카운트에 설정된 SIP Group'];
  const incomingTypes = ['전체', '허용', '차단'];
  const outgoingTypes = ['전체', '허용', '차단'];
  const itemsPerPage = ref(15);
  const page = ref(1);
  const selectAll = ref(false);
  
  // 다이얼로그 상태
  const addDialog = ref(false);
  const editDialog = ref(false);
  const deleteDialog = ref(false);
  
  // 추가 관련 상태
  const addMethod = ref('individual');
  const rangeStart = ref('');
  const rangeEnd = ref('');
  const newItem = ref({
    product: '줌 폰',
    numbers: '',
    sipGroup: 'Phone SIP Route Group'
  });
  
  // 편집 관련 상태
  const editItem = ref({
    number: '',
    product: '',
    user: '',
    sipGroup: '',
    country: '',
    incomingEnabled: false,
    outgoingEnabled: false
  });
  
  // 목데이터 
  const pstnList = ref([
    { id: 1, number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 2, number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용', selected: false },
    { id: 3, number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단', selected: false },
    { id: 4, number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 5, number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 6, number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용', selected: false },
    { id: 7, number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단', selected: false },
    { id: 8, number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 9, number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 10, number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용', selected: false },
    { id: 11, number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단', selected: false },
    { id: 12, number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 13, number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 14, number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용', selected: false },
    { id: 15, number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단', selected: false },
    { id: 16, number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 17, number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 18, number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용', selected: false },
    { id: 19, number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단', selected: false },
    { id: 20, number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 21, number: '02-1234-1234', product: '줌 폰', user: '김팀장 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
    { id: 22, number: '02-2234-1334', product: '줌 폰', user: '김김김 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '차단', outgoing: '허용', selected: false },
    { id: 23, number: '02-3234-1234', product: '줌 폰', user: '승승승 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '차단', selected: false },
    { id: 24, number: '02-4234-1234', product: '줌 폰', user: '빈빈빈 (내선번호)', sipGroup: 'Phone SIP Route Group', country: 'Korea', incoming: '허용', outgoing: '허용', selected: false },
  ])
  
  // 계산된 속성
  const filteredItems = computed(() => {
    return pstnList.value.filter(item => {
      const numberMatch = searchNumber.value ? item.number.includes(searchNumber.value) : true;
      const userMatch = searchUser.value ? item.user.includes(searchUser.value) : true;
      const productMatch = selectedProduct.value && selectedProduct.value !== '전체' ? item.product === selectedProduct.value : true;
      const sipGroupMatch = selectedSipGroup.value && selectedSipGroup.value !== '전체' ? item.sipGroup === selectedSipGroup.value : true;
      const incomingMatch = selectedIncoming.value && selectedIncoming.value !== '전체' ? item.incoming === selectedIncoming.value : true;
      const outgoingMatch = selectedOutgoing.value && selectedOutgoing.value !== '전체' ? item.outgoing === selectedOutgoing.value : true;
      
      return numberMatch && userMatch && productMatch && sipGroupMatch && incomingMatch && outgoingMatch;
    });
  });
  
  const displayedItems = computed(() => {
    const start = (page.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return filteredItems.value.slice(start, end);
  });
  
  const totalPages = computed(() => {
    return Math.ceil(filteredItems.value.length / itemsPerPage.value);
  });
  
  const totalItems = computed(() => {
    return filteredItems.value.length;
  });
  
  const selectedItems = computed(() => {
    return pstnList.value.filter(item => item.selected);
  });
  
  const getPstnList = () => {
    // API 호출 로직 구현
    console.log('PSTN 번호 조회:', {
      number: searchNumber.value,
      user: searchUser.value,
      product: selectedProduct.value,
    });
    window.alert('@@@@ PSTN 번호 조회기능 구현 예정입니다.');
  };
  

  // 메서드
  const fetchData = () => {
    // API 호출 로직 구현
    console.log('데이터 조회:', {
      number: searchNumber.value,
      user: searchUser.value,
      product: selectedProduct.value,
      sipGroup: selectedSipGroup.value,
      incoming: selectedIncoming.value,
      outgoing: selectedOutgoing.value,
      page: page.value,
      itemsPerPage: itemsPerPage.value
    });

    // 데이터 조회 후 페이지 번호 확인
    // 현재 페이지가 총 페이지 수보다 크면 마지막 페이지로 이동
    if (totalPages.value > 0 && page.value > totalPages.value) {
      page.value = totalPages.value;
    }
  };
  
  const downloadExcel = () => {
    console.log('엑셀 다운로드');
    window.alert('@@@@ PSTN 번호 엑셀 다운로드기능 구현 예정입니다.');
    // 엑셀 다운로드 로직 구현
  };

  const uploadExcel = () => {
    console.log('엑셀 업로드');
    window.alert('@@@@ PSTN 번호 엑셀 업로드기능 구현 예정입니다.');
    // 엑셀 업로드 로직 구현
  };
  
  const toggleSelectAll = () => {
    displayedItems.value.forEach(item => {
      item.selected = selectAll.value;
    });
  };
  
  const updateSelectAll = () => {
    selectAll.value = displayedItems.value.every(item => item.selected);
  };
  
  const openAddDialog = () => {
    newItem.value = {
      product: '줌 폰',
      numbers: '',
      sipGroup: 'Phone SIP Route Group'
    };
    addMethod.value = 'individual';
    rangeStart.value = '';
    rangeEnd.value = '';
    addDialog.value = true;
  };
  
  const openEditDialog = () => {
    const selected = selectedItems.value;
    if (selected.length !== 1) {
      alert('편집할 항목을 하나만 선택해주세요.');
      return;
    }
    
    const item = selected[0];
    editItem.value = {
      number: item.number,
      product: item.product,
      user: item.user,
      sipGroup: item.sipGroup,
      country: item.country,
      incomingEnabled: item.incoming === '허용',
      outgoingEnabled: item.outgoing === '허용'
    };
    
    editDialog.value = true;
  };
  
  const openDeleteDialog = () => {
    const selected = selectedItems.value;
    if (selected.length === 0) {
      alert('삭제할 항목을 선택해주세요.');
      return;
    }
    
    deleteDialog.value = true;
  };
  
  const addPstnNumber = () => {
    // API 호출 로직 구현
    console.log('PSTN 번호 추가:', {
      product: newItem.value.product,
      method: addMethod.value,
      numbers: addMethod.value === 'individual' ? newItem.value.numbers : `${rangeStart.value} ~ ${rangeEnd.value}`,
      sipGroup: newItem.value.sipGroup
    });
    window.alert('@@@@ PSTN 번호 저장기능 구현 예정입니다.');
    // 성공 시 다이얼로그 닫기 및 데이터 새로고침
    addDialog.value = false;
    fetchData();
  };
  
  const updatePstnNumber = () => {
    // API 호출 로직 구현
    console.log('PSTN 번호 업데이트:', {
      number: editItem.value.number,
      user: editItem.value.user,
      sipGroup: editItem.value.sipGroup,
      incoming: editItem.value.incomingEnabled ? '허용' : '차단',
      outgoing: editItem.value.outgoingEnabled ? '허용' : '차단'
    });
    
    window.alert('@@@@ PSTN 번호 업데이트기능 구현 예정입니다.');
    // 성공 시 다이얼로그 닫기 및 데이터 새로고침
    editDialog.value = false;
    fetchData();
  };
  
  const deletePstnNumbers = () => {
    // API 호출 로직 구현
    console.log('PSTN 번호 삭제:', {
      numbers: selectedItems.value.map(item => item.number)
    });
    window.alert('@@@@ PSTN 번호 삭제기능 구현 예정입니다.');
    // 성공 시 다이얼로그 닫기 및 데이터 새로고침
    deleteDialog.value = false;
    fetchData();
  };
  
  // 페이지 변경 시 선택 상태 초기화
  watch(page, () => {
    selectAll.value = false;
  });
  
  // itemsPerPage 변경 시 페이지 번호 초기화
  watch(itemsPerPage, () => {
    page.value = 1;
    selectAll.value = false;
  });
  
  // 필터링 조건 변경 시 페이지 번호 초기화
  watch([searchNumber, searchUser, selectedProduct, selectedSipGroup, selectedIncoming, selectedOutgoing], () => {
    page.value = 1;
    selectAll.value = false;
  });
  
  onMounted(() => {
    fetchData();
  });
  </script>