package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleAfficheDto;
import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.DTOs.ImmeubleEtageDto;
import com.bzdata.gestimospringbackend.Models.*;
import com.bzdata.gestimospringbackend.Services.ImmeubleService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.mappers.ImmeubleMapperImpl;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.repository.ImmeubleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.ImmeubleDtoValidator;

import com.bzdata.gestimospringbackend.validator.ImmeubleEtageDtoValidator;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmeubleServiceImpl implements ImmeubleService {

    final SiteRepository siteRepository;
    final ImmeubleRepository immeubleRepository;
    final UtilisateurRepository utilisateurRepository;
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    final ImmeubleMapperImpl immeubleMapper;
    final EtageRepository etageRepository;

    @Override
    public ImmeubleAfficheDto save(ImmeubleDto dto) {

        int numeroDubien = getNumeroDubien();

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
        Site site = getSite(dto.getIdSite());

        /**
         * Recherche du proprietaire
         */
        Utilisateur utilisateur = getUtilisateur(dto.getIdUtilisateur());
        if (utilisateur.getUrole().getRoleName().equals("PROPRIETAIRE")) {
            if (oldimmeuble.isPresent()) {
                oldimmeuble.get().setSite(site);
                oldimmeuble.get().setUtilisateur(utilisateur);
                oldimmeuble.get().setGarrage(dto.isGarrage());
                oldimmeuble.get().setDescription(dto.getDescriptionImmeuble());
                oldimmeuble.get().setNbrePieceImmeuble(dto.getNbrePieceImmeuble());
                oldimmeuble.get().setNomBien(dto.getNomBien());
                oldimmeuble.get().setNumeroImmeuble(dto.getNumeroImmeuble());
                oldimmeuble.get().setOccupied(dto.isOccupied());
                oldimmeuble.get().setStatutBien(dto.getStatutBien());
                oldimmeuble.get().setSuperficieBien(dto.getSuperficieBien());

                Immeuble immeubleSave = immeubleRepository.save(oldimmeuble.get());
                return gestimoWebMapperImpl.fromImmeuble(immeubleSave);
            }
            Immeuble immeuble = new Immeuble();
            immeuble.setSite(site);
            immeuble.setUtilisateur(utilisateur);
            immeuble.setAbrvBienimmobilier(site.getAbrSite() + "-IMME-" + dto.getAbrvNomImmeuble().toUpperCase());
            immeuble.setAbrvNomImmeuble(site.getAbrSite() + "-IMME-" + dto.getAbrvNomImmeuble().toUpperCase());
            immeuble.setGarrage(dto.isGarrage());
            immeuble.setNumBien(Long.valueOf(numeroDubien));
            immeuble.setIdAgence(dto.getIdAgence());
            immeuble.setDescription(dto.getDescriptionImmeuble());
            immeuble.setNbrEtage(dto.getNbrEtage());
            immeuble.setNbrePieceImmeuble(dto.getNbrePieceImmeuble());
            immeuble.setNomBien(dto.getNomBien());
            immeuble.setNumeroImmeuble(numeroDubien + 1);
            immeuble.setOccupied(false);
            immeuble.setStatutBien(dto.getStatutBien());
            immeuble.setSuperficieBien(dto.getSuperficieBien());
            immeuble.setDescriptionImmeuble(dto.getDescriptionImmeuble());
            Immeuble immeubleSave = immeubleRepository.save(immeuble);
            //return gestimoWebMapperImpl.fromImmeuble(immeubleSave);
            oldimmeuble.get().setNbrEtage(dto.getNbrEtage());
            return gestimoWebMapperImpl.fromImmeuble(immeubleSave);
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateur.getUrole().getRoleName(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }
    }

    private int getNumeroDubien() {
        int numeroDubien = immeubleRepository.getMaxNumImmeuble();
        return numeroDubien;
    }

    private Utilisateur getUtilisateur(Long IdUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository
                .findById(IdUtilisateur)
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + IdUtilisateur,
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        return utilisateur;
    }

    private Site getSite(Long idSite) {
        return siteRepository.findById(idSite).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun Site has been found with Code " + idSite,
                        ErrorCodes.SITE_NOT_FOUND));
    }

    @Override
    public ImmeubleEtageDto saveImmeubleEtageDto(ImmeubleEtageDto dto) {
        log.info(
                "We are going to create  a new Immeuble and the number of etage belong to the immeuble from layer service implemebtation {}",
                dto);
        List<String> errors = ImmeubleEtageDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'Immeuble n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Immeuble sont null.",
                    ErrorCodes.IMMEUBLE_NOT_VALID, errors);
        }
        Long numBien = 0L;

        Immeuble immeuble = new Immeuble();
        immeuble.setSite(getSite(dto.getIdSite()));
        immeuble.setUtilisateur(getUtilisateur(dto.getIdUtilisateur()));
        if (immeubleRepository.count() == 0) {
            numBien = 1L;
        } else {
            numBien=nombreVillaByIdSite(getSite(dto.getIdSite()));
           // log.info("le numero de la villa est le nuivant {}",numBien);
        }
        immeuble.setAbrvBienimmobilier(
                (immeuble.getSite().getAbrSite() + "-IMME-" + numBien).toUpperCase());
        immeuble.setAbrvNomImmeuble(
                ( immeuble.getSite().getNomSite() + "-IMMEUBLE-" + numBien).toUpperCase());
        immeuble.setGarrage(dto.isGarrage());
        immeuble.setNumBien(numBien);
        immeuble.setIdAgence(dto.getIdAgence());
        immeuble.setDescription(dto.getDescriptionImmeuble());
        immeuble.setNbrEtage(dto.getNbrEtage());
        immeuble.setNbrePieceImmeuble(dto.getNbrePieceImmeuble());
        immeuble.setNomBien(dto.getNomBien());
        immeuble.setNumeroImmeuble(getNumeroDubien() + 1);
        immeuble.setOccupied(false);
        immeuble.setStatutBien(dto.getStatutBien());
        immeuble.setSuperficieBien(dto.getSuperficieBien());
        immeuble.setDescriptionImmeuble(dto.getDescriptionImmeuble());
        Immeuble immeubleSave = immeubleRepository.save(immeuble);
        Etage etage;
        for (int i = 0; i <= immeubleSave.getNbrEtage(); i++) {
            etage = new Etage();

            etage.setIdAgence(immeubleSave.getIdAgence());
            etage.setIdCreateur(immeubleSave.getIdCreateur());
            if (i == 0) {
                etage.setNomEtage("rez-de-chaussée");

            } else {
                etage.setNomEtage("Etage N°" + i);
            }
            etage.setAbrvEtage("Etage" + i);
            etage.setNumEtage(i);
            etage.setImmeuble(immeubleSave);
            etageRepository.save(etage);
        }
        return immeubleMapper.fromImmeubleEtage(immeubleSave);
    }

    @Override
    public ImmeubleDto updateImmeuble(ImmeubleDto dto) {
        return null;
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
        Site site = getSite(id);
        if (site == null) {
            log.error("Immeuble not found for the Site.");
            return null;
        }
        return immeubleRepository.findBySite(site).stream()
                .map(ImmeubleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImmeubleAfficheDto> findAllPourAffichageImmeuble() {

        return immeubleRepository.findAll(Sort.by(Direction.ASC, "descriptionImmeuble")).stream()
                .map(gestimoWebMapperImpl::fromImmeuble)
                .collect(Collectors.toList());

    }

    private Long nombreVillaByIdSite(Site site){
        Map<Site, Long> numbreVillabySite = immeubleRepository.findAll()
                .stream()
                .filter(e->e.getSite().equals(site))
                .collect(Collectors.groupingBy(Bienimmobilier::getSite, Collectors.counting()));

        for (Map.Entry m : numbreVillabySite.entrySet()) {
            if(m.getKey().equals(site)){

                return (Long) m.getValue()+1L;

            }

        }
        return 1L;
    }

}
