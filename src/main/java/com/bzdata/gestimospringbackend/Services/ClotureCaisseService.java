package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.ClotureCaisseDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ClotureCaisseService
  extends AbstractService<ClotureCaisseDto> {
  int countClotureCaisse(Long idCaisse);
  boolean saveClotureCaisse(ClotureCaisseDto dto);
  List<ClotureCaisseDto> findNonCloturerByDInferieurOuEgaleDate(
    Instant dateEnCours,Long idCaisse
  );
  List<ClotureCaisseDto> findNonCloturerByDate(Instant dateEnCours,Long idCaisse);
  List<ClotureCaisseDto> findNAllClotureCaisseByDate(Instant dateEnCours,Long idCaisse);
  List<ClotureCaisseDto>findAllByCaissier(Long idCaisse);
}
