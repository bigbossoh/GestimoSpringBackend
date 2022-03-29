package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.StudioDto;
import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Studio;
import com.bzdata.gestimospringbackend.Services.StudioService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.repository.StudioRepository;
import com.bzdata.gestimospringbackend.validator.StudioDtoValidator;

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
public class StudioServiceImpl implements StudioService {
    final StudioRepository studioRepository;
    // final EtageService etageService;
    final EtageRepository etageRepository;

    @Override
    public StudioDto save(StudioDto dto) {
        Studio studio = new Studio();
        log.info("We are going to create  a new Studio {}", dto);
        List<String> errors = StudioDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le Studio n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Studio sont null.",
                    ErrorCodes.STUDIO_NOT_VALID, errors);
        }
        Etage etage = etageRepository.findById(dto.getIdEtage()).orElseThrow(() -> new InvalidEntityException(
                "Aucun étage trouvé",
                ErrorCodes.ETAGE_NOT_FOUND, errors));
        studio.setAbrvNomStudio(dto.getAbrvNomStudio());
        studio.setEtageStudio(etage);
        studio.setNomStudio(dto.getNomStudio());
        studio.setNumeroStudio(dto.getNumeroStudio());
        studio.setDescStudio(dto.getDescStudio());
        Studio studioSave = studioRepository.save(studio);
        return StudioDto.fromEntity(studioSave);
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Studio with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Studio");
            return false;
        }
        boolean exist = studioRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Studio avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.STUDIO_NOT_FOUND);
        }

        studioRepository.deleteById(id);
        return true;
    }

    @Override
    public List<StudioDto> findAll() {
        return studioRepository.findAll(Sort.by(Direction.ASC, "nomStudio")).stream()
                .map(StudioDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public StudioDto findById(Long id) {
        log.info("We are going to get back the Studio By {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        return studioRepository.findById(id).map(StudioDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Studio has been found with Code " + id,
                        ErrorCodes.STUDIO_NOT_FOUND));
    }

    @Override
    public StudioDto findByName(String nom) {
        log.info("We are going to get back the Studio By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Studio.");
            return null;
        }
        return studioRepository.findByNomStudio(nom).map(StudioDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Studio has been found with name " + nom,
                        ErrorCodes.STUDIO_NOT_FOUND));
    }

    @Override
    public List<StudioDto> findAllByIdEtage(Long id) {
        log.info("We are going to get back the Studio By {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        Etage etage = etageRepository.findById(id).orElseThrow(() -> new InvalidEntityException(
                "Aucun étage trouvé",
                ErrorCodes.ETAGE_NOT_FOUND));
        if (etage == null) {
            log.error("Studio not found for the Etage.");
            return null;
        }
        return studioRepository.findByEtageStudio(etage).stream()
                .map(StudioDto::fromEntity)
                .collect(Collectors.toList());
    }

}
