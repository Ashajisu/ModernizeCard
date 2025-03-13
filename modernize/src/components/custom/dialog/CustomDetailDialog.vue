<script setup lang="ts">
import {watch, ref} from "vue";
import UiParentCard from "@/components/shared/UiParentCard.vue";

const props = defineProps<{
  view: boolean;
  title: string;
  item: any;
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

/* 복사 */
function copyToClipboard(item : object) {
  const jsonString = JSON.stringify(item);
  navigator.clipboard.writeText(jsonString)
      .then(() => {
        alert('클립보드에 복사되었습니다!');
      })
      .catch((err: any) => {
        alert('복사 실패: ' + err);
      });
}


</script>
<!--persistent : 외부를 눌러도 닫히지 않게-->
<!--@click:outside="close" : 외부를 눌러서 닫혀도 dialog=false 값 유지되게-->
<template>
    <v-dialog v-model="dialog"  max-width="700"  @click:outside="close">
        <v-card>
            <UiParentCard :title="title" style="height: 100%">
                <div v-if="item">
<!--                  <p class="text-subtitle-2 text-medium-emphasis">{{ item }}</p>-->
                  <h6 class="text-h6 font-weight-bold mb-1">{{ item.name }}</h6>
                  <p class="mb-1">{{ item.post }}</p>
                  <p class="mb-1">{{ item.project }}</p>
                  <p class="mb-1">{{ item.status }}</p>
                  <p class="mb-1">{{ item.budget }}</p>
                  <v-btn color="primary" variant="plain" @click="copyToClipboard(item)" >복사</v-btn>
                </div>
                <p v-else class="text-subtitle-2 text-medium-emphasis text-error"> 사용자 정보가 없습니다. </p>

                <v-card-actions class="pa-4">
                  <v-spacer></v-spacer>
                  <v-btn color="primary" variant="outlined" @click="close">닫기</v-btn>
                </v-card-actions>
            </UiParentCard>
        </v-card>
    </v-dialog>
</template>

<style scoped lang="scss">

</style>