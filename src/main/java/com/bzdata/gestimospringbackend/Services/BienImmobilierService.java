package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BienImmobilierAffiheDto;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;

public interface BienImmobilierService {
    List<BienImmobilierAffiheDto> findAll(Long idAgence);

    List<BienImmobilierAffiheDto> findAllBienOccuper(Long idAgence);

    Bienimmobilier findBienByBailEnCours(Long idBail);
}
