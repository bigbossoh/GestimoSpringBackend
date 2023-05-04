package com.bzdata.gestimospringbackend.mappers;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.*;
import com.bzdata.gestimospringbackend.Models.hotel.CategorieChambre;
import com.bzdata.gestimospringbackend.Models.hotel.Reservation;
import com.bzdata.gestimospringbackend.Models.hotel.Prestation;
import com.bzdata.gestimospringbackend.Models.hotel.PrestationAdditionnelReservation;
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
    final ImageRepository imageRepository;
    final BienImmobilierRepository bienImmobilierRepository;
    final BailLocationRepository bailLocationRepository;
    final UtilisateurRepository utilisateurRepository;
    final AppelLoyerRepository appelLoyerRepository;
    final EtageRepository etageRepository;

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
        appelLoyersFactureDto.setNouveauMontantLoyer(appelLoyer.getMontantLoyerBailLPeriode());
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

    public AgenceRequestDto fromEntity(AgenceImmobiliere agenceImmobiliere) {
        AgenceRequestDto agenceImmobilierDTO = new AgenceRequestDto();
        BeanUtils.copyProperties(agenceImmobiliere, agenceImmobilierDTO);
        ImageModel imageModel = getImageData(agenceImmobiliere);
        agenceImmobilierDTO.setIdImage(imageModel.getId());
        agenceImmobilierDTO.setTypeImage(imageModel.getType());
        // agenceImmobilierDTO.setProfileAgenceUrl(imageModel.getProfileAgenceImageUrl());
        return agenceImmobilierDTO;
    }

    public CronMailDto fromCronMail(CronMail cronMail) {
        CronMailDto cronMailDto = new CronMailDto();
        BeanUtils.copyProperties(cronMail, cronMailDto);
        return cronMailDto;
    }

    private ImageModel getImageData(AgenceImmobiliere agenceImmobiliere) {
        ImageModel imageData = imageRepository.findByLogoAgence(agenceImmobiliere).orElse(null);
        if (imageData == null)
            throw new EntityNotFoundException("Image from GestimoMapper not found",
                    ErrorCodes.IMAGE_NOT_FOUND);
        return imageData;
    }

    public AgenceImmobilierDTO fromAgenceImmobilier(AgenceImmobiliere agenceImmobilier) {
        AgenceImmobilierDTO agenceImmoDTO = new AgenceImmobilierDTO();
        BeanUtils.copyProperties(agenceImmobilier, agenceImmoDTO);
        // ImageData imageData = getImageData(agenceImmobilier);
        // agenceImmoDTO.setIdImage(imageData.getId());
        // agenceImmoDTO.setTypeImage(imageData.getTypeImage());
        // agenceImmoDTO.setProfileAgenceUrl(imageData.getProfileAgenceImageUrl());
        return agenceImmoDTO;
    }

    // Immeuble
    public ImmeubleAfficheDto fromImmeuble(Immeuble immeuble) {
        ImmeubleAfficheDto immeubleAfficheDto = new ImmeubleAfficheDto();
        BeanUtils.copyProperties(immeuble, immeubleAfficheDto);
        immeubleAfficheDto.setNomPropio(immeuble.getUtilisateurProprietaire().getNom());
        immeubleAfficheDto.setPrenomProprio(immeuble.getUtilisateurProprietaire().getPrenom());
        immeubleAfficheDto.setAbrvNomImmeuble(immeuble.getCodeNomAbrvImmeuble());
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
        if (villa.getUtilisateurProprietaire().getId() != null || villa.getUtilisateurProprietaire().getId() != 0) {
            villaDto.setIdUtilisateur(villa.getUtilisateurProprietaire().getId());
            villaDto.setProprietaire(
                    villa.getUtilisateurProprietaire().getNom() + " " + villa.getUtilisateurProprietaire().getPrenom());
        }
        villaDto.setIdSite(villa.getSite().getId());
        return villaDto;
    }

    public Villa fromVillaDto(VillaDto villaDto) {
        Villa villa = new Villa();
        BeanUtils.copyProperties(villaDto, villa);
        return villa;
    }

    // UTILISATEUR MAPPER
    public static UtilisateurAfficheDto fromUtilisateurStatic(Utilisateur utilisateur) {
        UtilisateurAfficheDto utilisateurAfficheDto = new UtilisateurAfficheDto();
        BeanUtils.copyProperties(utilisateur, utilisateurAfficheDto);
        return utilisateurAfficheDto;
    }
      // UTILISATEUR MAPPER
      public  UtilisateurAfficheDto fromUtilisateur(Utilisateur utilisateur) {
        UtilisateurAfficheDto utilisateurAfficheDto = new UtilisateurAfficheDto();
        BeanUtils.copyProperties(utilisateur, utilisateurAfficheDto);
        return utilisateurAfficheDto;
    }

    public Utilisateur toUtilisateur(UtilisateurAfficheDto dto) {
        return null;
    }
    // BIEN IMMOBILIER MAPPER
    public BienImmobilierAffiheDto fromBienImmobilier(Bienimmobilier bienimmobilier) {
        BienImmobilierAffiheDto bienImmobilierAffiheDto = new BienImmobilierAffiheDto();
        BeanUtils.copyProperties(bienimmobilier, bienImmobilierAffiheDto);
        bienImmobilierAffiheDto.setNomPrenomProprio(bienimmobilier.getUtilisateurProprietaire().getNom() + " "
                + bienimmobilier.getUtilisateurProprietaire().getPrenom());
        bienImmobilierAffiheDto.setChapitre(bienimmobilier.getChapitre().getLibelleChapitre());
        return bienImmobilierAffiheDto;
    }

    // PERIODE BAIL APPEL
    public PeriodeDto fromPeriodeAppel(AppelLoyer appelLoyer) {
        PeriodeDto periodeDto = new PeriodeDto();
        BeanUtils.copyProperties(appelLoyer, periodeDto);
        periodeDto.setPeriodeAppelLoyer(appelLoyer.getPeriodeAppelLoyer());
        periodeDto.setPeriodeLettre(appelLoyer.getPeriodeLettre());
        return periodeDto;
    }

    // PERIODE BAIL APPEL
    public MessageEnvoyerDto fromMessageEnvoyer(MessageEnvoyer messageEnvoyer) {

        MessageEnvoyerDto messageEnvoyerDto = new MessageEnvoyerDto();
        BeanUtils.copyProperties(messageEnvoyer, messageEnvoyerDto);
        messageEnvoyerDto.setDestinaireNomPrenom(messageEnvoyer.getNomDestinaire());
        messageEnvoyerDto.setIdDestinaire(messageEnvoyer.getIdDestinaire());
        messageEnvoyerDto.setDateEnvoi(messageEnvoyer.getCreationDate());
        return messageEnvoyerDto;
    }

    public CategorieChambre toCategorieChambre(CategoryChambreSaveOrUpdateDto dto) {
        CategorieChambre categorieChambre = new CategorieChambre();
        BeanUtils.copyProperties(dto, categorieChambre);
        return categorieChambre;
    }

    public static CategoryChambreSaveOrUpdateDto fromCategoryChambre(CategorieChambre categorieChambre) {
        CategoryChambreSaveOrUpdateDto dto = new CategoryChambreSaveOrUpdateDto();
        BeanUtils.copyProperties(categorieChambre, dto);
        return dto;
    }

    public static Prestation toServiceAdditionnelle(PrestationSaveOrUpdateDto dto) {
        Prestation serviceAdditionnelle = new Prestation();
        BeanUtils.copyProperties(dto, serviceAdditionnelle);
        return serviceAdditionnelle;
    }

    public static PrestationSaveOrUpdateDto fromServiceAditionnel(Prestation serviceAdditionnelle) {
        PrestationSaveOrUpdateDto serviceAditionnelSaveOrUpdateDto = new PrestationSaveOrUpdateDto();
        BeanUtils.copyProperties(serviceAdditionnelle, serviceAditionnelSaveOrUpdateDto);
        return serviceAditionnelSaveOrUpdateDto;
    }

    public static ReservationAfficheDto fromReservation(Reservation reservation) {
        ReservationAfficheDto reservationSaveOrUpdateDto = new ReservationAfficheDto();
        BeanUtils.copyProperties(reservation, reservationSaveOrUpdateDto);
        reservationSaveOrUpdateDto
                .setBienImmobilierOperation(reservation.getBienImmobilierOperation().getNomBaptiserBienImmobilier());
                reservationSaveOrUpdateDto.setUtilisateurOperation(reservation.getUtilisateurOperation().getNom()+" "+reservation.getUtilisateurOperation().getPrenom());

        return reservationSaveOrUpdateDto;
    }

    public Reservation toReservation(ReservationSaveOrUpdateDto dto) {
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(dto, reservation);
        return reservation;
    }

    public Utilisateur fromUtilisateurRequestDto(UtilisateurRequestDto use) {
        Utilisateur usr = new Utilisateur();
        BeanUtils.copyProperties(use, usr);
        return usr;
    }

    public static PrestationAdditionnelReservationSaveOrrUpdate fromPrestationAdditionnelReservation(
            PrestationAdditionnelReservation prestationAdditionnelReservation) {
        PrestationAdditionnelReservationSaveOrrUpdate prestationAdditionnelReservationSaveOrrUpdate = new PrestationAdditionnelReservationSaveOrrUpdate();
        BeanUtils.copyProperties(prestationAdditionnelReservation, prestationAdditionnelReservationSaveOrrUpdate);
        prestationAdditionnelReservationSaveOrrUpdate
                .setNamePrestaion(prestationAdditionnelReservation.getServiceAdditionnelle().getName());
        prestationAdditionnelReservationSaveOrrUpdate.setAmountPrestation(prestationAdditionnelReservation.getServiceAdditionnelle().getAmount());
        return prestationAdditionnelReservationSaveOrrUpdate;
}

}
