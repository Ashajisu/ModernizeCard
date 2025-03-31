<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { createWaveSurferWithRegions } from '@/plugins/Wavesufer';

// Props 정의
const props = defineProps({
  url: {
    type: String,
    required: true,
  },
  audioTime: Number, // 부모에서 전달한 audioTime
});

// 이벤트 정의
const emit = defineEmits(['update:audioTime']);


// 음성파일로 파형 및 오디오 생성
const notFound = ref<boolean>(false);
onMounted(() => {
  if(!!props.url){
    const wavesurfer = createWaveSurferWithRegions(props.url);

    // 초 단위의 시간 currentTime 를 부모로 전달
    wavesurfer.on('timeupdate', (currentTime) => {
      emit('update:audioTime', currentTime);
    });
  }else {
    console.warn('not found audio path');
    notFound.value = true;
  }
});
</script>

<template>
    <div>
        <div id="waveform" style="width: 100%; height: 150px">
        </div>
        <div v-if="notFound">
            <span class="text-error">음성파일을 찾을 수 없습니다</span>
        </div>
    </div>
</template>

<style scoped></style>
