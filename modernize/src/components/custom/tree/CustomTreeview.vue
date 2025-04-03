<script setup lang="ts">
import UiChildCard from "@/components/shared/UiChildCard.vue";
import {ref, onMounted, watch} from "vue";

defineProps<{ selectedId: { id: number; type: string } }>();

const search = ref('');

//tui-tree
import TuiTree from "tui-tree";

// 트리 데이터를 정의
const treeData = ref([
  {
    text: { title: "최상위 조직정보 1", id: "A_1" },
    depthLabel: {title:' 최상위 조직정보',code:'A'},
    index: 0,
    children: [
      {
        text: { title: "2단계 조직정보 1", id: "B_1" },
        depthLabel: "2단계 조직정보",
        index: 0,
        state: "closed",
        children: [
          {
            text: { title: "3단계 조직정보 1", id: "C_1" },
            depthLabel: "3단계 조직정보",
            children: [
              { text: { title: "사용자 A", id: "user_ad_205" }, depthLabel: {title:' 사용자',code:'D'}, index: 0 },
              { text: { title: "사용자 B", id: "user_ad_206" }, depthLabel: {title:' 사용자',code:'D'}, index: 1 }
            ]
          }
        ]
      },
      {
        text: { title: "2단계 조직정보 2", id: "B_2" },
        depthLabel: "2단계 조직정보",
        index: 1,
        state: "closed",
        children: [
          {
            text: { title: "3단계 조직정보 2", id: "C_2" },
            depthLabel: "3단계 조직정보",
            children: [
              { text: { title: "사용자 C", id: "user_ad_207" }, depthLabel: {title:' 사용자',code:'D'}, index: 0 },
              { text: { title: "사용자 D", id: "user_ad_208" }, depthLabel: {title:' 사용자',code:'D'}, index: 1 }
            ]
          }
        ]
      }
    ]
  },
  {
    text: { title: "최상위 조직정보 2", id: "A_2" },
    depthLabel: "최상위 조직정보",
    index: 1,
    state: "closed",
    children: [
      { text: { title: "3단계 조직정보 3", id: "C_3" }, depthLabel: {title:' 사용자',code:'D'} },
      { text: { title: "3단계 조직정보 4", id: "C_4" }, depthLabel: {title:' 사용자',code:'D'}, index: 0 },
      { text: { title: "3단계 조직정보 5", id: "C_5" }, depthLabel: {title:' 사용자',code:'D'}, index: 1 }
    ]
  }
]);

// DOM 요소 참조
const treeContainer = ref<HTMLElement | null>(null);
const movedValue = ref("");
const selectedValue = ref<string>('');  // 선택된 노드의 텍스트를 저장할 ref
const selectedNodeId = ref<string | null>(null);  // 선택된 노드의 ID를 저장할 ref

// 트리 인스턴스
let tree: any = null;
let depthLabels: any[] = [{title:' 최상위 조직정보',code:'A'}, {title:' `2단계 조직정보',code:'B'}, {title:' 3단계 조직정보',code:'C'},{title:' 사용자',code:'D'}];

onMounted(() => {
  if (treeContainer.value) {
    tree = new TuiTree(treeContainer.value, {
      data: treeData.value,
      nodeDefaultState: "opened",
      template: {
        internalNode:
            '<div class="tui-tree-content-wrapper" style="padding-left: {{indent}}px">' +
            '<button type="button" class="tui-tree-toggle-btn tui-js-tree-toggle-btn">' +
            '<span class="tui-ico-tree"></span>' +
            '{{stateLabel}}' +
            '</button>' +
            '</button>' +
            '<span class="tui-tree-text tui-js-tree-text">' +
            '<span class="tui-tree-ico tui-ico-folder"></span>' +
            '{{text.title}}' +
            // <b>{{depthLabel}}</b>
            '</span>' +
            '</div>' +
            '<ul class="tui-tree-subtree tui-js-tree-subtree">{{children}}</ul>',
        leafNode:
            '<div class="tui-tree-content-wrapper" style="padding-left: {{indent}}px">' +
            '<span class="tui-tree-text tui-js-tree-text">' +
            '<span class="tui-tree-ico tui-ico-file"></span>' +
            '{{text.title}}' +
            // <b>{{depthLabel}}</b>
            '</span>' +
            '</div>'
      }
    }).enableFeature("Draggable", {
      helperClassName: "tui-tree-drop",
      lineClassName: "tui-tree-line",
      isSortable: true,
      useHelper: true
    }).enableFeature('Selectable', {
      selectedClassName: 'tui-tree-selected',  // 선택된 노드에 추가될 클래스
    });

    tree.on('beforeDraw', (evt: any) => {
      tree.each((child: any, childId:string) => {
        console.log(child.getData('index'), childId); // 이 변수값을
        let depth = tree.getDepth(childId);

        depth = Math.min(depth, depthLabels.length);
        child.setData({
          depthLabel: depthLabels[depth - 1] || ''
        });

      }, evt.nodeId);
    });

    // 부모 노드별로 index 재정렬
    function updateChildIndexes(parentId: any) {
      const children = tree.getChildIds(parentId);

      children.forEach((childId: any, index: number) => {
        tree.setNodeData(childId, { index }); // index 값 업데이트
      });
    }

    // 이동 이벤트 핸들러
    tree.on("move", (eventData: any) => {
      movedValue.value =
          `nodeId: ${eventData.nodeId}
          originalParentId: ${eventData.originalParentId}
          newParentId: ${eventData.newParentId}
          index: ${eventData.index}`;
      updateChildIndexes(eventData.newParentId);
      updateChildIndexes(eventData.originalParentId);
    });

    // 'select' 이벤트 리스너
    tree.on('select', (eventData: any) => {
      console.log(eventData);
      const nodeData = tree.getNodeData(eventData.nodeId);
      selectedValue.value = JSON.stringify(nodeData); // text 대신 id
      selectedNodeId.value = eventData.nodeId;  // 선택된 노드 ID 저장

      sendSelected(nodeData);

      // const formattedData = {
      //   id: nodeData?.text?.id || 0,
      //   type: nodeData?.depthLabel?.code === 'D' ? 'person' : 'group',
      //   data: {
      //     parentTitle: parentNodeData?.text?.title || null,
      //     parentCode: parentNodeData?.text?.id || null,
      //     index: nodeData?.index || 0,
      //     title: nodeData?.text?.title || null,
      //     code: nodeData?.text?.id || null,
      //     depthLabel: nodeData?.depthLabel?.title || null,
      //   }
      // }

    });
  }
});

// 모든 노드 열기
const openAllNodes = () => {
  if (treeContainer.value) {
    console.log('open');
    tree.eachAll((node:any, nodeId:string)=>{
      tree.open(nodeId, false);
    });
  }
};

// 모든 노드 닫기
const closeAllNodes = () => {
  if (treeContainer.value) {
    console.log('close');
    tree.eachAll((node:any, nodeId:string)=>{
      tree.close(nodeId, false);
    });
  }
};

// 검색기능
const searching = (newSearch:string)=>{
  const keyword = newSearch.replace(/\s+/g, "");
  console.log(keyword);
  if (treeContainer.value) {

    // 검색 실행
    const matchedNodeIds:string[] = tree.search((node: any, nodeId:string) => {
      const nodeData = node.getData('text');
      console.log(nodeData);
      return nodeData?.title.replace(/\s+/g, "").includes(keyword) || nodeData?.id.replace(/\s+/g, "").includes(keyword);
    });

    // 모든 노드를 닫고, 일치하는 노드만 열기.
    closeAllNodes();
    console.log(matchedNodeIds);
    matchedNodeIds.forEach(nodeId => {
      tree.open(nodeId, true);
    });
  }
}

watch(search, (newSearch:string) => {
  searching(newSearch);
});

// 선택된 정보를 부모 컴포넌트로 전달 : { id: 0, type: 'person'|'group' }
const emit = defineEmits(['update:selectedId']);
const sendSelected = (nodeData: any) => {
  if (!nodeData) return;
  const id =  nodeData?.text?.id || 0;
  const type = nodeData?.depthLabel?.code === 'D' ? 'person' : 'group';
  const index =  nodeData?.text?.index || 0;
  console.log({id,type,index});
  // 부모 컴포넌트로 이벤트 전달
  emit('update:selectedId', {id,type});
};

</script>
<!--tree view 는 부모컴포넌트에 position: relative; 설정이 들어가면 드래그좌표가 달라짐.
그래서 v-card, AppBaseCard, perpect-scroll-bar 내부에 들어가면 안됨.
-->
<template>
    <UiChildCard title="조직 정보" variant="flat">
        <v-row>
          <v-btn color="primary" @click="openAllNodes">전체 펼치기</v-btn>
          <v-btn color="primary" @click="closeAllNodes">전체 접기</v-btn>
          <v-btn color="primary"><v-icon icon="mdi-refresh"></v-icon></v-btn>
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
    <div>
      <textarea v-model="movedValue" readonly rows="5" style="width: 100%; margin-top: 10px;"></textarea>
      <textarea v-model="selectedValue" readonly rows="5" style="width: 100%; margin-top: 10px;"></textarea>
    </div>
</template>
<style scoped>
.custom-treeview {
  background: #ffffff;
  font-size: medium;
}
</style>