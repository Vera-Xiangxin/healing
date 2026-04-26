package com.healing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private ConsultationRecord consultation;

    private String senderRole;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime sendTime = LocalDateTime.now();
}