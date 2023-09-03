package com.bzdata.gestimospringbackend.Services.Impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyerEncaissDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.DTOs.LocataireEncaisDTO;
import com.bzdata.gestimospringbackend.DTOs.StatistiquePeriodeDto;
import com.bzdata.gestimospringbackend.Models.AgenceImmobiliere;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.EncaissementPrincipalService;
import com.bzdata.gestimospringbackend.Utils.SmsOrangeConfig;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.BailMapperImpl;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AgenceImmobiliereRepository;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.EncaissementPrincipalRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.EncaissementPayloadDtoValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EncaissementPrincipalServiceImpl implements EncaissementPrincipalService {
        final AppelLoyerRepository appelLoyerRepository;

        final GestimoWebMapperImpl gestimoWebMapper;
        final UtilisateurRepository utilisateurRepository;
        final AppelLoyerService appelLoyerService;
        final AgenceImmobiliereRepository agenceImmobiliereRepository;
        final EncaissementPrincipalRepository encaissementPrincipalRepository;
        BailMapperImpl bailMapperImpl;
        // final SmsOrangeConfig smsOrangeConfig;
        final SmsOrangeConfig envoiSmsOrange;

        @Override
        public boolean saveEncaissement(EncaissementPayloadDto dto) {
                // log.info("We are going to create a new encaissement
                // EncaissementPrincipalServiceImpl {}", dto);
                List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
                if (!errors.isEmpty()) {
                        log.error("L'encaissement n'est pas valide {}", errors);
                        throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                                        ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
                }
                AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyer()).orElse(null);
                if (appelLoyer == null)
                        throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found",
                                        ErrorCodes.APPELLOYER_NOT_FOUND);
                BailLocation bailLocation = appelLoyer.getBailLocationAppelLoyer();
                log.info("le bail concerner est {}", bailLocation.getId());
                List<AppelLoyersFactureDto> listAppelImpayerParBail = appelLoyerService
                                .findAllAppelLoyerImpayerByBailId(bailLocation.getId());
                double montantVerser = dto.getMontantEncaissement();
                EncaissementPrincipal encaissementPrincipal;
                for (AppelLoyersFactureDto appelLoyerDto : listAppelImpayerParBail) {
                        encaissementPrincipal = new EncaissementPrincipal();
                        if (montantVerser >= appelLoyerDto.getSoldeAppelLoyer()) {
                                // Total des encaissement percu pour le mois en cours;
                                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                appelLoyerDto.getId());
                                double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer()
                                                - totalEncaissementByIdAppelLoyer;
                                montantVerser = montantVerser - montantAPayerLeMois;
                                appelLoyerDto.setStatusAppelLoyer("Soldé");
                                appelLoyerDto.setSolderAppelLoyer(true);
                                appelLoyerDto.setSoldeAppelLoyer(0);
                                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                AppelLoyersFactureDto appelLoyersFactureDto = appelLoyerService
                                                .getFirstLoyerImpayerByBien(appelLoyerDto.getIdBailLocation());
                                appelLoyersFactureDto.setUnLock(true);
                                AppelLoyer unlockAppelLoyer = appelLoyerRepository
                                                .findById(appelLoyersFactureDto.getId()).orElse(null);
                                if (unlockAppelLoyer != null) {
                                        unlockAppelLoyer.setUnLock(true);
                                        appelLoyerRepository.save(unlockAppelLoyer);
                                }

                                encaissementPrincipal.setAppelLoyerEncaissement(
                                                gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                encaissementPrincipal.setOperationType(dto.getOperationType());
                                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                encaissementPrincipal.setSoldeEncaissement(0);
                                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                                encaissementPrincipal.setMontantEncaissement(montantAPayerLeMois);
                                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                encaissementPrincipalRepository
                                                .save(encaissementPrincipal);

                                if (montantVerser <= 0) {
                                        break;
                                }
                        } else {
                                // Total des encaissement percu pour le mois en cours;
                                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                appelLoyerDto.getId());
                                double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer()
                                                - totalEncaissementByIdAppelLoyer;
                                double montantPayer = montantAPayerLeMois - montantVerser;
                                appelLoyerDto.setStatusAppelLoyer("partiellement payé");
                                appelLoyerDto.setSolderAppelLoyer(false);
                                appelLoyerDto.setSoldeAppelLoyer(montantPayer);

                                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                AppelLoyersFactureDto appelLoyersFactureDto = appelLoyerService
                                                .getFirstLoyerImpayerByBien(appelLoyerDto.getIdBailLocation());
                                appelLoyersFactureDto.setUnLock(true);
                                AppelLoyer unlockAppelLoyer = appelLoyerRepository
                                                .findById(appelLoyersFactureDto.getId()).orElse(null);
                                if (unlockAppelLoyer != null) {
                                        unlockAppelLoyer.setUnLock(true);
                                        appelLoyerRepository.save(unlockAppelLoyer);
                                }
                                // EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                                encaissementPrincipal.setAppelLoyerEncaissement(
                                                gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                encaissementPrincipal.setOperationType(dto.getOperationType());
                                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                encaissementPrincipal.setSoldeEncaissement(montantPayer);
                                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                                encaissementPrincipal.setMontantEncaissement(montantVerser);
                                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                encaissementPrincipalRepository
                                                .save(encaissementPrincipal);
                                break;
                        }
                }

                return true;

        }

        @Override
        public boolean saveEncaissementMasse(List<EncaissementPayloadDto> dtos) {
                for (EncaissementPayloadDto dto : dtos) {

                        List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
                        if (!errors.isEmpty()) {

                                throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                                                ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
                        }
                        AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyer()).orElse(null);
                        if (appelLoyer == null)
                                throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found",
                                                ErrorCodes.APPELLOYER_NOT_FOUND);
                        BailLocation bailLocation = appelLoyer.getBailLocationAppelLoyer();

                        List<AppelLoyersFactureDto> listAppelImpayerParBail = appelLoyerService
                                        .findAllAppelLoyerImpayerByBailId(bailLocation.getId());
                        double montantVerser = dto.getMontantEncaissement();

                        for (AppelLoyersFactureDto appelLoyerDto : listAppelImpayerParBail) {
                                if (montantVerser >= appelLoyerDto.getSoldeAppelLoyer()) {
                                        // Total des encaissement percu pour le mois en cours;
                                        double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                        appelLoyerDto.getId());
                                        double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer()
                                                        - totalEncaissementByIdAppelLoyer;
                                        montantVerser = montantVerser - montantAPayerLeMois;
                                        appelLoyerDto.setStatusAppelLoyer("Soldé");
                                        appelLoyerDto.setSolderAppelLoyer(true);
                                        appelLoyerDto.setSoldeAppelLoyer(0);
                                        appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                        EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                                        encaissementPrincipal.setAppelLoyerEncaissement(
                                                        gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                        encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                        encaissementPrincipal.setOperationType(dto.getOperationType());
                                        encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                        encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                        encaissementPrincipal.setDateEncaissement(dto.getDateEncaissement());
                                        encaissementPrincipal.setMontantEncaissement(montantAPayerLeMois);
                                        encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                        encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                        encaissementPrincipalRepository
                                                        .save(encaissementPrincipal);
                                        if (montantVerser <= 0) {
                                                break;
                                        }
                                } else {
                                        // Total des encaissement percu pour le mois en cours;
                                        double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                        appelLoyerDto.getId());
                                        double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer()
                                                        - totalEncaissementByIdAppelLoyer;
                                        double montantPayer = montantAPayerLeMois - montantVerser;
                                        appelLoyerDto.setStatusAppelLoyer("partiellement payé");
                                        appelLoyerDto.setSolderAppelLoyer(false);
                                        appelLoyerDto.setSoldeAppelLoyer(montantPayer);
                                        appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                        EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                                        encaissementPrincipal.setAppelLoyerEncaissement(
                                                        gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                        encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                        encaissementPrincipal.setOperationType(dto.getOperationType());
                                        encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                        encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                        encaissementPrincipal.setDateEncaissement(dto.getDateEncaissement());
                                        encaissementPrincipal.setMontantEncaissement(montantVerser);
                                        encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                        encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                        encaissementPrincipalRepository
                                                        .save(encaissementPrincipal);
                                        break;
                                }
                        }

                }
                return true;
        }

        @Override
        public List<EncaissementPrincipalDTO> findAllEncaissement(Long idAgence) {
                Comparator<EncaissementPrincipal> compareBydatecreation = Comparator
                                .comparing(EncaissementPrincipal::getCreationDate);
                return encaissementPrincipalRepository.findAll()
                                .stream()
                                .filter(agence -> agence.getIdAgence() == idAgence)
                                .sorted(compareBydatecreation)
                                .map(gestimoWebMapper::fromEncaissementPrincipal)
                                .collect(Collectors.toList());
        }

        @Override
        public double getTotalEncaissementByIdAppelLoyer(Long idAppelLoyer) {
                List<Double> listeloyerEncaisser = encaissementPrincipalRepository.findAll()
                                .stream()
                                .filter(e -> e.getAppelLoyerEncaissement().getId() == idAppelLoyer)
                                .map(EncaissementPrincipal::getMontantEncaissement)
                                .collect(Collectors.toList());
                Double sum = listeloyerEncaisser.stream().mapToDouble(Double::doubleValue).sum();
                System.out.println(sum);
                return sum;
        }

        @Override
        public EncaissementPrincipalDTO findEncaissementById(Long id) {
                log.info("We are going to get back the Encaissement By id {}", id);
                if (id == null) {
                        log.error("you are not provided a Villa.");
                        return null;
                }
                EncaissementPrincipal encaissementPrincipal = encaissementPrincipalRepository.findById(id)
                                .orElseThrow(
                                                () -> new InvalidEntityException(
                                                                "Aucun Studio has been found with Code " + id,
                                                                ErrorCodes.ENCAISEMENT_NOT_FOUND));
                return gestimoWebMapper.fromEncaissementPrincipal(encaissementPrincipal);
        }

        @Override
        public List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id) {
                Comparator<EncaissementPrincipal> compareBydatecreation = Comparator
                                .comparing(EncaissementPrincipal::getId);
                return encaissementPrincipalRepository.findAll()
                                .stream().sorted(compareBydatecreation.reversed())
                                .filter(bien -> Objects
                                                .equals(bien.getAppelLoyerEncaissement().getBailLocationAppelLoyer()
                                                                .getBienImmobilierOperation().getId(), id))
                                .map(gestimoWebMapper::fromEncaissementPrincipal)
                                .collect(Collectors.toList());
        }

        @Override
        public List<EncaissementPrincipalDTO> findAllEncaissementByIdLocataire(Long id) {

                return encaissementPrincipalRepository.findAll()
                                .stream()
                                .filter(bien -> Objects.equals(
                                                bien.getAppelLoyerEncaissement().getBailLocationAppelLoyer()
                                                                .getUtilisateurOperation().getId(),
                                                id))
                                .map(gestimoWebMapper::fromEncaissementPrincipal)
                                .collect(Collectors.toList());
        }

        @Override
        public boolean delete(Long id) {
                return false;
        }

        @Override
        public List<EncaissementPrincipalDTO> saveEncaissementAvecRetourDeList(EncaissementPayloadDto dto) {

                log.info("We are going to create  a new encaissement EncaissementPrincipalServiceImpl {}", dto);
                List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
                if (!errors.isEmpty()) {
                        log.error("L'encaissement n'est pas valide {}", errors);
                        throw new InvalidEntityException("Certains attributs de l'object site sont null.",
                                        ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
                }
                AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyer()).orElse(null);
                if (appelLoyer == null)
                        throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found",
                                        ErrorCodes.APPELLOYER_NOT_FOUND);
                BailLocation bailLocation = appelLoyer.getBailLocationAppelLoyer();
                log.info("le bail concerner est {}", bailLocation.getId());
                List<AppelLoyersFactureDto> listAppelImpayerParBail = appelLoyerService
                                .findAllAppelLoyerImpayerByBailId(bailLocation.getId());
                double montantVerser = dto.getMontantEncaissement();

                log.info("BAIL A ENREGISTRER {} et la liste des appels impayés est {}", bailLocation.getId(),
                                listAppelImpayerParBail.size());
                EncaissementPrincipal encaissementPrincipal;
                for (AppelLoyersFactureDto appelLoyerDto : listAppelImpayerParBail) {
                        log.info("On est dans la boucle {},{}, {} ; {}", appelLoyerDto.getAbrvBienimmobilier(),
                                        appelLoyerDto.getMontantLoyerBailLPeriode(), montantVerser,
                                        appelLoyerDto.getSoldeAppelLoyer());
                        encaissementPrincipal = new EncaissementPrincipal();
                        if (montantVerser > appelLoyerDto.getSoldeAppelLoyer()) {
                                // Total des encaissement percu pour le mois en cours;
                                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                appelLoyerDto.getId());
                                double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer()
                                                - totalEncaissementByIdAppelLoyer;
                                montantVerser = montantVerser - montantAPayerLeMois;
                                appelLoyerDto.setStatusAppelLoyer("Soldé");
                                appelLoyerDto.setSolderAppelLoyer(true);
                                appelLoyerDto.setSoldeAppelLoyer(0);
                                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));

                                encaissementPrincipal.setAppelLoyerEncaissement(
                                                gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                encaissementPrincipal.setOperationType(dto.getOperationType());
                                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                encaissementPrincipal.setSoldeEncaissement(0);
                                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                                encaissementPrincipal.setMontantEncaissement(montantAPayerLeMois);
                                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository
                                                .save(encaissementPrincipal);

                                if (montantVerser <= 0) {
                                        break;
                                }
                        } else {
                                // Total des encaissement percu pour le mois en cours;
                                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                appelLoyerDto.getId());
                                double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer()
                                                - totalEncaissementByIdAppelLoyer;
                                double montantPayer = montantAPayerLeMois - montantVerser;
                                if (montantPayer > 0) {
                                        appelLoyerDto.setStatusAppelLoyer("partiellement payé");
                                        appelLoyerDto.setSolderAppelLoyer(false);
                                } else {
                                        appelLoyerDto.setStatusAppelLoyer("Soldé");
                                        appelLoyerDto.setSolderAppelLoyer(true);
                                }
                                // appelLoyerDto.setStatusAppelLoyer("partiellement payé");
                                // appelLoyerDto.setSolderAppelLoyer(false);
                                appelLoyerDto.setSoldeAppelLoyer(montantPayer);
                                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                // EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                                encaissementPrincipal.setAppelLoyerEncaissement(
                                                gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                encaissementPrincipal.setOperationType(dto.getOperationType());
                                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                encaissementPrincipal.setSoldeEncaissement(montantPayer);
                                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                                encaissementPrincipal.setMontantEncaissement(montantVerser);
                                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository
                                                .save(encaissementPrincipal);
                                break;
                        }
                }

                Comparator<EncaissementPrincipal> compareBydatecreation = Comparator
                                .comparing(EncaissementPrincipal::getId);
                AgenceImmobiliere agenceFound = agenceImmobiliereRepository.findById(bailLocation.getIdAgence())
                                .orElse(null);
                String nomString;
                if (agenceFound.getNomAgence() == "magiser") {
                        nomString = "MAGISER";
                } else {
                        nomString = "MAGISER";
                }
                try {
                        String leTok = envoiSmsOrange.getTokenSmsOrange();

                        String message = "L'Agence " + nomString + " accuse bonne reception de la somme de "
                                        + dto.getMontantEncaissement()
                                        + " F CFA pour le reglement de votre loyer du bail : "
                                        + bailLocation.getDesignationBail().toUpperCase() + ".";
                        envoiSmsOrange.sendSms(leTok, message, "+2250000",
                                        bailLocation.getUtilisateurOperation().getUsername(), nomString);
                        // System.out.println("********************* Le toke toke est : " + leTok);
                } catch (Exception e) {
                        System.err.println(e.getMessage());
                }
                boolean sauve = appelLoyerService.miseAjourDesUnlockDesBaux(dto.getIdAgence());

                return encaissementPrincipalRepository.findAll()
                                .stream().sorted(compareBydatecreation.reversed())
                                .filter(agence -> agence.getIdAgence() == dto.getIdAgence())
                                .filter(bien -> Objects.equals(
                                                bien.getAppelLoyerEncaissement().getBailLocationAppelLoyer()
                                                                .getBienImmobilierOperation().getId(),
                                                appelLoyer.getBailLocationAppelLoyer().getBienImmobilierOperation()
                                                                .getId()))
                                .map(gestimoWebMapper::fromEncaissementPrincipal)
                                .distinct()
                                .collect(Collectors.toList());

        }

        @Override
        public double sommeEncaisserParJour(String jour, Long idAgence, Long chapitre) {
                if (chapitre == 0 || chapitre == null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                        .withLocale(Locale.FRENCH);
                        LocalDate localDate = LocalDate.parse(jour, formatter);
                        System.out.println(" -----------------------------------");
                        System.out.println("La date est la suivante : " + localDate);

                        List<EncaissementPrincipal> listEncaissent = encaissementPrincipalRepository.findAll().stream()
                                        .filter(agence -> agence.getIdAgence() == idAgence
                                                        && agence.getDateEncaissement().equals(localDate))
                                        .collect(Collectors.toList());
                        List<Double> listEncaissDouble = listEncaissent.stream()
                                        .map(EncaissementPrincipal::getMontantEncaissement)
                                        .collect(Collectors.toList());

                        Double totalEncaissement = listEncaissDouble.stream().mapToDouble(Double::doubleValue).sum();
                        return totalEncaissement;
                } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                                        .withLocale(Locale.FRENCH);
                        LocalDate localDate = LocalDate.parse(jour, formatter);
                        System.out.println(" -----------------------------------");
                        System.out.println("La date est la suivante : " + localDate);

                        List<EncaissementPrincipal> listEncaissent = encaissementPrincipalRepository.findAll().stream()
                                        .filter(agence -> agence.getIdAgence() == idAgence
                                                        && agence.getDateEncaissement().equals(localDate)
                                                        && agence.getAppelLoyerEncaissement()
                                                                        .getBailLocationAppelLoyer()
                                                                        .getBienImmobilierOperation().getChapitre()
                                                                        .getId() == chapitre)
                                        .collect(Collectors.toList());
                        List<Double> listEncaissDouble = listEncaissent.stream()
                                        .map(EncaissementPrincipal::getMontantEncaissement)
                                        .collect(Collectors.toList());

                        Double totalEncaissement = listEncaissDouble.stream().mapToDouble(Double::doubleValue).sum();
                        return totalEncaissement;
                }

        }

        @Override
        public List<LocataireEncaisDTO> listeLocataireImpayerParAgenceEtPeriode(Long agence, String periode) {
                List<LocataireEncaisDTO> appelLocataire = appelLoyerRepository.findAll().stream()
                                .filter(app -> app.getSoldeAppelLoyer() > 0 && app.getIdAgence() == agence
                                                && app.getPeriodeAppelLoyer().equals(periode)
                                                && app.isCloturer() == false)
                                .map(bailMapperImpl::fromOperationAppelLoyer)
                                .collect(Collectors.toList());
                return appelLocataire;
        }

        @Override
        public double sommeEncaissementParAgenceEtParChapitreEtParPeriode(Long agence, Long chapitre, String dateDebut,
                        String dateFin) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException(
                                "Unimplemented method 'sommeEncaissementParAgenceEtParChapitreEtParPeriode'");
        }

        @Override
        public double sommeEncaissementParAgenceEtParPeriode(Long agence, LocalDate dateDebut, LocalDate dateFin) {
                List<Double> listeEncaissementParPeriode = encaissementPrincipalRepository.findAll()
                                .stream()
                                .filter(encaissement -> encaissement.getIdAgence() == agence &&
                                                encaissement.getDateEncaissement().isAfter(dateDebut) &&
                                                encaissement.getDateEncaissement().isBefore(dateFin))
                                .map(EncaissementPrincipal::getMontantEncaissement)
                                .collect(Collectors.toList());
                Double totalEncaissement = listeEncaissementParPeriode.stream().mapToDouble(Double::doubleValue).sum();
                return totalEncaissement;
        }

        @Override
        public double sommeImpayerParAgenceEtParChapitreEtParPeriode(Long agence, Long chapitre, String dateDebut,
                        String dateFin) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException(
                                "Unimplemented method 'sommeImpayerParAgenceEtParChapitreEtParPeriode'");
        }

        @Override
        public double sommeImpayerParAgenceEtParPeriode(Long agence, String dateDebut, String dateFin) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'sommeImpayerParAgenceEtParPeriode'");
        }

        @Override
        public Map<YearMonth, Double> getTotalEncaissementsParMois(Long idAgence, LocalDate debut, LocalDate fin) {

                return encaissementPrincipalRepository.findAll().stream()
                                .filter(e -> e.getIdAgence() == idAgence
                                                && !e.getAppelLoyerEncaissement().getDateDebutMoisAppelLoyer()
                                                                .isBefore(debut)
                                                && !e.getAppelLoyerEncaissement().getDateDebutMoisAppelLoyer()
                                                                .isAfter(fin))
                                .collect(Collectors.groupingBy(
                                                e -> YearMonth.from(e.getAppelLoyerEncaissement()
                                                                .getDateDebutMoisAppelLoyer()),
                                                Collectors.summingDouble(
                                                                EncaissementPrincipal::getMontantEncaissement)));

        }

        @Override
        public Map<YearMonth, Double[]> getTotalEncaissementsEtMontantsDeLoyerParMois(Long idAgence, LocalDate debut,
                        LocalDate fin) {
                // Initialisation de la map de résultats
                Map<YearMonth, Double[]> result = new HashMap<>();
                // YearMonth currentMonth
                YearMonth startMonth = YearMonth.from(debut);
                YearMonth endMonth = YearMonth.from(fin);

                YearMonth currentMonth = startMonth;
                // Boucle pour itérer sur chaque mois dans la période donnée
                while (!currentMonth.isAfter(endMonth)) {
                        YearMonth finalCurrentMonth = currentMonth;
                        // Utilisation d'un stream pour filtrer les loyers payés dans le mois actuel
                        List<EncaissementPrincipal> loyersDuMois = encaissementPrincipalRepository.findAll().stream()
                                        .filter(loyer -> loyer.getIdAgence() == idAgence && YearMonth
                                                        .from(loyer.getAppelLoyerEncaissement()
                                                                        .getDateDebutMoisAppelLoyer())
                                                        .equals(finalCurrentMonth))
                                        .collect(Collectors.toList());
                        List<AppelLoyersFactureDto> loyers = appelLoyerService.findAll(idAgence).stream()
                                        .filter(loyer -> YearMonth.from(loyer.getDateDebutMoisAppelLoyer())
                                                        .equals(finalCurrentMonth))
                                        .collect(Collectors.toList());
                        // Calcul du total des encaissements de loyer pour le mois actuel
                        Double totalEncaissements = loyersDuMois.stream()
                                        .mapToDouble(encaissement -> encaissement.getMontantEncaissement())
                                        .sum();
                        // Calcul du total des montants de loyers pour le mois actuel
                        Double totalMontantLoyers = loyers.stream()
                                        .mapToDouble(encaissement -> encaissement.getMontantLoyerBailLPeriode())
                                        .sum();

                        // Ajout des résultats dans la map de résultats
                        result.put(currentMonth, new Double[] { totalEncaissements, totalMontantLoyers });

                        // Passage au mois suivant
                        currentMonth = currentMonth.plusMonths(1);

                }
                return result;
        }

        @Override
        public List<EncaissementPrincipalDTO> listeEncaissementParPeriode(Long agence, LocalDate dateDebut,
                        LocalDate dateFin) {

                // Filtrer les paiements selon la période
              return  encaissementPrincipalRepository.findAll().stream()
                                .filter(encaissement -> encaissement.getIdAgence() == agence
                                                && encaissement.getDateEncaissement().isAfter(dateDebut) &&
                                                encaissement.getDateEncaissement().isBefore(dateFin))
                                 .map(gestimoWebMapper::fromEncaissementPrincipal)
                                .collect(Collectors.toList());                
        }

        @Override
        public List<LocataireEncaisDTO> saveEncaissementGrouperAvecRetourDeList(EncaissementPayloadDto dto) {
                List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
                Long idDeAgence = 0L;
                String laPeriode = "";

                if (!errors.isEmpty()) {
                        throw new InvalidEntityException("Certains attributs de l'objet site sont null.",
                                        ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
                }
                if (dto.getMontantEncaissement() > 0) {
                        log.info("Le dto {}",
                                        dto);
                        AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyer())
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                        "AppelLoyer from GestimoMapper not found",
                                                        ErrorCodes.APPELLOYER_NOT_FOUND));
                        laPeriode = appelLoyer.getPeriodeAppelLoyer();
                        List<EncaissementPrincipal> lesEncaii = encaissementPrincipalRepository
                                        .findByAppelLoyerEncaissement(appelLoyer);
                        log.info(" THE SIZE OFF ENCAISSEMENT : : : : : ", lesEncaii.size());
                        if (lesEncaii.size() == 0) {
                                EncaissementPrincipal encaissementPrincipal;
                                log.info("Le montant versé groupe  et le id {} , {}", dto.getMontantEncaissement(),
                                                dto.getIdAppelLoyer());

                                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(
                                                appelLoyer.getId());
                                double montantAPayerLeMois = appelLoyer.getMontantLoyerBailLPeriode()
                                                - totalEncaissementByIdAppelLoyer;
                                idDeAgence = appelLoyer.getIdAgence();

                                appelLoyer.setTypePaiement(dto.getTypePaiement());
                                appelLoyer.setStatusAppelLoyer("Soldé");
                                appelLoyer.setSolderAppelLoyer(true);
                                appelLoyer.setSoldeAppelLoyer(0);
                                appelLoyer.setUnLock(false);
                                appelLoyerRepository.saveAndFlush(appelLoyer);
                                // SAVE L"ENCAISSEMENT POUR LES APPELS

                                encaissementPrincipal = new EncaissementPrincipal();
                                encaissementPrincipal.setAppelLoyerEncaissement(appelLoyer);
                                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                                encaissementPrincipal.setOperationType(dto.getOperationType());
                                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                                encaissementPrincipal.setSoldeEncaissement(0);
                                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                                encaissementPrincipal.setMontantEncaissement(montantAPayerLeMois);
                                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                                encaissementPrincipalRepository.saveAndFlush(encaissementPrincipal);
                                log.info("THE APPEL SAVE IS id , appel , appel save, flux {},  {}, {} , {} ",
                                                idDeAgence,
                                                appelLoyer,
                                                appelLoyerRepository.save(appelLoyer),
                                                appelLoyerRepository.saveAndFlush(appelLoyer));

                                List<Long> getAllIbOperationInAppel = appelLoyerService
                                                .getAllIbOperationInAppel(idDeAgence);

                                if (getAllIbOperationInAppel.size() > 0) {
                                        for (int index = 0; index < getAllIbOperationInAppel.size(); index++) {
                                                AppelLoyersFactureDto findFirstAppelImpayerByBail = appelLoyerService
                                                                .findFirstAppelImpayerByBail(
                                                                                getAllIbOperationInAppel.get(index));

                                                if (findFirstAppelImpayerByBail != null) {
                                                        AppelLoyer appelLoyerUp = appelLoyerRepository
                                                                        .findById(findFirstAppelImpayerByBail.getId())
                                                                        .orElse(null);
                                                        if (appelLoyerUp != null) {
                                                                appelLoyerUp.setUnLock(true);
                                                                appelLoyerRepository.saveAndFlush(appelLoyerUp);
                                                        }
                                                }
                                        }
                                }
                        }
                        AgenceImmobiliere agenceFound = agenceImmobiliereRepository.findById(dto.getIdAgence())
                                        .orElse(null);
                        // System.out.println(sauve);

                        BailLocation bailLocation = appelLoyer.getBailLocationAppelLoyer();
                        String nomString;
                        if ("magiser".equals(agenceFound.getNomAgence())) {
                                nomString = "MAGISER";
                        } else {
                                nomString = "MAGISER";
                        }

                        try {
                                String leTok = envoiSmsOrange.getTokenSmsOrange();

                                String message = "L'Agence " + nomString + " accuse bonne réception de la somme de "
                                                + dto.getMontantEncaissement()
                                                + " F CFA pour le règlement de votre loyer du bail : "
                                                + bailLocation.getDesignationBail().toUpperCase() + ".";
                                envoiSmsOrange.sendSms(leTok, message, "+2250000",
                                                bailLocation.getUtilisateurOperation().getUsername(), nomString);
                        } catch (Exception e) {
                                System.err.println(e.getMessage());
                        }
                        return listeLocataireImpayerParAgenceEtPeriode(idDeAgence, laPeriode);
                } else {
                        return null;
                }

        }

        @Override
        public List<AppelLoyerEncaissDto> listeEncaisseLoyerEntreDeuxDate(Long agence, LocalDate dateDebut,
                        LocalDate dateFin) {
                   return  encaissementPrincipalRepository.findAll().stream()
                                .filter(encaissement -> encaissement.getIdAgence() == agence
                                                && encaissement.getDateEncaissement().isAfter(dateDebut) &&
                                                encaissement.getDateEncaissement().isBefore(dateFin))
                                 .map(gestimoWebMapper::fromEncaissementPrincipalAppelLoyerEncaissDto)
                                .collect(Collectors.toList());  
        }

        @Override
        public StatistiquePeriodeDto statistiquePeriodeEntreDeuxDate(String periodeDebut, String periodeDFin,
                        Long idAgence, Long chapitre) {
                
                throw new UnsupportedOperationException("Unimplemented method 'statistiquePeriodeEntreDeuxDate'");
        }

        @Override
        public StatistiquePeriodeDto statistiqueAnneeEntreDeuxDate(int anneeDebut, int anneeFin, Long idAgence,
                        Long chapitre) {
                
                throw new UnsupportedOperationException("Unimplemented method 'statistiqueAnneeEntreDeuxDate'");
        }

        @Override
        public double sommeLoyerEntreDeuxPeriode(Long agence, LocalDate dateDebut,
                        LocalDate dateFin) {
                      List<Double> listeDesMontantLoyerParPeriode = encaissementPrincipalRepository.findAll()
                                .stream()
                                .filter(encaissement -> encaissement.getIdAgence() == agence &&
                                                encaissement.getDateEncaissement().isAfter(dateDebut) &&
                                                encaissement.getDateEncaissement().isBefore(dateFin))
                                .map(EncaissementPrincipal::getMontantEncaissement)
                                .collect(Collectors.toList());
                             //LISTE DES SOLDES   
                                        List<Double> listeSoldLoyerParPeriode = encaissementPrincipalRepository.findAll()
                                .stream()
                                .filter(encaissement -> encaissement.getIdAgence() == agence &&
                                                encaissement.getDateEncaissement().isAfter(dateDebut) &&
                                                encaissement.getDateEncaissement().isBefore(dateFin))
                                .map(EncaissementPrincipal::getSoldeEncaissement)
                                .collect(Collectors.toList());
             //   Double totalEncaissement = listeDesMontantLoyerParPeriode.stream().mapToDouble(Double::doubleValue).sum();
               
                Double totalEncaissement =listeSoldLoyerParPeriode.stream().mapToDouble(Double::doubleValue).sum()+ listeDesMontantLoyerParPeriode.stream().mapToDouble(Double::doubleValue).sum();
               

                return totalEncaissement;
        }

        

}
