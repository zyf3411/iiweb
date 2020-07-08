package com.sunnyz.iiwebapi;

import com.alibaba.fastjson.JSONObject;
import com.sunnyz.iiwebapi.entity.User;
import com.sunnyz.iiwebapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IiwebApiApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void userTest() {

        System.out.println("findById:" + JSONObject.toJSONString(userService.findById(1)));

        User user = new User();
        user.setName("tom");
        user.setPassword("1");
        user.setRealname("汤姆");
        user.setStatus(10);
        System.out.println("insert id:" + userService.save(user).getId());
    }
}
