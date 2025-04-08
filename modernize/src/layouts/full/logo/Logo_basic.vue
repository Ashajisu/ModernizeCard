<script setup>
import { computed } from "vue";
import { useCustomizerStore } from "@/stores/customizer";
import LogoLight from "@/layouts/full/logo/LogoLight.vue";
import LogoDark from "@/layouts/full/logo/LogoDark.vue";

const customizer = useCustomizerStore();

//const dark = ref(false);
const dark = computed(() => {
  if (
    customizer.actTheme === "DARK_BLUE_THEME" ||
    customizer.actTheme === "DARK_AQUA_THEME" ||
    customizer.actTheme === "DARK_ORANGE_THEME" ||
    customizer.actTheme === "DARK_PURPLE_THEME" ||
    customizer.actTheme === "DARK_GREEN_THEME" ||
    customizer.actTheme === "DARK_CYAN_THEME"
  ) {
    return true;
  } else {
    return false;
  }
});


</script>
<template>
  <LogoLight v-if="dark" />
  <LogoDark v-else />
</template>
