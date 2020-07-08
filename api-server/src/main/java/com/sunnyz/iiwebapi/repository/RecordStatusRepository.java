package com.sunnyz.iiwebapi.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RecordStatusRepository<ID> {

    @Query(value = "update #{#entityName} e set e.status = 99 ,last_modified_by = ?#{principal.username},last_modified_date=current_timestamp where id = ?1")
    @Modifying
    @Transactional
    int softDelete(ID id);

    @Query(value = "update #{#entityName} e set e.status = 99 ,last_modified_by = ?#{principal.username},last_modified_date=current_timestamp where id in ?1")
    @Modifying
    @Transactional
    int softDelete(ID[] ids);

    @Query(value = "update #{#entityName} e set e.status = ?2 ,last_modified_by = ?#{principal.username},last_modified_date=current_timestamp where id = ?1")
    @Modifying
    @Transactional
    int setStatus(ID id, int to);

    @Query(value = "update #{#entityName} e set e.status = ?2 ,last_modified_by = ?#{principal.username},last_modified_date=current_timestamp where id in ?1")
    @Modifying
    @Transactional
    int setStatus(ID[] ids, int to);
}
