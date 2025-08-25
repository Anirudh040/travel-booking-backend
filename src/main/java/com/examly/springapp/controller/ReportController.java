package com.examly.springapp.controller;

import com.examly.springapp.dto.ReportDTOs;
import com.examly.springapp.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service){ this.service = service; }

    @GetMapping("/summary")
    public ResponseEntity<ReportDTOs.Summary> summary(){
        return ResponseEntity.ok(service.summary());
    }

    @GetMapping("/trend")
    public ResponseEntity<List<ReportDTOs.TrendPoint>> trend(@RequestParam(required=false) String from,
                                                             @RequestParam(required=false) String to){
        LocalDate f = (from!=null && !from.isBlank()) ? LocalDate.parse(from) : null;
        LocalDate t = (to!=null && !to.isBlank()) ? LocalDate.parse(to) : null;
        return ResponseEntity.ok(service.trend(f, t));
    }
}
