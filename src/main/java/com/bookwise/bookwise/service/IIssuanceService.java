package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IIssuanceService {

    IssuanceOutDTO createIssuance(IssuanceInDTO issuanceInDTO);
    IssuanceOutDTO getIssuanceById(Long id);
    IssuanceOutDTO deleteIssuanceById(Long id);
    IssuanceOutDTO updateIssuance(Long id, IssuanceInDTO issuanceInDTO);
    IssuanceOutDTO updateStatus(Long id, String newStatus);

    List<IssuanceOutDTO> getAllIssuanceByUserId(Long userId);
    List<IssuanceOutDTO> getAllIssuanceByMobile(String mobileNumber);

    List<IssuanceOutDTO> getAllIssuanceByBookId(Long bookId);
    List<IssuanceOutDTO> getAllIssuanceByBookTitle(String title);

    List<IssuanceOutDTO> getAllIssuanceByIssueDate(LocalDate date);
    List<IssuanceOutDTO> getAllIssuanceByReturnDate(LocalDate date);
    List<IssuanceOutDTO> getAllIssuanceByIssueTime(LocalTime time);
    List<IssuanceOutDTO> getAllIssuanceByReturnTime(LocalTime time);

    List<IssuanceOutDTO> getAllIssuanceByIssueDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<IssuanceOutDTO> getAllIssuanceByReturnDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<IssuanceOutDTO> getAllIssuanceByUserIdAndBookId(Long userId, Long bookId);
    List<IssuanceOutDTO> getAllIssuanceByMobileAndBookTitle(String mobileNumber, String title);




}
