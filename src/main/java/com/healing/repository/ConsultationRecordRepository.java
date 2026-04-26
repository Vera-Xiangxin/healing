package com.healing.repository;

import com.healing.entity.ConsultationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsultationRecordRepository extends JpaRepository<ConsultationRecord, Long> {
    List<ConsultationRecord> findByUser_IdAndStatus(Long userId, String status);
}