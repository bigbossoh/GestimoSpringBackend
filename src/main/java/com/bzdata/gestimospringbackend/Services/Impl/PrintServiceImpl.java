package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bzdata.gestimospringbackend.Services.PrintService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrintServiceImpl implements PrintService {

    final DataSource dataSourceSQL;

    @Override
    public byte[] quittanceLoyer(Long id) throws FileNotFoundException, JRException, SQLException {
        String path = "src/main/resources/templates";

        File file = ResourceUtils.getFile("classpath:templates/print/Recu_paiement.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idQuit", id);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
        JasperExportManager.exportReportToPdfFile(print, path + "/quittance" + id + ".pdf");

        return JasperExportManager.exportReportToPdf(print);
    }

    // @Override
    // public byte[] quittanceLoyerGrouperParPeriode(String periode)
    // throws FileNotFoundException, JRException, SQLException {
    // String path = "src/main/resources/templates";
    // File file =
    // ResourceUtils.getFile("classpath:templates/print/quittance_appel_loyer.jrxml");
    // Map<String, Object> parameters = new HashMap<>();
    // parameters.put("PARAMETER_PERIODE", periode);
    // JasperReport jasperReport =
    // JasperCompileManager.compileReport(file.getAbsolutePath());

    // JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters,
    // dataSourceSQL.getConnection());
    // JasperExportManager.exportReportToPdfFile(print, path + "/depot_etat/" +
    // periode + ".pdf");
    // return JasperExportManager.exportReportToPdf(print);
    // }

    @Override
    public byte[] quittancePeriode(String periode)
            throws FileNotFoundException, JRException, SQLException {
        String path = "src/main/resources/templates";
        File file = ResourceUtils.getFile("classpath:templates/print/quittance_appel_loyer.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PARAMETER_PERIODE", periode);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
        JasperExportManager.exportReportToPdfFile(print, path + "/depot_etat/appel_loyer_du_" + periode + ".pdf");
        // String fichier = path + "/depot_etat/appel_loyer_du_" + periode + ".pdf";
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public String quittancePeriodeString(String periode) throws FileNotFoundException, JRException, SQLException {

        try {
            String path = "src/main/resources/templates";
            File file = ResourceUtils.getFile("classpath:templates/print/quittance_appel_loyer.jrxml");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("PARAMETER_PERIODE", periode);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
            JasperExportManager.exportReportToPdfFile(print, path + "/depot_etat/appel_loyer_du_" + periode + ".pdf");
            return "Impression réussie";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Un probleme est survenu";
        }

    }

    // @Override
    // public byte[] readQuittance(String periode) throws FileNotFoundException,
    // JRException, SQLException {
    // // TODO Auto-generated method stub
    // return null;
    // }

}
