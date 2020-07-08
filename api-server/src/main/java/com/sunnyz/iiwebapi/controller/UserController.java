package com.sunnyz.iiwebapi.controller;

import com.sunnyz.iiwebapi.base.*;
import com.sunnyz.iiwebapi.entity.User;
import com.sunnyz.iiwebapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/findById")
    public AjaxResponse findById(Integer id) {
        User user = userService.findById(id).orElseThrow(() -> new BizException("用户不存在"));
        return AjaxResponse.success(user);
    }

    @PostMapping("/save")
    public AjaxResponse save(@Validated User userModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult);
        }
        User user = new User();
        //新增
        if (Objects.equals(null, user.getId())) {
            if (this.userService.findByName(userModel.getName()).isPresent()) {
                return AjaxResponse.error("用户名已存在");
            }
            user.setStatus(10);
        } else {
            user = this.userService.findById(userModel.getId()).orElseThrow(() -> new BizException("用户不存在"));
        }
        user.setName(userModel.getName());
        user.setRealname(userModel.getRealname());
        //TODO 加密
        user.setPassword(userModel.getPassword());
        user.setEmail(userModel.getEmail());
        user.setProperty1(userModel.getProperty1());
        user.setProperty2(userModel.getProperty2());
        this.userService.save(user);
        return AjaxResponse.success();
    }

    @PostMapping("/del")
    public AjaxResponse del(@RequestParam(value = "ids[]") Integer[] ids) {
        for (Integer id : ids) {
            userService.delete(id);
        }
        return AjaxResponse.success();
    }
}
