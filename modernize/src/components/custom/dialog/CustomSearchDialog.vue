<script setup lang="ts">
import {watch, ref, computed} from "vue";
import DialogBaseCard from "@/components/custom/dialog/DialogBaseCard.vue";
import type {searchType} from "@/types/HeaderTypes";
import UiParentCard from "@/components/shared/UiParentCard.vue";

const props = defineProps<{
  view: boolean;
  title: string;
  items: searchType[] | undefined;
}>();

const dialog = ref(props.view);
const result = ref<searchType>();
// 열릴때
watch(() => props.view, (newValue : boolean) => {
  dialog.value = newValue;
}, { deep: false });

// 닫힐때
const emit = defineEmits(["update:view", "update:selectedValue"]);

function close() {
  dialog.value = false;
  emit('update:view', false);
  console.log('close', dialog.value);
}

function save() {
  console.log('save', result.value);
  emit('update:selectedValue', result.value?.title);
  close();
}

// 검색
const searchQuery = ref('');
const filteredSugg = computed(() => {
  if(!!props.items){
    return props.items.filter(item =>
        item.title.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
  }
});

</script>
<!--persistent : 외부를 눌러도 닫히지 않게-->
<!--@click:outside="close" : 외부를 눌러서 닫혀도 dialog=false 값 유지되게-->
<template>
  <v-dialog v-model="dialog"  max-width="700"  @click:outside="close">
    <v-card>
      <v-card-title class="pa-4 bg-primary">
        <span class="title text-white">{{ title }}</span>
      </v-card-title>
      <DialogBaseCard>
        <template v-slot:leftpart>
            <div class="d-flex align-center justify-space-between pa-5">
              <v-text-field v-model="searchQuery"
                            placeholder="Search" color="primary" density="compact" variant="outlined" hide-details></v-text-field>
            </div>
            <v-divider></v-divider>
            <h5 class="text-h5 mt-4 px-5 pb-4">Quick Page Links</h5>
            <perfect-scrollbar style="height: 380px">
              <v-radio-group v-model="result">
                <v-list class="pt-0 pb-5" lines="two">
                  <v-list-item
                      :value="item"
                      v-for="(item, index) in filteredSugg"
                      :key="index"
                      color="primary"
                      class="px-5 py-2"
                  >
                    <v-label>
                        <v-radio :value="item"></v-radio>
                        <h6 class="text-subtitle-1 font-weight-bold mb-1">{{ item.title }}</h6>
                    </v-label>
                  </v-list-item>
                </v-list>
              </v-radio-group>
            </perfect-scrollbar>
        </template>
        <template v-slot:rightpart>
          <UiParentCard title="검색결과" style="height: 100%">
            <div v-if="result">
              <h6 class="text-subtitle-1 font-weight-bold mb-1">{{ result.title }}</h6>
              <p class="text-subtitle-2 text-medium-emphasis">{{ result.href }}</p>
            </div>
            <p v-else class="text-subtitle-2 text-medium-emphasis text-error"> 검색결과가 없습니다. </p>
          </UiParentCard>
        </template>
      </DialogBaseCard>
      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn color="error" @click="close">Cancel</v-btn>
        <v-btn color="primary" :disabled="!result" variant="flat" @click="save" >Save</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped lang="scss">

</style>