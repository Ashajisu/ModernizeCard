<script setup lang="ts">
import {watch, ref, computed} from "vue";
import DialogBaseCard from "@/components/custom/dialog/DialogBaseCard.vue";
import UiParentCard from "@/components/shared/UiParentCard.vue";

const props = defineProps<{
  view: boolean;
  title: string;
  single: boolean;
  searchField: string; // items 중 검색할 필드명
  items: any[] | undefined;
}>();

const searchField = computed(() => props.searchField);
const dialog = ref(props.view);
const result = ref<any>();
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
  const selectedValues = result.value.map((item:any) => item[searchField.value]);
  console.log('save', selectedValues);
  emit('update:selectedValue', selectedValues);
  close();
}

// 검색
//지정한 필드값 > 첫번째 필드 > 공백
const searchQuery = ref('');
const filteredSugg = computed(() => {
  if(!!props.items){
    return props.items.filter(item =>
    {
      const searchTarget = item[searchField.value] ?? item[Object.keys(item)[0]] ?? "";
      return searchTarget.toLowerCase().includes(searchQuery.value.toLowerCase());
    }
    );
  }
});

//선택값 제거
const removeItem= (item:any) => {
  result.value = result.value.filter((i:any) => i !== item);
};

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
            <h5 class="text-h5 mt-4 px-5 pb-4">{{ searchField }}</h5>
            <perfect-scrollbar style="height: 380px">
              <v-radio-group v-model="result" :multiple="!single">
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
                      <h6 class="text-subtitle-1 font-weight-bold mb-1">{{ item[searchField] }}</h6>
                    </v-label>
                  </v-list-item>
                </v-list>
              </v-radio-group>
            </perfect-scrollbar>
        </template>
        <template v-slot:rightpart>
          <UiParentCard title="검색결과" style="height: 100%">
            <div v-if="!!result">
              <h6 class="text-subtitle-1 font-weight-bold mb-1">{{ result[searchField] }}</h6>
              <div class="text-subtitle-1 text-medium-emphasis text-10 my-3 pt-2">
                  <div v-for="(value, key) in result" :key="key" v-if="single">
                      <v-btn readonly><span class="text-h6">{{ key }} : {{ value }}</span></v-btn>
                  </div>
                  <div v-for="(item, key2) in result" :key="key2" v-else>
                    <span class="text-h6">{{ item[searchField] }}<v-btn icon="mdi-delete" @click="removeItem(item)"></v-btn></span>
                  </div>
              </div>
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