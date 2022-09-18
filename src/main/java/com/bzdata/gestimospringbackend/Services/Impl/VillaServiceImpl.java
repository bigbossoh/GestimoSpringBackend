package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.Models.*;
import com.bzdata.gestimospringbackend.Services.VillaService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.RoleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.repository.VillaRepository;
import com.bzdata.gestimospringbackend.validator.VillaDtoValidator;

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
public class VillaServiceImpl implements VillaService {
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    final VillaRepository villaRepository;
    final SiteRepository siteRepository;
    final UtilisateurRepository utilisateurRepository;
    final RoleRepository roleRepository;

    @Override
    public VillaDto saveUneVilla(VillaDto dto) {
        log.info("We are going to create  a new Villa {}", dto);
        List<String> errors = VillaDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("la Villa n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Villa sont null.",
                    ErrorCodes.VILLA_NOT_VALID, errors);
        }
        Villa villa = new Villa();
        Site recoverySite = getSite(dto);
        Utilisateur utilisateurRequestDto = getUtilisateur(dto.getIdUtilisateur());
        Role leRole = getRole(utilisateurRequestDto.getUrole().getId());
        if (leRole.getRoleName().equals("PROPRIETAIRE")) {
            villa.setIdAgence(dto.getIdAgence());
            villa.setIdCreateur(dto.getIdCreateur());
            villa.setSite(recoverySite);
            villa.setDescription(dto.getDescription());
            villa.setOccupied(dto.isOccupied());
            villa.setSuperficieBien(dto.getSuperficieBien());
            villa.setNbrChambreVilla(dto.getNbrChambreVilla());
            villa.setNbrSalonVilla(dto.getNbrSalleEauVilla());
            villa.setNomBaptiserBienImmobilier(dto.getNomBaptiserBienImmobilier());
            villa.setUtilisateurProprietaire(utilisateurRequestDto);
            Long numBien = 0L;
            if (villaRepository.count() == 0) {
                numBien = 1L;
            } else {
                numBien = nombreVillaByIdSite(recoverySite);
            }
            villa.setNumVilla(numBien);
            villa.setCodeAbrvBienImmobilier((recoverySite.getAbrSite() + "-VILLA-" + numBien).toUpperCase());
            villa.setNomCompletBienImmobilier((recoverySite.getNomSite() + "-VILLA-" + numBien).toUpperCase());
            Villa villaSave = villaRepository.save(villa);
            return gestimoWebMapperImpl.fromVilla(villaSave);
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateurRequestDto.getRoleUsed(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }
    }

    private Role getRole(Long idUrole) {
        Role leRole = roleRepository.findById(idUrole).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun role has been found with Code " + idUrole,
                        ErrorCodes.SITE_NOT_FOUND));
        return leRole;
    }

    private Utilisateur getUtilisateur(Long idUtilisateur) {
        Utilisateur utilisateurRequestDto = utilisateurRepository
                .findById(idUtilisateur)
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + idUtilisateur,
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        return utilisateurRequestDto;
    }

    private Site getSite(VillaDto dto) {
        return siteRepository.findById(dto.getIdSite())
                .orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Site has been found with Code " + dto.getIdSite(),
                                ErrorCodes.SITE_NOT_FOUND));
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Villa with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Villa");
            return false;
        }
        boolean exist = villaRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Villa avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.STUDIO_NOT_FOUND);
        }

        villaRepository.deleteById(id);
        return true;

    }

    @Override
    public Long maxOfNumBien() {

        LongSummaryStatistics collectMaxNumBien = villaRepository.findAll().stream()
                .collect(Collectors.summarizingLong(Villa::getNumVilla));
        log.info(" countNberOfRecordVilla {}", collectMaxNumBien.getMax());
        return collectMaxNumBien.getMax();
    }

    @Override
    public List<VillaDto> findAll() {
        return villaRepository.findAll().stream()
                .map(gestimoWebMapperImpl::fromVilla)
                .collect(Collectors.toList());
    }

    @Override
    public VillaDto findById(Long id) {
        log.info("We are going to get back the Villa By {}", id);
        if (id == null) {
            log.error("you are not provided a Villa.");
            return null;
        }
        return villaRepository.findById(id).map(gestimoWebMapperImpl::fromVilla).orElseThrow(
                () -> new InvalidEntityException("Aucun Studio has been found with Code " + id,
                        ErrorCodes.VILLA_NOT_FOUND));
    }

    @Override
    public VillaDto findByName(String nom) {

        return null;
    }

    @Override
    public List<VillaDto> findAllBySite(SiteRequestDto siteRequestDto) {
        return null;
    }

    @Override
    public List<VillaDto> findAllByIdSite(Long id) {

        return null;
    }

    @Override
    public List<VillaDto> findAllLibre() {
        return villaRepository.findAll().stream()
                .map(gestimoWebMapperImpl::fromVilla)
                .filter((vil) -> vil.isOccupied() == false)
                .collect(Collectors.toList());
    }

    private Long nombreVillaByIdSite(Site site) {
        Map<Site, Long> numbreVillabySite = villaRepository.findAll()
                .stream()
                .filter(e -> e.getSite().equals(site))
              //  .filter(proprio->proprio)
                .collect(Collectors.groupingBy(Villa::getSite, Collectors.counting()));

        for (Map.Entry m : numbreVillabySite.entrySet()) {
            if (m.getKey().equals(site)) {

                return (Long) m.getValue() + 1L;

            }

        }
        return 1L;
    }
}
