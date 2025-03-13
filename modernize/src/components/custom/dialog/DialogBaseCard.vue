<script setup lang="ts">
</script>

<template>
    <!---/Left chat list -->
    <div class="d-flex mainbox">
        <div class="left-part">
            <!-- <perfect-scrollbar style="height: calc(100vh - 290px)"> -->
            <slot name="leftpart"></slot>
            <!-- </perfect-scrollbar> -->
        </div>

        <!---right chat conversation -->
        <div class="right-part">
            <slot name="rightpart"></slot>
        </div>

        <!---right chat conversation -->
    </div>
</template>

<style scoped lang="scss">
.mainbox {
    position: relative;
    overflow: hidden;
}
.left-part {
    width: 350px;
    border-right: 1px solid rgb(var(--v-theme-borderColor));
    min-height: 350px;
    transition: 0.1s ease-in;
    flex-shrink: 0;
}
.v-theme--light {
    .left-part {
        background: white;
    }
}
.v-theme--dark {
    .left-part {
        background: #2b2b2b;
    }
}
.right-part {
    width: 100%;
    min-height: 350px;
    position: relative;
}
</style>
