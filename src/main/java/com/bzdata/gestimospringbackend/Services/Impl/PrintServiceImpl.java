package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.Models.hotel.EncaissementReservation;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.PrintService;
import com.bzdata.gestimospringbackend.repository.EncaissementReservationRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
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
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrintServiceImpl implements PrintService {

  ResourceLoader resourceLoader;
  final DataSource dataSourceSQL;
  final EncaissementReservationRepository encaissementReservationRepository;

  @Override
  public byte[] quittanceLoyer(Long id)
    throws FileNotFoundException, JRException, SQLException {
    String path = "src/main/resources/templates";

    File file = ResourceUtils.getFile(path + "/print/Recu_paiement.jrxml");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("idQuit", id);
    JasperReport jasperReport = JasperCompileManager.compileReport(
      file.getAbsolutePath()
    );

    JasperPrint print = JasperFillManager.fillReport(
      jasperReport,
      parameters,
      dataSourceSQL.getConnection()
    );
    JasperExportManager.exportReportToPdfFile(
      print,
      path + "/quittance" + id + ".pdf"
    );

    return JasperExportManager.exportReportToPdf(print);
  }

  @Override
  public byte[] quittancePeriode(String periode, String proprio, Long idAgence)
    throws FileNotFoundException, JRException, SQLException {
    // ICI LE CONTROLLEUR ////
    String path = "src/main/resources/templates";
    File file = ResourceUtils.getFile(
      path + "/print/quittance_appel_loyer.jrxml"
    );
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("PARAMETER_PERIODE", periode);
    JasperReport jasperReport = JasperCompileManager.compileReport(
      file.getAbsolutePath()
    );

    JasperPrint print = JasperFillManager.fillReport(
      jasperReport,
      parameters,
      dataSourceSQL.getConnection()
    );
    JasperExportManager.exportReportToPdfFile(
      print,
      path + "/depot_etat/appel_loyer_du_" + periode + ".pdf"
    );
    // String fichier = path + "/depot_etat/appel_loyer_du_" + periode + ".pdf";
    return JasperExportManager.exportReportToPdf(print);
  }

  @Override
  public byte[] quittancePeriodeString(
    String periode,
    Long idAgence,
    String proprio
  ) throws FileNotFoundException, JRException, SQLException {
    try {
      String path = "src/main/resources/templates";
      File file = ResourceUtils.getFile(
        path + "/print/quittanceappelloyer.jrxml"
      );
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("PARAMETER_PERIODE", periode);
      parameters.put("PARAMETER_AGENCE", idAgence);
      parameters.put("NOM_PROPRIO", proprio);

      JasperReport jasperReport = JasperCompileManager.compileReport(
        file.getAbsolutePath()
      );
      File di = new File(path + "/depot_etat");
      boolean di1 = di.mkdirs();
      if (di1) {
        System.out.println("Folder is created successfully");
      }
      JasperPrint print = JasperFillManager.fillReport(
        jasperReport,
        parameters,
        dataSourceSQL.getConnection()
      );
      JasperExportManager.exportReportToPdfFile(
        print,
        path + "/depot_etat/appel_loyer_du_" + periode + ".pdf"
      );

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

      File filemagiser = ResourceUtils.getFile(path + "/print/magiser.jpeg");

      InputStream logoMagiser = resourceLoader
        .getResource(filemagiser.getPath())
        .getInputStream();
      File file = ResourceUtils.getFile(
        path + "/print/quittance_appel_loyer_indiv_pour_mail.jrxml"
      );

      Map<String, Object> parameters = new HashMap<>();

      parameters.put("PARAMETER_PERIODE", periode);
      parameters.put("ID_UTILISATEUR", id.toString());
      parameters.put("NOM_PROPRIO", proprio);
      parameters.put("LOGO", logoMagiser);
      JasperReport jasperReport = JasperCompileManager.compileReport(
        file.getAbsolutePath()
      );
      File di = new File(path + "/depot_etat");
      boolean di1 = di.mkdirs();
      if (di1) {
        System.out.println("Folder is created successfully");
      }
      JasperPrint print = JasperFillManager.fillReport(
        jasperReport,
        parameters,
        dataSourceSQL.getConnection()
      );
      JasperExportManager.exportReportToPdfFile(
        print,
        path + "/depot_etat/appel_loyer_du_" + periode + "_" + id + ".pdf"
      );
      log.info(
        "Le fichier {}",
        path + "/appel_loyer_du_" + periode + "_" + id + ".pdf"
      );
      return JasperExportManager.exportReportToPdf(print);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @Override
  public byte[] printQuittancePeriodeString(
    String periode,
    Long idAgence,
    String proprio
  ) throws FileNotFoundException, JRException, SQLException {
    try {
      String path = "src/main/resources/templates";
      File file = ResourceUtils.getFile(
        path + "/print/quittanceappelloyer.jrxml"
      );
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("PARAMETER_PERIODE", periode);
      parameters.put("PARAMETER_AGENCE", idAgence);
      parameters.put("NOM_PROPRIO", proprio);

      JasperReport jasperReport = JasperCompileManager.compileReport(
        file.getAbsolutePath()
      );
      File di = new File(path + "/depot_etat");
      boolean di1 = di.mkdirs();
      JasperPrint print = JasperFillManager.fillReport(
        jasperReport,
        parameters,
        dataSourceSQL.getConnection()
      );
      JasperExportManager.exportReportToPdfFile(
        print,
        path + "/depot_etat/appel_loyer_du_" + periode + ".pdf"
      );
      log.info(
        "Le fichier {}",
        path + "/depot_etat/appel_loyer_du_" + periode + ".pdf"
      );
      return JasperExportManager.exportReportToPdf(print);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @Override
  public byte[] printRecuPaiement(Long idEncaissemnt)
    throws FileNotFoundException, JRException, SQLException {
    try {
      String path = "src/main/resources/templates";
      File file = ResourceUtils.getFile(
        path + "/print/recupaiementappelloyer.jrxml"
      );
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("PARAMETER_ENCAISSEMENT", idEncaissemnt);

      JasperReport jasperReport = JasperCompileManager.compileReport(
        file.getAbsolutePath()
      );
      File di = new File(path + "/depot_etat");
      boolean di1 = di.mkdirs();
      if (di1) {
        System.out.println("Folder is created successfully");
      }
      JasperPrint print = JasperFillManager.fillReport(
        jasperReport,
        parameters,
        dataSourceSQL.getConnection()
      );
      JasperExportManager.exportReportToPdfFile(
        print,
        path + "/depot_etat/recu_paiement_du" + idEncaissemnt + ".pdf"
      );

      return JasperExportManager.exportReportToPdf(print);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @Override
  public byte[] recuReservation(Long idEncaisse, String proprio, Long idAgence)
    throws FileNotFoundException, JRException, SQLException {
    try {
      String path = "src/main/resources/templates";
      File file = ResourceUtils.getFile(
        path + "/print/quittancereservation.jrxml"
      );
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("id_encaissement", idEncaisse);
      parameters.put("PARAMETER_AGENCE", idAgence);
      parameters.put("NOM_PROPRIO", proprio);

      JasperReport jasperReport = JasperCompileManager.compileReport(
        file.getAbsolutePath()
      );
      File di = new File(path + "/depot_etat");
      boolean di1 = di.mkdirs();
      if (di1) {
        System.out.println("Folder is created successfully");
      }
      JasperPrint print = JasperFillManager.fillReport(
        jasperReport,
        parameters,
        dataSourceSQL.getConnection()
      );
      JasperExportManager.exportReportToPdfFile(
        print,
        path + "/depot_etat/recu_de_" + idEncaisse + ".pdf"
      );
      log.info(
        "Le fichier {}",
        path + "/depot_etat/recu_de_" + idEncaisse + ".pdf"
      );
      return JasperExportManager.exportReportToPdf(print);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  @Override
  public byte[] recuReservationParIdReservation(
    Long idReservation,
    String proprio,
    Long idAgence
  ) throws FileNotFoundException, JRException, SQLException {
    Long idEncaisse = 0L;
    try {
      EncaissementReservation encaissementReservation = encaissementReservationRepository
        .findAll(Sort.by(Sort.Direction.DESC, "id"))
        .stream()
        .filter(enc -> enc.getReservation().getId() == idReservation)
        .findFirst()
        .orElse(null);
      log.info(" id is {}", encaissementReservation.getId());
      if (encaissementReservation != null) {
        idEncaisse = encaissementReservation.getId();
      }
      String path = "src/main/resources/templates";
      File file = ResourceUtils.getFile(
        path + "/print/quittancereservation.jrxml"
      );
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("id_encaissement", idEncaisse);
      parameters.put("PARAMETER_AGENCE", idAgence);
      parameters.put("NOM_PROPRIO", proprio);

      JasperReport jasperReport = JasperCompileManager.compileReport(
        file.getAbsolutePath()
      );
      File di = new File(path + "/depot_etat");
      boolean di1 = di.mkdirs();
      if (di1) {
        System.out.println("Folder is created successfully");
      }
      JasperPrint print = JasperFillManager.fillReport(
        jasperReport,
        parameters,
        dataSourceSQL.getConnection()
      );
      JasperExportManager.exportReportToPdfFile(
        print,
        path + "/depot_etat/recu_de_" + idEncaisse + ".pdf"
      );
      log.info(
        "Le fichier {}",
        path + "/depot_etat/recu_de_" + idEncaisse + ".pdf"
      );
      return JasperExportManager.exportReportToPdf(print);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
}
