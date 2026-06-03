<script setup lang="ts">
// 应用程序根组件
</script>

<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style>
/* 全局样式 */
html,
body {
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

#app {
  height: 100vh;
  width: 100vw;
  overflow: auto;
}
</style>
