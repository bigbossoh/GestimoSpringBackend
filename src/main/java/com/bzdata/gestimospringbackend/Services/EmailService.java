package com.bzdata.gestimospringbackend.Services;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;

public interface EmailService {
    boolean sendMailWithAttachment(String periode, String to, String subject, String body, String fileToAttache);
    boolean sendMailGrouperWithAttachment(String periode)throws FileNotFoundException, JRException, SQLException ;
    boolean sendMailQuittanceWithAttachment( Long id);
}
