package com.bzdata.gestimospringbackend.Services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.bzdata.gestimospringbackend.Models.CronMail;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JRException;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CronJobService {
    final CronMailService cronMailService;
    final EmailService emailService;

    // @Scheduled(cron = "0 0 8 * * *")
    @Scheduled(cron = "* * * * * *")
    public void sendMail() throws InterruptedException, FileNotFoundException, JRException, SQLException {

        List<CronMail> sendCron = cronMailService.getAllCronMailNotSend();
        if (sendCron.size() > 0) {
            for (int i = 0; i < sendCron.size(); i++) {
                System.out.println("*************** Nazaire " + new Date());
                System.out.println("Non Non Nazaire " + sendCron.get(i).getIdAgence());
                emailService.sendMailGrouperWithAttachment(sendCron.get(i).getNextDateMail().getYear() + "-"
                        + sendCron.get(i).getNextDateMail().getMonthValue(), sendCron.get(i).getIdAgence());

                System.out.println(" Loca ");

            }

        }
        // CronMailDto cronMail = cronMailService.getLasCronMailDto();
        // if (cronMail.isDonne() == false) {
        // cronMailService.modifierUneDateDenvoi(cronMail.getId());

        // cronMailService.creerUneDateDenvoi(LocalDate.now());
        // } else {
        // System.out.println("Non Non Nazaire " + new Date());
        // }
        // LocalDate lDate = LocalDate.now();
        // cronMailService.creerUneDateDenvoi(lDate);

    }

}
