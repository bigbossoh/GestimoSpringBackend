package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.Models.SmsRequest;
import com.bzdata.gestimospringbackend.Services.SmsSender;
import com.bzdata.gestimospringbackend.TwilioConfiguration.TwilioConfiguration;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
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
