package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.*;

import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.repository.*;
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
    private final AppelLoyerRepository appelLoyerRepository;

    public AppelLoyer fromAppelLoyerDto(AppelLoyersFactureDto appelLoyersFactureDto){
        AppelLoyer appelLoyer= new AppelLoyer();
        BeanUtils.copyProperties(appelLoyersFactureDto,appelLoyer);
        BailLocation bail= new BailLocation();
        bail=bailLocationRepository.findById(appelLoyersFactureDto.getIdBailLocation()).orElse(null);
        if(bail!=null)
        appelLoyer.setBailLocationAppelLoyer(bail);
         return appelLoyer;
    }
    public AppelLoyersFactureDto fromAppelLoyer(AppelLoyer appelLoyer) {
        AppelLoyersFactureDto appelLoyersFactureDto= new AppelLoyersFactureDto();
        BeanUtils.copyProperties(appelLoyer,appelLoyersFactureDto);
        appelLoyersFactureDto.setAbrvCodeBail(appelLoyer.getBailLocationAppelLoyer().getAbrvCodeBail());

      //LOCATAIRE
        appelLoyersFactureDto.setPrenomLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getPrenom());
        appelLoyersFactureDto.setNomLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getNom());
        appelLoyersFactureDto.setGenreLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getGenre());
        appelLoyersFactureDto.setEmailLocatire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getEmail());
        appelLoyersFactureDto.setIdLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getId());
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
        appelLoyersFactureDto.setIdBailLocation(bailLocation.getId());
        appelLoyersFactureDto.setAbrvCodeBail(appelLoyer.getBailLocationAppelLoyer().getAbrvCodeBail());
        appelLoyersFactureDto.setNouveauMontantLoyer(bailLocation
                .getMontantLoyerBail()
                .stream()
                .filter(montantLoyerBail -> montantLoyerBail.isStatusLoyer() == true)
                .findFirst()
                .map(nouveauMontant -> nouveauMontant.getNouveauMontantLoyer())
                .orElse(null));
        appelLoyer.setMontantLoyerBailLPeriode(bailLocation
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
    public AnneeAppelLoyersDto fromAppelLoyerForAnnee(AppelLoyer appelLoyer){
        AnneeAppelLoyersDto anneeAppelLoyersDto=new AnneeAppelLoyersDto();
        BeanUtils.copyProperties(appelLoyer,anneeAppelLoyersDto);
        return anneeAppelLoyersDto;
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
    public EncaissementPrincipal fromEncaissementPrincipalDto(EncaissementPayloadDto encaissementPayloadDto){
        EncaissementPrincipal encaissementPrincipal=new EncaissementPrincipal();
        BeanUtils.copyProperties(encaissementPayloadDto,encaissementPrincipal);
        // Information sur l'appel loyer
        AppelLoyer appelLoyer=appelLoyerRepository.findById(encaissementPayloadDto.getIdAppelLoyer()).orElse(null);
        if(appelLoyer==null)
            throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found", ErrorCodes.APPELLOYER_NOT_FOUND);
        encaissementPrincipal.setAppelLoyerEncaissement(appelLoyer);
        return encaissementPrincipal;
    }
    public EncaissementPrincipalDTO fromEncaissementPrincipal(EncaissementPrincipal encaissementPrincipal){
        EncaissementPrincipalDTO encaissementPrincipalDTO= new EncaissementPrincipalDTO();
        BeanUtils.copyProperties(encaissementPrincipal,encaissementPrincipalDTO);
        encaissementPrincipalDTO.setAppelLoyersFactureDto(fromAppelLoyer(encaissementPrincipal.getAppelLoyerEncaissement()));
        return encaissementPrincipalDTO;
    }

}
