package com.healing.controller;

import com.healing.entity.Consultant;
import com.healing.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultant")
@CrossOrigin(origins = "*")
public class ConsultantController {

    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/list")
    public Map<String, Object> listConsultants() {
        Map<String, Object> result = new HashMap<>();
        List<Consultant> list = consultantService.getAllConsultants();
        result.put("success", true);
        result.put("data", list);
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> registerConsultant(@RequestBody Consultant consultant) {
        Map<String, Object> result = new HashMap<>();
        Consultant saved = consultantService.registerConsultant(consultant);
        result.put("success", true);
        result.put("data", saved);
        return result;
    }
}