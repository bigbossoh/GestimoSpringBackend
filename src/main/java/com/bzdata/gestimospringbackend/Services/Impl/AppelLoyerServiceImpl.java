package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.*;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
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
    final TwilioSmsSender twilioSmsSender;
    final GestimoWebMapperImpl gestimoWebMapper;

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

            YearMonth ym = YearMonth.from( start ) ;
           DateTimeFormatter f = DateTimeFormatter.ofPattern( "MMMM uuuu" , Locale.FRANCE ) ;
            appelLoyer.setPeriodeLettre( ym.format( f )) ;
            DateTimeFormatter mois = DateTimeFormatter.ofPattern( "MMMM" , Locale.FRANCE ) ;
            appelLoyer.setMoisUniquementLettre( ym.format( mois));

            appelLoyer.setIdAgence(dto.getIdAgence());
            appelLoyer.setPeriodeAppelLoyer(period.toString());
            appelLoyer.setStatusAppelLoyer("Impayé");
            appelLoyer.setDatePaiementPrevuAppelLoyer(datePaiementPrevu);
            appelLoyer.setDateDebutMoisAppelLoyer(start);
            appelLoyer.setDateFinMoisAppelLoyer(end);
            appelLoyer.setAnneeAppelLoyer(period.getYear());
            appelLoyer.setMoisChiffreAppelLoyer(period.getMonthValue());

            appelLoyer.setDescAppelLoyer("Appel groupé");
            appelLoyer.setSoldeAppelLoyer(dto.getMontantLoyerEnCours());
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
        SmsRequest sms = new SmsRequest(bailLocation.getUtilisateurOperation().getUsername(),
                "Vôtre baillocation a été créé avec succès.");
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
    public List<AppelLoyersFactureDto> findAll() {
        return appelLoyerRepository.findAll()
                .stream()
                .map(gestimoWebMapper::fromAppelLoyer)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public List<AppelLoyersFactureDto> findAllAppelLoyerByPeriode(String periodeAppelLoyer) {
        return appelLoyerRepository.findAll()
                .stream()
                .filter(appelLoyer -> appelLoyer.getPeriodeAppelLoyer().equals(periodeAppelLoyer))
                .sorted(Comparator.comparing(AppelLoyer::getPeriodeAppelLoyer))
                .map(gestimoWebMapper::fromAppelLoyer)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> listOfDistinctAnnee() {
        List<Integer> collectAnneAppelDistinct = appelLoyerRepository
                .findAll()
                .stream()
                .map(AppelLoyer::getAnneeAppelLoyer)
                .distinct()
                .collect(Collectors.toList());
        return collectAnneAppelDistinct;

    }

    @Override
    public List<String> listOfPerodesByAnnee(Integer annee) {
        List<String> collectPeriodeDistinct = appelLoyerRepository
                .findAll()
                .stream()
                .filter(appelLoyer -> appelLoyer.getAnneeAppelLoyer()==annee)
                .map(AppelLoyer::getPeriodeLettre)
                .distinct()
                .collect(Collectors.toList());

        return collectPeriodeDistinct;
    }

    @Override
    public List<AnneeAppelLoyersDto> listOfAppelLoyerByAnnee(Integer annee) {


        return appelLoyerRepository
                .findAll()
                .stream()
                .filter(appelLoyer -> appelLoyer.getAnneeAppelLoyer()==annee)
                .map(gestimoWebMapper::fromAppelLoyerForAnnee)
                .distinct()
                .collect(Collectors.toList());
    }


    @Override
    public AppelLoyersFactureDto findById(Long id) {

        log.info("We are going to get back the AppelLoyersFactureDTO By {}", id);
        if (id == null) {
            log.error("you are not provided a good Id of AppelLoyersFacture.");
            return null;
        }
        return appelLoyerRepository.findById(id)
                .map(gestimoWebMapper::fromAppelLoyer)
                .orElseThrow(
                () -> new InvalidEntityException("Aucun Appel loyer has been found with Code " + id,
                        ErrorCodes.APPELLOYER_NOT_FOUND));
    }

    @Override
    public List<AppelLoyerDto> findAllAppelLoyerByBailId(Long idBailLocation) {
        BailLocation bailLocation = bailLocationRepository.findById(idBailLocation)
                .orElseThrow(() -> new InvalidEntityException("Aucun BailMagasin has been found with Code " +
                        idBailLocation,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
        List<AppelLoyer> lesLoyers = appelLoyerRepository.findAllByBailLocationAppelLoyer(bailLocation);

        return lesLoyers.stream().filter(bail -> bail.getBailLocationAppelLoyer() == bailLocation)
                .map(AppelLoyerDto::fromEntity)
                .collect(Collectors.toList());
    }


}
