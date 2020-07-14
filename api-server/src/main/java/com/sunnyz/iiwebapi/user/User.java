package com.sunnyz.iiwebapi.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sunnyz.iiwebapi.base.BaseEntity;
import com.sunnyz.iiwebapi.role.Role;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Entity
@Table(name = "iiweb_user")
@Where(clause = "status < 99")
public class User extends BaseEntity {

    @NotBlank(message = "用户名为空")
    private String name;
    private String realname;

    @JsonIgnore
    @NotBlank(message = "密码为空")
    private String password;

    @Email(message = "邮件格式不正确")
    private String email;

    private String property1;
    private String property2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }


    //用户>角色列表

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "iiweb_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    public Collection<Role> getRoles() {
        return roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
