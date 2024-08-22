package com.bookwise.bookwise.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SMSController {

    // private String TWILIO_ACCOUNT_SID = "ACcb4bed65b875e2349ca56c7c67c99d4e";
    // private String TWILIO_AUTH_TOKEN = "93af76fe009c5d2385e655247e3a81b1";
    // private String TWILIO_PHONE_NUMBER = "+12562696971";

    // private Map<String, String> otpMap = new HashMap<>();

    // @GetMapping("/send-sms/{toMobileNo}/text")
    // public ResponseEntity sendMessage(@PathVariable("toMobileNo") String toMobileNo, @PathVariable("text") String text) {
    //     Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

    //     Message.creator(new PhoneNumber(toMobileNo), new PhoneNumber(TWILIO_PHONE_NUMBER), text).create();

    //     return ResponseEntity.status(HttpStatus.OK).body("Message sent successfully to mobile no. " + toMobileNo);
    // }

}
