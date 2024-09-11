package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.dto.dashboard.DashboardStatisticsDTO;
import com.bookwise.bookwise.service.IDashboardStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DashboardStatisticsController {

    private final IDashboardStatisticsService iDashboardStatisticsService;

    @GetMapping("/statistics")
    public ResponseEntity<DashboardStatisticsDTO> getStatistics() {
        DashboardStatisticsDTO dashboardStatisticsDTO = iDashboardStatisticsService.getStatistics();
        return ResponseEntity.status(HttpStatus.OK).body(dashboardStatisticsDTO);
    }

}
