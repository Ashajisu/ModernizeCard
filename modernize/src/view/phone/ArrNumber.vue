<script setup lang="ts">
import { ref, computed, watchEffect } from 'vue';

const props = defineProps<{
  field: { name: string; value: any }
}>();

const addMethod = ref<'individual' | 'range'>('individual');
const rangeStart = ref('');
const rangeEnd = ref('');

const arrNumber = computed(() => {
  const start = parseInt(rangeStart.value, 10);
  const end = parseInt(rangeEnd.value, 10);
  if (isNaN(start) || isNaN(end) || start > end || end - start > 100) return [];
  return Array.from({ length: end - start + 1 }, (_, i) => (start + i).toString());
});

// field.value 동기화
watchEffect(() => {
  if (addMethod.value === 'range') {
    props.field.value = arrNumber.value;
  }
});
</script>

<template>
  <v-row class="align-center">
    <v-col cols="12" sm="12" class="pb-sm-1 pb-0 custom-height">

  <v-radio-group v-model="addMethod" inline hide-details>
    <v-radio label="개별 입력" value="individual" />
    <v-radio label="범위 입력" value="range" />
  </v-radio-group>

  <v-text-field
      v-if="addMethod === 'individual'"
      v-model="field.value"
      label="PSTN 번호 (쉼표로 구분)"
      required variant="outlined" density="compact"
  />

  <v-row v-if="addMethod === 'range'">
    <v-col cols="5">
      <v-text-field
          v-model="rangeStart"
          label="시작 번호"
          required variant="outlined" density="compact"
      />
    </v-col>
    <v-col cols="2" class="d-flex align-center justify-center">
      <span class="text-h5">~</span>
    </v-col>
    <v-col cols="5">
      <v-text-field
          v-model="rangeEnd"
          label="끝 번호"
          required variant="outlined" density="compact"
      />
    </v-col>
  </v-row>
    </v-col>
  </v-row>
</template>
