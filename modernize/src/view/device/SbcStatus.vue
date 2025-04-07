<script setup lang="ts">

import { ref } from "vue";
import SystemView from '../../views/apps/kanban/SystemView.vue';
import CallView from '../../views/apps/kanban/CallView.vue';
import AlarmView from '../../views/apps/kanban/AlarmView.vue';

// 선택된 SBC 정보
const selectedServer = ref<{id: number; name: string; descirpiton: string; icon: string} | null > (null);
const dialog = ref(false);
const selectedTab = ref("system");

//SBC List
const sbcList = ref([
  {id : 1, name : "ArisysSBC #1", description: "본사에 설치되어있는 SBC", icon: "mdi-server-network"},
  {id : 2, name : "ArisysSBC #2", description: "본사에 설치되어있는 SBC", icon: "mdi-server-network"}
]);

//새 SBC data
const newSbc = ref({
  product: "",
  model: "",
  ip: "",
  snmpVersion: "v2",
  snmpString: "public",
  description: ""
});

const selectSbc = (sbc : any) => {
  selectedServer.value = sbc;
};

//SBC 추가
const addSbc = () => {
  if(!newSbc.value.product || !newSbc.value.model || !newSbc.value.ip){
    alert("모든 필드를 입력하세요!");
    return;
  }

  sbcList.value.push({
    id: sbcList.value.length + 1,
    name : newSbc.value.product,
    description : newSbc.value.description,
    icon: "mdi-server-network"
  });

  dialog.value = false;
  newSbc.value = {product : "", model: "", ip: "", snmpVersion: "v2", snmpString: "public", description: ""};
};


</script>

<template>

  <v-card class="pa-1">
    <v-card-title class="text-h4 font-weight-bold d-flex align-left"> SBC 현황 </v-card-title>
    <v-card-title class="text-h6 font-weight-bold d-flex align-left">줌 폰과 연동되어 있는 SBC 장비를 모니터링 할 수 있습니다.</v-card-title>

  </v-card>


  <br><br>
  <v-container>
    <!-- SBC 목록 -->
    <v-row>
      <v-col v-for="sbc in sbcList" :key="sbc.id" cols="4">
        <v-card class="pa-4 text-center" :class="{'bg-primary text-white' : selectedServer?.id === sbc.id}"
                @click = "selectSbc(sbc)">
          <v-icon size="40">{{sbc.icon}}</v-icon>
          <h3>{{sbc.name}}</h3>
          <p>{{sbc.description}}</p>
        </v-card>
      </v-col>

      <!-- SBC 추가 버튼 -->
      <v-col cols="2">
        <v-card class="pa-4 text-center bg-blue white--text" @click="dialog = true">
          <v-icon size="30">mdi-plus</v-icon>
          <h3>SBC 등록</h3>
        </v-card>
      </v-col>
    </v-row>


    <!-- 선택된 SBC 상세 정보 -->
    <v-card v-if="selectedServer" class="mt-4 pa-4">
      <v-card-title>{{ selectedServer.name }}</v-card-title>
      <v-divider></v-divider>
      <v-tabs v-model="selectedTab">
        <v-tab value="system">시스템 현황</v-tab>
        <v-tab value="call">콜 현황</v-tab>
        <v-tab value="alarm">알람 내역</v-tab>
      </v-tabs>

      <v-window v-model="selectedTab">
        <v-window-item value="system">
          <SystemView />
        </v-window-item>
        <v-window-item value="call">
          <CallView />
        </v-window-item>
        <v-window-item value="alarm">
          <AlarmView />
        </v-window-item>
      </v-window>
    </v-card>


    <!-- SBC 등록 모달 -->
    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title>SBC 신규 등록</v-card-title>
        <v-divider></v-divider>
        <v-card-text>
          <v-select v-model="newSbc.product" label="제품" :items="['Ribbon', 'Cisco', 'Nexus']"></v-select>
          <v-select v-model="newSbc.model" label="모델" :items="['Edge', 'Google', 'Chrome']"></v-select>
          <v-text-field v-model="newSbc.ip" label="IP 주소" placeholder="192.168.xxx.xxx"></v-text-field>

          <v-select v-model="newSbc.snmpVersion" label="SNMP Version" :items="['v1', 'v2', 'v3']"></v-select>
          <v-text-field v-model="newSbc.snmpString" label="SNMP String"></v-text-field>

          <v-textarea v-model="newSbc.description" label="설명" placeholder="본사에 설치되어 있는 SBC"></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="addSbc">저장</v-btn>
          <v-btn color="second" @click="dialog = false">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
  <br>


</template>