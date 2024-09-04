package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.constants.IssuanceConstants;
import com.bookwise.bookwise.dto.book.BookHistoryDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;
import com.bookwise.bookwise.dto.user.UserHistoryDTO;
import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Issuance;
import com.bookwise.bookwise.entity.User;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.mapper.BookMapper;
import com.bookwise.bookwise.mapper.IssuanceMapper;
import com.bookwise.bookwise.repository.BookRepository;
import com.bookwise.bookwise.repository.IssuanceRepository;
import com.bookwise.bookwise.repository.UserRepository;
import com.bookwise.bookwise.service.IIssuanceService;
import com.bookwise.bookwise.service.ISMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IssuanceServiceImpl implements IIssuanceService {

    private final IssuanceRepository issuanceRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ISMSService ismsService;

    @Override
    public List<IssuanceOutDTO> getAllIssuances(Sort sort) {
        return issuanceRepository.findAll(sort).stream()
                .map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<IssuanceOutDTO> getIssuances(Pageable pageable, String search) {
        Page<Issuance> issuancePage;
        if (search != null && !search.isEmpty()) {
            issuancePage = issuanceRepository.findByBookContainingIgnoreCase(search, pageable);
        } else {
            issuancePage = issuanceRepository.findAll(pageable);
        }

        return  issuancePage.map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO()));
    }

    @Override
    public Page<IssuanceOutDTO> getIssuances(Pageable pageable, List<String> titles,
                                             LocalDateTime issueTimeFrom, LocalDateTime issueTimeTo,
                                             LocalDateTime expectedReturnTimeFrom, LocalDateTime expectedReturnTimeTo,
                                             String status, String type) {

        Page<Issuance> issuancePage = issuanceRepository.filterIssuances(titles, issueTimeFrom, issueTimeTo, expectedReturnTimeFrom, expectedReturnTimeTo, status, type, pageable);

        List<IssuanceOutDTO> issuanceOutDTOPage = issuancePage.stream().map(issuance -> IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO()))
                .collect(Collectors.toList());

        return new PageImpl<>(issuanceOutDTOPage, pageable, issuancePage.getTotalElements());
    }

    @Override
    public Page<UserHistoryDTO> getUserHistory(Pageable pageable, String mobile, List<String> titles,
                                               LocalDateTime issueTimeFrom, LocalDateTime issueTimeTo,
                                               LocalDateTime expectedReturnTimeFrom, LocalDateTime expectedReturnTimeTo,
                                               String status, String type) {
        User user = userRepository.findByMobileNumber(mobile).orElseThrow(
                () -> new ResourceNotFoundException("User", "mobileNumber", mobile)
        );

        Page<Issuance> issuancePage = issuanceRepository.filterUserHistory(
                user.getId(), titles, issueTimeFrom, issueTimeTo,
                expectedReturnTimeFrom, expectedReturnTimeTo, status, type, pageable
        );

        List<UserHistoryDTO> userHistory = issuancePage.stream().map(issuance -> {
            UserHistoryDTO dto = new UserHistoryDTO();
            dto.setId(issuance.getId());
            dto.setBook(BookMapper.mapToBookOutDTO(issuance.getBook(), new BookOutDTO()));
            dto.setStatus(issuance.getStatus());
            dto.setType(issuance.getIssuanceType());
            dto.setIssueTime(issuance.getIssueTime());
            dto.setExpectedReturnTime(issuance.getExpectedReturnTime());
            dto.setActualReturnTime(issuance.getActualReturnTime());
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(userHistory, pageable, issuancePage.getTotalElements());
    }

    @Override
    public Page<BookHistoryDTO> getBookHistory(Pageable pageable, Long id) {
        List<Issuance> issuanceList = issuanceRepository.findAllByBookId(id);
        List<BookHistoryDTO> bookHistory = issuanceList.stream().map(issuance -> {
            BookHistoryDTO dto = new BookHistoryDTO();
            dto.setId(issuance.getId());
            dto.setUser(issuance.getUser());
            dto.setStatus(issuance.getStatus());
            dto.setType(issuance.getIssuanceType());
            dto.setIssueTime(issuance.getIssueTime());
            dto.setExpectedReturnTime(issuance.getExpectedReturnTime());
            dto.setActualReturnTime(issuance.getActualReturnTime());
            return dto;
        }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), bookHistory.size());
        List<BookHistoryDTO> pagedHistory = bookHistory.subList(start, end);

        return new PageImpl<>(pagedHistory, pageable, bookHistory.size());
    }

    @Override
    public IssuanceOutDTO createIssuance(IssuanceInDTO issuanceInDTO) {
        Issuance issuance = IssuanceMapper.mapToIssuance(issuanceInDTO, new Issuance(), userRepository, bookRepository);
        issuance.setIssueTime(LocalDateTime.now());
        issuance.setExpectedReturnTime(issuanceInDTO.getReturnTime());

        Book book = issuance.getBook();
        if (book.getAvlQty() > 0) {
            book.setAvlQty(book.getAvlQty()-1);
            bookRepository.save(book);
        } else {
            throw new IllegalStateException("The book quantity is 0 and cannot be issued!");
        }


        Issuance savedIssuance = issuanceRepository.save(issuance);

        String message = String.format("\nYou have issued the book '%s'\n" +
                                        "author '%s'\n" +
                                        "From %s\n" +
                                        "To %s",
                savedIssuance.getBook().getTitle(),
                savedIssuance.getBook().getAuthor(),
                savedIssuance.getIssueTime().toLocalDate(),
                savedIssuance.getExpectedReturnTime().toLocalDate());

//        ismsService.sendSms(savedIssuance.getUser().getMobileNumber(), message);

        IssuanceOutDTO issuanceOutDTO = IssuanceMapper.mapToIssuanceOutDTO(savedIssuance, new IssuanceOutDTO());

        return issuanceOutDTO;
    }

    @Override
    public IssuanceOutDTO getIssuanceById(Long id) {
        Issuance issuance = issuanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        IssuanceOutDTO issuanceOutDTO = IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO());

        return issuanceOutDTO;
    }

    @Override
    public IssuanceOutDTO deleteIssuanceById(Long id) {
        Issuance issuance = issuanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        issuanceRepository.deleteById(id);

        IssuanceOutDTO issuanceOutDTO = IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO());

        return  issuanceOutDTO;
    }

    @Override
    public IssuanceOutDTO updateIssuance(Long id, IssuanceInDTO issuanceInDTO) {

        Issuance issuance = issuanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        String oldSattus = issuance.getStatus();

        issuance = IssuanceMapper.mapToIssuance(issuanceInDTO, issuance, userRepository, bookRepository);

        if (issuanceInDTO.getStatus().equals(IssuanceConstants.ISSUANCE_STATUS_RETURNED) && !oldSattus.equals(IssuanceConstants.ISSUANCE_STATUS_RETURNED)) {
            Book book = issuance.getBook();
            issuance.setActualReturnTime(LocalDateTime.now());
            if (book.getAvlQty() < book.getTotalQty()) {
                book.setAvlQty(book.getAvlQty() + 1);
                bookRepository.save(book);
            }

        } else if (issuanceInDTO.getStatus().equals(IssuanceConstants.ISSUANCE_STATUS_RETURNED) && oldSattus.equals(IssuanceConstants.ISSUANCE_STATUS_RETURNED)) {

        } else {
            issuance.setActualReturnTime(null);
        }

        Issuance savedIssuance = issuanceRepository.save(issuance);

        IssuanceOutDTO issuanceOutDTO = IssuanceMapper.mapToIssuanceOutDTO(savedIssuance, new IssuanceOutDTO());

        return issuanceOutDTO;
    }

}
