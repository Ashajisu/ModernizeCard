<script setup lang="ts">
import { computed } from "vue";
import config from "@/config";

const props = defineProps<{
  theme: "dark" | "light";
}>()

const logoFiles = import.meta.glob<{ default : string }>("@/assets/images/logos/*.svg", { eager: true });

const logoSrc = computed(() => {
  const logoPath = `/src/assets/images/logos/${config.siteName}_${props.theme}.svg`;
  return logoFiles[logoPath]?.default || "";
});
</script>
<!--@/assets/images/logs/회사명_light.svg or 회사명_dark.svg 파일 저장-->
<!--사이즈는 svg 파일에서 수정-->
<!--@/config.ts 파일 siteName 변수에 회사명 저장-->
<template>
  <div class="logo">
    <RouterLink to="/">
      <img v-if="logoSrc" :src="logoSrc" alt="Company Logo" />
    </RouterLink>
  </div>
</template>