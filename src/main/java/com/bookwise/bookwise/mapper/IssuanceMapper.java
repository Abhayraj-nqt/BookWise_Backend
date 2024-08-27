package com.bookwise.bookwise.mapper;

import com.bookwise.bookwise.dto.book.BookOutDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceInDTO;
import com.bookwise.bookwise.dto.issuance.IssuanceOutDTO;
import com.bookwise.bookwise.dto.user.UserDTO;
import com.bookwise.bookwise.entity.Book;
import com.bookwise.bookwise.entity.Issuance;
import com.bookwise.bookwise.entity.User;
import com.bookwise.bookwise.exception.ResourceNotFoundException;
import com.bookwise.bookwise.repository.BookRepository;
import com.bookwise.bookwise.repository.UserRepository;

public final class IssuanceMapper {

    public static IssuanceOutDTO mapToIssuanceOutDTO(Issuance issuance, IssuanceOutDTO issuanceOutDTO) {
        issuanceOutDTO.setId(issuance.getId());
        issuanceOutDTO.setStatus(issuance.getStatus());
        issuanceOutDTO.setIssuanceType(issuance.getIssuanceType());
        issuanceOutDTO.setIssueTime(issuance.getIssueTime());
        issuanceOutDTO.setReturnTime(issuance.getReturnTime());
        issuanceOutDTO.setUser(UserMapper.mapToUserDTO(issuance.getUser(), new UserDTO()));
        issuanceOutDTO.setBook(BookMapper.mapToBookOutDTO(issuance.getBook(), new BookOutDTO()));

        return issuanceOutDTO;
    }

    public static Issuance mapToIssuance(IssuanceInDTO issuanceInDTO, Issuance issuance, UserRepository userRepository, BookRepository bookRepository) {

        User user = userRepository.findById(issuanceInDTO.getUser()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", issuanceInDTO.getUser().toString())
        );

        Book book = bookRepository.findById(issuanceInDTO.getBook()).orElseThrow(
                () -> new ResourceNotFoundException("Book", "id", issuanceInDTO.getBook().toString())
        );

        issuance.setStatus(issuanceInDTO.getStatus());
        issuance.setIssuanceType(issuanceInDTO.getIssuanceType());
        if (issuanceInDTO.getIssueTime() != null) {
            issuance.setIssueTime(issuanceInDTO.getIssueTime());
        }
        issuance.setReturnTime(issuanceInDTO.getReturnTime());

        issuance.setUser(user);
        issuance.setBook(book);

        return issuance;
    }

}
