package com.healing.repository;

import com.healing.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByUser_IdAndDiaryDate(Long userId, LocalDate date);
}