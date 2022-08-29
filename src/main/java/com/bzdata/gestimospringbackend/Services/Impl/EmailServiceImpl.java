package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.bzdata.gestimospringbackend.Services.EmailService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
    final JavaMailSender mailSender;

    @Override
    public boolean sendMailWithAttachment(String periode,String to, String subject, String body, String fileToAttache) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress("seve_investissment@outlook.fr"));
                mimeMessage.setSubject(subject);
                //mimeMessage.setText(body);
                File file = new File(fileToAttache);
                log.info("****** Le fichier a envoyer ***** {}",file);
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(body);
                MimeBodyPart attachementBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachementBodyPart.setDataHandler(new DataHandler(source));
                attachementBodyPart.setFileName("Quittance du mois de "+periode+".pdf");
                multipart.addBodyPart(textBodyPart);
                multipart.addBodyPart(attachementBodyPart);
                mimeMessage.setContent(multipart);
            }

        };
        try {
            System.out.println("*************** send mails for Test **********");
            mailSender.send(preparator);
            log.info("Le send mail {}", preparator);
            System.out.println("***************  end send mails**********");
            return true;
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
        return false;
    }

}
