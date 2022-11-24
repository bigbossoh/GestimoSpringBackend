package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.EmailService;
import com.bzdata.gestimospringbackend.Services.PrintService;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
    final JavaMailSender mailSender;
    final AppelLoyerService appelLoyerService;
    final PrintService printService;

    @Override
    public boolean sendMailWithAttachment(String periode, String to, String subject, String body,
            String fileToAttache) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress("seve_investissment@outlook.fr"));
                mimeMessage.setSubject(subject);
                // mimeMessage.setText(body);
                File file = new File(fileToAttache);
                log.info("****** Le fichier a envoyer ***** {}", file);
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textBodyPart = new MimeBodyPart();
                textBodyPart.setText(body);
                MimeBodyPart attachementBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachementBodyPart.setDataHandler(new DataHandler(source));
                attachementBodyPart.setFileName("Quittance du mois de " + periode + ".pdf");
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

    @Override
    public boolean sendMailGrouperWithAttachment(String periode,Long idAgence)
            throws FileNotFoundException, JRException, SQLException {
        List<AppelLoyersFactureDto> listDesLocataireAppel = this.appelLoyerService.findAllAppelLoyerByPeriode(periode,idAgence);

        try {
            for (int i = 0; i < listDesLocataireAppel.size(); i++) {
                this.printService.quittancePeriodeById(periode, listDesLocataireAppel.get(i).getIdLocataire(),
                        listDesLocataireAppel.get(i).getNomPropietaire() + " "
                                + listDesLocataireAppel.get(i).getPrenomPropietaire());
                System.out.println(" Les quittance par ID sont : " + periode + "** Locatire ID **"
                        + listDesLocataireAppel.get(i).getIdLocataire() + " *** Nom ***"
                        + listDesLocataireAppel.get(i).getNomLocataire());
                this.sendMailWithAttachment(periode, listDesLocataireAppel.get(i).getEmailLocatire(),
                        "Avis d'échéance de loyer.",
                        "Bonjour Monsieur/Madame " + listDesLocataireAppel.get(i).getNomLocataire().toUpperCase() + " "
                                + listDesLocataireAppel.get(i).getPrenomLocataire().toUpperCase()+","+"\n"+"Vous trouverez ci-joint votre avis d'échéance de loyer du "+listDesLocataireAppel.get(i).getPeriodeLettre()+".",
                        "src/main/resources/templates/depot_etat/appel_loyer_du_" + periode + "_"
                                + listDesLocataireAppel.get(i).getIdLocataire() + ".pdf");
                System.out.println(i);
            }
            return true;
        } catch (Exception e) {
            System.err.println("**** Erreur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean sendMailQuittanceWithAttachment(Long id) {
        AppelLoyersFactureDto factureLocataire = this.appelLoyerService.findById(id);
        try {
            log.info("facture du du client {} ", factureLocataire);
            this.printService.quittancePeriodeById(factureLocataire.getPeriodeAppelLoyer(),
                    factureLocataire.getIdLocataire(),
                    factureLocataire.getNomPropietaire() + " " + factureLocataire.getPrenomPropietaire());
            this.sendMailWithAttachment(factureLocataire.getPeriodeAppelLoyer(), "astairenazaire@gmail.com",
                    "Envoi de Quittance groupé",
                    "Bonjour,  " + factureLocataire.getNomLocataire() + " " + factureLocataire.getPrenomLocataire(),
                    "src/main/resources/templates/depot_etat/appel_loyer_du_" + factureLocataire.getPeriodeAppelLoyer()
                            + "_"
                            + factureLocataire.getIdLocataire() + ".pdf");
            System.out.println(factureLocataire.getIdLocataire() + "-" + factureLocataire.getNomLocataire() + " "
                    + factureLocataire.getPrenomLocataire());
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

}
