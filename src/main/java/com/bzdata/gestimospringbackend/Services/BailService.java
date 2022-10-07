package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;

public interface BailService {
    boolean closeBail(Long id);
    int nombreBauxActifs();
    List<AppelLoyersFactureDto> findAllByIdBienImmobilier(Long id);
    List<OperationDto> findAllByIdLocataire(Long id);
    List<OperationDto> findAllBauxLocation();
    boolean deleteOperationById(Long id);
}
