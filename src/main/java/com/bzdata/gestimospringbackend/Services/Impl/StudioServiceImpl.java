package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.StudioDto;
import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Studio;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.StudioService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.StudioRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
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
    final UtilisateurRepository utilisateurRepository;
    final EtageRepository etageRepository;
    final SiteRepository siteRepository;

    @Override
    public boolean save(StudioDto dto) {
        Optional<Studio> oldStudio = studioRepository.findById(dto.getId());
        Studio studio = new Studio();
        int numStu = studioRepository.getMaxNumStudio() + 1;
        log.info("We are going to create  a new Studio {}", dto);
        List<String> errors = StudioDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le Studio n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Studio sont null.",
                    ErrorCodes.STUDIO_NOT_VALID, errors);
        }
        Optional<Site> recoverySite = siteRepository.findById(dto.getIdSite());
        if (recoverySite.isPresent()) {
            studio.setSite(recoverySite.get());
        }
        Optional<Etage> recoveryEtage = etageRepository.findById(dto.getIdEtage());
        if (recoveryEtage.isPresent()) {
            studio.setEtageStudio(recoveryEtage.get());
        }
        try {
            Utilisateur utilisateurRequestDto = utilisateurRepository
                    .findById(dto.getIdUtilisateur())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                            ErrorCodes.UTILISATEUR_NOT_FOUND));
            if (utilisateurRequestDto.getUrole().getRoleName().equals("PROPRIETAIRE")) {
                studio.setIdAgence(dto.getIdAgence());
                Long numBien = 0L;
                if (studioRepository.count() == 0) {
                    numBien = 1L;
                } else {
                    numBien = Long.valueOf(numStu);
                }
                studio.setNumBien(numBien);
                studio.setDescription(dto.getDescription().toUpperCase());
                studio.setStatutBien(dto.getStatutBien());
                studio.setSuperficieBien(dto.getSuperficieBien());
                studio.setUtilisateur(utilisateurRequestDto);

                if (!StringUtils.hasLength(dto.getAbrvNomStudio())) {
                    studio.setAbrvNomStudio("studio-".toUpperCase() + numBien);
                    // studio.setAbrvBienimmobilier("studio-".toUpperCase() + numBien);
                    if (recoverySite.isPresent()) {
                        studio.setNomBien(
                                (recoverySite.get().getAbrSite() + "-studio-" +
                                        numBien).toUpperCase());
                    }

                } else {
                    studio.setAbrvBienimmobilier("studio-".toUpperCase() + dto.getNomStudio() + "-" +
                            numBien);
                    // studio.setAbrvBienimmobilier("studio-".toUpperCase() + dto.getNomBien() + "-"
                    // +
                    // numBien);
                    if (recoverySite.isPresent()) {
                        studio.setNomBien(
                                (recoverySite.get().getAbrSite() + "-studio-".toUpperCase() + dto.getNomStudio() + "-"
                                        +
                                        numBien)
                                        .toUpperCase());
                    }

                }

                studio.setAbrvNomStudio(
                        (recoverySite.get().getAbrSite() + "-" +
                                dto.getAbrvNomStudio()).toUpperCase());
                studio.setId(0L);
                log.info("Before save {} ", studio);
                studioRepository.save(studio);
                return true;
            } else {
                throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                        + utilisateurRequestDto.getUrole().getRoleName(),
                        ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
            }
        } catch (Exception e) {
            throw new InvalidEntityException("L'erreur suivante est parvenue " + e.getMessage());
        }

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
