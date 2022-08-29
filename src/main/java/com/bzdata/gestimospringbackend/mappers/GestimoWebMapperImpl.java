package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.AgenceImmobilierDTO;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.Models.*;

import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GestimoWebMapperImpl {

    private final AgenceImmobiliereRepository agenceImmobiliereRepository;
    private final BienImmobilierRepository bienImmobilierRepository;
    private final BailLocationRepository bailLocationRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AppelLoyer fromAppelLoyerDto(AppelLoyersFactureDto appelLoyersFactureDto){
        AppelLoyer appelLoyer= new AppelLoyer();
        BeanUtils.copyProperties(appelLoyersFactureDto,appelLoyer);
         return appelLoyer;
    }
    public AppelLoyersFactureDto fromAppelLoyer(AppelLoyer appelLoyer) {
        AppelLoyersFactureDto appelLoyersFactureDto= new AppelLoyersFactureDto();
        BeanUtils.copyProperties(appelLoyer,appelLoyersFactureDto);
        appelLoyersFactureDto.setAbrvCodeBail(appelLoyer.getBailLocationAppelLoyer().getAbrvCodeBail());
         appelLoyer.setMontantBailLPeriode(appelLoyer.getBailLocationAppelLoyer().getMontantCautionBail());
      //LOCATAIRE
        appelLoyersFactureDto.setPrenomLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getPrenom());
        appelLoyersFactureDto.setNomLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getNom());
        appelLoyersFactureDto.setGenreLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getGenre());
        //AGENCE
        AgenceImmobiliere agenceImmobiliere=agenceImmobiliereRepository.findById(appelLoyer.getIdAgence()).orElse(null);
        if(agenceImmobiliere==null)
            throw new EntityNotFoundException("Agence Immobilier  from GestimoMapper not found", ErrorCodes.AGENCE_NOT_FOUND);
        appelLoyersFactureDto.setNomAgence(agenceImmobiliere.getNomAgence());
        appelLoyersFactureDto.setTelAgence(agenceImmobiliere.getTelAgence());
        appelLoyersFactureDto.setCompteContribuableAgence(agenceImmobiliere.getCompteContribuable());
        appelLoyersFactureDto.setEmailAgence(agenceImmobiliere.getEmailAgence());
        appelLoyersFactureDto.setMobileAgence(agenceImmobiliere.getMobileAgence());
        appelLoyersFactureDto.setRegimeFiscaleAgence(agenceImmobiliere.getRegimeFiscaleAgence());
        appelLoyersFactureDto.setFaxAgence(agenceImmobiliere.getFaxAgence());
        appelLoyersFactureDto.setSigleAgence(agenceImmobiliere.getSigleAgence());

//        BienImmobilier
        Bienimmobilier bienImmobilier=bienImmobilierRepository.findById(appelLoyer.getBailLocationAppelLoyer().getBienImmobilierOperation().getId())
                .orElse(null);
        if(bienImmobilier == null)
            throw new EntityNotFoundException("Bien immobilier from GestimoMapper not found", ErrorCodes.BIEN_IMMOBILIER_NOT_FOUND);
        appelLoyersFactureDto.setAbrvBienimmobilier(bienImmobilier.getAbrvBienimmobilier());
        StringBuilder str = new StringBuilder(bienImmobilier.getNomBien());
        str.delete(0,14);
        appelLoyersFactureDto.setBienImmobilierFullName(str.toString());
        //Bail
        BailLocation bailLocation=bailLocationRepository.findById(appelLoyer.getBailLocationAppelLoyer().getId()).orElse(null);
        if(bailLocation==null)
            throw new EntityNotFoundException("bail from GestimoMapper not found", ErrorCodes.BAILLOCATION_NOT_FOUND);
        appelLoyersFactureDto.setAbrvCodeBail(appelLoyer.getBailLocationAppelLoyer().getAbrvCodeBail());
        appelLoyersFactureDto.setNouveauMontantLoyer(bailLocation
                .getMontantLoyerBail()
                .stream()
                .filter(montantLoyerBail -> montantLoyerBail.isStatusLoyer() == true)
                .findFirst()
                .map(nouveauMontant -> nouveauMontant.getNouveauMontantLoyer())
                .orElse(null));
    // Information sur le proprietaire
        Utilisateur utilisateur=utilisateurRepository.findById(appelLoyer.getBailLocationAppelLoyer().getBienImmobilierOperation().getUtilisateur().getId()).orElse(null);
        if(utilisateur==null)
            throw new EntityNotFoundException("utilisateur from GestimoMapper not found", ErrorCodes.UTILISATEUR_NOT_FOUND);
        appelLoyersFactureDto.setNomPropietaire(utilisateur.getNom());
        appelLoyersFactureDto.setPrenomPropietaire(utilisateur.getPrenom());
        appelLoyersFactureDto.setGenrePropietaire(utilisateur.getGenre());
        return appelLoyersFactureDto;
    }


    public AgenceImmobiliere fromAgenceImmobilierDTO(AgenceImmobilierDTO agenceImmobilierDTO){
        AgenceImmobiliere agenceImmo= new AgenceImmobiliere();
        BeanUtils.copyProperties(agenceImmobilierDTO,agenceImmo);
        return  agenceImmo;
    }
    public AgenceImmobilierDTO fromAgenceImmobilier(AgenceImmobiliere agenceImmobilier){
        AgenceImmobilierDTO agenceImmoDTO= new AgenceImmobilierDTO();
        BeanUtils.copyProperties(agenceImmobilier,agenceImmoDTO);
        return  agenceImmoDTO;

    }

}
