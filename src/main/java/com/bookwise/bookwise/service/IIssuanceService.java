package com.bookwise.bookwise.service;

import com.bookwise.bookwise.dto.book.BookHistoryDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;
import com.bookwise.bookwise.dto.user.UserHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public interface IIssuanceService {

    Page<IssuanceOutDTO> getIssuances(Pageable pageable, List<String> titles,
                                      LocalDateTime issueTimeFrom, LocalDateTime issueTimeTo,
                                      LocalDateTime expectedReturnTimeFrom, LocalDateTime expectedReturnTimeTo,
                                      String status, String type, String search);
    List<IssuanceOutDTO> getAllIssuances(Sort sort);
    Page<IssuanceOutDTO> getIssuances(Pageable pageable, String search);

    Page<UserHistoryDTO> getUserHistory(Pageable pageable, String mobile, List<String> titles,
                                        LocalDateTime issueTimeFrom, LocalDateTime issueTimeTo,
                                        LocalDateTime expectedReturnTimeFrom, LocalDateTime expectedReturnTimeTo,
                                        String status, String type);

    Page<BookHistoryDTO> getBookHistory(Pageable pageable, Long id);

    IssuanceOutDTO createIssuance(IssuanceInDTO issuanceInDTO);
    IssuanceOutDTO getIssuanceById(Long id);
    IssuanceOutDTO deleteIssuanceById(Long id);
    IssuanceOutDTO updateIssuance(Long id, IssuanceInDTO issuanceInDTO);

}
