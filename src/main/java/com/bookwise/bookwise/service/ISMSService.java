package com.bookwise.bookwise.service;

import org.springframework.stereotype.Service;

@Service
public interface ISMSService {

    void sendSms(String toMobileNumber, String message);

}
