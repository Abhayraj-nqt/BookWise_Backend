package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;
import com.bookwise.bookwise.entity.Issuance;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.mapper.IssuanceMapper;
import com.bookwise.bookwise.repository.BookRepository;
import com.bookwise.bookwise.repository.IssuanceRepository;
import com.bookwise.bookwise.repository.UserRepository;
import com.bookwise.bookwise.service.IIssuanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IssuanceServiceImpl implements IIssuanceService {

    private final IssuanceRepository issuanceRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public IssuanceOutDTO createIssuance(IssuanceInDTO issuanceInDTO) {
        Issuance issuance = IssuanceMapper.mapToIssuance(issuanceInDTO, new Issuance(), userRepository, bookRepository);
        Issuance savedIssuance = issuanceRepository.save(issuance);

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

        issuance = IssuanceMapper.mapToIssuance(issuanceInDTO, issuance, userRepository, bookRepository);

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
        List<Issuance> issuanceList = issuanceRepository.findAllByReturnDate(date);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
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
        List<Issuance> issuanceList = issuanceRepository.findAllByReturnTime(time);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
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
        List<Issuance> issuanceList = issuanceRepository.findAllByReturnDateRange(startDate, endDate);
        List<IssuanceOutDTO> issuanceOutDTOList = new ArrayList<>();

        issuanceList.forEach(issuance -> issuanceOutDTOList.add(IssuanceMapper.mapToIssuanceOutDTO(issuance, new IssuanceOutDTO())));

        return issuanceOutDTOList;
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
