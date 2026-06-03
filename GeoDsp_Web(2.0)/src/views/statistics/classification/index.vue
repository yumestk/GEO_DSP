<template>
  <PageLayout>
    <div class="classification-statistics">
      <h2>分类统计</h2>
      <p>这里是分类统计功能的内容。</p>

      <el-card class="box-card">
        <template #header>
          <div class="card-header">
            <span>分类统计选项</span>
          </div>
        </template>
        <div class="item">
          <el-button type="primary">按类型统计</el-button>
          <el-button>按区域统计</el-button>
          <el-button>按时间统计</el-button>
          <el-button>自定义统计</el-button>
        </div>
      </el-card>
    </div>
  </PageLayout>
</template>

<script setup lang="ts">
// 分类统计组件
import PageLayout from "@/components/PageLayout.vue";
</script>

<style scoped>
.classification-statistics {
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
