<script setup lang="ts">
import {watch, ref} from "vue";
import UiParentCard from "@/components/shared/UiParentCard.vue";

const props = defineProps<{
  view: boolean;
  title: string;
  titleSec: string;
}>();

const dialog = ref(props.view);
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

</script>
<!--persistent : 외부를 눌러도 닫히지 않게-->
<!--@click:outside="close" : 외부를 눌러서 닫혀도 dialog=false 값 유지되게-->
<template>
    <v-dialog v-model="dialog"  min-width="400" max-width="1200"  @click:outside="close">
        <v-card>
          <UiParentCard :title="title">
            <div>
              <slot name="top"/>
            </div>
          </UiParentCard>
          <UiParentCard :title="titleSec">
            <div>
              <slot name="bottom"/>
            </div>
            <v-card-actions class="pa-4">
              <v-spacer></v-spacer><v-btn color="primary" variant="outlined" @click="close">닫기</v-btn>
            </v-card-actions>
          </UiParentCard>
        </v-card>
    </v-dialog>
</template>

<style scoped lang="scss">

</style>