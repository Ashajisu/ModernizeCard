<script setup lang="ts">
import {computed} from "vue";
import config from "@/config";

const logoFiles = import.meta.glob<{ default : string }>("@/assets/images/logos/*.svg", { eager: true });
const defaultPath = '/src/assets/images/logos/logo-icon.svg';

const logoSrc = computed(() => {
  const logoPath = `/src/assets/images/logos/${config.siteName}_logo_icon.svg`;
  return logoFiles[logoPath]?.default || logoFiles[defaultPath]?.default || '';
});
</script>

<template>
  <div class="v-container v-locale--is-ltr max-width-1218 px-md-0 px-3 py-0">
    <div class="py-sm-10 py-6 border-t">
      <div class="d-sm-flex justify-space-between">
        <div class="d-flex ga-2 text-15 opacity-80 justify-sm-start justify-center">
          <img v-if="logoSrc" :src="logoSrc" height="24" class="mx-2" alt="logo"> All rights reserved by {{config.siteName}}
        </div>
        <p class="text-15 opacity-80 d-flex align-center justify-sm-end justify-center pt-sm-0 pt-2"> Produced by
          <a class="v-btn v-theme--BLUE_THEME text-primary v-btn--density-default v-btn--size-default v-btn--variant-text text-hover-primary link-btn text-15 ms-1" href="https://adminmart.com/" target="_blank">
            <span class="v-btn__overlay"></span>
            <span class="v-btn__underlay"></span>
            <span class="v-btn__content" data-no-activator=""> {{config.siteName}} </span>
          </a>
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">

</style>