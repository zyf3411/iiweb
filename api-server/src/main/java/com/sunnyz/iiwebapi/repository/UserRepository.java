package com.sunnyz.iiwebapi.repository;


import com.sunnyz.iiwebapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>, RecordStatusRepository {
    Optional<User> findByName(String name);
}
