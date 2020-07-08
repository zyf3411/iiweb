package com.sunnyz.iiwebapi.service;


import com.sunnyz.iiwebapi.entity.User;
import com.sunnyz.iiwebapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

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
}
