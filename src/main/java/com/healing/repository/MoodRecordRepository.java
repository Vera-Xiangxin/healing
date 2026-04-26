package com.healing.repository;

import com.healing.entity.MoodRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MoodRecordRepository extends JpaRepository<MoodRecord, Long> {
    List<MoodRecord> findByUser_IdAndRecordDateBetween(Long userId, LocalDate start, LocalDate end);
    MoodRecord findByUser_IdAndRecordDate(Long userId, LocalDate date);
    List<MoodRecord> findByUser_Id(Long userId);
}