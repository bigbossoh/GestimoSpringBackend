package com.bzdata.gestimospringbackend.Services;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;

public interface PrintService {
    byte[] quittanceLoyer(Long id) throws FileNotFoundException, JRException, SQLException;

    byte[] quittancePeriode(String periode, String proprio,Long idAgence) throws FileNotFoundException, JRException, SQLException;

    byte[] quittancePeriodeString(String periode,Long idAgence,String proprio) throws FileNotFoundException, JRException, SQLException;

    byte[] printQuittancePeriodeString(String periode,Long idAgence,String proprio) throws FileNotFoundException, JRException, SQLException;
    byte[] printRecuPaiement(Long idEncaissemnt) throws FileNotFoundException, JRException, SQLException;
    byte[] quittancePeriodeById(String periode, Long id, String proprio)
            throws FileNotFoundException, JRException, SQLException;

  byte[] recuReservation(Long idEncaisse, String proprio,Long idAgence) throws FileNotFoundException, JRException, SQLException;

    // InputStreamSource downloadReportByPeriode(String periode,Long idAgence,String proprio) throws IOException,FileNotFoundException, JRException, SQLException;
}
