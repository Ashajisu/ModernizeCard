<script setup lang="ts">
// const items = [
//     {
//         id: 1,
//         title: 'Applications :',
//         children: [
//             { id: 2, title: 'Calendar : app' },
//             { id: 3, title: 'Chrome : app' },
//             { id: 4, title: 'Webstorm : app' }
//         ]
//     },
//     {
//         id: 5,
//         title: 'Documents :',
//         children: [
//             {
//                 id: 6,
//                 title: 'vuetify :',
//                 children: [
//                     {
//                         id: 7,
//                         title: 'src :',
//                         children: [
//                             { id: 8, title: 'index : ts' },
//                             { id: 9, title: 'bootstrap : ts' }
//                         ]
//                     }
//                 ]
//             },
//             {
//                 id: 10,
//                 title: 'material2 :',
//                 children: [
//                     {
//                         id: 11,
//                         title: 'src :',
//                         children: [
//                             { id: 12, title: 'v-btn : ts' },
//                             { id: 13, title: 'v-card : ts' },
//                             { id: 14, title: 'v-window : ts' }
//                         ]
//                     }
//                 ]
//             }
//         ]
//     },
//     {
//         id: 15,
//         title: 'Downloads :',
//         children: [
//             { id: 16, title: 'October : pdf' },
//             { id: 17, title: 'November : pdf' },
//             { id: 18, title: 'Tutorial : html' }
//         ]
//     },
//     {
//         id: 19,
//         title: 'Videos :',
//         children: [
//             {
//                 id: 20,
//                 title: 'Tutorials :',
//                 children: [
//                     { id: 21, title: 'Basic layouts : mp4' },
//                     { id: 22, title: 'Advanced techniques : mp4' },
//                     { id: 23, title: 'All about app : dir' }
//                 ]
//             },
//             { id: 24, title: 'Intro : mov' },
//             { id: 25, title: 'Conference introduction : avi' }
//         ]
//     }
// ];
import Searchbar from "@/layouts/full/vertical-header/Searchbar.vue";
import UiChildCard from "@/components/shared/UiChildCard.vue";
import {UserIcon} from "vue-tabler-icons";
import {ref} from "vue";

const search = ref('');
const selectedIds = ref(3);  // 선택된 항목들의 id를 저장할 배열
const files : Record<string, string> ={
  html: 'mdi-language-html5',
  js: 'mdi-nodejs',
  json: 'mdi-code-json',
  md: 'mdi-language-markdown',
  pdf: 'mdi-file-pdf-box',
  png: 'mdi-file-image',
  txt: 'mdi-file-document-outline',
  xls: 'mdi-file-excel',
  menu: 'mdi-menu',
  user: 'mdi-account',
  open: 'mdi-folder-open'
}

const items = [
  {
    id: 1,
    title: '최상위 조직정보',
    children: [
      {
        id: 2,
        title: '2단계 조직정보',
        children: [
          {
            id: 3,
            title: '3단계 조직정보',
            children: [
              {
                id: 4,
                title: '최하위 조직정보',
                children: [
                  {
                    id: 5,
                    title: '사용자 A',
                    file: 'user',
                  },
                  {
                    id: 6,
                    title: '사용자 B',
                    file: 'user',
                  },
                  {
                    id: 7,
                    title: '사용자 C',
                    file: 'user',
                  },
                  {
                    id: 8,
                    title: '사용자 D',
                    file: 'user',
                  },
                ],
                file: 'menu',
              },
            ],
            file: 'open',
          },
        ],
        file: 'open',
      },
      {
        id: 9,
        title: '2단계 조직정보',
        children: [
          {
            id: 10,
            title: '3단계 조직정보',
            children: [
              {
                id: 11,
                title: '최하위 조직정보',
                children: [
                  {
                    id: 12,
                    title: '사용자 A',
                    file: 'user',
                  },
                  {
                    id: 13,
                    title: '사용자 B',
                    file: 'user',
                  },
                  {
                    id: 14,
                    title: '사용자 C',
                    file: 'user',
                  },
                  {
                    id: 15,
                    title: '사용자 D',
                    file: 'user',
                  },
                ],
                file: 'menu',
              },
            ],
            file: 'open',
          },
        ],
        file: 'open',
      },
    ],
    file: 'open',
  },
];
</script>
<template>
    <UiChildCard title="조직 정보">
      <v-row>
        <v-btn color="primary">전체 펼치기</v-btn>
        <v-btn color="primary">전체 접기</v-btn>
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
    </UiChildCard>

<!--    <div>선택된 ID: {{ selectedIds }}</div>-->
    <v-treeview :items="items" item-value="id" density="compact" color="primary" activatable
                v-model:active="selectedIds">
      <template v-slot:prepend="{ item, isOpen }">
        <v-icon v-if="!item.file" :icon="isOpen ? 'mdi-folder-open' : 'mdi-folder'"></v-icon>

        <v-icon v-else :icon="files[item.file]"></v-icon>
      </template>
    </v-treeview>
</template>
