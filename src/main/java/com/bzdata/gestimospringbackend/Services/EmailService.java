package com.bzdata.gestimospringbackend.Services;

public interface EmailService {
    boolean sendMailWithAttachment(String periode,String to,String subject, String body,String fileToAttache );
}
