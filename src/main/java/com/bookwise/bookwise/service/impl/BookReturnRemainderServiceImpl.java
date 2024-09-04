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
//    @Scheduled(cron = "0 16 18 * * *") // Every day at 6:15 PM
//    @Scheduled(cron = "0 */15 * * * *") // Every 15 minutes
    @Scheduled(cron = "0 * * * * *") // Every minute
    public void sendReturnRemainder() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<Issuance> dueTomorrow = issuanceRepository.findAllByExpectedReturnTime(tomorrow);
        System.out.println("SCHEDULER CALLED" + dueTomorrow);
//        for (Issuance issuance : dueTomorrow) {
//            String message = String.format("\nReminder:\n" +
//                            "Please return the book '%s'\n" +
//                            "Author '%s'\n"+
//                            "by tomorrow (%s).",
//                    issuance.getBook().getTitle(), issuance.getBook().getAuthor(),
//                    issuance.getExpectedReturnTime().toLocalDate());
//            ismsService.sendSms(issuance.getUser().getMobileNumber(), message);
//        }
    }
}
