package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.dto.dashboard.DashboardStatisticsDTO;
import com.bookwise.bookwise.repository.BookRepository;
import com.bookwise.bookwise.repository.CategoryRepository;
import com.bookwise.bookwise.repository.IssuanceRepository;
import com.bookwise.bookwise.repository.UserRepository;
import com.bookwise.bookwise.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardStatisticsServiceImpl implements IDashboardStatisticsService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final IssuanceRepository issuanceRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardStatisticsDTO getStatistics() {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        DashboardStatisticsDTO dashboardStatisticsDTO = new DashboardStatisticsDTO();

        dashboardStatisticsDTO.setTotalCategories(categoryRepository.count());

        dashboardStatisticsDTO.setTotalIssuance(issuanceRepository.count());
        dashboardStatisticsDTO.setTotalActiveInHouseIssuance(issuanceRepository.countDistinctUsersByStatusAndIssuanceType("Issued", "In house"));
        dashboardStatisticsDTO.setTotalActiveTakeAwayIssuance(issuanceRepository.countDistinctUsersByStatusAndIssuanceType("Issued", "Take away"));

        dashboardStatisticsDTO.setTotalBooks(bookRepository.getTotalBooksCount());
        dashboardStatisticsDTO.setTotalBookTitles(bookRepository.count());

        dashboardStatisticsDTO.setAvlBooks(bookRepository.getTotalAvailableBooks());
        dashboardStatisticsDTO.setTotalAvlBookTitles(bookRepository.countByAvlQtyGreaterThan(0));

        Long totalUser = userRepository.count() - userRepository.findByRole("ROLE_ADMIN").size();
        dashboardStatisticsDTO.setTotalUsers(totalUser);

        dashboardStatisticsDTO.setTotalActiveUsers(issuanceRepository.countDistinctUsersByStatus("Issued"));

        dashboardStatisticsDTO.setTotalUsersInLibrary(issuanceRepository.countDistinctUsersInLibraryToday(startOfDay, endOfDay));

        return dashboardStatisticsDTO;
    }
}
