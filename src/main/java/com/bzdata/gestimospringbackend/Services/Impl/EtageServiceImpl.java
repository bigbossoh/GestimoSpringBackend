package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.EtageDto;
import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Services.EtageService;
import com.bzdata.gestimospringbackend.Services.ImmeubleService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.validator.EtageDtoValidator;

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
public class EtageServiceImpl implements EtageService {
    final EtageRepository etageRepository;
    final ImmeubleService immeubleService;

    @Override
    public EtageDto save(EtageDto dto) {
        log.info("We are going to create  a new Etage {}", dto);
        List<String> errors = EtageDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'Etage n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Etage sont null.",
                    ErrorCodes.ETAGE_NOT_VALID, errors);
        }
        /**
         * Set the Immeuble object from its Id
         */
        dto.setImmeubleDto(immeubleService.findById(dto.getImmeubleDto().getId()));
        Etage etage = etageRepository.save(EtageDto.toEntity(dto));
        return EtageDto.fromEntity(etage);
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Etage with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Etage");
            return false;
        }
        boolean exist = etageRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Etage avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.ETAGE_NOT_FOUND);
        }
        Optional<Etage> eta = etageRepository.findById(id);
        if (eta.isPresent()) {
            if (eta.get().getMagasins().size() != 0 || eta.get().getAppartements().size() != 0 || eta.get().getStudios()
                    .size() != 0) {
                throw new EntityNotFoundException("l'Etage avec l'ID = " + id + " "
                        + "n' est pas vide ", ErrorCodes.IMMEUBLE_ALREADY_IN_USE);
            }
        }
        etageRepository.deleteById(id);
        return true;
    }

    @Override
    public List<EtageDto> findAll() {
        return etageRepository.findAll(Sort.by(Direction.ASC, "nomEtage")).stream()
                .map(EtageDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EtageDto findById(Long id) {
        log.info("We are going to get back the Etage By {}", id);
        if (id == null) {
            log.error("you are not provided a Etage.");
            return null;
        }
        return etageRepository.findById(id).map(EtageDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Etage has been found with Code " + id,
                        ErrorCodes.ETAGE_NOT_FOUND));
    }

    @Override
    public EtageDto findByName(String nom) {
        log.info("We are going to get back the Etage By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Etage.");
            return null;
        }
        return etageRepository.findByNomEtage(nom).map(EtageDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucune Etage has been found with name " + nom,
                        ErrorCodes.ETAGE_NOT_FOUND));
    }

    @Override
    public List<EtageDto> findAllByIdImmeuble(Long id) {

        log.info("We are going to get back the Etage By {}", id);
        if (id == null) {
            log.error("you are not provided a Etage.");
            return null;
        }
        ImmeubleDto immeubleDto = immeubleService.findById(id);
        if (immeubleDto == null) {
            log.error("Etage not found for the Immeuble.");
            return null;
        }
        return etageRepository.findByImmeuble(ImmeubleDto.toEntity(immeubleDto)).stream()
                .map(EtageDto::fromEntity)
                .collect(Collectors.toList());
    }

}
