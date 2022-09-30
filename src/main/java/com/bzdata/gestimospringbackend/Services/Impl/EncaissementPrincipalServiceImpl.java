package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.EncaissementPrincipalService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.EncaissementPrincipalRepository;
import com.bzdata.gestimospringbackend.validator.EncaissementPayloadDtoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EncaissementPrincipalServiceImpl implements EncaissementPrincipalService {
    final AppelLoyerRepository appelLoyerRepository;
    final GestimoWebMapperImpl gestimoWebMapper;
    final AppelLoyerService appelLoyerService;
    final EncaissementPrincipalRepository encaissementPrincipalRepository;

    @Override
    public boolean saveEncaissement(EncaissementPayloadDto dto) {
        log.info("We are going to create  a new encaissement EncaissementPrincipalServiceImpl {}", dto);
        List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("L'encaissement n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                    ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
        }
        AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyer()).orElse(null);
        if (appelLoyer == null)
            throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found", ErrorCodes.APPELLOYER_NOT_FOUND);
        BailLocation bailLocation = appelLoyer.getBailLocationAppelLoyer();
        log.info("le bail concerner est {}", bailLocation.getId());
        List<AppelLoyersFactureDto> listAppelImpayerParBail = appelLoyerService.findAllAppelLoyerImpayerByBailId(bailLocation.getId());
        double montantVerser = dto.getMontantEncaissement();
        log.info("le bail concerner est {} et la liste des appels impayés est {}", bailLocation.getId(), listAppelImpayerParBail.size());
        EncaissementPrincipal encaissementPrincipal;
        for (AppelLoyersFactureDto appelLoyerDto : listAppelImpayerParBail) {
            encaissementPrincipal= new EncaissementPrincipal();
            if (montantVerser >= appelLoyerDto.getSoldeAppelLoyer()) {
                //Total des encaissement percu pour le mois en cours;
                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(appelLoyerDto.getId());
                double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer() - totalEncaissementByIdAppelLoyer;
                montantVerser = montantVerser - montantAPayerLeMois;
                appelLoyerDto.setStatusAppelLoyer("Payé");
                appelLoyerDto.setSolderAppelLoyer(true);
                appelLoyerDto.setSoldeAppelLoyer(0);
                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));

                encaissementPrincipal.setAppelLoyerEncaissement(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                encaissementPrincipal.setOperationType(dto.getOperationType());
                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                encaissementPrincipal.setSoldeEncaissement(0);
                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                encaissementPrincipal.setMontantEncaissement(montantAPayerLeMois);
                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository.save(encaissementPrincipal);
                if(montantVerser<=0){
                    break;
                }
            }
            else
            {
                //Total des encaissement percu pour le mois en cours;
                double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(appelLoyerDto.getId());
                double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer() - totalEncaissementByIdAppelLoyer;
                double montantPayer =  montantAPayerLeMois-montantVerser;
                appelLoyerDto.setStatusAppelLoyer("partiellement payé");
                appelLoyerDto.setSolderAppelLoyer(false);
                appelLoyerDto.setSoldeAppelLoyer(montantPayer);
                appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                //EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                encaissementPrincipal.setAppelLoyerEncaissement(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                encaissementPrincipal.setModePaiement(dto.getModePaiement());
                encaissementPrincipal.setOperationType(dto.getOperationType());
                encaissementPrincipal.setIdAgence(dto.getIdAgence());
                encaissementPrincipal.setSoldeEncaissement(montantPayer);
                encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                encaissementPrincipal.setDateEncaissement(LocalDate.now());
                encaissementPrincipal.setMontantEncaissement(montantVerser);
                encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository.save(encaissementPrincipal);
                break;
            }
        }


        return true;

    }

    @Override
    public boolean saveEncaissementMasse(List<EncaissementPayloadDto> dtos) {
        for(EncaissementPayloadDto dto: dtos){
            log.info("We are going to create  a new encaissement EncaissementPrincipalServiceImpl {}", dtos);
            List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
            if (!errors.isEmpty()) {
                log.error("L'encaissement n'est pas valide {}", errors);
                throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                        ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
            }
            AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyer()).orElse(null);
            if (appelLoyer == null)
                throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found", ErrorCodes.APPELLOYER_NOT_FOUND);
            BailLocation bailLocation = appelLoyer.getBailLocationAppelLoyer();
            log.info("le bail concerner est {}", bailLocation.getId());
            List<AppelLoyersFactureDto> listAppelImpayerParBail = appelLoyerService.findAllAppelLoyerImpayerByBailId(bailLocation.getId());
            double montantVerser = dto.getMontantEncaissement();
            log.info("le bail concerner est {} et la liste des appels impayés est {}", bailLocation.getId(), listAppelImpayerParBail.size());
            for (AppelLoyersFactureDto appelLoyerDto : listAppelImpayerParBail) {
                if (montantVerser >= appelLoyerDto.getSoldeAppelLoyer()) {
                    //Total des encaissement percu pour le mois en cours;
                    double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(appelLoyerDto.getId());
                    double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer() - totalEncaissementByIdAppelLoyer;
                    montantVerser = montantVerser - montantAPayerLeMois;
                    appelLoyerDto.setStatusAppelLoyer("Payé");
                    appelLoyerDto.setSolderAppelLoyer(true);
                    appelLoyerDto.setSoldeAppelLoyer(0);
                    appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                    EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                    encaissementPrincipal.setAppelLoyerEncaissement(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                    encaissementPrincipal.setModePaiement(dto.getModePaiement());
                    encaissementPrincipal.setOperationType(dto.getOperationType());
                    encaissementPrincipal.setIdAgence(dto.getIdAgence());
                    encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                    encaissementPrincipal.setDateEncaissement(dto.getDateEncaissement());
                    encaissementPrincipal.setMontantEncaissement(montantAPayerLeMois);
                    encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                    encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                    EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository.save(encaissementPrincipal);
                    if(montantVerser<=0){
                        break;
                    }
                }
                else
                {
                    //Total des encaissement percu pour le mois en cours;
                    double totalEncaissementByIdAppelLoyer = getTotalEncaissementByIdAppelLoyer(appelLoyerDto.getId());
                    double montantAPayerLeMois = appelLoyerDto.getNouveauMontantLoyer() - totalEncaissementByIdAppelLoyer;
                    double montantPayer =  montantAPayerLeMois-montantVerser;
                    appelLoyerDto.setStatusAppelLoyer("partiellement payé");
                    appelLoyerDto.setSolderAppelLoyer(false);
                    appelLoyerDto.setSoldeAppelLoyer(montantPayer);
                    appelLoyerRepository.save(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                    EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();
                    encaissementPrincipal.setAppelLoyerEncaissement(gestimoWebMapper.fromAppelLoyerDto(appelLoyerDto));
                    encaissementPrincipal.setModePaiement(dto.getModePaiement());
                    encaissementPrincipal.setOperationType(dto.getOperationType());
                    encaissementPrincipal.setIdAgence(dto.getIdAgence());
                    encaissementPrincipal.setIdCreateur(dto.getIdCreateur());
                    encaissementPrincipal.setDateEncaissement(dto.getDateEncaissement());
                    encaissementPrincipal.setMontantEncaissement(montantVerser);
                    encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
                    encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
                    EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository.save(encaissementPrincipal);
                    break;
                }
            }




        }
        return true;
    }

    @Override
    public List<EncaissementPrincipalDTO> findAllEncaissement() {
        Comparator<EncaissementPrincipal> compareBydatecreation = Comparator
                .comparing(EncaissementPrincipal::getCreationDate);
        return encaissementPrincipalRepository.findAll()
                .stream()
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
                        () -> new InvalidEntityException("Aucun Studio has been found with Code " + id,
                                ErrorCodes.ENCAISEMENT_NOT_FOUND));
        return gestimoWebMapper.fromEncaissementPrincipal(encaissementPrincipal);
    }



    @Override
    public List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id) {

        return encaissementPrincipalRepository.findAll()
        .stream()
        .filter(bien->Objects.equals(bien.getAppelLoyerEncaissement().getBailLocationAppelLoyer().getBienImmobilierOperation().getId(), id))
        .map(gestimoWebMapper::fromEncaissementPrincipal)
        .collect(Collectors.toList());
    }

    @Override
    public List<EncaissementPrincipalDTO> findAllEncaissementByIdLocataire(Long id) {

        return  encaissementPrincipalRepository.findAll()
        .stream()
        .filter(bien->Objects.equals(bien.getAppelLoyerEncaissement().getBailLocationAppelLoyer().getUtilisateurOperation().getId(), id))
        .map(gestimoWebMapper::fromEncaissementPrincipal)
        .collect(Collectors.toList());
    }
    @Override
    public boolean delete(Long id) {
        return false;
    }
}
