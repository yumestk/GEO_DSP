<template>
  <PageLayout>
    <div class="data-quality">
      <h2>数据质量管理</h2>
      <p>这里是数据质量管理模块的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>数据质量功能</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">质量检测</el-button>
          <el-button>质量报告</el-button>
          <el-button>异常处理</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 数据质量管理组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.data-quality {
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
