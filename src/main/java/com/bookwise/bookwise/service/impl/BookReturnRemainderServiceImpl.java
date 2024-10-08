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

    private final IssuanceRepository issuanceRepository;
    private final ISMSService ismsService;

//    second minute hour day-of-month month day-of-week

    @Override
//    @Scheduled(cron = "0 56 12 * * *", zone = "Asia/Kolkata")
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Kolkata") // Every day at 00:00 (midnight)
    public void sendReturnRemainder() {

        LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1);
        List<Issuance> dueTomorrow = issuanceRepository.findAllByExpectedReturnTimeBetweenAndStatus(startOfTomorrow, endOfTomorrow, "Issued");

        System.out.println("SCHEDULER CALLED" + dueTomorrow);

        for (Issuance issuance : dueTomorrow) {
            String message = String.format("\nReminder:\n" +
                            "Please return the book '%s'\n" +
                            "Author '%s'\n"+
                            "by tomorrow (%s).",
                    issuance.getBook().getTitle(), issuance.getBook().getAuthor(),
                    issuance.getExpectedReturnTime().toLocalDate());
            ismsService.sendSms(issuance.getUser().getMobileNumber(), message);
        }

    }
}
