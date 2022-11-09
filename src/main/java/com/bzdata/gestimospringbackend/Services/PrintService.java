package com.bzdata.gestimospringbackend.Services;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;

public interface PrintService {
    byte[] quittanceLoyer(Long id) throws FileNotFoundException, JRException, SQLException;

    // byte[] quittanceLoyerGrouperParPeriode(String periode) throws
    // FileNotFoundException, JRException, SQLException;
    // byte[] readQuittance(String periode) throws FileNotFoundException,
    // JRException, SQLException;
    byte[] quittancePeriode(String periode, String proprio,Long idAgence) throws FileNotFoundException, JRException, SQLException;

    byte[] quittancePeriodeString(String periode,Long idAgence,String proprio) throws FileNotFoundException, JRException, SQLException;
    byte[] quittancePeriodeById(String periode,Long id,String proprio) throws FileNotFoundException, JRException, SQLException;
}
