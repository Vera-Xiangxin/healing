package com.healing.service;

import com.healing.entity.MoodRecord;
import com.healing.entity.User;
import com.healing.repository.MoodRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MoodService {
    @Autowired
    private MoodRecordRepository moodRecordRepository;

    // 保存或更新心情
    public MoodRecord saveOrUpdateMood(User user, LocalDate date, String emoji, String note) {
        MoodRecord existing = moodRecordRepository.findByUser_IdAndRecordDate(user.getId(), date);
        if (existing != null) {
            existing.setEmoji(emoji);
            existing.setNote(note);
            return moodRecordRepository.save(existing);
        } else {
            MoodRecord record = new MoodRecord();
            record.setUser(user);
            record.setRecordDate(date);
            record.setEmoji(emoji);
            record.setNote(note);
            return moodRecordRepository.save(record);
        }
    }

    // 获取日期范围内的心情记录
    public List<MoodRecord> getMoodRecords(User user, LocalDate start, LocalDate end) {
        return moodRecordRepository.findByUser_IdAndRecordDateBetween(user.getId(), start, end);
    }

    // 获取某一天的心情记录
    public MoodRecord getMoodRecord(User user, LocalDate date) {
        return moodRecordRepository.findByUser_IdAndRecordDate(user.getId(), date);
    }

    // 获取用户所有心情记录
    public List<MoodRecord> getAllRecords(User user) {
        return moodRecordRepository.findByUser_Id(user.getId());
    }
}