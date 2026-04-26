package com.healing.controller;

import com.healing.entity.MoodRecord;
import com.healing.entity.User;
import com.healing.service.MoodService;
import com.healing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/mood")
@CrossOrigin(origins = "*")
public class MoodController {

    @Autowired
    private MoodService moodService;

    @Autowired
    private UserService userService;

    // 保存心情
    @PostMapping("/save")
    public Map<String, Object> saveMood(@RequestParam Long userId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                        @RequestParam String emoji,
                                        @RequestParam(required = false) String note) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        MoodRecord record = moodService.saveOrUpdateMood(userOpt.get(), date, emoji, note);
        result.put("success", true);
        result.put("data", record);
        return result;
    }

    // 获取日期范围内的心情记录
    @GetMapping("/range")
    public Map<String, Object> getMoodRange(@RequestParam Long userId,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        List<MoodRecord> list = moodService.getMoodRecords(userOpt.get(), start, end);
        result.put("success", true);
        result.put("data", list);
        return result;
    }

    // ==================== 新增接口 ====================
    // 获取今日心情
    @GetMapping("/today")
    public Map<String, Object> getTodayMood(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        LocalDate today = LocalDate.now();
        MoodRecord record = moodService.getMoodRecord(userOpt.get(), today);
        if (record != null) {
            result.put("success", true);
            result.put("emoji", record.getEmoji());
            result.put("text", emojiToText(record.getEmoji()));
        } else {
            result.put("success", true);
            result.put("emoji", "📝");
            result.put("text", "未记录");
        }
        return result;
    }

    // 获取统计数据（累计天数、平均心情分）
    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }
        List<MoodRecord> allRecords = moodService.getAllRecords(userOpt.get());
        long totalDays = allRecords.size();
        double avgScore = allRecords.stream()
                .mapToInt(r -> emojiToScore(r.getEmoji()))
                .average()
                .orElse(5.0);
        result.put("success", true);
        result.put("totalDays", totalDays);
        result.put("avgScore", Math.round(avgScore * 10) / 10.0);
        return result;
    }

    // 获取近7天心情指数（用于图表）
    @GetMapping("/weekly")
    public Map<String, Object> getWeeklyMood(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        List<MoodRecord> records = moodService.getMoodRecords(userOpt.get(), startDate, endDate);

        Map<LocalDate, Integer> dateScoreMap = new HashMap<>();
        for (MoodRecord record : records) {
            dateScoreMap.put(record.getRecordDate(), emojiToScore(record.getEmoji()));
        }

        List<String> dates = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            dates.add(date.getMonthValue() + "/" + date.getDayOfMonth());
            int score = dateScoreMap.getOrDefault(date, 5);
            scores.add(score);
        }

        result.put("success", true);
        result.put("dates", dates);
        result.put("scores", scores);
        return result;
    }

    // 辅助方法：表情转分数
    private int emojiToScore(String emoji) {
        if (emoji == null) return 5;
        switch (emoji) {
            case "🥰": case "😍": case "😇": return 9;
            case "😊": case "😜": return 8;
            case "😐": return 5;
            case "🥺": case "😴": return 3;
            case "😠": case "🤯": return 2;
            default: return 5;
        }
    }

    // 辅助方法：表情转文字
    private String emojiToText(String emoji) {
        if (emoji == null) return "未记录";
        switch (emoji) {
            case "🥰": case "😍": case "😇": return "幸福";
            case "😊": case "😜": return "开心";
            case "😐": return "平静";
            case "🥺": return "难过";
            case "😠": return "生气";
            case "😴": return "疲惫";
            default: return "未记录";
        }
    }
}