package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.*;

import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.repository.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GestimoWebMapperImpl {

    final AgenceImmobiliereRepository agenceImmobiliereRepository;
    final BienImmobilierRepository bienImmobilierRepository;
    final BailLocationRepository bailLocationRepository;
    final UtilisateurRepository utilisateurRepository;
    final AppelLoyerRepository appelLoyerRepository;
    final EtageRepository etageRepository;
    final AppartementRepository appartementRepository;
    final MagasinRepository magasinRepository;

    // AppelLoyer
    public AppelLoyer fromAppelLoyerDto(AppelLoyersFactureDto appelLoyersFactureDto) {
        AppelLoyer appelLoyer = new AppelLoyer();
        BeanUtils.copyProperties(appelLoyersFactureDto, appelLoyer);
        BailLocation bail = new BailLocation();
        bail = bailLocationRepository.findById(appelLoyersFactureDto.getIdBailLocation()).orElse(null);
        if (bail != null)
            appelLoyer.setBailLocationAppelLoyer(bail);
        return appelLoyer;
    }

    public AppelLoyersFactureDto fromAppelLoyer(AppelLoyer appelLoyer) {
        AppelLoyersFactureDto appelLoyersFactureDto = new AppelLoyersFactureDto();
        BeanUtils.copyProperties(appelLoyer, appelLoyersFactureDto);
        appelLoyersFactureDto.setAbrvCodeBail(appelLoyer.getBailLocationAppelLoyer().getAbrvCodeBail());

        // LOCATAIRE
        appelLoyersFactureDto
                .setPrenomLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getPrenom());
        appelLoyersFactureDto
                .setNomLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getNom());
        appelLoyersFactureDto
                .setGenreLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getGenre());
        appelLoyersFactureDto
                .setEmailLocatire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getEmail());
        appelLoyersFactureDto.setIdLocataire(appelLoyer.getBailLocationAppelLoyer().getUtilisateurOperation().getId());
        // AGENCE
        AgenceImmobiliere agenceImmobiliere = agenceImmobiliereRepository.findById(appelLoyer.getIdAgence())
                .orElse(null);
        if (agenceImmobiliere == null)
            throw new EntityNotFoundException("Agence Immobilier  from GestimoMapper not found",
                    ErrorCodes.AGENCE_NOT_FOUND);
        appelLoyersFactureDto.setNomAgence(agenceImmobiliere.getNomAgence());
        appelLoyersFactureDto.setTelAgence(agenceImmobiliere.getTelAgence());
        appelLoyersFactureDto.setCompteContribuableAgence(agenceImmobiliere.getCompteContribuable());
        appelLoyersFactureDto.setEmailAgence(agenceImmobiliere.getEmailAgence());
        appelLoyersFactureDto.setMobileAgence(agenceImmobiliere.getMobileAgence());
        appelLoyersFactureDto.setRegimeFiscaleAgence(agenceImmobiliere.getRegimeFiscaleAgence());
        appelLoyersFactureDto.setFaxAgence(agenceImmobiliere.getFaxAgence());
        appelLoyersFactureDto.setSigleAgence(agenceImmobiliere.getSigleAgence());

        // BienImmobilier
        Bienimmobilier bienImmobilier = bienImmobilierRepository
                .findById(appelLoyer.getBailLocationAppelLoyer().getBienImmobilierOperation().getId())
                .orElse(null);
        if (bienImmobilier == null)
            throw new EntityNotFoundException("Bien immobilier from GestimoMapper not found",
                    ErrorCodes.BIEN_IMMOBILIER_NOT_FOUND);
        appelLoyersFactureDto.setAbrvBienimmobilier(bienImmobilier.getCodeAbrvBienImmobilier());
        StringBuilder str = new StringBuilder(bienImmobilier.getNomCompletBienImmobilier());
        str.delete(0, 14);
        appelLoyersFactureDto.setBienImmobilierFullName(str.toString());
        // Bail
        BailLocation bailLocation = bailLocationRepository.findById(appelLoyer.getBailLocationAppelLoyer().getId())
                .orElse(null);
        if (bailLocation == null)
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
        Utilisateur utilisateur = utilisateurRepository
                .findById(appelLoyer.getBailLocationAppelLoyer().getBienImmobilierOperation()
                        .getUtilisateurProprietaire().getId())
                .orElse(null);
        if (utilisateur == null)
            throw new EntityNotFoundException("utilisateur from GestimoMapper not found",
                    ErrorCodes.UTILISATEUR_NOT_FOUND);
        appelLoyersFactureDto.setNomPropietaire(utilisateur.getNom());
        appelLoyersFactureDto.setPrenomPropietaire(utilisateur.getPrenom());
        appelLoyersFactureDto.setGenrePropietaire(utilisateur.getGenre());
        return appelLoyersFactureDto;
    }

    public AnneeAppelLoyersDto fromAppelLoyerForAnnee(AppelLoyer appelLoyer) {
        AnneeAppelLoyersDto anneeAppelLoyersDto = new AnneeAppelLoyersDto();
        BeanUtils.copyProperties(appelLoyer, anneeAppelLoyersDto);
        return anneeAppelLoyersDto;
    }

    // AgenceImmobiliere
    public AgenceImmobiliere fromAgenceImmobilierDTO(AgenceImmobilierDTO agenceImmobilierDTO) {
        AgenceImmobiliere agenceImmo = new AgenceImmobiliere();
        BeanUtils.copyProperties(agenceImmobilierDTO, agenceImmo);
        return agenceImmo;
    }

    public AgenceImmobilierDTO fromAgenceImmobilier(AgenceImmobiliere agenceImmobilier) {
        AgenceImmobilierDTO agenceImmoDTO = new AgenceImmobilierDTO();
        BeanUtils.copyProperties(agenceImmobilier, agenceImmoDTO);
        return agenceImmoDTO;

    }

    // Immeuble
    public ImmeubleAfficheDto fromImmeuble(Immeuble immeuble) {
        ImmeubleAfficheDto immeubleAfficheDto = new ImmeubleAfficheDto();
        BeanUtils.copyProperties(immeuble, immeubleAfficheDto);
        immeubleAfficheDto.setNomPropio(immeuble.getUtilisateurProprietaire().getNom());
        immeubleAfficheDto.setPrenomProprio(immeuble.getUtilisateurProprietaire().getPrenom());
        return immeubleAfficheDto;
    }

    public Immeuble fromImmeubleDTO(ImmeubleAfficheDto immeubleAfficheDto) {
        Immeuble immeuble = new Immeuble();
        BeanUtils.copyProperties(immeubleAfficheDto, immeuble);
        return immeuble;

    }

    // Encaissement Principal
    public EncaissementPrincipal fromEncaissementPrincipalDto(EncaissementPayloadDto encaissementPayloadDto) {
        EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
        BeanUtils.copyProperties(encaissementPayloadDto, encaissementPrincipal);

        AppelLoyer appelLoyer = appelLoyerRepository.findById(encaissementPayloadDto.getIdAppelLoyer()).orElse(null);
        if (appelLoyer == null)
            throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found",
                    ErrorCodes.APPELLOYER_NOT_FOUND);
        encaissementPrincipal.setAppelLoyerEncaissement(appelLoyer);
        return encaissementPrincipal;
    }

    public EncaissementPrincipalDTO fromEncaissementPrincipal(EncaissementPrincipal encaissementPrincipal) {
        EncaissementPrincipalDTO encaissementPrincipalDTO = new EncaissementPrincipalDTO();
        BeanUtils.copyProperties(encaissementPrincipal, encaissementPrincipalDTO);
        encaissementPrincipalDTO
                .setAppelLoyersFactureDto(fromAppelLoyer(encaissementPrincipal.getAppelLoyerEncaissement()));
        return encaissementPrincipalDTO;
    }

    // MAPPER DES ETAGES
    public EtageAfficheDto fromEtage(Etage etage) {
        EtageAfficheDto etageAfficheDto = new EtageAfficheDto();
        BeanUtils.copyProperties(etage, etageAfficheDto);
        Etage etageFound = etageRepository.findById(etage.getId()).orElse(null);
        if (etageFound == null)
            throw new EntityNotFoundException("Etage from GestimoMapper not found",
                    ErrorCodes.BIEN_IMMOBILIER_NOT_FOUND);
        Log.info("Le id est le suivant {} " + etage.getId());
        etageAfficheDto.setId(etage.getId());
        etageAfficheDto.setNomPropio(etageFound.getImmeuble().getUtilisateurProprietaire().getNom());
        etageAfficheDto.setPrenomProprio(etageFound.getImmeuble().getUtilisateurProprietaire().getPrenom());
        etageAfficheDto.setAbrvEtage(etage.getCodeAbrvEtage());
        etageAfficheDto.setNomEtage(etage.getNomBaptiserEtage());
        etageAfficheDto.setNomImmeuble(etage.getImmeuble().getNomBaptiserImmeuble());
        return etageAfficheDto;

    }

    public Appartement fromAppartementDto(AppartementDto appartementDto) {
        Appartement appartement = new Appartement();
        BeanUtils.copyProperties(appartementDto, appartement);
        return appartement;
    }

    public AppartementDto fromAppartement(Appartement appartement) {
        AppartementDto appartementDto = new AppartementDto();
        BeanUtils.copyProperties(appartement, appartementDto);
        appartementDto.setFullNameProprio(
                appartement.getEtageAppartement().getImmeuble().getUtilisateurProprietaire().getNom() + " " +
                        appartement.getEtageAppartement().getImmeuble().getUtilisateurProprietaire().getPrenom());
        return appartementDto;
    }

    // MAGASIN
    public MagasinDto fromMagasin(Magasin magasin) {
        MagasinDto magasinDto = new MagasinDto();
        BeanUtils.copyProperties(magasin, magasinDto);
        magasinDto.setIdSite(magasin.getSite().getId());
        magasinDto.setIdUtilisateur(magasin.getUtilisateurProprietaire().getId());
        magasinDto.setProprietaire(magasin.getUtilisateurProprietaire().getNom() + " "
                + magasin.getUtilisateurProprietaire().getPrenom());
        return magasinDto;
    }

    public Magasin fromMagasinDto(MagasinDto magasinDto) {
        Magasin magasin = new Magasin();
        BeanUtils.copyProperties(magasinDto, magasin);
        return magasin;
    }

    // VILLA
    public VillaDto fromVilla(Villa villa) {
        VillaDto villaDto = new VillaDto();
        BeanUtils.copyProperties(villa, villaDto);
        villaDto.setIdSite(villa.getSite().getId());
        villaDto.setIdUtilisateur(villa.getUtilisateurProprietaire().getId());
        villaDto.setProprietaire(
                villa.getUtilisateurProprietaire().getNom() + " " + villa.getUtilisateurProprietaire().getPrenom());
        return villaDto;
    }

    public Villa fromVillaDto(VillaDto villaDto) {
        Villa villa = new Villa();
        BeanUtils.copyProperties(villaDto, villa);
        return villa;
    }

    // UTILISATEUR MAPPER
    public UtilisateurAfficheDto fromUtilisateur(Utilisateur utilisateur) {
        UtilisateurAfficheDto utilisateurAfficheDto = new UtilisateurAfficheDto();
        BeanUtils.copyProperties(utilisateur, utilisateurAfficheDto);
        return utilisateurAfficheDto;
    }

    // BIEN IMMOBILIER MAPPER
    public BienImmobilierAffiheDto fromBienImmobilier(Bienimmobilier bienimmobilier) {
        BienImmobilierAffiheDto bienImmobilierAffiheDto = new BienImmobilierAffiheDto();
        BeanUtils.copyProperties(bienimmobilier, bienImmobilierAffiheDto);
        bienImmobilierAffiheDto.setNomPrenomProprio(bienimmobilier.getUtilisateurProprietaire().getNom() + " "
                + bienimmobilier.getUtilisateurProprietaire().getPrenom());
        return bienImmobilierAffiheDto;
    }
}
