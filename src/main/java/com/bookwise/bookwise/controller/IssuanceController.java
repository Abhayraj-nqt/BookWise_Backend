package com.bookwise.bookwise.controller;

import com.bookwise.bookwise.constants.IssuanceConstants;
import com.bookwise.bookwise.dto.book.BookHistoryDTO;
import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;
import com.bookwise.bookwise.dto.response.ResponseDTO;
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
    public ResponseEntity<Page<IssuanceOutDTO>> getAllIssuances (
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) List<String> titles,
            @RequestParam(required = false) LocalDateTime issueTimeFrom,
            @RequestParam(required = false) LocalDateTime issueTimeTo,
            @RequestParam(required = false) LocalDateTime expectedReturnTimeFrom,
            @RequestParam(required = false) LocalDateTime expectedReturnTimeTo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<IssuanceOutDTO> issuanceOutDTOPage = iIssuanceService.getIssuances(pageable, titles, issueTimeFrom, issueTimeTo,
                expectedReturnTimeFrom, expectedReturnTimeTo,
                status, type, search);

        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTOPage);

    }

    @GetMapping("/user/history/{mobile}")
    public ResponseEntity<Page<UserHistoryDTO>> getUserHistory(
            @PathVariable String mobile,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) List<String> titles,
            @RequestParam(required = false) LocalDateTime issueTimeFrom,
            @RequestParam(required = false) LocalDateTime issueTimeTo,
            @RequestParam(required = false) LocalDateTime expectedReturnTimeFrom,
            @RequestParam(required = false) LocalDateTime expectedReturnTimeTo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
        Page<UserHistoryDTO> userHistoryDTOPage = iIssuanceService.getUserHistory(
                pageable, mobile, titles, issueTimeFrom, issueTimeTo,
                expectedReturnTimeFrom, expectedReturnTimeTo, status, type
        );

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

    @GetMapping("/issuance/{id}")
    public ResponseEntity<IssuanceOutDTO> getIssuanceById(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.getIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(issuanceOutDTO);
    }

    @PostMapping("/issuance")
    public ResponseEntity<ResponseDTO> createIssuance(@RequestBody IssuanceInDTO issuanceInDTO) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.createIssuance(issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), IssuanceConstants.ISSUANCE_CREATE_MSG));
    }

    @PutMapping("/issuance/{id}")
    public ResponseEntity<ResponseDTO> updateIssuance(@PathVariable Long id, @RequestBody IssuanceInDTO issuanceInDTO) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.updateIssuance(id, issuanceInDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), IssuanceConstants.ISSUANCE_UPDATE_MSG));
    }

    @DeleteMapping("/issuance/{id}")
    public ResponseEntity<ResponseDTO> deleteIssuance(@PathVariable Long id) {
        IssuanceOutDTO issuanceOutDTO = iIssuanceService.deleteIssuanceById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(HttpStatus.OK.toString(), IssuanceConstants.ISSUANCE_DELETE_MSG));
    }

}
