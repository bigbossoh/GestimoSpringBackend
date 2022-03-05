package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.DTOs.SiteResponseDto;
import com.bzdata.gestimospringbackend.Models.Immeuble;
import com.bzdata.gestimospringbackend.Services.ImmeubleService;
import com.bzdata.gestimospringbackend.Services.SiteService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.ImmeubleRepository;
import com.bzdata.gestimospringbackend.validator.ImmeubleValidator;

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
public class ImmeubleServiceImpl implements ImmeubleService {
    final SiteService siteService;
    final ImmeubleRepository immeubleRepository;

    @Override
    public ImmeubleDto save(ImmeubleDto dto) {
        log.info("We are going to create  a new Immeuble {}", dto);
        List<String> errors = ImmeubleValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'Immeuble n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Immeuble sont null.",
                    ErrorCodes.IMMEUBLE_NOT_VALID, errors);
        }
        Immeuble immeuble = immeubleRepository.save(ImmeubleDto.toEntity(dto));
        return ImmeubleDto.fromEntity(immeuble);
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Immeuble with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Immeuble");
            return false;
        }
        boolean exist = immeubleRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Immeuble avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.IMMEUBLE_NOT_FOUND);
        }
        Optional<Immeuble> imm = immeubleRepository.findById(id);
        if (imm.isPresent()) {
            if (imm.get().getEtages().size() != 0) {
                throw new EntityNotFoundException("Aucune Immeuble avec l'ID = " + id + " "
                        + "n' est pas vide ", ErrorCodes.IMMEUBLE_ALREADY_IN_USE);
            }
        }
        immeubleRepository.deleteById(id);
        return true;
    }

    @Override
    public List<ImmeubleDto> findAll() {
        return immeubleRepository.findAll(Sort.by(Direction.ASC, "descriptionImmeuble")).stream()
                .map(ImmeubleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ImmeubleDto findById(Long id) {
        log.info("We are going to get back the Immeuble By {}", id);
        if (id == null) {
            log.error("you are not provided a Immeuble.");
            return null;
        }
        return immeubleRepository.findById(id).map(ImmeubleDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucune Immeuble has been found with Code " + id,
                        ErrorCodes.IMMEUBLE_NOT_FOUND));
    }

    @Override
    public ImmeubleDto findByName(String nom) {
        log.info("We are going to get back the Immeuble By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Immeuble.");
            return null;
        }
        return immeubleRepository.findByDescriptionImmeuble(nom).map(ImmeubleDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucune Immeuble has been found with name " + nom,
                        ErrorCodes.IMMEUBLE_NOT_FOUND));
    }

    @Override
    public List<ImmeubleDto> findAllByIdSite(Long id) {

        log.info("We are going to get back the Immeuble By {}", id);
        if (id == null) {
            log.error("you are not provided a Immeuble.");
            return null;
        }
        SiteResponseDto site = siteService.findById(id);
        if (site == null) {
            log.error("Immeuble not found for the Site.");
            return null;
        }
        return immeubleRepository.findBySite(SiteResponseDto.toEntity(site)).stream()
                .map(ImmeubleDto::fromEntity)
                .collect(Collectors.toList());
    }

}
