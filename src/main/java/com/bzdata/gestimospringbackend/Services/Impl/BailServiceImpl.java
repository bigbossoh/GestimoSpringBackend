package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.BailModifDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.Operation;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.BailService;
import com.bzdata.gestimospringbackend.Services.BienImmobilierService;
import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.BailMapperImpl;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;
import com.bzdata.gestimospringbackend.repository.EncaissementPrincipalRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BailServiceImpl implements BailService {
    final MontantLoyerBailService montantLoyerBailService;
    final BailLocationRepository bailLocationRepository;
    final AppelLoyerService appelLoyerService;
    final MontantLoyerBailRepository montantLoyerBailRepository;
    final BienImmobilierRepository bienImmobilierRepository;
    final BailMapperImpl bailMapperImpl;
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    final UtilisateurRepository utilisateurRepository;
    final AppelLoyerRepository appelLoyerRepository;
    final EncaissementPrincipalRepository encaissementRepository;
    final BienImmobilierService bienImmobilierService;

    @Override
    public boolean closeBail(Long id) {
        log.info("We are going to close a bail ID {}", id);

        if (id != null) {
            BailLocation newBailLocation = bailLocationRepository.findById(id).orElse(null);
            if (newBailLocation == null)
                throw new EntityNotFoundException("BailLocation from id not found", ErrorCodes.BAILLOCATION_NOT_FOUND);
            // Mise a jour de la table Operation
            Bienimmobilier bienLiberer = bienImmobilierService.findBienByBailEnCours(id);
            if (bienLiberer != null) {
                bienLiberer.setOccupied(false);
                bienImmobilierRepository.save(bienLiberer);
            }
            newBailLocation.setEnCoursBail(false);
            newBailLocation.setDateCloture(LocalDate.now());

            bailLocationRepository.save(newBailLocation);

            // Determinons le montant du loyer du bail en question
            List<MontantLoyerBail> byBailLocation = montantLoyerBailRepository.findByBailLocation(newBailLocation);
            Double montantBail = byBailLocation.stream()
                    .filter(MontantLoyerBail::isStatusLoyer)
                    .map(MontantLoyerBail::getNouveauMontantLoyer)
                    .findFirst().orElse(0.0);
            // Mise a jour de la table AppelLoyer
            // Determinons la date de cloture du bail
            LocalDate dateCloture = LocalDate.now();
            YearMonth periodOfCloture = YearMonth.of(dateCloture.getYear(), dateCloture.getMonth());
            LocalDate initial = LocalDate.of(periodOfCloture.getYear(), periodOfCloture.getMonth(), 1);
            System.out.println("La date de debut initial est: " + initial);
            LocalDate dateClotureEffectif = initial.withDayOfMonth(1);
            System.out.println("La date de debut dateClotureEffectif est: " + dateClotureEffectif);
            // Determinons la liste des appels loyers dont la date de debut est superieur a
            // la dateClotureEffecture
            List<AppelLoyer> listeAppelLoyerAyantDateDebutSupDateCloture = appelLoyerRepository.findAll()
                    .stream()
                    .filter(appelLoyersFactureDto -> Objects.equals(appelLoyersFactureDto.getBailLocationAppelLoyer()
                            .getId(),
                            newBailLocation
                                    .getId()))
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getDateDebutMoisAppelLoyer()
                            .isAfter(dateClotureEffectif))
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getSoldeAppelLoyer() == montantBail)
                    .collect(Collectors.toList());
            for (AppelLoyer dto : listeAppelLoyerAyantDateDebutSupDateCloture) {
                System.out.println(dto);
                appelLoyerService.cloturerAppelDto(dto.getId());
            }
        }
        return true;
    }

    @Override
    public int nombreBauxActifs(Long idAgence) {
        return (int) bailLocationRepository.findAll()
                .stream()
                .filter(agence -> Objects.equals(agence.getIdAgence(), idAgence))
                .filter(BailLocation::isEnCoursBail)
                .count();
    }

    @Override
    public List<AppelLoyersFactureDto> findAllByIdBienImmobilier(Long id) {
        log.info("We are going to get back the Bail By bien {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        Bienimmobilier bien = bienImmobilierRepository.findById(id).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with Code " + id,
                        ErrorCodes.BIEN_IMMOBILIER_NOT_FOUND));
        return appelLoyerRepository.findAll().stream()
                .filter(bienImm -> bienImm.getBailLocationAppelLoyer().getBienImmobilierOperation().equals(bien))
                .map(gestimoWebMapperImpl::fromAppelLoyer)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDto> findAllByIdLocataire(Long id) {
        log.info("We are going to get back the Bail By bien {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        Utilisateur locataire = utilisateurRepository.findById(id).orElseThrow(
                () -> new InvalidEntityException("Aucun utilisateur has been found with Code " + id,
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        return bailLocationRepository.findAll().stream()
                .filter(bienImm -> bienImm.getUtilisateurOperation().equals(locataire))
                .map(bailMapperImpl::fromOperation)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDto> findAllBauxLocation(Long idAgence) {
        return null;
    }

    @Override
    public boolean deleteOperationById(Long id) {
        // log.info("We are going to delete a Appartement with the ID {}", id);
        if (id == null) {
            throw new EntityNotFoundException("Aucune Operation avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.BAILLOCATION_NOT_FOUND);
        }
        boolean exist = bailLocationRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Operation avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.BAILLOCATION_NOT_FOUND);
        }
        List<EncaissementPrincipalDTO> encaissement = encaissementRepository.findAll().stream()
                .filter(operation -> operation.getAppelLoyerEncaissement().getBailLocationAppelLoyer().getId()
                        .equals(id))
                .map(gestimoWebMapperImpl::fromEncaissementPrincipal)
                .collect(Collectors.toList());
        if (!encaissement.isEmpty()) {
            throw new EntityNotFoundException("L = " + id + " "
                    + "Il existe des encaissement our ce bail", ErrorCodes.APPARTEMENT_NOT_FOUND);
        }
        Operation operation = bailLocationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucune Operation avec l'ID = " + id + " "
                        + "Il existe des encaissement our ce bail", ErrorCodes.APPARTEMENT_NOT_FOUND));
        Bienimmobilier bienAModifier = bienImmobilierRepository
                .findById(operation.getBienImmobilierOperation().getId()).get();
        bienAModifier.setOccupied(false);
        bienImmobilierRepository.save(bienAModifier);
        montantLoyerBailService.supprimerUnMontantParIdBail(id);
        appelLoyerService.deleteAppelsByIdBail(id);
        bailLocationRepository.deleteById(id);
        return true;
    }

    @Override
    public int nombreBauxNonActifs(Long idAgence) {
        return (int) bailLocationRepository.findAll()
                .stream()
                .filter(agence -> Objects.equals(agence.getIdAgence(), idAgence))
                .filter(encourrs -> !encourrs.isEnCoursBail())
                .count();
    }

    @Override
    public OperationDto modifierUnBail(BailModifDto dto) {
        BailLocation operation = bailLocationRepository.findById(dto.getIdBail())
                .orElseThrow(() -> new EntityNotFoundException("Aucune Operation avec l'ID = " + dto.getIdBail(),
                        ErrorCodes.APPARTEMENT_NOT_FOUND));
        // MODIFIER LE BAIL
        // operation.setDateDebut(dto.getDateDePriseEncompte());
        operation.setDateFin(dto.getDateFin());
        // operation.setNbreMoisCautionBail(dto.getNombreMoisCaution());
        // operation.setMontantCautionBail(dto.getNouveauMontantLoyer());
        BailLocation bailSave = bailLocationRepository.save(operation);
        System.out.println("Le modifiable");
        System.out.println(dto.getAncienMontantLoyer() + " " + dto.getNouveauMontantLoyer());
        // METTRE A JOUR LE MONTANT DU BAIL
        boolean modifMontantLoyerbail = montantLoyerBailService.saveNewMontantLoyerBail(0L,
                dto.getNouveauMontantLoyer(), dto.getAncienMontantLoyer(), dto.getIdBail(), bailSave.getIdAgence(),
                dto.getDateDePriseEncompte());
        System.out.println("le montant est les suivant");
        System.out.println(modifMontantLoyerbail);
        if (modifMontantLoyerbail) {
            // MODIFIER LES LOYERS
            log.info("La période est {}",
                    dto.getDateDePriseEncompte().getYear() + "-" + dto.getDateDePriseEncompte().getMonthValue());
            List<AppelLoyersFactureDto> loyers = appelLoyerService.listeDesloyerSuperieurAUnePeriode(
                    dto.getDateDePriseEncompte().getYear() + "-" + dto.getDateDePriseEncompte().getMonthValue(),
                    dto.getIdBail());
            if (!loyers.isEmpty()) {
                for (int index = 0; index < loyers.size(); index++) {
                    AppelLoyer lappelTrouver = appelLoyerRepository.findById(loyers.get(index).getId()).orElse(null);
                    lappelTrouver.setSoldeAppelLoyer(dto.getNouveauMontantLoyer());
                    if (lappelTrouver.getSoldeAppelLoyer()>0) {
                        lappelTrouver.setSoldeAppelLoyer(dto.getNouveauMontantLoyer()-(lappelTrouver.getMontantLoyerBailLPeriode()-lappelTrouver.getSoldeAppelLoyer()));
                    }
                    lappelTrouver.setMontantLoyerBailLPeriode(dto.getNouveauMontantLoyer());
                    appelLoyerRepository.save(lappelTrouver);
                }

            }
        }
        return bailMapperImpl.fromOperation(bailSave);
    }

    @Override
    public OperationDto findOperationById(Long id) {
        BailLocation findBailLocation = bailLocationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aucune Operation avec l'ID = " + id,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
        return bailMapperImpl.fromOperation(findBailLocation);
    }
}
