package com.bookwise.bookwise.service.impl;

import com.bookwise.bookwise.entity.Issuance;
import com.bookwise.bookwise.repository.IssuanceRepository;
import com.bookwise.bookwise.service.IBookReturnRemainderService;
import com.bookwise.bookwise.service.ISMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReturnRemainderServiceImpl implements IBookReturnRemainderService {

    private IssuanceRepository issuanceRepository;
    private ISMSService ismsService;

    @Override
//    @Scheduled(cron = "0 0 8 * * *") // Every day at 8 AM
    public void sendReturnRemainder() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<Issuance> dueTomorrow = issuanceRepository.findAllByExpectedReturnTime(tomorrow);

        for (Issuance issuance : dueTomorrow) {
            String message = String.format("Reminder: Please return the book '%s' by tomorrow (%s).",
                    issuance.getBook().getTitle(), issuance.getExpectedReturnTime().toLocalDate());
            ismsService.sendSms(issuance.getUser().getMobileNumber(), message);
        }
    }
}
