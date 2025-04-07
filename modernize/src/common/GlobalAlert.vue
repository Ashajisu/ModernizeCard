<!-- components/common/GlobalAlert.vue -->
<template>
  <v-dialog v-model="visible" persistent max-width="400">
    <v-card>
      <v-card-text class="py-6 px-6 text-center" style="white-space: pre-line">
        {{ alertState?.message }}
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn v-if="alertState?.type === 'confirm'" @click="respond(false)">취소</v-btn>
        <v-btn color="primary" @click="respond(true)">확인</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { computed, watch, ref } from 'vue';
import { alertState, closeAlert } from '@/common/alertService';

const visible = ref(false);
const state = computed(() => alertState.value);

watch(alertState, (val) => {
  visible.value = !!val;
});

function respond(result: boolean) {
  closeAlert(result);
}
</script>
