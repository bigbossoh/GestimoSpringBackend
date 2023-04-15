package com.bzdata.gestimospringbackend.Services;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import com.bzdata.gestimospringbackend.Utils.SmsOrangeConfig;

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
public class CronJobService {
    final CronMailService cronMailService;
    final AppelLoyerService appelLoyerService;
    final SmsOrangeConfig envoiSmsOrange;

     @Scheduled(cron = "0 0 15 * * FRI")
    //@Scheduled(cron = "0 18 * * FRI *")
     public void sendMail() throws InterruptedException, FileNotFoundException, JRException, SQLException {

        System.out.println("**************** Ici on est bien la ***********");
        try {

            LocalDate dateEnvoiSms = LocalDate.now();
            String leMois = "" + dateEnvoiSms.getMonthValue();
            double montant = 0.0;
            double personneAyantpayer = 0.0;
            double personneNonPayer = 0.0;
            double totatLocataire = 0.0;

            if (dateEnvoiSms.getMonthValue() < 10) {
                leMois = "0" + dateEnvoiSms.getMonthValue();
            }
            montant = appelLoyerService.payeParPeriode(dateEnvoiSms.getYear() + "-" + leMois, 1L, 0L);
            personneAyantpayer = appelLoyerService.nombreBauxPaye(dateEnvoiSms.getYear() + "-" + leMois, 1L, 0L);
            personneNonPayer = appelLoyerService.nombreBauxImpaye(dateEnvoiSms.getYear() + "-" + leMois, 1L, 0L);
            totatLocataire = personneAyantpayer + personneNonPayer;
            System.out.println(dateEnvoiSms.getYear()+"-"+leMois+'-'+montant);
            String leTok = envoiSmsOrange.getTokenSmsOrange();

            String message = "ENCAISSEMENT du 01-"+leMois+'-'+dateEnvoiSms.getYear()+" au "+dateEnvoiSms.getDayOfMonth()+"-"+leMois+'-'+dateEnvoiSms.getYear()+" est de "+montant+ " FCFA CONCERNANT "+personneAyantpayer+" LOCATAIRE(S) SUR "+totatLocataire;
            envoiSmsOrange.sendSms(leTok, message, "+2250000",    "0758448344", "MAGISER");
            System.out.println("********************* Le toke toke est : " + leTok);
    } catch (Exception e) {
            System.err.println(e.getMessage());
    }
        // cronMailService.creerUneDateDenvoi(LocalDate.now());
        // List<CronMail> sendCron = cronMailService.getAllCronMailNotSend();
        // if (sendCron.size() > 0) {
        //     // ENVOI DE SMS GROUPE
        //     if (sendCron.get(0).getNextDateMail().getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
        //         cronMailService.modifierUneDateDenvoi(sendCron.get(0).getId());
        //         boolean sms_envoyer = appelLoyerService
        //                 .sendSmsAppelLoyerGroupe(sendCron.get(0).getNextDateMail().getYear() + "-"
        //                         + sendCron.get(0).getNextDateMail().getMonthValue(), sendCron.get(0).getIdAgence());
        //         LocalDate nexDate = LocalDate.now();
        //         cronMailService.creerUneDateDenvoi(nexDate.plusMonths(1));
        //         log.info("Valeur du sms {}", sms_envoyer);
        //         if (sms_envoyer) {
        //             log.info("Sms Envoye {}", sms_envoyer);
        //         }
        //         cronMailService.creerUneDateDenvoi(LocalDate.now());
        //     }

        //     for (int i = 0; i < sendCron.size(); i++) {
        //         System.out.println("*************** Nazaire " + new Date());
        //         System.out.println("Non Non Nazaire " + sendCron.get(i).getIdAgence());
        //         System.out.println(" Loca ");

        //     }

        // }

    }

}
