package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.SiteResponseDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.Immeuble;
import com.bzdata.gestimospringbackend.Services.ImmeubleService;
import com.bzdata.gestimospringbackend.Services.SiteService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.ImmeubleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.ImmeubleDtoValidator;

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
    final SiteRepository siteRepository;
    final ImmeubleRepository immeubleRepository;
    final UtilisateurRepository utilisateurRepository;

    @Override
    public ImmeubleDto save(ImmeubleDto dto) {
        log.info("We are going to create  a new Immeuble from layer service implemebtation {}", dto);
        List<String> errors = ImmeubleDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'Immeuble n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Immeuble sont null.",
                    ErrorCodes.IMMEUBLE_NOT_VALID, errors);
        }
        /**
         * Recherche du Site
         */
        SiteRequestDto recoverySite = siteRepository.findById(dto.getSiteRequestDto().getId()).map(SiteRequestDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun Site has been found with Code " + dto.getSiteRequestDto().getId(),
                        ErrorCodes.SITE_NOT_FOUND));

        /**
         *  Recherche du proprietaire
         */
        UtilisateurRequestDto utilisateurRequestDto = utilisateurRepository
                .findById(dto.getUtilisateurRequestDto().getId()).map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getUtilisateurRequestDto().getId(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if (utilisateurRequestDto.getRoleRequestDto().getRoleName().equals("PROPRIETAIRE")) {

            dto.setSiteRequestDto(recoverySite);
            dto.setUtilisateurRequestDto(utilisateurRequestDto);
            //TODO
            /**
             * set abreviation et nom de l'immeuble
             */
            Immeuble immeuble = immeubleRepository.save(ImmeubleDto.toEntity(dto));
            return ImmeubleDto.fromEntity(immeuble);
        }
        else{
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateurRequestDto.getRoleRequestDto().getRoleName(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }

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
