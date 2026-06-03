<template>
  <PageLayout>
    <div class="device-analysis">
      <h2>设备分析</h2>
      <p>这里是设备分析模块的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>设备分析功能</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">性能分析</el-button>
          <el-button>趋势分析</el-button>
          <el-button>报表生成</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 设备分析组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.device-analysis {
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
