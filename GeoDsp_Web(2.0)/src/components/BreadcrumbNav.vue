<template>
  <div class="breadcrumb-nav">
    <div class="nav-container">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item
            v-for="(item, index) in breadcrumbList"
            :key="index"
            :to="item.path"
          >
            {{ item.title }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useRoute } from "vue-router";
import { usePermissionStore } from "@/stores/modules/permission";
import { storeToRefs } from "pinia";

interface BreadcrumbItem {
  title: string;
  path?: string;
}

const route = useRoute();
const permissionStore = usePermissionStore();
const { menus } = storeToRefs(permissionStore);

/**
 * 根据当前路径从菜单树中递归查找面包屑
 * @param path 当前路由路径，如 "/system/user"
 * @param menuList 菜单树数据
 * @param parents 父级菜单链（递归时传递）
 * @returns 面包屑数组
 */
const findBreadcrumbByPath = (
  path: string,
  menuList: any[],
  parents: any[] = []
): BreadcrumbItem[] => {
  for (const menu of menuList) {
    // 生成当前菜单的路由路径（和 dynamic.ts 中的规则一致）
    const menuPath = `/${menu.permissionCode.replace(/:/g, '/')}`;
    
    // 如果当前菜单的路径匹配，返回完整的父级链 + 当前菜单
    if (menuPath === path) {
      return [...parents, { title: menu.permissionName, path: menuPath }];
    }
    
    // 如果有子菜单，递归查找
    if (menu.children && menu.children.length) {
      const result = findBreadcrumbByPath(path, menu.children, [
        ...parents,
        { title: menu.permissionName, path: menuPath }
      ]);
      if (result.length) {
        return result;
      }
    }
  }
  return [];
};

// 面包屑列表 - 从菜单树中动态生成
const breadcrumbList = computed<BreadcrumbItem[]>(() => {
  const currentPath = route.path;
  
  // 从菜单树中查找完整的面包屑路径
  const breadcrumbs = findBreadcrumbByPath(currentPath, menus.value);
  
  // 如果没找到（比如访问的是非动态路由页面），回退到路由 meta
  if (breadcrumbs.length === 0) {
    const matched = route.matched.filter((item) => item.meta && item.meta.title);
    return matched.map((item, index) => ({
      title: item.meta.title as string,
      path: index === matched.length - 1 ? undefined : item.path,
    }));
  }
  
  // 最后一个面包屑不可点击（添加安全判断，防止 undefined）
  const lastIndex = breadcrumbs.length - 1;
  if (lastIndex >= 0 && breadcrumbs[lastIndex]) {
    breadcrumbs[lastIndex].path = undefined;
  }
  
  return breadcrumbs;
});
</script>

<style scoped>
/* 面包屑导航主容器 */
.breadcrumb-nav {
  width: 100%;
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0;
  height: 50px;
  flex-shrink: 0;
  z-index: 10;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 导航容器 */
.nav-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

/* 面包屑导航区域 */
.breadcrumb {
  flex: 0 0 auto;
  height: 50px;
  line-height: 50px;
  padding: 0 20px;
}

/* 面包屑项 - 放大字体 */
:deep(.el-breadcrumb__item) {
  font-size: 20px;
  line-height: 50px;
}

:deep(.el-breadcrumb__inner) {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
}

:deep(.el-breadcrumb__separator) {
  font-size: 18px;
  margin: 0 6px;
  font-weight: normal;
}

:deep(.el-breadcrumb__inner.is-link:hover) {
  color: #409eff;
}
</style>