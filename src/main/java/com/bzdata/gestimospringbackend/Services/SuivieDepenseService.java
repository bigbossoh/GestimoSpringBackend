package com.bzdata.gestimospringbackend.Services;

import java.time.LocalDate;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaisPeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaissementDto;


public interface SuivieDepenseService {
   List<SuivieDepenseDto> saveNewDepense(SuivieDepenseDto dto);
   boolean annulerTransactionByCodeTransaction(String codeTransation);
   boolean annulerTransactionById(String Id);
   List<SuivieDepenseDto> supprimerUneEcritureById(Long Id,Long idAgence);
   SuivieDepenseDto findById(Long id);
   SuivieDepenseDto findByCodeTransaction(String codeTransation);
   List<SuivieDepenseDto> findByDateEncaissement(SuivieDepenseEncaissementDto suivieDepenseEncaissementDto);
   List<SuivieDepenseDto> findByAllEncaissementByPeriode(SuivieDepenseEncaisPeriodeDto suivieDepenseEncaisPeriodeDto);
   List<SuivieDepenseDto> findAlEncaissementParAgence(Long idAgence);
   SuivieDepenseEncaisPeriodeDto totalSuiviDepenseEntreDeuxDate(Long idAgence,LocalDate debut,LocalDate fin);
}
