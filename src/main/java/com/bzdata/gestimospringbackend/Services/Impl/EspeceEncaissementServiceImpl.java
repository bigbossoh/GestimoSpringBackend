package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.EspeceEncaissementDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;
import com.bzdata.gestimospringbackend.Models.EspeceEncaissement;
import com.bzdata.gestimospringbackend.Services.EspeceEncaissementService;
import com.bzdata.gestimospringbackend.Services.UtilisateurService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AppelLoyerRepository;
import com.bzdata.gestimospringbackend.repository.EspeceEncaissementRepository;
import com.bzdata.gestimospringbackend.validator.EspeceEncaissementDtoValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EspeceEncaissementServiceImpl implements EspeceEncaissementService {
    final EspeceEncaissementRepository encaissementRepository;
    final AppelLoyerRepository appelLoyerRepository;
    final UtilisateurService utilisateurService;

    @Override
    public EspeceEncaissementDto save(EspeceEncaissementDto dto) {
        EspeceEncaissement especeEncaissement = new EspeceEncaissement();
        double soldeLoyer = 0;

        log.info("Nous allons faire un encaiisement par especes {}", dto);
        List<String> errors = EspeceEncaissementDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error(" Encaissement non valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object encaissement sont null.",
                    ErrorCodes.ENCAISSEMENT_NOT_VALID, errors);
        }
        AppelLoyer appelLoyer = appelLoyerRepository.findById(dto.getIdAppelLoyerEncaissement())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Appel n'a été trouvé avec l'Id " + dto.getIdAppelLoyerEncaissement(),
                        ErrorCodes.APPELLOYER_NOT_FOUND));
        UtilisateurRequestDto utilisateurRequestDto = utilisateurService.findById(dto.getIdAppelLoyerEncaissement());

        especeEncaissement.setAppelLoyerEncaissement(appelLoyer);
        especeEncaissement.setDateEncaissement(dto.getDateEncaissement());
        especeEncaissement.setMontantEncaissement(dto.getMontantEncaissement());
        //TODO il faudra verifier que utilisateur est soit un gerant ou le locataire lui meme 
        especeEncaissement.setUtilisateurEncaissement(UtilisateurRequestDto.toEntity(utilisateurRequestDto));
        // especeEncaissement.set

        EspeceEncaissement especeEncaissementSave = encaissementRepository.save(especeEncaissement);
        // calcul des soldes
        soldeLoyer = appelLoyer.getMontantBailLPeriode() - dto.getMontantEncaissement();
        appelLoyer.setSoldeAppelLoyer(soldeLoyer);
        if (soldeLoyer == 0) {
            appelLoyer.setSolderAppelLoyer(true);
        } else {
            appelLoyer.setSolderAppelLoyer(false);
        }
        appelLoyerRepository.save(appelLoyer);
        return EspeceEncaissementDto.fromEntity(especeEncaissementSave);
    }

    @Override
    public boolean delete(Long id) {

        return false;
    }

    @Override
    public List<EspeceEncaissementDto> findAll() {

        return null;
    }

    @Override
    public EspeceEncaissementDto findById(Long id) {

        return null;
    }

    @Override
    public EspeceEncaissementDto findByName(String nom) {

        return null;
    }

    @Override
    public List<EspeceEncaissementDto> findAllByIdBienImmobilier(Long id) {

        return null;
    }

    @Override
    public List<EspeceEncaissementDto> findAllByIdLocataire(Long id) {

        return null;
    }

}
