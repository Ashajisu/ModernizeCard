<script setup lang="ts">
import {computed} from 'vue';
import {RecordChatExample} from "@/_mockApis/custom/ZoomData";

// 부모에서 전달받은 audioTime
const props = defineProps({
  audioTime: Number
});

// 채팅 가져오기
const chatDetail: any = computed(() => {
    console.log(RecordChatExample);
  return RecordChatExample;
});

// computed 로 audioTime 을 포맷
const formattedAudioTime = computed(() => {
  if(props.audioTime){
  return formatTime(props.audioTime);
  }
  return 'undefined';
});

// 시간 포맷 함수 (초 단위 숫자를 hh:mm:ss.sss 형식으로 변환)
const formatTime = (time: number): string => {
    const hours = Math.floor(time / 3600); // 시 계산 (3600초 = 1시간)
    const minutes = Math.floor(time / 60); // 분 계산
    const seconds = Math.floor(time % 60); // 초 계산
    const milliseconds = Math.floor((time % 1) * 1000); // 밀리초 계산

    // 분, 초, 밀리초를 2자리로 맞추기
    const formattedHours = hours < 10 ? '0' + hours : hours.toString();
    const formattedMinutes = minutes < 10 ? '0' + minutes : minutes.toString();
    const formattedSeconds = seconds < 10 ? '0' + seconds : seconds.toString();
  const formattedMilliseconds = milliseconds < 100 ? '00' + milliseconds : (milliseconds < 10 ? '0' + milliseconds : milliseconds.toString());

    return `${formattedHours}:${formattedMinutes}:${formattedSeconds}.${formattedMilliseconds}`;
};

// 실행에 맞춰서 채팅 highlight
const isCurrentText = (ts: string, end_ts: string) => {
  const currentTime = formattedAudioTime.value;
  return currentTime >= ts && currentTime <= end_ts;
};
</script>
<template>
    <div v-if="chatDetail" class="customHeight">
        <div>
            <!---Chat History-->
            <perfect-scrollbar>
                <div class="d-flex">
                    <div class="w-100">
                        <div v-for="chat in chatDetail.chatHistory" :key="chat.id" class="pa-1">
                            <div v-if="chatDetail.userId === chat.userId" class="justify-end d-flex text-end mb-1">
                                <!--right-->
                                <div>
                                    <small class="text-medium-emphasis text-subtitle-2" v-if="chat.ts">
                                        {{ chat.ts }}
                                    </small>
                                    <v-sheet class="bg-grey100 rounded-md px-3 py-2 mb-1" :class="{'highlight': isCurrentText(chat.ts, chat.end_ts)}">
                                        <p class="text-body-1">{{ chat.text }}</p>
                                    </v-sheet>
                                </div>
                            </div>
                            <div v-else class="d-flex align-items-start gap-3 mb-1">
                                <!---left-->
                                <div>
                                    <small class="text-medium-emphasis text-subtitle-2" v-if="chat.ts">
                                      {{ chat.ts }}
                                    </small>
                                    <v-sheet class="bg-grey100 rounded-md px-3 py-2 mb-1" :class="{'highlight': isCurrentText(chat.ts, chat.end_ts)}">
                                        <p class="text-body-1">{{ chat.text }}</p>
                                    </v-sheet>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </perfect-scrollbar>
        </div>
    </div>
</template>
<style lang="scss" scoped>
.highlight{
    background-color: rgba(191, 0, 255, 0.2) !important;
}
</style>
