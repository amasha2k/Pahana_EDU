package org.example.pahanaedu.controller;

import org.example.pahanaedu.Routes;
import org.example.pahanaedu.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:14192")
@RestController
@RequestMapping(Routes.DASHBOARD)
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping(Routes.STATS)
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping(Routes.ACTIVITY)
    public ResponseEntity<?> getActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }
} 