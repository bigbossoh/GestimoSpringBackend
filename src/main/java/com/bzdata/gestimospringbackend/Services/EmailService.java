package com.bzdata.gestimospringbackend.Services;

public interface EmailService {
    boolean sendMailWithAttachment(String to,String subject, String body,String fileToAttache );
}
