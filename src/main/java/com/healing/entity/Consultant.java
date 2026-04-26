package com.healing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultant")
@Data
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String school;
    private Integer workYears;
    private String address;
    private String certificate;
    private String idCard;
    private String avatarUrl;
    private Boolean verified = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}