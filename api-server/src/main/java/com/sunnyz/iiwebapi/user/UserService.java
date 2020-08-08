package com.sunnyz.iiwebapi.user;


import com.sunnyz.iiwebapi.base.BaseService;
import com.sunnyz.iiwebapi.util.orm.CommonSpecification;
import com.sunnyz.iiwebapi.util.orm.Query;
import com.sunnyz.iiwebapi.util.orm.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends BaseService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.softDelete(id);
    }


    public List<User> findAll(List<Query> queries) {
        return userRepository.findAll(new CommonSpecification<>(queries));
    }

    public Page<User> findAll(List<Query> queries, Integer page, Integer size, Map<String, SortType> sorts) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, this.getSort(sorts));
        return userRepository.findAll(new CommonSpecification<>(queries), pageRequest);
    }
}
