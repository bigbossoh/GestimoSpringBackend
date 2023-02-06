package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.BailModifDto;
import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;

public interface BailService {
    boolean closeBail(Long id);

    OperationDto modifierUnBail(BailModifDto dto);

    int nombreBauxActifs(Long idAgence);

    int nombreBauxNonActifs(Long idAgence);

    List<AppelLoyersFactureDto> findAllByIdBienImmobilier(Long id);

    List<OperationDto> findAllByIdLocataire(Long id);

    OperationDto findOperationById(Long id);

    List<OperationDto> findAllBauxLocation(Long idAgence);

    boolean deleteOperationById(Long id);

    LocataireEncaisDTO bailBayLocataireEtBien(Long locataire,Long bienImmobilier);

}
