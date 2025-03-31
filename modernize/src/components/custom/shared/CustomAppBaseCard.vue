<script setup lang="ts">
import { ref } from 'vue';
import { useDisplay } from 'vuetify';
const { lgAndUp } = useDisplay();
const sDrawer = ref(false);
</script>

<template>
    <!---/Left chat list -->
    <div class="d-flex mainbox">
        <div class="left-part rounded-md">
            <slot name="leftpart"></slot>
        </div>

        <!---right chat conversation -->
        <div class="right-part">
            <slot name="rightpart"></slot>
        </div>
    </div>
</template>

<style lang="scss">
//.mainbox {
//    position: relative;
//    overflow: hidden;
//}
.left-part {
    width: 320px;
    border: 1px solid rgb(var(--v-theme-borderColor));
    radious:5px;
    min-height: 730px;
//    //transition: 0.1s ease-in;
//    //flex-shrink: 0;
}
//.v-theme--light {
//    .left-part {
//        background: white;
//    }
//}
//.v-theme--dark {
//    .left-part {
//        background: #2b2b2b;
//    }
//}
.right-part {
    width: 100%;
    min-height: 730px;
    border: 1px solid rgb(var(--v-theme-borderColor));
    radious:5px;
//    position: relative;
}
</style>
