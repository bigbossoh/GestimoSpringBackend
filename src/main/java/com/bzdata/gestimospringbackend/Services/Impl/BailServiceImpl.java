package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.BailService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.validator.EncaissementPayloadDtoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class BailServiceImpl implements BailService {

    final BailLocationRepository bailLocationRepository;
    final AppelLoyerService appelLoyerService;
    final MontantLoyerBailRepository montantLoyerBailRepository;

    @Override
    public boolean closeBail(Long id) {
        log.info("We are going to close a bail ID {}", id);
        // List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
        if (id != null) {
            BailLocation newBailLocation = bailLocationRepository.findById(id).orElse(null);
            if (newBailLocation == null)
                throw new EntityNotFoundException("BailLocation from id not found", ErrorCodes.BAILLOCATION_NOT_FOUND);
            //Mise a jour de la table Operation
            newBailLocation.setEnCoursBail(false);
            newBailLocation.setDateCloture(LocalDate.now());
            bailLocationRepository.save(newBailLocation);

            //Determinons le montant du loyer du bail en question
            List<MontantLoyerBail> byBailLocation = montantLoyerBailRepository.findByBailLocation(newBailLocation);
            Double montantBail = byBailLocation.stream()
                    .filter(MontantLoyerBail::isStatusLoyer)
                    .map(MontantLoyerBail::getNouveauMontantLoyer)
                    .findFirst().orElse(0.0);
            //Mise a jour de la table AppelLoyer
            //Determinons la date de cloture du bail
            LocalDate dateCloture = LocalDate.now();
            YearMonth periodOfCloture = YearMonth.of(dateCloture.getYear(), dateCloture.getMonth());
            LocalDate initial = LocalDate.of(periodOfCloture.getYear(), periodOfCloture.getMonth(), 1);
            System.out.println("La date de debut initial est: " + initial);
            LocalDate dateClotureEffectif = initial.withDayOfMonth(1);
            System.out.println("La date de debut dateClotureEffectif est: " + dateClotureEffectif);
            //Determinons la liste des appels loyers dont la date de debut est superieur a la dateClotureEffecture
            List<AppelLoyersFactureDto> listeAppelLoyerAyantDateDebutSupDateCloture = appelLoyerService.findAll().stream()
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getIdBailLocation() == newBailLocation.getId())
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getDateDebutMoisAppelLoyer().isAfter(dateClotureEffectif))
                    .filter(appelLoyersFactureDto -> appelLoyersFactureDto.getSoldeAppelLoyer() == montantBail)
                    .collect(Collectors.toList());
            for (AppelLoyersFactureDto dto: listeAppelLoyerAyantDateDebutSupDateCloture){
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
                .filter(encourrs-> encourrs.isEnCoursBail())
                .count();
    }

}
