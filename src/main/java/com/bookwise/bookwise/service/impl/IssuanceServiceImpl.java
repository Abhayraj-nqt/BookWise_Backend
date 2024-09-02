package com.bookwise.bookwise.service.impl;

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
    public Long getTotalActiveUsers() {
        return issuanceRepository.countDistinctUsersByStatus("ISSUED");
    }

    @Override
    public Page<UserHistoryDTO> getUserHistory(Pageable pageable, String mobile) {
        User user = userRepository.findByMobileNumber(mobile).orElseThrow(
                () -> new ResourceNotFoundException("User", "mobileNumber", mobile)
        );

        List<Issuance> issuanceList = issuanceRepository.findAllByUserId(user.getId());

        List<UserHistoryDTO> userHistory = issuanceList.stream().map(issuance -> {
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

        // Implement pagination
//        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), userHistory.size());
        List<UserHistoryDTO> pagedHistory = userHistory.subList(start, end);

        return new PageImpl<>(pagedHistory, pageable, userHistory.size());

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
            throw new RuntimeException("The book is out of stock!");
        }


        Issuance savedIssuance = issuanceRepository.save(issuance);

//        String message = String.format("Info: You have issued the book '%s'\nFrom %s\nTo %s",
//                savedIssuance.getBook().getTitle(),
//                savedIssuance.getIssueTime().toLocalDate(),
//                savedIssuance.getExpectedReturnTime().toLocalDate());
//
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

        if (issuanceInDTO.getStatus().equals("RETURNED") && !oldSattus.equals("RETURNED")) {
            Book book = issuance.getBook();
            issuance.setActualReturnTime(LocalDateTime.now());
            if (book.getAvlQty() < book.getTotalQty()) {
                book.setAvlQty(book.getAvlQty() + 1);
                bookRepository.save(book);
            }

        } else if (issuanceInDTO.getStatus().equals("RETURNED") && oldSattus.equals("RETURNED")) {

        } else {
            issuance.setActualReturnTime(null);
        }

        Issuance savedIssuance = issuanceRepository.save(issuance);

        IssuanceOutDTO issuanceOutDTO = IssuanceMapper.mapToIssuanceOutDTO(savedIssuance, new IssuanceOutDTO());

        return issuanceOutDTO;
    }

    @Override
    public IssuanceOutDTO updateStatus(Long id, String newStatus) {

        Issuance issuance = issuanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Issuance", "id", id.toString())
        );

        issuance.setStatus(newStatus);
        issuanceRepository.save(issuance);

        IssuanceOutDTO issuanceOutDTO = IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO());

        return issuanceOutDTO;

    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByUserId(Long userId) {
        List<Issuance> issuanceList = issuanceRepository.findAllByUserId(userId);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByMobile(String mobileNumber) {
        List<Issuance> issuanceList = issuanceRepository.findAllByUserMobile(mobileNumber);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByBookId(Long bookId) {
        List<Issuance> issuanceList = issuanceRepository.findAllByBookId(bookId);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByBookTitle(String title) {
        List<Issuance> issuanceList = issuanceRepository.findAllByBookTitle(title);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByIssueDate(LocalDate date) {
        List<Issuance> issuanceList = issuanceRepository.findAllByIssueDate(date);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByReturnDate(LocalDate date) {
//        List<Issuance> issuanceList = issuanceRepository.findAllByReturnDate(date);
//        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();
//
//        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return null;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByIssueTime(LocalTime time) {
        List<Issuance> issuanceList = issuanceRepository.findAllByIssueTime(time);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByReturnTime(LocalTime time) {
//        List<Issuance> issuanceList = issuanceRepository.findAllByReturnTime(time);
//        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();
//
//        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return null;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByIssueDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Issuance> issuanceList = issuanceRepository.findAllByIssueDateRange(startDate, endDate);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByReturnDateRange(LocalDateTime startDate, LocalDateTime endDate) {
//        List<Issuance> issuanceList = issuanceRepository.findAllByReturnDateRange(startDate, endDate);
//        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();
//
//        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return null;
    }


    @Override
    public List<IssuanceOutDTO> getAllIssuanceByUserIdAndBookId(Long userId, Long bookId) {
        List<Issuance> issuanceList = issuanceRepository.findAllByUserIdAndBookId(userId, bookId);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }

    @Override
    public List<IssuanceOutDTO> getAllIssuanceByMobileAndBookTitle(String mobileNumber, String title) {
        List<Issuance> issuanceList = issuanceRepository.findAllByUserMobileAndBookTitle(mobileNumber, title);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
    }
}
