import request from "../utils/request";

export const api = {
  // ==================== 用户相关API ====================
  user: {
    // 登录
    login: (data: { account: string; password: string }) =>
      request.post("/api/user/login", data),

    // 获取用户信息
    getUserInfo: () => request.get("/api/user/info"),

    // 退出登录
    logout: () => request.post("/api/user/logout"),

    // 用户搜索
    search: (params: {
      userName: string;
      currentPage?: number;
      pageSize?: number;
    }) => request.get("/api/user/search", { params }),

    // 获取用户列表（分页）
    getList: (params: {
      userName?: string;
      status?: number;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/user/list", { params }),

    // 新增用户
    create: (
      data: {
        userName: string;
        account: string;
        password: string;
        sex?: number;
        email: string;
        phoneNum: string;
        companyId?: number;
        userType?: number;
        roleName?: string;
        roleCode?: string;
        roleDesc?: string;
      },
      createBy: number,
    ) => request.post("/api/user/add", data, { params: { createBy } }),

    // 编辑用户
    update: (
      data: {
        uuid: string;
        userName: string;
        account: string;
        sex?: number;
        email?: string;
        phoneNum?: string;
        status?: number;
        companyId?: number;
        userType?: number;
        roleName?: string;
        roleCode?: string;
        roleDesc?: string;
        note?: string;
      },
      updateBy: number,
    ) => request.put("/api/user/update", data, { params: { updateBy } }),

    // 删除用户（单条）
    delete: (userUuid: string, updateBy: number) =>
      request.delete(`/api/user/${userUuid}`, { params: { updateBy } }),

    // 切换用户状态
    toggleStatus: (userUuid: string, status: number, updateBy: number) =>
      request.put(`/api/user/status/${userUuid}?status=${status}&updateBy=${updateBy}`),

    // ========== 批量操作（通用） ==========
    /**
     * 批量操作用户
     * @param uuids 用户UUID数组
     * @param action 操作类型：delete | enable | disable | export
     * @param updateBy 当前操作人ID
     */
    batchOperation: (uuids: string[], action: string, updateBy: number) =>
      request.post("/api/user/batch", { uuids, action, updateBy }),
  },

  // ==================== 角色相关API ====================
  role: {
    // 获取角色列表（分页 + 关键词搜索）
    getList: (params: {
      keyword?: string;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/system/roles/list", { params }),

    // 新增角色
    create: (
      data: {
        roleName: string;
        roleCode: string;
        roleDesc?: string;
        status?: number;
      },
      createBy: number,
    ) => request.post("/api/system/roles/add", data, { params: { createBy } }),

    // 编辑角色
    update: (
      data: {
        uuid: string;
        roleName: string;
        roleCode: string;
        roleDesc?: string;
        status?: number;
      },
      updateBy: number,
    ) => request.put("/api/system/roles/update", data, { params: { updateBy } }),

    // 删除角色（单条）
    delete: (uuid: string) => request.delete(`/api/system/roles/${uuid}`),

    // 获取全部角色（不分页，供下拉框用）
    getAll: () => request.get("/api/system/roles/all"),
  },

  // ==================== 企业管理相关API ====================
  company: {
    // 获取企业列表（分页 + 关键词搜索）
    getList: (params: {
      keyword?: string;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/system/companies/list", { params }),

    // 新增企业
    create: (
      data: {
        companyName: string;
        companyAbbr: string;
        contactName: string;
        contactPhone: string;
        contactEmail?: string;
        address?: string;
        licenseNum: string;
        note?: string;
        status?: number;
      },
      createBy: number,
    ) => request.post("/api/system/companies/add", data, { params: { createBy } }),

    // 编辑企业
    update: (
      data: {
        uuid: string;
        companyName: string;
        companyAbbr: string;
        contactName: string;
        contactPhone: string;
        contactEmail?: string;
        address?: string;
        licenseNum: string;
        note?: string;
        status?: number;
      },
      updateBy: number,
    ) => request.put("/api/system/companies/update", data, { params: { updateBy } }),

    // 删除企业
    delete: (uuid: string) => request.delete(`/api/system/companies/${uuid}`),
  },

  // ==================== 权限管理相关API ====================
  permission: {
    // 获取权限列表（分页 + 关键词搜索）
    getList: (params: {
      keyword?: string;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/system/permissions/list", { params }),

    // 新增权限
    create: (
      data: {
        permissionName: string;
        permissionCode: string;
        permissionDesc?: string;
        permissionType?: string;
        parentPermission?: string;
        status?: string;
      },
      createBy: number,
    ) => request.post("/api/system/permissions/add", data, { params: { createBy } }),

    // 编辑权限
    update: (
      data: {
        uuid: string;
        permissionName: string;
        permissionCode: string;
        permissionDesc?: string;
        permissionType?: string;
        parentPermission?: string;
        status?: string;
      },
      updateBy: number,
    ) => request.put("/api/system/permissions/update", data, { params: { updateBy } }),

    // 删除权限（单条）
    delete: (uuid: string) => request.delete(`/api/system/permissions/${uuid}`),

    // 获取全部权限（不分页，供角色权限分配穿梭框用）
    getAll: () => request.get("/api/system/permissions/all"),

    // 根据角色UUID获取已分配的权限UUID列表
    getByRole: (roleUuid: string) =>
      request.get(`/api/system/permissions/role/${roleUuid}`),

    // ========== 保存角色权限分配 ==========
    /**
     * 保存角色的权限分配
     * @param roleUuid 角色UUID
     * @param permissionCodes 分配的权限编码数组
     * @param updateBy 当前操作人ID（可选）
     */
    saveRolePermissions: (roleUuid: string, permissionCodes: string[], updateBy?: number) =>
      request.post("/api/system/permissions/role/save", { roleUuid, permissionCodes, updateBy }),
  },

  // ==================== 字典管理相关API ====================
  dict: {
    // 获取字典类型列表
    getTypeList: () => request.get("/api/system/dict/type/list"),

    // 新增字典类型
    addType: (
      data: {
        dictName: string;
        dictCode: string;
        dictDesc?: string;
      },
      createBy: number,
    ) => request.post("/api/system/dict/type/add", data, { params: { createBy } }),

    // 编辑字典类型
    updateType: (
      data: {
        uuid: string;
        dictName: string;
        dictCode: string;
        dictDesc?: string;
      },
      updateBy: number,
    ) => request.put("/api/system/dict/type/update", data, { params: { updateBy } }),

    // 根据字典编码查询数据列表
    getDataList: (dictCode: string) =>
      request.get("/api/system/dict/data/list", { params: { dictCode } }),

    // 新增字典数据
    addData: (
      data: {
        dictCode: string;
        dictKey: string;
        dictStrVal?: string;
        dictNumVal?: number;
        dictDesc?: string;
      },
      createBy: number,
    ) => request.post("/api/system/dict/data/add", data, { params: { createBy } }),

    // 编辑字典数据
    updateData: (
      data: {
        uuid: string;
        dictKey: string;
        dictStrVal?: string;
        dictNumVal?: number;
        dictDesc?: string;
      },
      updateBy: number,
    ) => request.put("/api/system/dict/data/update", data, { params: { updateBy } }),

    // 删除字典（类型或数据）
    delete: (uuid: string) => request.delete(`/api/system/dict/delete/${uuid}`),
  },

  // ==================== 操作员相关API ====================
  operator: {
    // 获取操作员列表
    getList: (params: {
      operatorName?: string;
      phoneNum?: string;
      status?: number;
      startDate?: string;
      endDate?: string;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/operator/list", { params }),

    // 新增操作员
    add: (
      data: {
        operatorName: string;
        phoneNum: string;
        status: number;
        startTime: string;
        stopTime: string;
        note?: string;
        userId?: number;
      },
      createBy: number,
    ) => request.post("/api/operator/add", data, { params: { createBy } }),

    // 编辑操作员
    update: (
      data: {
        uuid: string;
        operatorName: string;
        phoneNum: string;
        status: number;
        startTime: string;
        stopTime: string;
        note?: string;
        userId?: number;
      },
      updateBy: number,
    ) => request.put("/api/operator/update", data, { params: { updateBy } }),

    // 删除操作员（单条）
    delete: (uuid: string, updateBy: number) =>
      request.delete(`/api/operator/${uuid}`, { params: { updateBy } }),

    // 切换操作员状态
    toggleStatus: (uuid: string, status: number, updateBy: number) =>
      request.put(`/api/operator/status/${uuid}?status=${status}&updateBy=${updateBy}`),

    // ========== 批量操作（通用） ==========
    /**
     * 批量操作操作员
     * @param uuids 操作员UUID数组
     * @param action 操作类型：delete | enable | disable
     * @param updateBy 当前操作人ID
     */
    batchOperation: (uuids: string[], action: string, updateBy: number) =>
      request.post("/api/operator/batch", { uuids, action, updateBy }),
  },

  // ==================== 设备相关API ====================
  device: {
    // 获取设备类型列表
    getTypeList: (params?: any) => request.get("/api/device/type/list", { params }),

    // 获取设备列表
    getList: (params: {
      deviceNum?: string;
      antennaNum?: string;
      status?: number;
      deviceTypeId?: number;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/device/list", { params }),

    // 新增设备
    add: (data: {
      companyId: number;
      deviceNum: string;
      deviceTypeId: number;
      antennaNum?: number;
      status: number;
      note?: string;
      operateBy?: number;
    }) => request.post("/api/device", data),

    // 编辑设备
    update: (
      uuid: string,
      data: {
        companyId?: number;
        deviceNum?: string;
        deviceTypeId?: number;
        antennaNum?: number;
        status?: number;
        note?: string;
        operateBy?: number;
      },
    ) => request.put(`/api/device/${uuid}`, data),

    // 删除设备（单条）
    delete: (uuid: string, updateBy: number) =>
      request.delete(`/api/device/${uuid}`, { params: { updateBy } }),

    // 切换设备状态
    toggleStatus: (uuid: string, status: number, updateBy: number) =>
      request.put(`/api/device/status/${uuid}?status=${status}&updateBy=${updateBy}`),

    // ========== 批量操作（通用） ==========
    /**
     * 批量操作设备
     * @param uuids 设备UUID数组
     * @param action 操作类型：delete | enable | disable
     * @param updateBy 当前操作人ID
     */
    batchOperation: (uuids: string[], action: string, updateBy: number) =>
      request.post("/api/device/batch", { uuids, action, updateBy }),
  },

  // ==================== 工作参数(天线配置)相关API ====================
  dataWorkset: {
    getList: (params: {
      keyword?: string;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/data-workset/list", { params }),

    getByUuid: (uuid: string) => request.get(`/api/data-workset/${uuid}`),

    add: (data: {
      workConfig?: string;
      sendcoilLen?: number;
      sendcoilWidth?: number;
      sendcoilTurns?: number;
      recvcoilSize?: number;
      recvcoilGain?: number;
      note?: string;
    }) => request.post("/api/data-workset", data),

    update: (uuid: string, data: {
      workConfig?: string;
      sendcoilLen?: number;
      sendcoilWidth?: number;
      sendcoilTurns?: number;
      recvcoilSize?: number;
      recvcoilGain?: number;
      note?: string;
    }) => request.put(`/api/data-workset/${uuid}`, data),

    delete: (uuid: string) => request.delete(`/api/data-workset/${uuid}`),
  },

  // ==================== 项目管理相关API ====================
  project: {
    getList: (params: {
      keyword?: string;
      companyId?: number;
      startDate?: string;
      endDate?: string;
      status?: number;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/project/list", { params }),

    getByUuid: (uuid: string) => request.get(`/api/project/${uuid}`),

    getByCompany: (companyUuid: string) =>
      request.get(`/api/project/company/${companyUuid}`),

    create: (data: {
      companyId: number;
      projectName: string;
      projectCode?: string;
      projectAddress?: string;
      startTime?: string;
      endTime?: string;
      managerId?: number;
      status?: number;
      note?: string;
    }) => request.post("/api/project", data),

    update: (uuid: string, data: {
      projectName?: string;
      projectAddress?: string;
      endTime?: string;
      status?: number;
      note?: string;
    }) => request.put(`/api/project/${uuid}`, data),

    delete: (uuid: string, updateBy?: number) =>
      request.delete(`/api/project/${uuid}`, { params: { updateBy } }),

    updateStatus: (uuid: string, status: number, updateBy?: number) =>
      request.put(`/api/project/status/${uuid}?status=${status}&updateBy=${updateBy}`),
  },

  // ==================== 采集任务相关API ====================
  collectTask: {
    getList: (params: {
      keyword?: string;
      projectUuid?: string;
      status?: number;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/collect-task/list", { params }),

    getByUuid: (uuid: string) => request.get(`/api/collect-task/${uuid}`),

    getByProject: (projectUuid: string) =>
      request.get(`/api/collect-task/project/${projectUuid}`),

    create: (data: {
      projectId?: number;
      taskName: string;
      taskDesc?: string;
      executorId?: number;
      startTime?: string;
      endTime?: string;
      methodId?: number;
      deviceId?: number;
      status?: number;
      note?: string;
    }) => request.post("/api/collect-task", data),

    update: (uuid: string, data: {
      taskName?: string;
      endTime?: string;
      status?: number;
      note?: string;
    }) => request.put(`/api/collect-task/${uuid}`, data),

    delete: (uuid: string, updateBy?: number) =>
      request.delete(`/api/collect-task/${uuid}`, { params: { updateBy } }),

    updateStatus: (uuid: string, status: number, updateBy?: number) =>
      request.put(`/api/collect-task/status/${uuid}?status=${status}&updateBy=${updateBy}`),
  },

  // ==================== 操作方法相关API ====================
  operateMethod: {
    getList: (params?: { keyword?: string; pageNum?: number; pageSize?: number }) =>
      request.get("/api/operate-method/list", { params }),

    getByUuid: (uuid: string) => request.get(`/api/operate-method/${uuid}`),

    create: (data: {
      methodName: string;
      methodCode?: string;
      methodDesc?: string;
    }) => request.post("/api/operate-method", data),

    update: (uuid: string, data: {
      methodName?: string;
      methodCode?: string;
      methodDesc?: string;
    }) => request.put(`/api/operate-method/${uuid}`, data),

    delete: (uuid: string) => request.delete(`/api/operate-method/${uuid}`),
  },

  // ==================== 操作日志相关API ====================
  operationLog: {
    getList: (params: {
      keyword?: string;
      pageNum?: number;
      pageSize?: number;
    }) => request.get("/api/operation-log/list", { params }),

    getByUuid: (uuid: string) => request.get(`/api/operation-log/${uuid}`),

    getByProject: (projectUuid: string) =>
      request.get(`/api/operation-log/project/${projectUuid}`),

    create: (data: {
      projectId?: number;
      engineeringId?: number;
      taskId?: number;
      logDate?: string;
      content?: string;
      creatorId?: number;
    }) => request.post("/api/operation-log", data),

    updateContent: (uuid: string, content: string, updateBy?: number) =>
      request.put(`/api/operation-log/${uuid}/content?content=${content}&updateBy=${updateBy}`),

    delete: (uuid: string, updateBy?: number) =>
      request.delete(`/api/operation-log/${uuid}`, { params: { updateBy } }),
  },
};

export default api;