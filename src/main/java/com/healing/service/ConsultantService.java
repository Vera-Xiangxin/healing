package com.healing.service;

import com.healing.entity.Consultant;
import com.healing.repository.ConsultantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConsultantService {
    @Autowired
    private ConsultantRepository consultantRepository;

    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }

    public Consultant registerConsultant(Consultant consultant) {
        return consultantRepository.save(consultant);
    }
}