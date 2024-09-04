package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.dashboard.DashboardStatisticsDTO;
import org.springframework.stereotype.Service;

@Service
public interface IDashboardStatisticsService {

    DashboardStatisticsDTO getStatistics();

}
