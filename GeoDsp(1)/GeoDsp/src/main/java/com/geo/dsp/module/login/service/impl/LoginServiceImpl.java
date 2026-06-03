package com.geo.dsp.module.login.service.impl;

import com.geo.dsp.common.util.JwtUtil;
import com.geo.dsp.module.login.dto.LoginDTO;
import com.geo.dsp.module.login.service.LoginService;
import com.geo.dsp.module.login.service.SmsService;
import com.geo.dsp.module.login.vo.LoginVO;
import com.geo.dsp.module.privilege.mapper.PermissionMapper;
import com.geo.dsp.module.privilege.mapper.RoleMapper;
import com.geo.dsp.module.privilege.mapper.RolePermisMapper;
import com.geo.dsp.module.privilege.mapper.UserRoleMapper;
import com.geo.dsp.module.privilege.vo.UserPermissionVO;
import com.geo.dsp.module.user.entity.User;
import com.geo.dsp.module.user.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private static final int LOGIN_TYPE_PASSWORD = 1;
    private static final int LOGIN_TYPE_SMS = 2;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermisMapper rolePermisMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SmsService smsService;

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public LoginVO login(LoginDTO dto) {
        User user;

        if (dto.getLoginType() == null || dto.getLoginType() == LOGIN_TYPE_PASSWORD) {
            user = loginByPassword(dto.getAccount(), dto.getPassword());
        } else if (dto.getLoginType() == LOGIN_TYPE_SMS) {
            user = loginBySmsCode(dto.getAccount(), dto.getSmsCode());
        } else {
            throw new RuntimeException("登录类型错误");
        }

        List<String> authorities = getUserAuthorities(user.getUuid());

        String token = jwtUtil.generateToken(
                user.getId(), user.getAccount(),
                user.getUserType() == null ? 3 : user.getUserType().intValue(),
                authorities);

        List<UserPermissionVO> permissionList = permissionMapper.selectUserPermissionsByUserUuid(user.getUuid());

        List<String> permissionCodes = permissionList.stream()
                .map(UserPermissionVO::getPermissionCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Map<String, Object>> menus = buildMenuTree(permissionList);

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setPermissions(permissionList);
        vo.setMenus(menus);

        String roleCode = userRoleMapper.selectRoleCodeByUserUuid(user.getUuid());
        List<String> roles = roleCode != null ? Collections.singletonList(roleCode) : Collections.emptyList();

        LoginVO.UserInfo userInfo = new LoginVO.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUuid(user.getUuid());
        userInfo.setAccount(user.getAccount());
        userInfo.setUsername(user.getUserName());
        userInfo.setNickname(user.getUserName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhoneNum());
        userInfo.setPhoneNum(user.getPhoneNum());
        userInfo.setAvatar("");
        userInfo.setUserType(user.getUserType());
        userInfo.setCompanyId(user.getCompanyId());
        userInfo.setRoles(roles);
        userInfo.setPermissions(permissionCodes);
        vo.setUserInfo(userInfo);

        log.info("用户 {} 登录成功", user.getAccount());
        return vo;
    }

    @Override
    public LoginVO.UserInfo getUserInfo(String token) {
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new RuntimeException("无效的token");
        }
        String account = jwtUtil.getAccountFromToken(token);
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        List<UserPermissionVO> permissionList = permissionMapper.selectUserPermissionsByUserUuid(user.getUuid());

        List<String> permissionCodes = permissionList.stream()
                .map(UserPermissionVO::getPermissionCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        String roleCode = userRoleMapper.selectRoleCodeByUserUuid(user.getUuid());
        List<String> roles = roleCode != null ? Collections.singletonList(roleCode) : Collections.emptyList();

        LoginVO.UserInfo userInfo = new LoginVO.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUuid(user.getUuid());
        userInfo.setAccount(user.getAccount());
        userInfo.setUsername(user.getUserName());
        userInfo.setNickname(user.getUserName());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhoneNum());
        userInfo.setPhoneNum(user.getPhoneNum());
        userInfo.setAvatar("");
        userInfo.setUserType(user.getUserType());
        userInfo.setCompanyId(user.getCompanyId());
        userInfo.setRoles(roles);
        userInfo.setPermissions(permissionCodes);
        return userInfo;
    }

    private User loginByPassword(String account, String password) {
        if (account == null || account.isEmpty()) {
            throw new RuntimeException("账号不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("密码不能为空");
        }

        User user;
        if (isPhoneNumber(account)) {
            user = userMapper.selectByPhoneNum(account);
        } else {
            user = userMapper.selectByAccount(account);
        }

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("用户已禁用");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    private User loginBySmsCode(String phoneNum, String smsCode) {
        if (phoneNum == null || phoneNum.isEmpty()) {
            throw new RuntimeException("手机号不能为空");
        }
        if (smsCode == null || smsCode.isEmpty()) {
            throw new RuntimeException("验证码不能为空");
        }

        User user = userMapper.selectByPhoneNum(phoneNum);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("用户已禁用");
        }

        if (!smsService.verifyCode(phoneNum, smsCode)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        return user;
    }

    private boolean isPhoneNumber(String str) {
        return str != null && str.matches("^1[3-9]\\d{9}$");
    }

    @Override
    public void logout(String token) {
        log.info("用户登出");
    }

    private List<String> getUserAuthorities(String userUuid) {
        String roleCode = userRoleMapper.selectRoleCodeByUserUuid(userUuid);
        if (roleCode == null) return Collections.emptyList();
        Long roleId = roleMapper.selectIdByRoleCode(roleCode);
        if (roleId == null) return Collections.emptyList();
        List<String> codes = rolePermisMapper.selectPermissionCodesByRoleId(roleId);
        return codes == null ? Collections.emptyList() : codes;
    }

    private List<Map<String, Object>> buildMenuTree(List<UserPermissionVO> permissionList) {
        if (permissionList == null || permissionList.isEmpty()) return Collections.emptyList();

        Map<String, List<UserPermissionVO>> childrenMap = new HashMap<>();
        List<UserPermissionVO> roots = new ArrayList<>();

        for (UserPermissionVO p : permissionList) {
            if (p.getParentPermission() == null || p.getParentPermission().isEmpty()) {
                roots.add(p);
            } else {
                childrenMap.computeIfAbsent(p.getParentPermission(), k -> new ArrayList<>()).add(p);
            }
        }

        List<Map<String, Object>> tree = new ArrayList<>();
        for (UserPermissionVO root : roots) {
            tree.add(buildNode(root, childrenMap));
        }
        return tree;
    }

    private Map<String, Object> buildNode(UserPermissionVO p, Map<String, List<UserPermissionVO>> childrenMap) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("permissionCode", p.getPermissionCode());
        node.put("permissionName", p.getPermissionName());
        node.put("permissionType", p.getPermissionType());
        node.put("componentPath", p.getComponentPath());
        node.put("parentPermission", p.getParentPermission());

        List<UserPermissionVO> children = childrenMap.get(p.getUuid());
        if (children != null && !children.isEmpty()) {
            List<Map<String, Object>> childList = new ArrayList<>();
            for (UserPermissionVO child : children) {
                childList.add(buildNode(child, childrenMap));
            }
            node.put("children", childList);
        }
        return node;
    }
}
