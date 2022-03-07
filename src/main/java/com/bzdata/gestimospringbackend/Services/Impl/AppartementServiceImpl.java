package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;
import com.bzdata.gestimospringbackend.DTOs.EtageDto;
import com.bzdata.gestimospringbackend.Models.Appartement;
import com.bzdata.gestimospringbackend.Services.AppartementService;
import com.bzdata.gestimospringbackend.Services.EtageService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AppartementRepository;
import com.bzdata.gestimospringbackend.validator.AppartementDtoValidator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppartementServiceImpl implements AppartementService {
    public final AppartementRepository appartementRepository;
    final EtageService etageService;

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Appartement with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Appartement");
            return false;
        }
        boolean exist = appartementRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Appartement avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.APPARTEMENT_NOT_FOUND);
        }

        appartementRepository.deleteById(id);
        return true;
    }

    @Override
    public List<AppartementDto> findAll() {
        return appartementRepository.findAll(Sort.by(Direction.ASC, "nomApp")).stream()
                .map(AppartementDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AppartementDto findById(Long id) {
        log.info("We are going to get back the Appartement By {}", id);
        if (id == null) {
            log.error("you are not provided a Appartement.");
            return null;
        }
        return appartementRepository.findById(id).map(AppartementDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Appartement has been found with Code " + id,
                        ErrorCodes.APPARTEMENT_NOT_FOUND));
    }

    @Override
    public AppartementDto findByName(String nom) {
        log.info("We are going to get back the Appartement By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Appartement.");
            return null;
        }
        return appartementRepository.findByNomApp(nom).map(AppartementDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Appartement has been found with name " + nom,
                        ErrorCodes.APPARTEMENT_NOT_FOUND));
    }

    @Override
    public List<AppartementDto> findAllByIdEtage(Long id) {
        log.info("We are going to get back the Appartement By {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        EtageDto etage = etageService.findById(id);
        if (etage == null) {
            log.error("Appartement not found for the Etage.");
            return null;
        }
        return appartementRepository.findByEtageAppartement(EtageDto.toEntity(etage)).stream()
                .map(AppartementDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AppartementDto save(AppartementDto dto) {
        log.info("We are going to create  a new Appartement {}", dto);
        List<String> errors = AppartementDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'Appartement n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Appartement sont null.",
                    ErrorCodes.APPARTEMENT_NOT_VALID, errors);
        }
        Appartement appartement = appartementRepository.save(AppartementDto.toEntity(dto));
        return AppartementDto.fromEntity(appartement);
    }

}
