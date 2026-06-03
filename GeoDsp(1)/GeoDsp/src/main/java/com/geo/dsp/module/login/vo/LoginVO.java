package com.geo.dsp.module.login.vo;

import com.geo.dsp.module.privilege.vo.UserPermissionVO;
import lombok.Data;
import java.util.List;

@Data
public class LoginVO {

    private String token;

    private UserInfo userInfo;

    private List<UserPermissionVO> permissions;

    private List<?> menus;

    @Data
    public static class UserInfo {
        private Long id;
        private String uuid;
        private String account;
        private String username;
        private String nickname;
        private String email;
        private String phone;
        private String phoneNum;
        private String avatar;
        private Short userType;
        private Long companyId;
        private List<String> roles;
        private List<String> permissions;
    }
}
