package com.geo.dsp.module.user.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.privilege.entity.UserRole;
import com.geo.dsp.module.privilege.mapper.RoleMapper;
import com.geo.dsp.module.privilege.mapper.RolePermisMapper;
import com.geo.dsp.module.privilege.mapper.UserRoleMapper;
import com.geo.dsp.module.user.dto.UserAddDTO;
import com.geo.dsp.module.user.dto.UserUpdateDTO;
import com.geo.dsp.module.user.entity.User;
import com.geo.dsp.module.user.mapper.UserMapper;
import com.geo.dsp.module.user.service.UserService;
import com.geo.dsp.module.user.vo.UserListVO;
import com.geo.dsp.common.vo.PageVo;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 用户服务层实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermisMapper rolePermisMapper;

    // 密码加密
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final String DEFAULT_PWD = "123456";


    // 新增用户（含角色关联）
    @Override
    public Result<Boolean> addUser(UserAddDTO dto, Long createBy) {
        // 1. 生成用户UUID
        // String Uuid = UUID.randomUUID().toString();

        // 2. 构建用户实体
        User user = new User();
        // user.setUuid(Uuid);
        user.setUserName(dto.getUserName());
        user.setAccount(dto.getAccount());
        user.setPassword(encoder.encode(dto.getPassword())); // 密码加密
        user.setEmail(dto.getEmail());
        user.setPhoneNum(dto.getPhoneNum());
        user.setUserType((short) 3);  // 默认启用
        user.setStatus((short) 1); // 默认启用
        user.setCreateBy(createBy);
        user.setCreateTime(OffsetDateTime.now());
        user.setUpdateBy(createBy);
        user.setUpdateTime(OffsetDateTime.now());
        user.setIsDel(false);
        user.setSex(dto.getSex());

        // 3. 插入用户
        int userRows = userMapper.insertUser(user);
        if (userRows <= 0) {
            return Result.fail("新增用户失败");
        }

        // 3.2 获取数据库自动生成的 UUID
        User insertedUser = userMapper.selectByAccount(dto.getAccount());
        String userUuid = insertedUser.getUuid();

        // 4. 构建用户角色关联
        UserRole userRole = new UserRole();
        userRole.setUserUuid(userUuid);
        userRole.setRoleCode(dto.getRoleCode());
        userRole.setRoleName(dto.getRoleName());
        userRole.setRoleDesc(dto.getRoleDesc());

        // 5. 插入角色关联时调用 UserRoleMapper
        int roleRows = userRoleMapper.insertUserRole(userRole);
        if (roleRows <= 0) {
            throw new RuntimeException("新增用户角色关联失败");
        }

        return Result.success(true);
    }


    /**
     * 删除用户（事务保证：用户逻辑删除 + 角色关联删除）
     * @param uuid 用户 UUID
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务：失败则回滚
    public Result<Boolean> deleteUser(String uuid) {
        // 1. 逻辑删除用户（更新 is_del = true）
        int userRows = userMapper.deleteByUuid(uuid);
        if (userRows <= 0) {
            return Result.fail("删除用户失败，用户不存在或已删除");
        }

        // 2. 删除用户角色关联（物理删除，因为用户已逻辑删除）
        int roleRows = userRoleMapper.deleteByUserUuid(uuid);
        // 角色关联可能不存在，所以不判断行数，直接继续

        // 3. 返回成功结果
        return Result.success(true);
    }

    /**
     * 修改用户（事务保证：用户信息 + 角色信息同时更新）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateUser(UserUpdateDTO dto, Long updateBy) {
        String uuid = dto.getUuid();

        // 1. 构建用户修改实体
        User user = new User();
        user.setUuid(uuid);
        user.setUserName(dto.getUserName());
        user.setAccount(dto.getAccount());
        user.setEmail(dto.getEmail());
        user.setPhoneNum(dto.getPhoneNum());
        user.setStatus(dto.getStatus());
        user.setUpdateBy(updateBy);
        user.setUpdateTime(OffsetDateTime.now());
        user.setSex(dto.getSex());

        // 2. 执行修改
        int rows = userMapper.updateByUuid(user);
        if (rows <= 0) {
            return Result.fail("用户不存在或已删除");
        }

        // 3. 删除旧角色
        userRoleMapper.deleteByUserUuid(uuid);

        // 4. 插入新角色
        UserRole userRole = new UserRole();
        userRole.setUserUuid(uuid);
        userRole.setRoleCode(dto.getRoleCode());
        userRole.setRoleName(dto.getRoleName());
        userRole.setRoleDesc(dto.getRoleDesc());
        userRoleMapper.insertUserRole(userRole);

        return Result.success(true);
    }












    //*****************************************************************************//



    @Override
    public Result<Boolean> updateUser(User user, Long updateBy) {
        user.setUpdateBy(updateBy);
        int rows = userMapper.update(user);
        return Result.success(rows > 0);
    }

    @Override
    public Result<User> findById(Long id) {
        return Result.success(userMapper.selectById(id));
    }

    @Override
    public Result<User> findByUuid(String uuid) {
        return Result.success(userMapper.selectByUuid(uuid));
    }

    @Override
    public Result<User> findByAccount(String account) {
        return Result.success(userMapper.selectByAccount(account));
    }

    @Override
    public Result<Map<String, Object>> pageList(
            Long companyId, String userName, Short status,
            Integer pageNum, Integer pageSize
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", new ArrayList<>());
        map.put("total", 0L);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        return Result.success(map);
    }

    @Override
    public Result<Boolean> resetPwd(Long id, Long updateBy) {
        User user = new User();
        user.setId(id);
        user.setPassword(encoder.encode(DEFAULT_PWD));
        user.setUpdateBy(updateBy);
        int rows = userMapper.update(user);
        return Result.success(rows > 0);
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Short status, Long updateBy) {
        User user = new User();
        user.setUuid(uuid);
        user.setStatus(status);
        user.setUpdateBy(updateBy);
        user.setUpdateTime(OffsetDateTime.now());
        int rows = userMapper.updateByUuid(user);
        return Result.success(rows > 0);
    }

    @Override
    public PageVo<UserListVO> getUserPage(String userName, Short status, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<UserListVO> records = userMapper.selectUserPage(userName, status, offset, pageSize);
        long total = userMapper.selectUserCount(userName, status);
        PageVo<UserListVO> pageVo = new PageVo<>();
        pageVo.setRecords(records);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }

    @Override
    public Result<List<String>> getPermissionCodesByUserUuid(String userUuid) {
        // 1. 查用户的 roleCode
        String roleCode = userRoleMapper.selectRoleCodeByUserUuid(userUuid);
        if (roleCode == null) return Result.success(Collections.emptyList());

        // 2. 用 roleCode 查 roleId
        Long roleId = roleMapper.selectIdByRoleCode(roleCode);
        if (roleId == null) return Result.success(Collections.emptyList());

        // 3. 用 roleId 查权限码列表
        List<String> codes = rolePermisMapper.selectPermissionCodesByRoleId(roleId);
        return Result.success(codes);
    }
}