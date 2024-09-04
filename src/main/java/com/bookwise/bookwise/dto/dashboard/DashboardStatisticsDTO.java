package com.bookwise.bookwise.dto.dashboard;

import lombok.*;

@Getter @Setter @ToString
@RequiredArgsConstructor
public class DashboardStatisticsDTO {

    private Long totalBooks;
    private Long totalBookTitles;
    private Long avlBooks;
    private Long totalAvlBookTitles;

    private Long totalUsers;
    private Long totalActiveUsers;
    private Long totalUsersInLibrary;

    private Long totalActiveInHouseIssuance;
    private Long totalActiveTakeAwayIssuance;

    private Long totalCategories;
    private Long totalIssuance;

}
