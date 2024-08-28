package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.dto.book.BookHistoryDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;
import com.bookwise.bookwise.dto.user.UserHistoryDTO;
import com.bookwise.bookwise.service.IIssuanceService;
import com.fasterxml.jackson.core.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class IssuanceController {

    private final IIssuanceService iIssuanceService;

    @GetMapping("/issuances")
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        if (page == null || size == null) {
            List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuances(Sort.by(Sort.Direction.fromString(sortDir), sortBy));
            return ResponseEntity.ok(issuanceOutDTOList);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<IssuanceOutDTO> issuanceOutDTOPage = iIssuanceService.getIssuances(pageable, search);
            return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOPage);
        }
    }

    @GetMapping("/user/history/{mobile}")
    public ResponseEntity<Page<UserHistoryDTO>> getUserHistory(@PathVariable String mobile,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortDir,
                                            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<UserHistoryDTO> userHistoryDTOPage = iIssuanceService.getUserHistory(pageable, mobile);

        return ResponseEntity.status(HttpStatus.OK).body(userHistoryDTOPage);
    }

    @GetMapping("/book/history/{id}")
    public ResponseEntity<Page<BookHistoryDTO>> getBookHistory(@PathVariable Long id,
                                                         @RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir,
                                                         @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<BookHistoryDTO> bookHistoryDTOPage = iIssuanceService.getBookHistory(pageable, id);

        return ResponseEntity.status(HttpStatus.OK).body(bookHistoryDTOPage);
    }



    @GetMapping("/users/active-count")
    public ResponseEntity<Long> getTotalActiveUsers() {
        Long count = iIssuanceService.getTotalActiveUsers();
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @PostMapping("/issuance")
    public ResponseEntity<IssuanceOutDTO> createIssuance(@RequestBody IssuanceInDTO issuanceInDTO) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.createIssuance(issuanceInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(issuanceOutDTO);
    }

    @PutMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDTO> updateIssuance(@PathVariable Long id, @RequestBody IssuanceInDTO issuanceInDTO) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.updateIssuance(id, issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @PutMapping("/issuance/status/{id}")
    public ResponseEntity<IssuanceOutDTO> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("newStatus");
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.updateStatus(id, newStatus);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }


    @DeleteMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDTO> deleteIssuance(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.deleteIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @GetMapping("/issuance/userId/{id}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByUserId(@PathVariable Long id) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/mobile/{mobileNumber}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByMobile(@PathVariable String mobileNumber) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByMobile(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/bookId/{id}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByBookId(@PathVariable Long id) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByBookId(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/bookTitle/{title}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByBookTitle(@PathVariable String title) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByBookTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/issueDate/{date}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByIssueDate(@PathVariable LocalDate date) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByIssueDate(date);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/returnDate/{date}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByReturnDate(@PathVariable LocalDate date) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByReturnDate(date);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/issueTime/{time}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByIssueTime(@PathVariable LocalTime time) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByIssueTime(time);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

//    @GetMapping("/issuance/issueTime")
//    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByIssueTime(@RequestParam("time") String timeStr) {
//        LocalTime time = LocalTime.parse(timeStr);
//        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByIssueTime(time);
//        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
//    }

    @GetMapping("/issuance/returnTime/{time}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByReturnTime(@PathVariable LocalTime time) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByReturnTime(time);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/issueDateRange/{startDate}/{endDate}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByIssueDateRange(@PathVariable LocalDateTime startDate, @PathVariable LocalDateTime endDate) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByIssueDateRange(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/returnDateRange/{startDate}/{endDate}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByReturnDateRange(@PathVariable LocalDateTime startDate, @PathVariable LocalDateTime endDate) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByReturnDateRange(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/user/book/{userId}/{bookId}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByUserIdAndBookId(@PathVariable Long userId, @PathVariable Long bookId) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByUserIdAndBookId(userId, bookId);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }

    @GetMapping("/issuance/mobile/title/{mobile}/{title}")
    public ResponseEntity<List<IssuanceOutDTO>> getIssuancesByUserIdAndBookId(@PathVariable String mobile, @PathVariable String title) {
        List<IssuanceOutDTO> issuanceOutDTOList = iIssuanceService.getAllIssuanceByMobileAndBookTitle(mobile, title);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOList);
    }


}
