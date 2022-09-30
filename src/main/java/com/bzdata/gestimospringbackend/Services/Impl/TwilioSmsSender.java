package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.Models.SmsRequest;
import com.bzdata.gestimospringbackend.Services.SmsSender;
import com.bzdata.gestimospringbackend.TwilioConfiguration.TwilioConfiguration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TwilioSmsSender implements SmsSender {

    private final TwilioConfiguration twilioConfiguration;

    @Override
    public void sendSms(SmsRequest smsRequest) {
        // if(isPhoneNumberValid(smsRequest.getPhoneNumber())){
        // PhoneNumber to= new PhoneNumber(smsRequest.getPhoneNumber());
        // PhoneNumber from= new PhoneNumber(twilioConfiguration.getTrialNumber());
        // String message=smsRequest.getMessage();
        // MessageCreator creator= Message.creator(to,from,message);
        // creator.create();
        // log.warn("Send SMS {} ",smsRequest.toString());

        // }else {
        // throw new IllegalArgumentException(
        // "phone number ["+smsRequest.getPhoneNumber()+"] is not correct number."
        // );
        // }

    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // TODO
        return true;
    }
}
