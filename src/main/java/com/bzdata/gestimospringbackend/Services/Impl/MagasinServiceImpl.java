package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.MagasinDto;
import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.MagasinService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.repository.MagasinRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.MagasinDtoValidator;

import org.springframework.data.domain.Sort;
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
public class MagasinServiceImpl implements MagasinService {

    final MagasinRepository magasinRepository;
    final SiteRepository siteRepository;
    final UtilisateurRepository utilisateurRepository;
    final EtageRepository etageRepository;

    @Override
    public boolean save(MagasinDto dto) {
        Magasin magasin = new Magasin();
        log.info("We are going to create  a new Magasin {}", dto);
        List<String> errors = MagasinDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Le magasin n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Magasin sont null.",
                    ErrorCodes.MAGASIN_NOT_VALID, errors);
        }
        // magasin.setSite(dto.getIdSite());
        Optional<Site> recoverySite = siteRepository.findById(dto.getIdSite());
        if (recoverySite.isPresent()) {
            magasin.setSite(recoverySite.get());
        }
        // magasin.setEtageMagasin(null);
        Optional<Etage> etage = etageRepository.findById(dto.getIdEtage());
        if (etage.isPresent()) {
            magasin.setEtageMagasin(etage.get());
        }

        Utilisateur utilisateurRequestDto = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        // System.out.println(utilisateurRequestDto.getUrole().getRoleName());
        if (utilisateurRequestDto.getUrole().getRoleName().equals("PROPRIETAIRE")) {
            magasin.setIdAgence(dto.getIdAgence());
            Long numBien = 0L;
            if (magasinRepository.count() == 0) {
                numBien = 1L;
            } else {
                numBien = Long.valueOf(magasinRepository.getMaxNumMagasin() + 1);
            }
            magasin.setNumBien(numBien);
            magasin.setDescription(dto.getDescription().toUpperCase());
            magasin.setStatutBien(dto.getStatutBien());
            magasin.setSuperficieBien(dto.getSuperficieBien());
            magasin.setUtilisateur(utilisateurRequestDto);

            if (!StringUtils.hasLength(dto.getAbrvNomMagasin())) {
                magasin.setAbrvNomMagasin("magasin-".toUpperCase() + numBien);
                magasin.setAbrvBienimmobilier("magasin-".toUpperCase() + numBien);
                magasin.setNomBien(
                        (recoverySite.get().getAbrSite() + "-magasin-" +
                                numBien).toUpperCase());
            } else {
                magasin.setAbrvBienimmobilier("magasin-".toUpperCase() + dto.getNomMagasin() + "-" +
                        numBien);
                magasin.setAbrvNomMagasin("magasin-".toUpperCase() + dto.getNomMagasin() + "-" +
                        numBien);
                magasin.setNomBien(
                        (recoverySite.get().getAbrSite() + "-magasin-".toUpperCase() + dto.getNomMagasin() + "-" +
                                numBien)
                                .toUpperCase());
            }

            magasin.setAbrvNomMagasin(
                    (recoverySite.get().getAbrSite() + "-" +
                            dto.getAbrvNomMagasin()).toUpperCase());

            magasinRepository.save(magasin);

            return true;
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateurRequestDto.getUrole().getRoleName(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Magasin with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Magasin");
            return false;
        }
        boolean exist = magasinRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Magasin avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.MAGASIN_NOT_FOUND);
        }

        magasinRepository.deleteById(id);
        return true;
    }

    @Override
    public Long maxOfNumBienMagasin() {
        LongSummaryStatistics collectMaxNumBien = magasinRepository.findAll()
                .stream()
                .collect(Collectors.summarizingLong(Magasin::getNumBien));
        return collectMaxNumBien.getMax();
    }

    @Override
    public List<MagasinDto> findAll() {
        return magasinRepository.findAll(Sort.by(Sort.Direction.ASC, "nomBien")).stream()
                .map(MagasinDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MagasinDto findById(Long id) {
        log.info("We are going to get back the MagasinDto By {}", id);
        if (id == null) {
            log.error("you are not provided a Villa.");
            return null;
        }
        return magasinRepository.findById(id).map(MagasinDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Studio has been found with Code " + id,
                        ErrorCodes.MAGASIN_NOT_FOUND));
    }

    @Override
    public MagasinDto findByName(String nom) {

        Magasin magasinByUsername = magasinRepository.findMagasinByNomBien(nom);
        if (magasinByUsername != null) {
            return MagasinDto.fromEntity(magasinByUsername);
        } else {
            return null;
        }
    }

    @Override
    public List<MagasinDto> findAllBySite(SiteRequestDto siteRequestDto) {

        return magasinRepository.findAll().stream()
                .filter(magasin -> magasin.getSite().equals(siteRequestDto))
                .map(MagasinDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MagasinDto> findAllByIdSite(Long id) {

        return magasinRepository.findAll().stream()
                .filter(magasin -> magasin.getSite().getId().equals(id))
                .map(MagasinDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MagasinDto> findAllByIdEtage(Long id) {

        return magasinRepository.findAll().stream()
                .filter(magasin -> magasin.getEtageMagasin().getId().equals(id))
                .map(MagasinDto::fromEntity)
                .collect(Collectors.toList());
    }
}
