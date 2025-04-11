<script setup lang="ts">
import { computed, defineProps, defineEmits } from 'vue'

const props = defineProps<{
  itemsPerPage: number,
  pagination: number,
  pageCount: number,
}>()

const emit = defineEmits<{
  (e: 'update:itemsPerPage', value: number): void
  (e: 'update:pagination', value: number): void
}>()

const updateItemsPerPage = (val: any) => {
  emit('update:itemsPerPage', parseInt(val, 10))
}

const page = computed({
  get: () => props.pagination,
  set: (val: number) => emit('update:pagination', val),
})
</script>

<template>
  <v-row align="center" justify="space-between" class="pt-2 mt-3 px-3">
    <v-col cols="auto">
      <v-text-field
          :model-value="itemsPerPage"
          class="pa-2 center-text"
          label="페이지당 항목 수"
          type="number"
          min="-1"
          max="15"
          hide-details
          style="max-width: 130px; min-width: 130px;"
          @update:model-value="updateItemsPerPage"
      />
    </v-col>
    <v-col style="margin-right: 160px;">
      <div class="text-center">
        <v-container>
          <v-row justify="center">
            <v-col cols="8">
              <v-container class="max-width">
                <v-pagination
                    v-model="page"
                    :length="pageCount"
                    class="my-4"
                ></v-pagination>
              </v-container>
            </v-col>
          </v-row>
        </v-container>
      </div>
    </v-col>
  </v-row>
</template>