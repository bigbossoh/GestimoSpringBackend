package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.PrintService;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrintServiceImpl implements PrintService {
    ResourceLoader resourceLoader;
    final DataSource dataSourceSQL;
    final AppelLoyerService appelLoyerService;

    @Override
    public byte[] quittanceLoyer(Long id) throws FileNotFoundException, JRException, SQLException {
        String path = "src/main/resources/templates";

        File file = ResourceUtils.getFile(path+"/print/Recu_paiement.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idQuit", id);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
        JasperExportManager.exportReportToPdfFile(print, path + "/quittance" + id + ".pdf");

        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public byte[] quittancePeriode(String periode, String proprio, Long idAgence)
            throws FileNotFoundException, JRException, SQLException {

        // try {
        //     String path = "src/main/resources/templates";
        //   //  InputStream logoMagiser = resourceLoader.getResource(path+"/print/magiser.jpeg").getInputStream();
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        String path = "src/main/resources/templates";
        File file = ResourceUtils.getFile(path+"/print/quittance_appel_loyer.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PARAMETER_PERIODE", periode);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
        JasperExportManager.exportReportToPdfFile(print, path + "/depot_etat/appel_loyer_du_" + periode + ".pdf");
        // String fichier = path + "/depot_etat/appel_loyer_du_" + periode + ".pdf";
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public byte[] quittancePeriodeString(String periode, Long idAgence, String proprio)
            throws FileNotFoundException, JRException, SQLException {

        try {
            String path = "src/main/resources/templates";
            InputStream logoMagiser = resourceLoader.getResource(path+"/print/magiser.jpeg")
                    .getInputStream();

            File file = ResourceUtils.getFile(path+"/print/quittance_appel_loyer.jrxml");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("PARAMETER_PERIODE", periode);
            parameters.put("PARAMETER_AGENCE", idAgence);
            parameters.put("NOM_PROPRIO", proprio);
            parameters.put("LOGO", logoMagiser);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            File di = new File(path + "/depot_etat");
            boolean di1 = di.mkdirs();
            if (di1) {
                System.out.println("Folder is created successfully");
            } else {
                System.out.println("Error Found!");
            }
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
            JasperExportManager.exportReportToPdfFile(print, path + "/depot_etat/appel_loyer_du_" + periode + ".pdf");
            log.info("Le fichier {}", path + "/depot_etat/appel_loyer_du_" + periode + ".pdf");
           // ENVOI DE MESSAGCE DE QUITTANCE
           // boolean sms_envoyer = appelLoyerService.sendSmsAppelLoyerGroupe(periode, idAgence);
            // if (sms_envoyer) {
            //     log.info("Sms Envoye {}", sms_envoyer);
            // }
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public byte[] quittancePeriodeById(String periode, Long id, String proprio)
            throws FileNotFoundException, JRException, SQLException {
        try {
            String path = "src/main/resources/templates";
            InputStream logoMagiser = resourceLoader.getResource(path+"/print/magiser.jpeg")
                    .getInputStream();

            File file = ResourceUtils.getFile(path+"/print/quittance_appel_loyer_indiv_pour_mail.jrxml");

            Map<String, Object> parameters = new HashMap<>();

            parameters.put("PARAMETER_PERIODE", periode);
            parameters.put("ID_UTILISATEUR", id.toString());
            parameters.put("NOM_PROPRIO", proprio);
            parameters.put("LOGO", logoMagiser);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            File di = new File(path + "/depot_etat");
            boolean di1 = di.mkdirs();
            if (di1) {
                System.out.println("Folder is created successfully");
            } else {
                System.out.println("Error Found!");
            }
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
            JasperExportManager.exportReportToPdfFile(print,
                    path + "/depot_etat/appel_loyer_du_" + periode + "_" + id + ".pdf");
            log.info("Le fichier {}", path + "/appel_loyer_du_" + periode + "_" + id + ".pdf");
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
