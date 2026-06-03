<template>
  <PageLayout>
    <div class="settings-management">
      <h2>配置管理</h2>
      <p>这里是系统设置模块的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>配置管理</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">基本配置</el-button>
          <el-button>安全配置</el-button>
          <el-button>参数配置</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 系统设置组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.settings-management {
  padding: 20px;
  color: black;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 100%;
}
</style>
