package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;

import java.util.List;

public interface EncaissementPrincipalService {
    boolean saveEncaissement(EncaissementPayloadDto dto);

    boolean saveEncaissementMasse(List<EncaissementPayloadDto> dtos);

    List<EncaissementPrincipalDTO> findAllEncaissement();

    double getTotalEncaissementByIdAppelLoyer(Long idAppelLoyer);

    EncaissementPrincipalDTO findEncaissementById(Long id);

    List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id);

    List<EncaissementPrincipalDTO> findAllEncaissementByIdLocataire(Long id);

    List<EncaissementPrincipalDTO> saveEncaissementAvecRetourDeList(EncaissementPayloadDto dto);

    double sommeEncaisserParJour(String jour);

    boolean delete(Long id);
}
