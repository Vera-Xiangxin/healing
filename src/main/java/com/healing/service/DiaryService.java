package com.healing.service;

import com.healing.entity.Diary;
import com.healing.entity.User;
import com.healing.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class DiaryService {
    @Autowired
    private DiaryRepository diaryRepository;

    public Diary saveDiary(User user, LocalDate date, String content) {
        Optional<Diary> existing = diaryRepository.findByUser_IdAndDiaryDate(user.getId(), date);
        if (existing.isPresent()) {
            existing.get().setContent(content);
            return diaryRepository.save(existing.get());
        } else {
            Diary diary = new Diary();
            diary.setUser(user);
            diary.setDiaryDate(date);
            diary.setContent(content);
            return diaryRepository.save(diary);
        }
    }

    public Optional<Diary> getDiary(User user, LocalDate date) {
        return diaryRepository.findByUser_IdAndDiaryDate(user.getId(), date);
    }
}