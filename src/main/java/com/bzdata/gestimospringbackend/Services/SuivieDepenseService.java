package com.bzdata.gestimospringbackend.Services;

import java.time.LocalDate;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaisPeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.SuivieDepenseEncaissementDto;


public interface SuivieDepenseService {
   SuivieDepenseDto saveNewDepense(SuivieDepenseDto dto);
   boolean annulerTransactionByCodeTransaction(String codeTransation);
   boolean annulerTransactionById(String Id);
   boolean SupprimerUneEcritureById(String Id);
   SuivieDepenseDto findById(Long id);
   SuivieDepenseDto findByCodeTransaction(String codeTransation);
   List<SuivieDepenseDto> findByDateEncaissement(SuivieDepenseEncaissementDto suivieDepenseEncaissementDto);
   List<SuivieDepenseDto> findByAllEncaissementByPeriode(SuivieDepenseEncaisPeriodeDto suivieDepenseEncaisPeriodeDto);
   List<SuivieDepenseDto> findAlEncaissementParAgence(Long idAgence);
}
