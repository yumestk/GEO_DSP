<template>
  <PageLayout>
    <div class="data-display">
      <h2>数据显示</h2>
      <p>这里是数据显示功能的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>数据显示选项</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">图表显示</el-button>
          <el-button>表格显示</el-button>
          <el-button>地图显示</el-button>
          <el-button>实时数据</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 数据显示组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.data-display {
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
