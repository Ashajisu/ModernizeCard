<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { createWaveSurferWithRegions } from '@/plugins/WaveSufer';

// Props 정의
const props = defineProps({
  url: {
    type: String,
    required: true,
  },
});
const notFound = ref<boolean>(false);
// 음성파일로 파형 및 오디오 생성
onMounted(() => {
  if(!!props.url){
    createWaveSurferWithRegions(props.url);
  }else {
    console.warn('not found audio path');
    notFound.value = true;
  }
});
</script>

<template>
    <div>
        <div id="waveform" style="width: 650px; height: 150px"></div>
        <div v-if="notFound">
            <span class="text-error">음성파일을 찾을 수 없습니다</span>
        </div>
    </div>
</template>

<style scoped></style>
