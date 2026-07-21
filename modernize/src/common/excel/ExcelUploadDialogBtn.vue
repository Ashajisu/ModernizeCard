<script setup lang="ts">
import { ref } from 'vue';
import ExcelUploadForm from './ExcelUploadForm.vue';
import UiParentCard from "@/components/shared/UiParentCard.vue";

interface Props {
  save: any;
  url: any;
  title?: string;
}
const props = defineProps<Props>();
const dialog = ref<boolean>(false);
const excelForm = ref<any>(null);
const saveExcel = async () => {
  if (excelForm.value) {
    const result = await props.save(excelForm.value?.validateForm, props.url);
    if (result) {
      dialog.value = false;
    }
  }
};
</script>

<template>
      <v-btn color="grey" variant="outlined" @click.stop="dialog = true">{{ title || '엑셀 업로드' }}</v-btn>
      <v-dialog v-model="dialog" width="700" @click:outside="dialog = false">
          <v-card>
              <UiParentCard :title="title || '엑셀 업로드'" style="height: 100%">
                  <div>
                       <ExcelUploadForm ref="excelForm" />
                  </div>
                  <v-card-actions class="justify-end">
                      <v-btn variant="outlined" @click="dialog = false">취소</v-btn>
                      <v-btn color="primary" variant="flat" @click="saveExcel">저장</v-btn>
                  </v-card-actions>
              </UiParentCard>
          </v-card>
      </v-dialog>
</template>
