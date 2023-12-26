package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.ReservationAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationRequestDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationSaveOrUpdateDto;
import java.util.List;

public interface ReservationService
  extends AbstractService<ReservationSaveOrUpdateDto> {
  public ReservationAfficheDto saveOrUpdateGood(ReservationRequestDto dto);

  List<ReservationAfficheDto> findAlGood();

  public ReservationAfficheDto findByIdGood(Long id);

  public boolean saveOrUpdateReservation(
    ReservationRequestDto dto
  );
  List<EncaissementPrincipalDTO> saveEncaissementReservationAvecREsrourDeList(EncaissementPrincipalDTO dto);
    List<ReservationAfficheDto> listeDesReservationParAgence(Long idAgence);
      List<ReservationAfficheDto> listeDesReservationOuvertParAgence(Long idAgence);
  }
