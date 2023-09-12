package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.ReservationAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ReservationSaveOrUpdateDto;
import java.util.List;

public interface ReservationService
  extends AbstractService<ReservationSaveOrUpdateDto> {
  public ReservationAfficheDto saveOrUpdateGood(ReservationSaveOrUpdateDto dto);
  public List<ReservationAfficheDto> findAlGood();
  public ReservationAfficheDto findByIdGood(Long id);
}
