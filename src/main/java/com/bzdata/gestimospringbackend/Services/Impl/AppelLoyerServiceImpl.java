package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.SmsRequest;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.validator.AppelLoyerRequestValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * Cette classe permet la creation du service
 * d'appel de loyer
 *
 * @version 1.1
 * @Author Michel Bossoh
 */
@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppelLoyerServiceImpl implements AppelLoyerService {

    /**
     * Auto injection
     */

    final MontantLoyerBailRepository montantLoyerBailRepository;
    final BailLocationRepository bailLocationRepository;
    final AppelLoyerRepository appelLoyerRepository;
    private final TwilioSmsSender twilioSmsSender;

    /**
     * Cette methode est utilisé pour enregister tous les appels loyers
     * de l'utilisateur durant la periode de contrat
     *
     * @param dto de l appelLoyrRequestDtio il comprend :
     *            Long idAgence;
     *            Long idBailLocation;
     *            double montantLoyerEnCours;
     * @return une liste de periodes ou les loyers sont appeler
     */

    @Override
    public List<String> save(AppelLoyerRequestDto dto) {

        log.info("We are going to create  a new Appel loyer bail {}", dto);
        List<String> errors = AppelLoyerRequestValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("L'appel du loyer n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object appelloyer sont null.",
                    ErrorCodes.APPELLOYER_NOT_VALID, errors);
        }
        BailLocation bailLocation = bailLocationRepository.findById(dto.getIdBailLocation())
                .orElseThrow(() -> new InvalidEntityException("Aucun BailMagasin has been found with Code " +
                        dto.getIdBailLocation(),
                        ErrorCodes.BAILLOCATION_NOT_FOUND));

        LocalDate dateDebut = bailLocation.getDateDebut();
        LocalDate dateFin = bailLocation.getDateFin();
        YearMonth ym1 = YearMonth.of(dateDebut.getYear(), dateDebut.getMonth());
        List<AppelLoyer> appelLoyerList = new ArrayList<>();
        for (int k = 1; k <= (ChronoUnit.MONTHS.between(dateDebut, dateFin) + 1); k++) {
            AppelLoyer appelLoyer = new AppelLoyer();
            YearMonth period = ym1.plus(Period.ofMonths(k));
            LocalDate initial = LocalDate.of(period.getYear(), period.getMonth(), 1);
            LocalDate start = initial.withDayOfMonth(1);
            LocalDate datePaiementPrevu = initial.withDayOfMonth(10);
            LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
            appelLoyer.setIdAgence(dto.getIdAgence());
            appelLoyer.setPeriodeAppelLoyer(period.toString());
            appelLoyer.setStatusAppelLoyer("Impayé");
            appelLoyer.setDatePaiementPrevuAppelLoyer(datePaiementPrevu);
            appelLoyer.setDateDebutMoisAppelLoyer(start);
            appelLoyer.setDateFinMoisAppelLoyer(end);
            appelLoyer.setAnneeAppelLoyer(period.getYear());
            appelLoyer.setMoisChiffreAppelLoyer(period.getMonthValue());
            appelLoyer.setDescAppelLoyer("Appel groupé");
            List<MontantLoyerBail> byBailLocation = montantLoyerBailRepository.findByBailLocation(bailLocation);
            Double montantBail = byBailLocation.stream()
                    .filter(st -> st.isStatusLoyer() == true)
                    .map(MontantLoyerBail::getNouveauMontantLoyer)
                    .findFirst().orElse(0.0);
            log.info("montantBail {}", montantBail);
            appelLoyer.setMontantBailLPeriode(montantBail);
            appelLoyer.setBailLocationAppelLoyer(bailLocation);
            appelLoyerList.add(appelLoyer);
        }

        appelLoyerRepository.saveAll(appelLoyerList);
            log.info("we are going lo launch sms to the user ");
         SmsRequest sms =new SmsRequest(bailLocation.getUtilisateurOperation().getUsername(),"Vôtre bail de location a été créé avec succès.");
        twilioSmsSender.sendSms(sms);
        log.info("Sms sent");
        return appelLoyerList
                .stream()
                .map(AppelLoyer::getPeriodeAppelLoyer)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteAppelDto(Long id) {
        return false;
    }

    @Override
    public List<AppelLoyer> findAll() {
        return null;
    }

    @Override
    public AppelLoyer findById(Long id) {
        return null;
    }

    @Override
    public List<AppelLoyer> findAllAppelLoyerByBailId(Long idBailLocation) {
        return null;
    }
}
