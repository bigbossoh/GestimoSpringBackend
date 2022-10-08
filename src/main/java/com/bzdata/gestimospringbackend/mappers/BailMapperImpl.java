package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyerAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.BailAppartementDto;
import com.bzdata.gestimospringbackend.DTOs.BailMagasinDto;
import com.bzdata.gestimospringbackend.DTOs.BailVillaDto;
import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;
import com.bzdata.gestimospringbackend.DTOs.PeriodeDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BailMapperImpl {
    // BAIL VILLA MAPPER
    public BailVillaDto fromBailVilla(BailLocation bailLocation) {
        BailVillaDto bailLocaDto = new BailVillaDto();
        BeanUtils.copyProperties(bailLocation, bailLocaDto);
        bailLocaDto.setIdVilla(bailLocation.getBienImmobilierOperation().getId());
        bailLocaDto.setFullNomLocatire(bailLocation.getUtilisateurOperation().getNom() + " "
                + bailLocation.getUtilisateurOperation().getPrenom());
        bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
        return bailLocaDto;
    }

    // BAIL MAGASIN MAPPER
    public BailMagasinDto fromBailMagasin(BailLocation bailLocation) {
        BailMagasinDto bailLocaDto = new BailMagasinDto();
        BeanUtils.copyProperties(bailLocation, bailLocaDto);

        bailLocaDto.setNomLocataire(bailLocation.getUtilisateurOperation().getNom() + " "
                + bailLocation.getUtilisateurOperation().getPrenom());
        bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
        bailLocaDto.setCodeBien(bailLocation.getBienImmobilierOperation().getCodeAbrvBienImmobilier());
        return bailLocaDto;
    }

    // BAIL MAGASIN MAPPER
    public BailAppartementDto fromBailAppartement(BailLocation bailLocation) {
        BailAppartementDto bailLocaDto = new BailAppartementDto();
        BeanUtils.copyProperties(bailLocation, bailLocaDto);
        bailLocaDto.setIdBienImmobilier(bailLocation.getBienImmobilierOperation().getId());
        bailLocaDto.setNomLocataire(bailLocation.getUtilisateurOperation().getNom() + " "
                + bailLocation.getUtilisateurOperation().getPrenom());
        bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
        bailLocaDto.setCodeBien(bailLocation.getBienImmobilierOperation().getCodeAbrvBienImmobilier());
        return bailLocaDto;
    }
        // BAIL MAGASIN MAPPER
        public OperationDto fromOperation(BailLocation bailLocation) {
                OperationDto bailLocaDto = new OperationDto();
                BeanUtils.copyProperties(bailLocation, bailLocaDto);
                bailLocaDto.setIdBienImmobilier(bailLocation.getBienImmobilierOperation().getId());
                bailLocaDto.setUtilisateurOperation(bailLocation.getUtilisateurOperation().getNom() + " "
                                + bailLocation.getUtilisateurOperation().getPrenom());
                bailLocaDto.setIdLocataire(bailLocation.getUtilisateurOperation().getId());
                bailLocaDto
                                .setCodeAbrvBienImmobilier(
                                                bailLocation.getBienImmobilierOperation().getCodeAbrvBienImmobilier());
                return bailLocaDto;
        }

        public LocataireEncaisDTO fromOperationBailLocation(BailLocation bailLocation) {
                LocataireEncaisDTO locataireEncaisDTO = new LocataireEncaisDTO();
                locataireEncaisDTO.setId(bailLocation.getUtilisateurOperation().getId());
             
                locataireEncaisDTO.setNom(bailLocation.getUtilisateurOperation().getNom());
                locataireEncaisDTO.setPrenom(bailLocation.getUtilisateurOperation().getPrenom());
                return locataireEncaisDTO;
        }


}
