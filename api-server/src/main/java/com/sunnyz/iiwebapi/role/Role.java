package com.sunnyz.iiwebapi.role;

import com.sunnyz.iiwebapi.base.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "iiweb_role")
@Where(clause = "status < 99")
public class Role extends BaseEntity {

    @NotBlank(message = "角色名为空")
    private String name;
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
