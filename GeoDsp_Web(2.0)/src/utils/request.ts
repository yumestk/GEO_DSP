import axios from "axios";
import type { AxiosError, AxiosResponse } from "axios";

// 后端统一响应格式
export interface Result<T> {
  code: number;
  msg: string;
  data: T;
  total?: number;
}

// 定义自定义的axios实例类型
interface CustomAxiosInstance {
  get<T = any>(url: string, config?: { params?: any }): Promise<Result<T>>;
  post<T = any>(
    url: string,
    data?: any,
    config?: { params?: any },
  ): Promise<Result<T>>;
  put<T = any>(
    url: string,
    data?: any,
    config?: { params?: any },
  ): Promise<Result<T>>;
  delete<T = any>(url: string, config?: { params?: any }): Promise<Result<T>>;
}

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 验证实例配置
console.log("Request实例初始化，baseURL:", axiosInstance.defaults.baseURL);
console.log("环境变量VITE_API_BASE_URL:", import.meta.env.VITE_API_BASE_URL);

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 确保关键参数存在
    if (!config.url) {
      console.error("请求URL为空!");
      throw new Error("请求URL不能为空");
    }

    if (!config.method) {
      config.method = "get";
    }

    // 从localStorage获取token并添加到请求头
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    console.log("发送请求:", {
      url: config.url,
      method: config.method,
      baseURL: config.baseURL,
      fullUrl: `${config.baseURL}${config.url}`,
      params: config.params,
      data: config.data,
    });

    return config;
  },
  (error) => {
    console.error("请求拦截器错误:", error);
    return Promise.reject(error);
  },
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    console.log("收到响应:", {
      status: response.status,
      url: response.config.url,
      method: response.config.method,
      data: response.data,
    });

    // 直接返回响应数据
    return response.data;
  },
  (error: AxiosError) => {
    console.error("网络错误:", error);
    console.error("请求配置:", error.config);
    console.error("响应状态:", error.response?.status);
    console.error("响应数据:", error.response?.data);

    let errorMessage = "网络请求失败";
    if (error.response) {
      switch (error.response.status) {
        case 400:
          errorMessage = "请求参数错误";
          break;
        case 401:
          errorMessage = "未授权，请登录";
          break;
        case 403:
          errorMessage = "拒绝访问";
          break;
        case 404:
          errorMessage = "请求地址不存在";
          break;
        case 500:
          errorMessage = "服务器内部错误";
          break;
        default:
          errorMessage = `请求失败，状态码: ${error.response.status}`;
      }
    } else if (error.request) {
      errorMessage = "网络连接失败，请检查网络或后端服务是否启动";
    } else {
      errorMessage = error.message || "请求失败";
    }

    return Promise.reject(new Error(errorMessage));
  },
);

// 封装请求方法
const request = {
  get: async <T = any>(
    url: string,
    config?: { params?: any },
  ): Promise<Result<T>> => {
    console.log("调用get方法:", url, config?.params);
    try {
      // 由于响应拦截器已经将响应转换为了response.data，所以这里直接返回响应
      const response = await axiosInstance.get(url, config);
      // 使用unknown类型作为中间转换，避免类型检查错误
      return response as unknown as Result<T>;
    } catch (error) {
      console.error("GET请求失败:", error);
      throw error;
    }
  },

  post: async <T = any>(
    url: string,
    data?: any,
    config?: { params?: any },
  ): Promise<Result<T>> => {
    console.log("调用post方法:", url, data, config?.params);
    try {
      const response = await axiosInstance.post(url, data, config);
      return response as unknown as Result<T>;
    } catch (error) {
      console.error("POST请求失败:", error);
      throw error;
    }
  },

  put: async <T = any>(
    url: string,
    data?: any,
    config?: { params?: any },
  ): Promise<Result<T>> => {
    console.log("调用put方法:", url, data, config?.params);
    try {
      const response = await axiosInstance.put(url, data, config);
      return response as unknown as Result<T>;
    } catch (error) {
      console.error("PUT请求失败:", error);
      throw error;
    }
  },

  delete: async <T = any>(
    url: string,
    config?: { params?: any },
  ): Promise<Result<T>> => {
    console.log("调用delete方法:", url, config?.params);
    try {
      const response = await axiosInstance.delete(url, config);
      return response as unknown as Result<T>;
    } catch (error) {
      console.error("DELETE请求失败:", error);
      throw error;
    }
  },
};

export default request;
