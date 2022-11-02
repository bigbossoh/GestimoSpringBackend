package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.SiteResponseDto;
import com.bzdata.gestimospringbackend.Models.Quartier;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Services.SiteService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.QuartierRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.validator.SiteDtoValidator;

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
public class SiteServiceImpl implements SiteService {
    final SiteRepository siteRepository;
    final QuartierRepository quartierRepository;

    @Override
    public boolean save(SiteRequestDto dto) {
        Optional<Site> oldSite = siteRepository.findById(dto.getId());

        log.info("We are going to create  a new site {}", dto);
        List<String> errors = SiteDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Le site n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                    ErrorCodes.SITE_NOT_VALID, errors);
        }
        try {
            if (oldSite.isPresent()) {

                Quartier quartier = quartierRepository.findById(dto.getIdQuartier()).orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Quartier has been found with Code " + dto.getIdQuartier(),
                                ErrorCodes.SITE_NOT_FOUND));
                // oldSite.get().setAbrSite(quartier.getAbrvQuartier() + "-" +
                // dto.getAbrSite());
                // oldSite.get().setNomSite(dto.getNomSite());
                oldSite.get().setQuartier(quartier);
                oldSite.get().setIdAgence(dto.getIdAgence());
                oldSite.get().setAbrSite(org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                        .getCommune().getVille().getPays().getAbrvPays())
                        + "-" +
                        org.apache.commons.lang3.StringUtils
                                .deleteWhitespace(quartier.getCommune().getVille().getAbrvVille())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                                .getCommune().getAbrvCommune())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier.getAbrvQuartier()));
                oldSite.get().setNomSite(
                        quartier.getCommune().getVille().getPays().getNomPays()
                                + "-" + quartier.getCommune().getVille().getNomVille()
                                + "-" + quartier.getCommune().getNomCommune()
                                + "-" + quartier.getNomQuartier());
                siteRepository.save(oldSite.get());
                return true;
            } else {
                Site site = new Site();
                Quartier quartier = quartierRepository.findById(dto.getIdQuartier()).orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Quartier has been found with Code " + dto.getIdQuartier(),
                                ErrorCodes.SITE_NOT_FOUND));
                // site.setAbrSite(quartier.getAbrvQuartier() + "-" + dto.getAbrSite());

                site.setIdAgence(dto.getIdAgence());
                site.setQuartier(quartier);
                site.setIdAgence(1L);
                site.setAbrSite(org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                        .getCommune().getVille().getPays().getAbrvPays())
                        + "-" +
                        org.apache.commons.lang3.StringUtils
                                .deleteWhitespace(quartier.getCommune().getVille().getAbrvVille())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                                .getCommune().getAbrvCommune())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier.getAbrvQuartier()));
                site.setNomSite(
                        quartier.getCommune().getVille().getPays().getNomPays()
                                + "-" + quartier.getCommune().getVille().getNomVille()
                                + "-" + quartier.getCommune().getNomCommune()
                                + "-" + quartier.getNomQuartier());

                siteRepository.save(site);
                return true;
            }
        } catch (Exception e) {
            throw new InvalidEntityException(" Erreur : " + e.getMessage());
        }

    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Site with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Site");
            return false;
        }
        boolean exist = siteRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Site avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.SITE_NOT_FOUND);
        }
        siteRepository.deleteById(id);
        return true;
    }

    @Override
    public List<SiteResponseDto> findAll(Long idAgence) {
        return siteRepository.findAll().stream()
                .map(SiteResponseDto::fromEntity)
                .filter(agence->agence.getIdAgence()==idAgence)
                .collect(Collectors.toList());
    }

    @Override
    public SiteResponseDto findById(Long id) {
        log.info("We are going to get back the Site By ID : {}", id);
        if (id == null) {
            log.error("you are not provided a Site.");
            return null;
        }
        return siteRepository.findById(id).map(SiteResponseDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Pays has been found with Code " + id,
                        ErrorCodes.SITE_NOT_FOUND));
    }

    @Override
    public SiteResponseDto findByName(String nom) {
        log.info("We are going to get back the Site By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Site.");
            return null;
        }
        return siteRepository.findByNomSite(nom).map(SiteResponseDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Site has been found with name " + nom,
                        ErrorCodes.SITE_NOT_FOUND));
    }

    @Override
    public SiteResponseDto saveSite(SiteRequestDto dto) {
        Optional<Site> oldSite = siteRepository.findById(dto.getId());

        log.info("We are going to create  a new site {}", dto);
        List<String> errors = SiteDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Le site n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object site sont null.",
                    ErrorCodes.SITE_NOT_VALID, errors);
        }
        try {
            if (oldSite.isPresent()) {

                Quartier quartier = quartierRepository.findById(dto.getIdQuartier()).orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Quartier has been found with Code " + dto.getIdQuartier(),
                                ErrorCodes.SITE_NOT_FOUND));
                // oldSite.get().setAbrSite(quartier.getAbrvQuartier() + "-" +
                // dto.getAbrSite());
                // oldSite.get().setNomSite(dto.getNomSite());
                oldSite.get().setQuartier(quartier);
                oldSite.get().setIdAgence(dto.getIdAgence());
                oldSite.get().setAbrSite(org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                        .getCommune().getVille().getPays().getAbrvPays())
                        + "-" +
                        org.apache.commons.lang3.StringUtils
                                .deleteWhitespace(quartier.getCommune().getVille().getAbrvVille())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                                .getCommune().getAbrvCommune())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier.getAbrvQuartier()));
                oldSite.get().setNomSite(
                        quartier.getCommune().getVille().getPays().getNomPays()
                                + "-" + quartier.getCommune().getVille().getNomVille()
                                + "-" + quartier.getCommune().getNomCommune()
                                + "-" + quartier.getNomQuartier());
                Site siteSave = siteRepository.save(oldSite.get());
                return SiteResponseDto.fromEntity(siteSave);
            } else {
                Site site = new Site();
                Quartier quartier = quartierRepository.findById(dto.getIdQuartier()).orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Quartier has been found with Code " + dto.getIdQuartier(),
                                ErrorCodes.SITE_NOT_FOUND));
                // site.setAbrSite(quartier.getAbrvQuartier() + "-" + dto.getAbrSite());

                site.setIdAgence(dto.getIdAgence());
                site.setQuartier(quartier);
                site.setIdAgence(1L);
                site.setAbrSite(org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                        .getCommune().getVille().getPays().getAbrvPays())
                        + "-" +
                        org.apache.commons.lang3.StringUtils
                                .deleteWhitespace(quartier.getCommune().getVille().getAbrvVille())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier
                                .getCommune().getAbrvCommune())
                        + "-" + org.apache.commons.lang3.StringUtils.deleteWhitespace(quartier.getAbrvQuartier()));
                site.setNomSite(
                        quartier.getCommune().getVille().getPays().getNomPays()
                                + "-" + quartier.getCommune().getVille().getNomVille()
                                + "-" + quartier.getCommune().getNomCommune()
                                + "-" + quartier.getNomQuartier());
                Site siteSave = siteRepository.save(site);

                return SiteResponseDto.fromEntity(siteSave);
            }
        } catch (Exception e) {
            throw new InvalidEntityException(" Erreur : " + e.getMessage());
        }
    }
}
