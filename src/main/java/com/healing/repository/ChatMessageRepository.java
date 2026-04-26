package com.healing.repository;

import com.healing.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConsultation_IdOrderBySendTimeAsc(Long consultationId);
}