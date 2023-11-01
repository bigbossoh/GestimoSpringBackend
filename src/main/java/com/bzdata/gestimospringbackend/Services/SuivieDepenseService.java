package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaisPeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaissementDto;
import java.time.LocalDate;
import java.util.List;

public interface SuivieDepenseService {
  List<SuivieDepenseDto> saveNewDepense(SuivieDepenseDto dto);
  boolean annulerTransactionByCodeTransaction(String codeTransation);
  boolean annulerTransactionById(String Id);
  List<SuivieDepenseDto> supprimerUneEcritureById(Long Id, Long idAgence);
  SuivieDepenseDto findById(Long id);
  SuivieDepenseDto findByCodeTransaction(String codeTransation);
  List<SuivieDepenseDto> findByDateEncaissement(
    SuivieDepenseEncaissementDto suivieDepenseEncaissementDto
  );
  List<SuivieDepenseDto> findByAllEncaissementByPeriode(
    SuivieDepenseEncaisPeriodeDto suivieDepenseEncaisPeriodeDto
  );
  int countSuiviNonCloturerAvantDate(LocalDate dateEncaii,Long idCreateur);
  List<SuivieDepenseDto> findAlEncaissementParAgence(Long idAgence);
  SuivieDepenseEncaisPeriodeDto totalSuiviDepenseEntreDeuxDate(
    Long idAgence,
    LocalDate debut,
    LocalDate fin
  );
  List<SuivieDepenseDto> listSuiviDepenseEntreDeuxDate(
    Long idAgence,
    LocalDate debut,
    LocalDate fin
  );
}
