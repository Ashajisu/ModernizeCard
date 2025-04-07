<script setup lang="ts">
import { ref } from "vue";
import UiParentCard from "@/components/shared/UiParentCard.vue";

const props = defineProps<{
  title: string;
  width?: undefined | number | string;
}>();

const showDialog = ref(false);
const open = () => {
  showDialog.value = true;
};
const close = () => {
  showDialog.value = false;
}
//외부에서 사용가능하게 함
defineExpose({ showDialog, open, close });

</script>
<!--persistent : 외부를 눌러도 닫히지 않게-->
<!--@click:outside="close" : 외부를 눌러서 닫혀도 dialog=false 값 유지되게-->
<template>
    <v-dialog v-model="showDialog" :width="width ?? 700" min-width="400" max-width="1200" @click:outside="close">
        <v-card>
            <UiParentCard :title="title" style="height: 100%">
                <div>
                  <slot name="inCard"/>
                </div>
                <v-card-actions class="pa-4">
                  <v-spacer></v-spacer>
                  <slot name="btn"/>
                  <v-btn color="primary" variant="outlined" @click="close">닫기</v-btn>
                </v-card-actions>
            </UiParentCard>
        </v-card>
    </v-dialog>
</template>

<style scoped lang="scss">

</style>