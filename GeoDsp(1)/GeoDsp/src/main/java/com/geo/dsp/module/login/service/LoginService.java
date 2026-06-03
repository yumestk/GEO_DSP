package com.geo.dsp.module.login.service;

import com.geo.dsp.module.login.dto.LoginDTO;
import com.geo.dsp.module.login.vo.LoginVO;

public interface LoginService {
    LoginVO login(LoginDTO dto);
    void logout(String token);
    LoginVO.UserInfo getUserInfo(String token);
}
