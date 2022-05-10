package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.CommuneDto;
import com.bzdata.gestimospringbackend.DTOs.QuartierDto;
import com.bzdata.gestimospringbackend.Models.Commune;
import com.bzdata.gestimospringbackend.Models.Quartier;
import com.bzdata.gestimospringbackend.Services.CommuneService;
import com.bzdata.gestimospringbackend.Services.QuartierService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.CommuneRepository;
import com.bzdata.gestimospringbackend.repository.QuartierRepository;
import com.bzdata.gestimospringbackend.validator.QuartierDtoValidator;

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
@AllArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuartierServiceImpl implements QuartierService {
    final QuartierRepository quartierRepository;
    final CommuneRepository communeRepository;
    final CommuneService communeService;

    @Override
    public QuartierDto save(QuartierDto dto) {

        log.info("We are going to create  a new Quartier {}", dto);
        List<String> errors = QuartierDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le Quartier n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Quartier sont null.",
                    ErrorCodes.QUARTIER_NOT_VALID, errors);
        }
        Optional<Quartier> oldQuatier = quartierRepository.findById(dto.getId());
        if (!oldQuatier.isPresent()) {
            Quartier quartier = new Quartier();
            Commune commune = communeRepository.findById(dto.getIdCommune())
                    .orElseThrow(() -> new InvalidEntityException("Certain attributs de l'object Quartier sont null.",
                            ErrorCodes.COMMUNE_NOT_FOUND, errors));
            quartier.setAbrvQuartier(commune.getAbrvCommune() + "-" + dto.getAbrvQuartier());
            quartier.setNomQuartier(dto.getNomQuartier());
            quartier.setCommune(commune);
            quartier.setIdAgence(dto.getIdAgence());
            Quartier quartierSave = quartierRepository.save(quartier);
            return QuartierDto.fromEntity(quartierSave);
        } else {

            Commune commune = communeRepository.findById(dto.getIdCommune())
                    .orElseThrow(() -> new InvalidEntityException("Certain attributs de l'object Quartier sont null.",
                            ErrorCodes.COMMUNE_NOT_FOUND, errors));
            oldQuatier.get().setAbrvQuartier(commune.getAbrvCommune() + "-" + dto.getAbrvQuartier());
            oldQuatier.get().setNomQuartier(dto.getNomQuartier());
            oldQuatier.get().setCommune(commune);
            oldQuatier.get().setIdAgence(dto.getIdAgence());
            Quartier quartierSave = quartierRepository.save(oldQuatier.get());
            return QuartierDto.fromEntity(quartierSave);
        }

    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Quartier with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Quartier");
            return false;
        }
        boolean exist = communeRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucun Quartier avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.QUARTIER_NOT_FOUND);
        }
        quartierRepository.deleteById(id);
        return true;
    }

    @Override
    public List<QuartierDto> findAll() {
        return quartierRepository.findAll(Sort.by(Direction.ASC, "nomQuartier")).stream()
                .map(QuartierDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public QuartierDto findById(Long id) {
        log.info("We are going to get back the Quartier By {}", id);
        if (id == null) {
            log.error("you are not provided a Quartier.");
            return null;
        }
        return quartierRepository.findById(id).map(QuartierDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Quartier has been found with Code " + id,
                        ErrorCodes.QUARTIER_NOT_FOUND));
    }

    @Override
    public QuartierDto findByName(String nom) {
        log.info("We are going to get back the Qaurtier By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Quartier.");
            return null;
        }
        return quartierRepository.findByNomQuartier(nom).map(QuartierDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Quartier has been found with name " + nom,
                        ErrorCodes.QUARTIER_NOT_FOUND));
    }

    @Override
    public List<QuartierDto> findAllByIdCommune(Long id) {
        log.info("We are going to get back the Commune By {}", id);
        if (id == null || id == 0) {
            log.error("you are not provided a Commune.");
            new InvalidEntityException("Aucun Quartier has been found with name " + id,
                    ErrorCodes.COMMUNE_NOT_FOUND);
            return null;
        }
        CommuneDto communeDto = communeService.findById(id);
        if (communeDto == null) {

        }
        Optional<Commune> c = communeRepository.findById(id);
        if (!c.isPresent()) {
            log.error("Commune not found for the Quiartier.");
            return null;
        }
        return quartierRepository.findByCommune(c.get()).stream()
                .map(QuartierDto::fromEntity)
                .collect(Collectors.toList());
    }

}
