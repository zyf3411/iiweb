package com.sunnyz.iiwebapi.user;


import com.sunnyz.iiwebapi.base.RecordStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>, RecordStatusRepository {
    Optional<User> findByName(String name);
}
