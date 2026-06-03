package com.geo.dsp.module.user.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.user.dto.UserAddDTO;
import com.geo.dsp.module.user.dto.UserUpdateDTO;
import com.geo.dsp.module.user.entity.User;
import com.geo.dsp.module.user.vo.UserListVO;
import com.geo.dsp.common.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * 用户服务层接口
 */
public interface UserService {

    // 新增用户（含角色关联）
    Result<Boolean> addUser(UserAddDTO dto, Long createBy);

    /**
     * 根据 UUID 删除用户（逻辑删除 + 角色关联删除）
     * @param uuid 用户 UUID
     * @return 删除结果
     */
    Result<Boolean> deleteUser(String uuid);

    /**
     * 修改用户（含角色）
     */
    Result<Boolean> updateUser(UserUpdateDTO dto, Long updateBy);

    /**
     * 分页查询用户列表（前端表格用）
     * @param userName 用户名（模糊搜索）
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageVo<UserListVO> getUserPage(String userName, Short status, Integer pageNum, Integer pageSize);

    // 修改用户
    Result<Boolean> updateUser(User user, Long updateBy);

    // 根据ID查询
    Result<User> findById(Long id);

    // 根据UUID查询
    Result<User> findByUuid(String uuid);

    // 根据账号查询
    Result<User> findByAccount(String account);

    // 分页查询
    Result<Map<String, Object>> pageList(
            Long companyId, String userName, Short status,
            Integer pageNum, Integer pageSize
    );

    // 重置密码
    Result<Boolean> resetPwd(Long id, Long updateBy);

    // 修改状态（用UUID）
    Result<Boolean> updateStatus(String uuid, Short status, Long updateBy);

    // 根据用户UUID获取其权限码列表（前端动态控件用）
    Result<List<String>> getPermissionCodesByUserUuid(String userUuid);
}