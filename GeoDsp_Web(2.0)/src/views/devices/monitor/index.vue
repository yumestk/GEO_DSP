<template>
  <PageLayout>
    <div class="device-monitor">
      <h2>设备监控</h2>
      <p>这里是设备监控模块的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>设备监控功能</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">实时监控</el-button>
          <el-button>告警管理</el-button>
          <el-button>监控日志</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 设备监控组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.device-monitor {
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
