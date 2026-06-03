<template>
  <div class="page-layout">
    <!-- 公共导航栏 -->
    <BreadcrumbNav />

    <!-- 页面内容 -->
    <div class="page-content">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineAsyncComponent } from 'vue';
const BreadcrumbNav = defineAsyncComponent(() => import('./BreadcrumbNav.vue'));
</script>

<style scoped>
.page-layout {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.page-content {
  flex: 1;
  padding: 0;
  background-color: #ffffff;
  overflow-y: auto;
  min-height: 0;
  margin-top: 0;
  max-height: calc(100vh - 100px);  /* 限制最大高度，超出滚动 */
}
</style>