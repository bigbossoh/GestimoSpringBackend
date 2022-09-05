package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;


import java.util.List;

public interface EncaissementPrincipalService {
    boolean saveEncaissement(EncaissementPayloadDto dto);
    boolean saveEncaissementMasse(List<EncaissementPayloadDto> dtos);
    List<EncaissementPrincipalDTO> findAllEncaissement();
    double getTotalEncaissementByIdAppelLoyer(Long idAppelLoyer);
    EncaissementPrincipalDTO findEncaissementById(Long id);
    List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id);
    List<EncaissementPrincipal> findAllEncaissementByIdLocataire(Long id);
    boolean delete(Long id);
}
