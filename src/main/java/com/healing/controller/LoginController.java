package com.healing.controller;

import com.healing.entity.User;
import com.healing.repository.UserRepository;
import com.healing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.login(username, password);
        if (userOpt.isPresent()) {
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("userId", userOpt.get().getId());
            result.put("username", username);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.register(username, password);
        if (user != null) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "用户名已存在");
        }
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestParam Long userId,
                                          @RequestParam(required = false) String username,
                                          @RequestParam(required = false) String avatarUrl) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        User user = userOpt.get();
        if (username != null && !username.trim().isEmpty()) {
            Optional<User> existing = userRepository.findByUsername(username);
            if (existing.isPresent() && !existing.get().getId().equals(userId)) {
                result.put("success", false);
                result.put("message", "用户名已存在");
                return result;
            }
            user.setUsername(username);
        }
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            user.setAvatarUrl(avatarUrl);
        }
        userRepository.save(user);
        result.put("success", true);
        result.put("message", "更新成功");
        result.put("username", user.getUsername());
        result.put("avatarUrl", user.getAvatarUrl());
        return result;
    }

    // ========== 新增接口 ==========
    @GetMapping("/info")
    public Map<String, Object> getUserInfo(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        User user = userOpt.get();
        result.put("success", true);
        result.put("username", user.getUsername());
        result.put("avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
        result.put("role", user.getRole() != null ? user.getRole() : "user");
        return result;
    }

    @PutMapping("/updateRole")
    public Map<String, Object> updateRole(@RequestParam Long userId, @RequestParam String role) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        User user = userOpt.get();
        user.setRole(role);
        userRepository.save(user);
        result.put("success", true);
        result.put("message", "身份更新成功");
        return result;
    }
}