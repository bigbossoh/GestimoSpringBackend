package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AnneeAppelLoyersDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.PeriodeDto;
import com.bzdata.gestimospringbackend.DTOs.PourcentageAppelDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.SmsRequest;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Utils.SmsOrangeConfig;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
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

        final MontantLoyerBailRepository montantLoyerBailRepository;
        final BailLocationRepository bailLocationRepository;
        final AppelLoyerRepository appelLoyerRepository;
        final UtilisateurRepository utilisateurRepository;
        final GestimoWebMapperImpl gestimoWebMapper;
        final BienImmobilierRepository bienImmobilierRepository;
        final SmsOrangeConfig envoiSmsOrange;

        final AgenceImmobiliereRepository agenceImmobiliereRepository;

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
                                .orElseThrow(() -> new InvalidEntityException(
                                                "Aucun BailMagasin has been found with Code " +
                                                                dto.getIdBailLocation(),
                                                ErrorCodes.BAILLOCATION_NOT_FOUND));

                LocalDate dateDebut = bailLocation.getDateDebut();
                LocalDate dateFin = bailLocation.getDateFin();
                YearMonth ym1 = YearMonth.of(dateDebut.getYear(), dateDebut.getMonth());
                List<AppelLoyer> appelLoyerList = new ArrayList<>();
                AppelLoyer appelLoyer;
                for (int k = 1; k < (ChronoUnit.MONTHS.between(dateDebut, dateFin) + 1); k++) {
                        appelLoyer = new AppelLoyer();
                        YearMonth period = ym1.plus(Period.ofMonths(k));
                        LocalDate initial = LocalDate.of(period.getYear(), period.getMonth(), 1);
                        LocalDate start = initial.withDayOfMonth(1);
                        LocalDate datePaiementPrevu = initial.withDayOfMonth(10);
                        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());

                        YearMonth ym = YearMonth.from(start);
                        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMMM uuuu", Locale.FRANCE);
                        appelLoyer.setPeriodeLettre(ym.format(f));
                        DateTimeFormatter mois = DateTimeFormatter.ofPattern("MMMM", Locale.FRANCE);
                        appelLoyer.setMoisUniquementLettre(ym.format(mois));
                        appelLoyer.setMessageReduction("");
                        appelLoyer.setIdAgence(dto.getIdAgence());
                        appelLoyer.setPeriodeAppelLoyer(period.toString());
                        appelLoyer.setStatusAppelLoyer("Impayé");
                        appelLoyer.setDatePaiementPrevuAppelLoyer(datePaiementPrevu);
                        appelLoyer.setDateDebutMoisAppelLoyer(start);
                        appelLoyer.setDateFinMoisAppelLoyer(end);
                        appelLoyer.setAnneeAppelLoyer(period.getYear());
                        appelLoyer.setMoisChiffreAppelLoyer(period.getMonthValue());
                        appelLoyer.setCloturer(false);
                        appelLoyer.setSolderAppelLoyer(false);
                        appelLoyer.setDescAppelLoyer("Appel groupé");
                        appelLoyer.setSoldeAppelLoyer(dto.getMontantLoyerEnCours());
                        List<MontantLoyerBail> byBailLocation = montantLoyerBailRepository
                                        .findByBailLocation(bailLocation);
                        Double montantBail = byBailLocation.stream()
                                        .filter(MontantLoyerBail::isStatusLoyer)
                                        .map(MontantLoyerBail::getNouveauMontantLoyer)
                                        .findFirst().orElse(0.0);
                        log.info("montantBail {}", montantBail);
                        appelLoyer.setMontantLoyerBailLPeriode(montantBail);
                        appelLoyer.setBailLocationAppelLoyer(bailLocation);
                        appelLoyerList.add(appelLoyer);
                }

                appelLoyerRepository.saveAll(appelLoyerList);
                log.info("we are going lo launch sms to the user ");
                // SmsRequest sms = new
                // SmsRequest(bailLocation.getUtilisateurOperation().getUsername(),
                // "Vôtre baillocation a été créé avec succès.");

                // log.info("Sms sent");
                return appelLoyerList
                                .stream()
                                .map(AppelLoyer::getPeriodeAppelLoyer)
                                .collect(Collectors.toList());
        }

        @Override
        public boolean cloturerAppelDto(Long id) {
                log.info("We are going to set isCloture at true a AppelLoyer with the ID {}", id);
                if (id == null) {
                        log.error("you are provided a null ID for the Bail");
                        return false;
                }
                boolean exist = appelLoyerRepository.existsById(id);
                if (!exist) {

                        throw new EntityNotFoundException("Aucune Studio avec l'ID = " + id + " "
                                        + "n' ete trouve dans la BDD", ErrorCodes.BAILLOCATION_NOT_FOUND);
                }
                AppelLoyersFactureDto byId = findById(id);
                byId.setCloturer(true);
                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(byId));
                return true;
        }

        @Override
        public List<AppelLoyersFactureDto> findAll(Long idAgence) {
                return appelLoyerRepository.findAll()
                                .stream()
                                .filter(appelLoyer -> appelLoyer.getIdAgence() == idAgence)
                                .map(gestimoWebMapper::fromAppelLoyer)
                                .collect(Collectors.toList());
        }

        @Override
        public List<AppelLoyersFactureDto> findAllAppelLoyerByPeriode(String periodeAppelLoyer, Long idAgence) {
                return appelLoyerRepository.findAll()
                                .stream()
                                // .filter(appelLoyer -> !appelLoyer.isCloturer())
                                .filter(appelLoyer -> appelLoyer.getPeriodeAppelLoyer().equals(periodeAppelLoyer))
                                .filter(appelLoyer -> appelLoyer.getIdAgence() == idAgence)
                                .sorted(Comparator.comparing(AppelLoyer::getPeriodeAppelLoyer))
                                .map(gestimoWebMapper::fromAppelLoyer)
                                .collect(Collectors.toList());
        }

        @Override
        public List<Integer> listOfDistinctAnnee(Long idAgence) {
                List<Integer> collectAnneAppelDistinct = appelLoyerRepository
                                .findAll()
                                .stream()
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .map(AppelLoyer::getAnneeAppelLoyer)
                                .distinct()
                                .sorted()
                                .collect(Collectors.toList());
                return collectAnneAppelDistinct;

        }

        @Override
        public List<PeriodeDto> listOfPerodesByAnnee(Integer annee, Long idAgence) {
                List<PeriodeDto> collectPeriodeDistinct = appelLoyerRepository
                                .findAll()
                                .stream()
                                .filter(appelLoyer -> appelLoyer.getAnneeAppelLoyer() == annee)
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .map(gestimoWebMapper::fromPeriodeAppel)
                                .distinct()
                                .collect(Collectors.toList());

                return collectPeriodeDistinct;
        }

        @Override
        public List<AnneeAppelLoyersDto> listOfAppelLoyerByAnnee(Integer annee, Long idAgence) {

                return appelLoyerRepository
                                .findAll()
                                .stream()
                                .filter(appelLoyer -> appelLoyer.getAnneeAppelLoyer() == annee)
                                .filter(agence -> agence.getIdAgence() == idAgence)
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
                                // .filter(appelLoyer -> !appelLoyer.isCloturer())
                                .map(gestimoWebMapper::fromAppelLoyer)
                                .orElseThrow(
                                                () -> new InvalidEntityException(
                                                                "Aucun Appel loyer has been found with Code " + id,
                                                                ErrorCodes.APPELLOYER_NOT_FOUND));
        }

        @Override
        public List<AppelLoyerDto> findAllAppelLoyerByBailId(Long idBailLocation) {
                BailLocation bailLocation = bailLocationRepository.findById(idBailLocation)
                                .orElseThrow(() -> new InvalidEntityException(
                                                "Aucun BailMagasin has been found with Code " +
                                                                idBailLocation,
                                                ErrorCodes.BAILLOCATION_NOT_FOUND));
                List<AppelLoyer> lesLoyers = appelLoyerRepository.findAllByBailLocationAppelLoyer(bailLocation);

                return lesLoyers
                                .stream()
                                // .filter(appelLoyer -> !appelLoyer.isCloturer())
                                .filter(bail -> bail.getBailLocationAppelLoyer() == bailLocation)
                                .map(AppelLoyerDto::fromEntity)
                                .collect(Collectors.toList());
        }

        @Override
        public List<AppelLoyersFactureDto> findAllAppelLoyerImpayerByBailId(Long idBailLocation) {
                BailLocation bailLocation = bailLocationRepository.findById(idBailLocation)
                                .orElseThrow(() -> new InvalidEntityException(
                                                "Aucun BailMagasin has been found with Code " +
                                                                idBailLocation,
                                                ErrorCodes.BAILLOCATION_NOT_FOUND));
                List<AppelLoyer> lesLoyers = appelLoyerRepository.findAllByBailLocationAppelLoyer(bailLocation);
                // first date debut du mois
                Comparator<AppelLoyer> appelLoyerByDateDebutAppelLoyer = Comparator
                                .comparing(AppelLoyer::getDateDebutMoisAppelLoyer);
                return lesLoyers.stream()
                                .filter(bail -> bail.getBailLocationAppelLoyer() == bailLocation)
                                // .filter(appelLoyer -> !appelLoyer.isCloturer())
                                .filter(bail -> !bail.isSolderAppelLoyer())
                                .sorted(appelLoyerByDateDebutAppelLoyer)

                                .map(gestimoWebMapper::fromAppelLoyer)
                                .collect(Collectors.toList());
        }

        @Override
        public double soldeArrierer(Long idBailLocation) {

                return 0;
        }

        @Override
        public AppelLoyersFactureDto getFirstLoyerImpayerByBien(Long bien) {
                Bienimmobilier bienImmobilier = bienImmobilierRepository.findById(bien)
                                .orElseThrow(() -> new InvalidEntityException(
                                                "Aucun Bien a été trouvé avec l'adresse " +
                                                                bien,
                                                ErrorCodes.BIEN_IMMOBILIER_NOT_FOUND));
                List<AppelLoyer> lesLoyers = appelLoyerRepository.findAll();

                return lesLoyers.stream()
                                .filter(bienTrouver -> bienTrouver.getBailLocationAppelLoyer()
                                                .getBienImmobilierOperation().equals(bienImmobilier))
                                .filter(loyers -> loyers.getMontantLoyerBailLPeriode() > 0)
                                // .filter(perio->perio.getPeriodeAppelLoyer().compareTo(bienPeriodeDto.getPeriode())<0)
                                .map(gestimoWebMapper::fromAppelLoyer)
                                .findFirst().orElseThrow(null);
                // .collect(Collectors.toList());
        }

        @Override
        public double impayeParPeriode(String periode, Long idAgence) {

                List<Double> soldeImpaye = appelLoyerRepository.findAll().stream()
                                .filter(period -> period.getPeriodeAppelLoyer().equals(periode))
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .filter(solde -> solde.getSoldeAppelLoyer() > 0)
                                .map(AppelLoyer::getSoldeAppelLoyer)
                                .collect(Collectors.toList());
                return soldeImpaye.stream().mapToDouble(Double::doubleValue).sum();
        }

        @Override
        public double payeParPeriode(String periode, Long idAgence) {
                log.info("total des paiements sur periode et par agence {},{}", periode, idAgence);
                List<Double> soldeImpaye = appelLoyerRepository.findAll().stream()
                                .filter(period -> period.getPeriodeAppelLoyer().equals(periode))
                                .filter(agence -> agence.getIdAgence() == idAgence)

                                .map(AppelLoyer::getMontantLoyerBailLPeriode)
                                .collect(Collectors.toList());
                double TotalMontantLoyerParPeriodeParAgence = soldeImpaye.stream().mapToDouble(Double::doubleValue)
                                .sum();
                log.info("Total montant loyer par periode par agence {}, {}", TotalMontantLoyerParPeriodeParAgence,
                                impayeParPeriode(periode, idAgence));
                return TotalMontantLoyerParPeriodeParAgence - impayeParPeriode(periode, idAgence);

                // return appelLoyerRepository.payeParMois(periode);
        }

        @Override
        public double impayeParAnnee(int annee, Long idAgence) {

                List<Double> soldeImpaye = appelLoyerRepository.findAll().stream()
                                .filter(period -> period.getAnneeAppelLoyer() == (annee))
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .filter(solde -> solde.getSoldeAppelLoyer() > 0)
                                .map(AppelLoyer::getSoldeAppelLoyer)
                                .collect(Collectors.toList());
                return soldeImpaye.stream().mapToDouble(Double::doubleValue).sum();

        }

        @Override
        public double payeParAnnee(int annee, Long idAgence) {

                List<Double> soldeImpaye = appelLoyerRepository.findAll().stream()
                                .filter(period -> period.getAnneeAppelLoyer() == (annee))
                                .filter(agence -> agence.getIdAgence() == idAgence)

                                .map(AppelLoyer::getMontantLoyerBailLPeriode)
                                .collect(Collectors.toList());
                double TotalMontantLoyerParPeriodeParAgence = soldeImpaye.stream().mapToDouble(Double::doubleValue)
                                .sum();

                return TotalMontantLoyerParPeriodeParAgence - impayeParAnnee(annee, idAgence);
        }

        @Override
        public Long nombreBauxImpaye(String periode, Long idAgence) {

                return appelLoyerRepository.findAll().stream()
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .count();
        }

        @Override
        public Long nombreBauxPaye(String periode, Long idAgence) {

                return 0L;
        }

        @Override
        public double montantBeauxImpayer(String periode, Long idAgence) {

                return 0;
        }

        @Override
        public List<PeriodeDto> findAllPeriode(Long idAgence) {
                List<PeriodeDto> collectPeriodeDistinct = appelLoyerRepository
                                .findAll()
                                .stream()
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .map(gestimoWebMapper::fromPeriodeAppel)
                                .sorted(Comparator.comparing(PeriodeDto::getPeriodeAppelLoyer))
                                .distinct()
                                .collect(Collectors.toList());
                return collectPeriodeDistinct;

        }

        @Override
        public boolean deleteAppelsByIdBail(Long idBail) {
                List<AppelLoyer> appelsloyer = appelLoyerRepository.findAll().stream()
                                .filter(bail -> bail.getBailLocationAppelLoyer().getId() == idBail)
                                .collect(Collectors.toList());
                if (appelsloyer.size() > 0) {
                        for (int index = 0; index < appelsloyer.size() - 1; index++) {
                                System.out.println(appelsloyer.get(index));
                                appelLoyerRepository.delete(appelsloyer.get(index));
                        }
                } else {
                        return false;
                }
                return true;
        }

        @Override
        public boolean sendSmsAppelLoyerGroupe(String periodeAppelLoyer, Long idAgence) {

                List<AppelLoyer> appelLoyersFactureDtos = appelLoyerRepository.findAll().stream()
                                .filter(sold -> sold.isSolderAppelLoyer() == false)
                                .filter(agen -> agen.getIdAgence() == idAgence)
                                .filter(perio -> perio.getPeriodeAppelLoyer().equals(periodeAppelLoyer))
                                .collect(Collectors.toList());

                if (appelLoyersFactureDtos.size() > 0) {
                        appelLoyersFactureDtos.forEach(ap -> {
                                Utilisateur locataire = utilisateurRepository.findById(
                                                ap.getBailLocationAppelLoyer().getUtilisateurOperation().getId())
                                                .orElse(null);
                                try {
                                        String leTok = envoiSmsOrange.getTokenSmsOrange();
                                        AgenceImmobiliere agenceFound = agenceImmobiliereRepository
                                                        .findById(ap.getIdAgence()).orElse(null);
                                        String message = "Bonjour, " + locataire.getGenre() + " " + locataire.getNom()
                                                        + " votre agence " +
                                                        agenceFound.getNomAgence().toUpperCase()
                                                        + ", vous informe que le montant de " + ap.getSoldeAppelLoyer()
                                                        + " F CFA correspondant au solde de votre loyer pour la periode de "
                                                        + ap.getPeriodeLettre()
                                                        + ", doit etre regler avant le " +
                                                        ap.getDatePaiementPrevuAppelLoyer()
                                                        + ". Merci de regulariser votre situation.";
                                        envoiSmsOrange.sendSms(leTok, message, "+2250000",
                                                        locataire.getUsername(), "Sms Societe");
                                        System.out.println("********************* Le toke toke est : " + leTok);
                                } catch (Exception e) {
                                        System.err.println(e.getMessage());
                                }
                        });
                        return true;
                }
                return false;
        }

        @Override
        public List<AppelLoyersFactureDto> reductionLoyerByPeriode(PourcentageAppelDto pourcentageAppelDto) {
                log.info("Le pouce est {},{},{},{}", pourcentageAppelDto.getMessageReduction(),
                                pourcentageAppelDto.getPeriodeAppelLoyer(), pourcentageAppelDto.getTauxApplique(),
                                pourcentageAppelDto.getIdAgence());
                List<AppelLoyersFactureDto> listAppels = findAllAppelLoyerByPeriode(
                                pourcentageAppelDto.getPeriodeAppelLoyer(), pourcentageAppelDto.getIdAgence());

                if (listAppels.size() > 0) {
                        for (int i = 0; i < listAppels.size(); i++) {
                                double montantApresReduction = 0.0;
                                AppelLoyer appelLoyerTrouve = appelLoyerRepository.findById(listAppels.get(i).getId())
                                                .orElseThrow(null);
                                montantApresReduction = listAppels.get(i).getMontantLoyerBailLPeriode()
                                                * (1 - pourcentageAppelDto.getTauxApplique() / 100);
                                appelLoyerTrouve.setAncienMontant(listAppels.get(i).getMontantLoyerBailLPeriode());
                                appelLoyerTrouve.setPourcentageReduction(pourcentageAppelDto.getTauxApplique());
                                appelLoyerTrouve.setMontantLoyerBailLPeriode(montantApresReduction);
                                appelLoyerTrouve.setMessageReduction(pourcentageAppelDto.getMessageReduction());
                                log.info("Le montant loyer est le suivant : {},{} ,{}", listAppels.get(i).getId(),
                                                montantApresReduction,
                                                appelLoyerTrouve.getMontantLoyerBailLPeriode());
                                AppelLoyer leSave = appelLoyerRepository.saveAndFlush(appelLoyerTrouve);
                                log.info("info nouveau {}", leSave.getMontantLoyerBailLPeriode());
                        }
                        // List<AppelLoyersFactureDto> listAppelsModifier = findAllAppelLoyerByPeriode(
                        // pourcentageAppelDto.getPeriodeAppelLoyer(),
                        // pourcentageAppelDto.getIdAgence());
                        return listAppels;
                } else {
                        return null;
                }

        }
}
