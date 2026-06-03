package com.geo.dsp.module.user.mapper;

import com.geo.dsp.module.privilege.entity.UserRole;
import com.geo.dsp.module.user.entity.User;
import com.geo.dsp.module.user.vo.UserListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.UUID;
import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {


    /**
     * 新增用户
     */
    int insertUser(User user);


    /**
     * 根据 UUID 逻辑删除用户（更新 is_del 为 true）
     * @param uuid 用户 UUID
     * @return 影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);

    /**
     * 根据UUID修改用户信息
     */
    int updateByUuid(User user);

    /**
     * 分页查询用户列表（关联角色表，返回前端需要的字段）
     * @param userName 用户名（模糊搜索，为 null 时查询全部）
     * @param offset 分页偏移量 = (pageNum - 1) * pageSize
     * @param size 每页条数
     * @return 前端表格展示的用户列表 VO
     */
    List<UserListVO> selectUserPage(
            @Param("userName") String userName,
            @Param("status") Short status,
            @Param("offset") int offset,
            @Param("size") int size
    );

    /**
     * 查询用户总数（用于分页）
     * @param userName 用户名（模糊搜索）
     * @return 总数
     */
    long selectUserCount(@Param("userName") String userName, @Param("status") Short status);












    //********************************************************************************//



    /**
     * 修改用户
     */
    int update(User user);

    /**
     * 根据ID查询
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据UUID查询
     */
    User selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据账号查询（登录用）
     */
    User selectByAccount(@Param("account") String account);

    /**
     * 根据手机号查询（登录用）
     */
    User selectByPhoneNum(@Param("phoneNum") String phoneNum);

    /**
     * 分页条件查询
     */
    List<User> selectPage(
            @Param("companyId") Long companyId,
            @Param("userName") String userName,
            @Param("status") Short status,
            @Param("offset") int offset,
            @Param("size") int size
    );

    /**
     * 查询总条数
     */
    long selectCount(
            @Param("companyId") Long companyId,
            @Param("userName") String userName,
            @Param("status") Short status
    );


}