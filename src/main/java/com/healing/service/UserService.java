package com.healing.service;

import com.healing.entity.User;
import com.healing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 登录验证，返回 Optional<User>
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    // 根据ID查找用户
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 注册新用户，返回 User 对象（可能为 null）
    public User register(String username, String password) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            return null;
        }
        User user = new User(username, password);
        return userRepository.save(user);
    }
}