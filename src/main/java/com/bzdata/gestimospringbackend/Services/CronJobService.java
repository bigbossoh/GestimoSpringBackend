package com.bzdata.gestimospringbackend.Services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.bzdata.gestimospringbackend.Models.CronMail;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CronJobService {
    final CronMailService cronMailService;
    final AppelLoyerService appelLoyerService;

    @Scheduled(cron = "0 0 8 * * *")
    // @Scheduled(cron = "* * * * * *")
    public void sendMail() throws InterruptedException, FileNotFoundException, JRException, SQLException {
        System.out.println("**************** Ici on est bien la ... ***********");
        cronMailService.creerUneDateDenvoi(LocalDate.now());
        List<CronMail> sendCron = cronMailService.getAllCronMailNotSend();
        if (sendCron.size() > 0) {
            // ENVOI DE SMS GROUPE
            if (sendCron.get(0).getNextDateMail().getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
                cronMailService.modifierUneDateDenvoi(sendCron.get(0).getId());
                boolean sms_envoyer = appelLoyerService
                        .sendSmsAppelLoyerGroupe(sendCron.get(0).getNextDateMail().getYear() + "-"
                                + sendCron.get(0).getNextDateMail().getMonthValue(), sendCron.get(0).getIdAgence());
                LocalDate nexDate = LocalDate.now();
                cronMailService.creerUneDateDenvoi(nexDate.plusMonths(1));
                log.info("Valeur du sms {}", sms_envoyer);
                if (sms_envoyer) {
                    log.info("Sms Envoye {}", sms_envoyer);
                }
                cronMailService.creerUneDateDenvoi(LocalDate.now());
            }

            for (int i = 0; i < sendCron.size(); i++) {
                System.out.println("*************** Nazaire " + new Date());
                System.out.println("Non Non Nazaire " + sendCron.get(i).getIdAgence());
                System.out.println(" Loca ");

            }

        }

    }

}
