package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.Models.Immeuble;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.ImmeubleService;
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

    final SiteRepository siteRepository;
    final ImmeubleRepository immeubleRepository;
    final UtilisateurRepository utilisateurRepository;

    @Override
    public ImmeubleDto save(ImmeubleDto dto) {

        int numeroDubien = immeubleRepository.getMaxNumImmeuble();

        Optional<Immeuble> oldimmeuble = immeubleRepository.findById(dto.getId());
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
        Site site = siteRepository.findById(dto.getIdSite()).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun Site has been found with Code " + dto.getIdSite(),
                        ErrorCodes.SITE_NOT_FOUND));

        /**
         * Recherche du proprietaire
         */
        Utilisateur utilisateur = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if (utilisateur.getUrole().getRoleName().equals("PROPRIETAIRE")) {
            if (oldimmeuble.isPresent()) {
                oldimmeuble.get().setSite(site);
                oldimmeuble.get().setUtilisateur(utilisateur);
                // oldimmeuble.get()
                // .setAbrvBienimmobilier(site.getAbrSite() + "-IMME-" + (numeroDubien + 1));
                oldimmeuble.get().setGarrage(dto.isGarrage());
                // oldimmeuble.get().setArchived(dto.i);
                oldimmeuble.get().setDescription(dto.getDescriptionImmeuble());
                // immeuble.setAbrvNomImmeuble(site.getAbrSite() + "-IMME-" + (numeroDubien +
                // 1));
                oldimmeuble.get().setNbrEtage(dto.getNbrEtage());
                oldimmeuble.get().setNbrePieceImmeuble(dto.getNbrePieceImmeuble());
                oldimmeuble.get().setNomBien(dto.getNomBien());
                // oldimmeuble.get().setNumBien(n);// a changer
                oldimmeuble.get().setNumeroImmeuble(dto.getNumeroImmeuble());
                oldimmeuble.get().setOccupied(dto.isOccupied());
                oldimmeuble.get().setStatutBien(dto.getStatutBien());
                oldimmeuble.get().setSuperficieBien(dto.getSuperficieBien());

                Immeuble immeubleSave = immeubleRepository.save(oldimmeuble.get());
                return ImmeubleDto.fromEntity(immeubleSave);
            }
            Immeuble immeuble = new Immeuble();
            immeuble.setSite(site);
            immeuble.setUtilisateur(utilisateur);
            immeuble.setAbrvBienimmobilier(site.getAbrSite() + "-IMME-" + dto.getAbrvNomImmeuble().toUpperCase());
            immeuble.setAbrvNomImmeuble(site.getAbrSite() + "-IMME-" + dto.getAbrvNomImmeuble().toUpperCase());
            immeuble.setGarrage(dto.isGarrage());
            immeuble.setNumBien(Long.valueOf(numeroDubien));
            immeuble.setIdAgence(dto.getIdAgence());
            // immeuble.setArchived(immeuble.isArchived());
            immeuble.setDescription(dto.getDescriptionImmeuble());
            immeuble.setNbrEtage(dto.getNbrEtage());
            immeuble.setNbrePieceImmeuble(dto.getNbrePieceImmeuble());
            immeuble.setNomBien(dto.getNomBien());
            // immeuble.setNumBien(n);// a changer
            immeuble.setNumeroImmeuble(numeroDubien + 1);
            immeuble.setOccupied(false);
            immeuble.setStatutBien(dto.getStatutBien());
            immeuble.setSuperficieBien(dto.getSuperficieBien());
            immeuble.setDescriptionImmeuble(dto.getDescriptionImmeuble());
            Immeuble immeubleSave = immeubleRepository.save(immeuble);
            return ImmeubleDto.fromEntity(immeubleSave);
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateur.getUrole().getRoleName(),
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
        Site site = siteRepository.findById(id).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun Site has been found with Code " + id,
                        ErrorCodes.SITE_NOT_FOUND));
        if (site == null) {
            log.error("Immeuble not found for the Site.");
            return null;
        }
        return immeubleRepository.findBySite(site).stream()
                .map(ImmeubleDto::fromEntity)
                .collect(Collectors.toList());
    }

}
