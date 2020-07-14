package com.sunnyz.iiwebapi.role;


import com.sunnyz.iiwebapi.base.AjaxResponse;
import com.sunnyz.iiwebapi.base.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/role")
@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/{id}")
    public AjaxResponse get(@PathVariable("id") Integer id) {
        Role role = roleService.findById(id).orElseThrow(() -> new BizException("角色不存在"));
        return AjaxResponse.success(role);
    }

    @PostMapping("/save")
    public AjaxResponse save(@Validated Role roleModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult);
        }
        Role role = new Role();
        //新增
        if (Objects.equals(null, roleModel.getId())) {
            if (this.roleService.findByName(roleModel.getName()).isPresent()) {
                return AjaxResponse.error("角色名已存在");
            }
            role.setStatus(10);
        } else {
            role = this.roleService.findById(roleModel.getId()).orElseThrow(() -> new BizException("角色不存在"));
        }
        role.setName(roleModel.getName());
        role.setDescription(roleModel.getDescription());

        this.roleService.save(role);
        return AjaxResponse.success();
    }

    @PostMapping("/del")
    public AjaxResponse del(@RequestParam(value = "ids[]") Integer[] ids) {
        for (Integer id : ids) {
            roleService.delete(id);
        }
        return AjaxResponse.success();
    }

}
