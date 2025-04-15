<script setup lang="ts">
import UiChildCard from "@/components/shared/UiChildCard.vue";
import {ref, onMounted, watch} from "vue";
import { confirm} from "@/common/alertService";
import { initTree, openAllNodes, closeAllNodes, searching, resetFlat } from '@/components/custom/tree/treeService';
// DOM 요소 참조
const treeContainer = ref<HTMLElement | null>(null);
let tree: any = null;
const search = ref('');

onMounted(() => {
  if (treeContainer.value) {
    tree = initTree(treeContainer);
  }
});

watch(search, (newSearch:string) => {
  searching(newSearch);
});

const resetConfirm = async ()=>{
    console.log('reset before: ', tree);
  if(await confirm('저장하지 않은 변경사항이 있습니다. 리셋하시겠습니까?')){
    tree = initTree(treeContainer); //화면만 새로 그림.
    console.log('reset after: ', tree);
    resetFlat();
  }
}

</script>
<!--tree view 는 부모컴포넌트에 position: relative; 설정이 들어가면 드래그좌표가 달라짐.
그래서 v-card, AppBaseCard, perfect-scroll-bar 내부에 들어가면 안됨.
-->
<template>
  <div>
    <UiChildCard title="조직 정보" variant="flat">
        <v-row>
          <v-btn color="primary" @click="openAllNodes">전체 펼치기</v-btn>
          <v-btn color="primary" @click="closeAllNodes">전체 접기</v-btn>
          <v-btn color="primary" @click="resetConfirm"><v-icon icon="mdi-refresh"></v-icon></v-btn>
        </v-row>
        <v-row>
          <v-col cols="12">
            <v-text-field density="compact" v-model="search" label="부서명, 사용자를 검색하세요." hide-details variant="outlined" append-outer-icon="mdi-account-search">
              <template v-slot:append-inner>
                <v-icon icon="mdi-account-search" class="text-right"></v-icon>
              </template>
            </v-text-field>
          </v-col>
        </v-row>
    <!--tui-tree-->
    </UiChildCard>
    <div>
      <div ref="treeContainer" id="tree" class="tui-tree-wrap custom-treeview"/>
    </div>
  </div>
</template>
<!--글씨 크기, 화이트 테마에서의 배경 색상, 다크 테마에서의 배경/텍스트 색상, 선택 및 hover 시 컬러 변화-->
<style>
.custom-treeview {
  background: #ffffff;
}

.custom-treeview .tui-tree-text{
  font-size: 14px;
  padding: 5px;
}

.v-theme--DARK_BLUE_THEME .custom-treeview .tui-tree-root > .tui-tree-opened > .tui-tree-content-wrapper .tui-tree-text {
  color: rgb(234, 239, 244) !important;
}

.v-theme--DARK_BLUE_THEME .custom-treeview {
  background-color: rgb(42, 52, 71);
  color: rgb(234, 239, 244);
}

.v-theme--DARK_BLUE_THEME .custom-treeview .tui-tree-text {
  color: rgb(234, 239, 244);
}

.v-theme--DARK_BLUE_THEME .custom-treeview .tui-tree-selected {
  background-color: rgb(51, 63, 85);
  color: rgb(255, 255, 255);
}

.v-theme--DARK_BLUE_THEME .custom-treeview .tui-tree-content-wrapper:hover {
  background-color: rgb(51, 63, 85);
  color: rgb(255, 255, 255);
}
</style>