package com.sunnyz.iiwebapi.user;

import com.sunnyz.iiwebapi.base.*;
import com.sunnyz.iiwebapi.util.orm.QueryBuilder;
import com.sunnyz.iiwebapi.util.orm.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public AjaxResponse get(@PathVariable("id") Integer id) {
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
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setEmail(userModel.getEmail());
        user.setProperty1(userModel.getProperty1());
        user.setProperty2(userModel.getProperty2());
        this.userService.save(user);
        return AjaxResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/del")
    public AjaxResponse del(@RequestParam(value = "ids[]") Integer[] ids) {
        for (Integer id : ids) {
            userService.delete(id);
        }
        return AjaxResponse.success();
    }


    //region jpa 构建动态查询

    @GetMapping("/list")
    public AjaxResponse list() {
        //构造查询表达式
        QueryBuilder queryBuilder = new QueryBuilder().setEqual("name", "jack");

        return AjaxResponse.success(userService.findAll(queryBuilder.getQueries()));
    }

    @GetMapping("/listP")
    public AjaxResponse listP(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                              @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
                              UserRequest request) {

        //region search parameter
        QueryBuilder queryBuilder = new QueryBuilder()
                .setLike("name", request.getName())
                .setLike("realname", request.getRealname())
                .setGreatThanEqual("createDate", request.getDataFrom())
                .setLessThanEqual("createDate", request.getDataTo());
        //endregion

        //sort[可选择]
        Map<String, SortType> sorts = new HashMap<>();
        sorts.put("status", SortType.desc);

        Page<User> users = userService.findAll(queryBuilder.getQueries(), page, size, sorts);

        return AjaxResponse.success(new PageDto(page, users.getTotalElements(), users.getContent()));
    }

    public class UserRequest extends User {

        private String dataFrom;
        private String dataTo;

        public String getDataFrom() {
            return dataFrom;
        }

        public void setDataFrom(String dataFrom) {
            this.dataFrom = dataFrom;
        }

        public String getDataTo() {
            return dataTo;
        }

        public void setDataTo(String dataTo) {
            this.dataTo = dataTo;
        }
    }

    //endregion
}
