package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyerAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.OperationDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.BailService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.BailMapperImpl;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class BailServiceImpl implements BailService {

    final BailLocationRepository bailLocationRepository;
    final AppelLoyerService appelLoyerService;
    final MontantLoyerBailRepository montantLoyerBailRepository;
    final BienImmobilierRepository bienImmobilierRepository;
    final BailMapperImpl bailMapperImpl;
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    final UtilisateurRepository utilisateurRepository;
    final AppelLoyerRepository appelLoyerRepository;

    @Override
    public boolean closeBail(Long id) {
        log.info("We are going to close a bail ID {}", id);
        // List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
        if (id != null) {
            BailLocation newBailLocation = bailLocationRepository.findById(id).orElse(null);
            if (newBailLocation == null)
                throw new EntityNotFoundException("BailLocation from id not found", ErrorCodes.BAILLOCATION_NOT_FOUND);
            // Mise a jour de la table Operation
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
            List<AppelLoyersFactureDto> listeAppelLoyerAyantDateDebutSupDateCloture = appelLoyerService.findAll()
                    .stream()
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getIdBailLocation() == newBailLocation
                            .getId())
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getDateDebutMoisAppelLoyer()
                            .isAfter(dateClotureEffectif))
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getSoldeAppelLoyer() == montantBail)
                    .collect(Collectors.toList());
            for (AppelLoyersFactureDto dto : listeAppelLoyerAyantDateDebutSupDateCloture) {
                System.out.println(dto);
                appelLoyerService.deleteAppelDto(dto.getId());
            }
        }

        return true;
    }

    @Override
    public int nombreBauxActifs() {

        return (int) bailLocationRepository.findAll()
                .stream()
                .filter(encourrs -> encourrs.isEnCoursBail())
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

}
