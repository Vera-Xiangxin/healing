package com.healing.controller;

import com.healing.entity.Diary;
import com.healing.entity.User;
import com.healing.service.DiaryService;
import com.healing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/diary")
@CrossOrigin(origins = "*")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;
    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public Map<String, Object> saveDiary(@RequestParam Long userId,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                         @RequestParam String content) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        Diary diary = diaryService.saveDiary(userOpt.get(), date, content);
        result.put("success", true);
        result.put("data", diary);
        return result;
    }

    @GetMapping("/get")
    public Map<String, Object> getDiary(@RequestParam Long userId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        Optional<Diary> diary = diaryService.getDiary(userOpt.get(), date);
        result.put("success", true);
        result.put("data", diary.orElse(null));
        return result;
    }
}