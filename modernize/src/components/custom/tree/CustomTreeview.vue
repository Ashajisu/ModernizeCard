<script setup lang="ts">
import UiChildCard from "@/components/shared/UiChildCard.vue";
import {ref, onMounted, watch} from "vue";
import { confirm} from "@/common/alertService";
import { initTree, openAllNodes, closeAllNodes, searching, resetFlat, isDirty } from '@/components/custom/tree/treeService';
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
    console.log('check isDirty, reset before : ', isDirty.value, tree);
    // 변경사항이 있는 경우, 확인창 표시
    if(isDirty.value && !await confirm('저장하지 않은 변경사항이 있습니다. 리셋하시겠습니까?')){
          return;
    }

    // 리셋 동작
    resetFlat(); //데이터 재 할당
    tree = initTree(treeContainer); //화면만 새로 그림.
    console.log('check isDirty, reset after: ', isDirty.value, tree);
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
<style lang="scss">
.custom-treeview {
  background-color: rgb(var(--v-theme-surface));
}
.custom-treeview .tui-tree-text{
  font-size: 14px;
  padding: 5px;
  color: rgb(var(--v-theme-textPrimary));
}

.custom-treeview .tui-tree-selected > .tui-tree-content-wrapper{
  background-color: rgb(var(--v-theme-primary), 0.1);
  //background-color: rgb(var(--v-theme-primary));
  border-radius: 7px;
}
.custom-treeview .tui-tree-selected > .tui-tree-content-wrapper .tui-tree-text{
  color: rgb(var(--v-theme-primary));
  //color: white;
}
.custom-treeview .tui-tree-content-wrapper:hover {
  background-color: rgb(var(--v-theme-lightprimary), 0.2);
  color: rgb(var(--v-theme-primary));
}
.custom-treeview .tui-tree-content-wrapper .tui-tree-text:hover {
  color: rgb(var(--v-theme-primary));
}
</style>