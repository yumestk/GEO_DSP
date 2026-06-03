<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>GeoDsp 系统登录</h2>
        <p>欢迎使用地理数据处理系统</p>
      </div>

      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        label-width="0"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { User, Lock } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import { useUserStore } from "../../stores/modules/user";

const router = useRouter();
const loginFormRef = ref<FormInstance>();
const loading = ref(false);
const userStore = useUserStore(); // 使用用户Store

const loginForm = reactive({
  username: "",
  password: "",
});

const loginRules: FormRules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    {
      min: 3,
      max: 20,
      message: "用户名长度在 3 到 20 个字符",
      trigger: "blur",
    },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在 6 到 20 个字符", trigger: "blur" },
  ],
};

const handleLogin = async () => {
  if (!loginFormRef.value) return;

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      console.log("登录信息:", loginForm);

      try {
        const result = await userStore.login(
          loginForm.username,
          loginForm.password,
        );

        if (result.success) {
          ElMessage.success("登录成功！");

          router.push("/");
        } else {
          ElMessage.error(
            result.error?.message || "登录失败，请检查用户名和密码",
          );
        }
      } catch (error) {
        console.error("登录错误:", error);
        ElMessage.error("登录失败，请稍后重试");
      } finally {
        loading.value = false;
      }
    } else {
      console.log("表单验证失败");
    }
  });
};
</script>

<style scoped>
/* 登录页面主容器 */
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #74b9ff, #0984e3);
}

/* 登录卡片 */
.login-card {
  width: 400px;
  padding: 30px;
  border-radius: 10px;
  background: white;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

/* 登录卡片头部 */
.login-header {
  text-align: center;
  margin-bottom: 30px;
}

/* 登录标题 */
.login-header h2 {
  color: #2d3748;
  margin-bottom: 8px;
}

/* 登录描述文字 */
.login-header p {
  color: #718096;
  font-size: 14px;
}

/* 登录表单 */
.login-form {
  margin-top: 20px;
}

/* 登录按钮 */
.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  letter-spacing: 1px;
}
</style>
