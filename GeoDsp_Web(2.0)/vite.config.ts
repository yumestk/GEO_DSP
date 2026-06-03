import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // 加载环境变量
  const env = loadEnv(mode, process.cwd(), "");
  console.log("Vite环境变量:", env);

  return {
    plugins: [vue()],
    resolve: {
      alias: {
        "@": resolve(__dirname, "src"),
      },
    },
    server: {
      port: 3000, // 前端端口
      host: true,
      open: true,
      proxy: {
        // 🔥 关键配置：代理设置
        '/api': {
          target: 'http://localhost:9966/geo', // 后端地址 + 上下文路径
          changeOrigin: true, // 允许跨域
          //rewrite: (path) => path.replace(/^\/api\/v1/, '/api') 
          // 解释：
          // 前端请求：/api/v1/user/login
          // 转发给后端：/api/user/login (去掉了 /v1，以匹配您后端的 @RequestMapping("/api/user"))
        }
      }
    },
    // 确保环境变量可用
    define: {
      __APP_ENV__: JSON.stringify(env.VITE_API_BASE_URL),
    },
  };
});