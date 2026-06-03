<template>
  <PageLayout>
    <div class="device-maintenance">
      <h2>设备维护</h2>
      <p>这里是设备维护模块的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>设备维护功能</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">维护计划</el-button>
          <el-button>维护记录</el-button>
          <el-button>故障处理</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 设备维护组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.device-maintenance {
  padding: 20px;
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
