package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.CommuneDto;
import com.bzdata.gestimospringbackend.DTOs.VilleDto;
import com.bzdata.gestimospringbackend.Models.Commune;
import com.bzdata.gestimospringbackend.Models.Ville;
import com.bzdata.gestimospringbackend.Services.CommuneService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.CommuneRepository;
import com.bzdata.gestimospringbackend.repository.VilleRepository;
import com.bzdata.gestimospringbackend.validator.CommuneValidator;

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
public class CommuneServiceImpl implements CommuneService {
    final CommuneRepository communeRepository;
    final VilleRepository villeRepository;

    @Override
    public CommuneDto save(CommuneDto dto) {
        log.info("We are going to create  a new Commune {}", dto);
        List<String> errors = CommuneValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("la Commune n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Communeœœœœœœœœœœœœœœœœœœ sont null.",
                    ErrorCodes.COMMUNE_NOT_VALID, errors);
        }
        Commune commune = communeRepository.save(CommuneDto.toEntity(dto));
        return CommuneDto.fromEntity(commune);
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Commune with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Commune");
            return false;
        }
        boolean exist = communeRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Commune avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.COMMUNE_NOT_FOUND);
        }
        communeRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CommuneDto> findAll() {
        return communeRepository.findAll(Sort.by(Direction.ASC, "nomCommune")).stream()
                .map(CommuneDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CommuneDto findById(Long id) {
        log.info("We are going to get back the Commune By {}", id);
        if (id == null) {
            log.error("you are not provided a Commune.");
            return null;
        }
        return communeRepository.findById(id).map(CommuneDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucune Ville has been found with Code " + id,
                        ErrorCodes.COMMUNE_NOT_FOUND));
    }

    @Override
    public CommuneDto findByName(String nom) {
        log.info("We are going to get back the Commune By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Commune.");
            return null;
        }
        return communeRepository.findByNomCommune(nom).map(CommuneDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Commune has been found with name " + nom,
                        ErrorCodes.COMMUNE_NOT_FOUND));
    }

    @Override
    public List<CommuneDto> findAllByVille(VilleDto villeDto) {
        log.info("We are going to get back the Commune By {}", villeDto);
        if (!StringUtils.hasLength(villeDto.getNomVille())) {
            log.error("you are not provided a Ville.");
            return null;
        }
        return communeRepository.findByVille(VilleDto.toEntity(villeDto)).stream().map(CommuneDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommuneDto> findAllByIdVille(Long id) {

        log.info("We are going to get back the Ville By {}", id);
        if (id == null) {
            log.error("you are not provided a Ville.");
            return null;
        }
        Optional<Ville> v = villeRepository.findById(id);
        if (!v.isPresent()) {
            log.error("Commune not found for the Ville.");
            return null;
        }
        return communeRepository.findByVille(VilleDto.toEntity(VilleDto.fromEntity(v.get()))).stream()
                .map(CommuneDto::fromEntity)
                .collect(Collectors.toList());
    }

}
