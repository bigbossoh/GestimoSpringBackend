package com.bzdata.gestimospringbackend.Services.Impl;

import com.bzdata.gestimospringbackend.DTOs.EncaissementPayloadDto;
import com.bzdata.gestimospringbackend.DTOs.EncaissementPrincipalDTO;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.EncaissementPrincipal;
import com.bzdata.gestimospringbackend.Services.EncaissementPrincipalService;
import com.bzdata.gestimospringbackend.Services.EspeceEncaissementService;
import com.bzdata.gestimospringbackend.enumeration.EntiteOperation;
import com.bzdata.gestimospringbackend.enumeration.ModePaiement;
import com.bzdata.gestimospringbackend.enumeration.OperationType;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.EncaissementPrincipalRepository;
import com.bzdata.gestimospringbackend.validator.BailVillaDtoValidator;
import com.bzdata.gestimospringbackend.validator.EncaissementPayloadDtoValidator;
import com.bzdata.gestimospringbackend.validator.EncaissementPrincipalDTOValidor;
import com.bzdata.gestimospringbackend.validator.SiteDtoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class EncaissementPrincipalServiceImpl implements EncaissementPrincipalService {
    final AppelLoyerRepository appelLoyerRepository;
    final GestimoWebMapperImpl gestimoWebMapper;
    final EncaissementPrincipalRepository encaissementPrincipalRepository;
    @Override
    public EncaissementPrincipalDTO saveEncaissement(EncaissementPayloadDto dto) {
        EncaissementPrincipal encaissementPrincipal = new EncaissementPrincipal();

        log.info("We are going to create  a new encaissement {}", dto);
        List<String> errors = EncaissementPayloadDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("L'encaissement n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                    ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
        }
        encaissementPrincipal.setModePaiement(dto.getModePaiement());
        encaissementPrincipal.setOperationType(dto.getOperationType());
        encaissementPrincipal.setDateEncaissement(LocalDate.now());
        encaissementPrincipal.setMontantEncaissement(dto.getMontantEncaissement());
        encaissementPrincipal.setIntituleDepense(dto.getIntituleDepense());
        encaissementPrincipal.setEntiteOperation(dto.getEntiteOperation());
        AppelLoyer appelLoyer=appelLoyerRepository.findById(dto.getIdAppelLoyer()).orElse(null);
        if(appelLoyer==null)
            throw new EntityNotFoundException("AppelLoyer from GestimoMapper not found", ErrorCodes.APPELLOYER_NOT_FOUND);
        if(appelLoyer.getMontantBailLPeriode()<dto.getMontantEncaissement()){
            appelLoyer.setSolderAppelLoyer(false);
            appelLoyer.setSoldeAppelLoyer(appelLoyer.getSoldeAppelLoyer()-dto.getMontantEncaissement());
            appelLoyer.setStatusAppelLoyer("Paiement partiel");
            appelLoyerRepository.save(appelLoyer);
        }
        if(appelLoyer.getMontantBailLPeriode()==dto.getMontantEncaissement()){
            appelLoyer.setSolderAppelLoyer(true);
            appelLoyer.setSoldeAppelLoyer(appelLoyer.getSoldeAppelLoyer()-dto.getMontantEncaissement());
            appelLoyer.setStatusAppelLoyer("Payé");
            appelLoyerRepository.save(appelLoyer);
        }

        encaissementPrincipal.setAppelLoyerEncaissement(appelLoyer);
        EncaissementPrincipal saveEncaissement = encaissementPrincipalRepository.save(encaissementPrincipal);

        return gestimoWebMapper.fromEncaissementPrincipal(saveEncaissement);

    }

    @Override
    public List<EncaissementPrincipalDTO> findAllEncaissement() {
        return null;
    }

    @Override
    public EncaissementPrincipalDTO findEncaissementById(Long id) {
        return null;
    }

    @Override
    public List<EncaissementPrincipalDTO> findAllEncaissementByIdBienImmobilier(Long id) {
        return null;
    }

    @Override
    public List<EncaissementPrincipalDTO> findAllEncaissementByIdLocataire(Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
