package com.sunnyz.iiwebapi.role;


import com.sunnyz.iiwebapi.base.RecordStatusRepository;
import com.sunnyz.iiwebapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<User>, RecordStatusRepository {

    Optional<Role> findByName(String name);

}
