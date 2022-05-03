package com.bzdata.gestimospringbackend.Services.Impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.bzdata.gestimospringbackend.Services.PrintService;

import org.springframework.core.io.ResourceLoader;
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

    final ResourceLoader resource;
    final DataSource dataSourceSQL;

    @Override
    public byte[] quittanceLoyer(Long id) throws FileNotFoundException, JRException, SQLException {

        String path = "src/main/resources/templates";

        File file = ResourceUtils.getFile("classpath:templates/print/Recu_paiement.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idQuit", id);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSourceSQL.getConnection());
        JasperExportManager.exportReportToPdfFile(print, path + "/testetat" + id + ".pdf");

        return JasperExportManager.exportReportToPdf(print);
    }

}
