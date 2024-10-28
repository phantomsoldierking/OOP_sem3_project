package com.example.scheduler.controller;

import com.example.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/create")
    public ResponseEntity<String> createSchedule() {
        String result = scheduleService.assignSchedule();
        return ResponseEntity.ok(result);
    }
}
