package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.EspeceEncaissementDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

public interface EncaissementPrincipalService {
    boolean saveEncaissement(EncaissementPayloadDto dto);
    List<EncaissementPrincipalDTO> findAllEncaissement();
    double getTotalEncaissementByIdAppelLoyer(Long idAppelLoyer);
    EncaissementPrincipalDTO findEncaissementById(Long id);
    List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id);
    List<EncaissementPrincipalDTO> findAllEncaissementByIdLocataire(Long id);
    boolean delete(Long id);
}
