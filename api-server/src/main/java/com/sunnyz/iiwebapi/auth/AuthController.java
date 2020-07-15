package com.sunnyz.iiwebapi.auth;


import com.alibaba.fastjson.JSONObject;
import com.sunnyz.iiwebapi.base.AjaxResponse;
import com.sunnyz.iiwebapi.base.BizException;
import com.sunnyz.iiwebapi.user.User;
import com.sunnyz.iiwebapi.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public AjaxResponse login(String username, String password) {
        logger.info("用户[{}] 登陆了", username);

        User user = userService.findByName(username).orElseThrow(() -> new BizException("用户不存在"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException("用户名/密码错误");
        }
        String token = jwtTokenUtil.generateToken(new JwtUser(user.getId(), user.getName()));
        return AjaxResponse.success(new JSONObject().fluentPut("token", token));
    }
}
