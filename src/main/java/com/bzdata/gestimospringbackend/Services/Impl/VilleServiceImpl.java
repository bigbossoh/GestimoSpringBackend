package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.PaysDto;
import com.bzdata.gestimospringbackend.DTOs.VilleDto;
import com.bzdata.gestimospringbackend.Models.Pays;
import com.bzdata.gestimospringbackend.Models.Ville;
import com.bzdata.gestimospringbackend.Services.VilleService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.PaysRepository;
import com.bzdata.gestimospringbackend.repository.VilleRepository;
import com.bzdata.gestimospringbackend.validator.VilleDtoValidator;

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
public class VilleServiceImpl implements VilleService {

    final VilleRepository villeRepository;
    final PaysRepository paysRepository;

    @Override
    public VilleDto save(VilleDto dto) {
        log.info("We are going to create  a new Ville {}", dto);
        List<String> errors = VilleDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("la Ville n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Ville sont null.",
                    ErrorCodes.VILLE_NOT_VALID, errors);
        }
        Ville ville = villeRepository.save(VilleDto.toEntity(dto));
        return VilleDto.fromEntity(ville);
    }

    @Override
    public boolean delete(Long id) {

        log.info("We are going to delete a Ville with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Ville");
            return false;
        }
        boolean exist = villeRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Ville avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.VILLE_NOT_FOUND);
        }
        villeRepository.deleteById(id);
        return true;
    }

    @Override
    public List<VilleDto> findAll() {
        return villeRepository.findAll(Sort.by(Direction.ASC, "nomVille")).stream()
                .map(VilleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VilleDto findById(Long id) {
        log.info("We are going to get back the Ville By {}", id);
        if (id == null) {
            log.error("you are not provided a Ville.");
            return null;
        }
        return villeRepository.findById(id).map(VilleDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucune Ville has been found with Code " + id,
                        ErrorCodes.VILLE_NOT_FOUND));
    }

    @Override
    public VilleDto findByName(String nom) {
        log.info("We are going to get back the Ville By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Ville.");
            return null;
        }
        return villeRepository.findByNomVille(nom).map(VilleDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Ville has been found with name " + nom,
                        ErrorCodes.VILLE_NOT_FOUND));
    }

    @Override
    public List<VilleDto> findAllByPays(PaysDto paysDto) {
        log.info("We are going to get back the Ville By {}", paysDto);
        if (!StringUtils.hasLength(paysDto.getNomPays())) {
            log.error("you are not provided a Ville.");
            return null;
        }
        return villeRepository.findByPays(PaysDto.toEntity(paysDto)).stream().map(VilleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<VilleDto> findAllByIdPays(Long id) {

        log.info("We are going to get back the Ville By {}", id);
        if (id == null) {
            log.error("you are not provided a Ville.");
            return null;
        }
        Optional<Pays> p = paysRepository.findById(id);
        if (!p.isPresent()) {
            log.error("Pays not found for the Ville.");
            return null;
        }
        return villeRepository.findByPays(PaysDto.toEntity(PaysDto.fromEntity(p.get()))).stream()
                .map(VilleDto::fromEntity)
                .collect(Collectors.toList());
    }

}
