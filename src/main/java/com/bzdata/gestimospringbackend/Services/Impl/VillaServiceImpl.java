package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.Models.Role;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Models.Villa;
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
    public VillaDto saveUneVilla(VillaDto dto){
        Villa villa = new Villa();
        log.info("We are going to create  a new Villa {}", dto);

        List<String> errors = VillaDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("la Villa n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Villa sont null.",
                    ErrorCodes.VILLA_NOT_VALID, errors);
        }
        Site recoverySite = siteRepository.findById(dto.getIdSite())
                .orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Site has been found with Code " + dto.getIdSite(),
                                ErrorCodes.SITE_NOT_FOUND));
        Utilisateur utilisateurRequestDto = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));

        Role leRole = roleRepository.findById(utilisateurRequestDto.getUrole().getId()).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun role has been found with Code " + utilisateurRequestDto.getRoleUsed(),
                        ErrorCodes.SITE_NOT_FOUND));
        if (leRole.getRoleName().equals("PROPRIETAIRE")) {
            villa.setIdAgence(dto.getIdAgence());
            villa.setSite(recoverySite);
            villa.setDescription(dto.getDescription());
            villa.setArchived(dto.isArchived());
            villa.setOccupied(dto.isOccupied());
            villa.setStatutBien(dto.getStatutBien());
            villa.setSuperficieBien(dto.getSuperficieBien());
            villa.setGarageVilla(dto.isGarageVilla());
            villa.setNbrChambreVilla(dto.getNbrChambreVilla());
            villa.setNbrSalonVilla(dto.getNbrSalleEauVilla());
            villa.setNomVilla(dto.getNomVilla());
            villa.setUtilisateur(utilisateurRequestDto);
            Long numBien = 0L;
            if (villaRepository.count() == 0) {
                numBien = 1L;
            } else {
                numBien = Long.valueOf(villaRepository.getMaxNumVilla() + 1);
            }

            villa.setNumBien(numBien);
            if (!StringUtils.hasLength(dto.getAbrvVilla())) {
                villa.setAbrvVilla("villa-".toUpperCase() + numBien);

                villa.setNomBien((recoverySite.getNomSite() + "-villa-" + numBien));
            } else {
                villa.setAbrvVilla("villa-" + dto.getNomVilla() + "-" + numBien);
                villa.setNomBien((recoverySite.getNomSite() + "-villa-" + dto.getNomVilla() + "-" + numBien)
                        .toUpperCase(Locale.ROOT));
            }
            villa.setAbrvBienimmobilier(
                    (recoverySite.getAbrSite() + "-" + dto.getAbrvVilla()).toUpperCase()+ "-" + numBien);
         Villa villaSave=   villaRepository.save(villa);
            return gestimoWebMapperImpl.fromVilla(villaSave);
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateurRequestDto.getRoleUsed(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }
    }
    @Override
    public boolean save(VillaDto dto) {
        Villa villa = new Villa();
        log.info("We are going to create  a new Villa {}", dto);

        List<String> errors = VillaDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("la Villa n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Villa sont null.",
                    ErrorCodes.VILLA_NOT_VALID, errors);
        }
        Site recoverySite = siteRepository.findById(dto.getIdSite())
                .orElseThrow(
                        () -> new InvalidEntityException(
                                "Aucun Site has been found with Code " + dto.getIdSite(),
                                ErrorCodes.SITE_NOT_FOUND));
        Utilisateur utilisateurRequestDto = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));

        Role leRole = roleRepository.findById(utilisateurRequestDto.getUrole().getId()).orElseThrow(
                () -> new InvalidEntityException(
                        "Aucun role has been found with Code " + utilisateurRequestDto.getRoleUsed(),
                        ErrorCodes.SITE_NOT_FOUND));
        if (leRole.getRoleName().equals("PROPRIETAIRE")) {
            villa.setIdAgence(dto.getIdAgence());
            villa.setSite(recoverySite);
            villa.setDescription(dto.getDescription());
            villa.setArchived(dto.isArchived());
            villa.setOccupied(dto.isOccupied());
            villa.setStatutBien(dto.getStatutBien());
            villa.setSuperficieBien(dto.getSuperficieBien());
            villa.setGarageVilla(dto.isGarageVilla());
            villa.setNbrChambreVilla(dto.getNbrChambreVilla());
            villa.setNbrSalonVilla(dto.getNbrSalleEauVilla());
            villa.setNomVilla(dto.getNomVilla());
            villa.setUtilisateur(utilisateurRequestDto);
            Long numBien = 0L;
            if (villaRepository.count() == 0) {
                numBien = 1L;
            } else {
                numBien = Long.valueOf(villaRepository.getMaxNumVilla() + 1);
            }

            villa.setNumBien(numBien);
            if (!StringUtils.hasLength(dto.getAbrvVilla())) {
                villa.setAbrvVilla("villa-".toUpperCase() + numBien);

                villa.setNomBien((recoverySite.getNomSite() + "-villa-" + numBien));
            } else {
                villa.setAbrvVilla("villa-" + dto.getNomVilla() + "-" + numBien);
                villa.setNomBien((recoverySite.getNomSite() + "-villa-" + dto.getNomVilla() + "-" + numBien)
                        .toUpperCase(Locale.ROOT));
            }
            villa.setAbrvBienimmobilier(
                    (recoverySite.getAbrSite() + "-" + dto.getAbrvVilla()).toUpperCase()+ "-" + numBien);
            villaRepository.save(villa);
            return true;
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateurRequestDto.getRoleUsed(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }

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
                .collect(Collectors.summarizingLong(Villa::getNumBien));
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
        // log.info("We are going to get back the Villa By {}", nom);
        // if (!StringUtils.hasLength(nom)) {
        // log.error("you are not provided a Studio.");
        // return null;
        // }
        // return
        // villaRepository.findByNomVilla(nom).map(VillaDto::fromEntity).orElseThrow(
        // () -> new InvalidEntityException("Aucun Villa has been found with name " +
        // nom,
        // ErrorCodes.VILLA_NOT_FOUND));
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
                .filter((vil)->vil.isOccupied()==false)
                .collect(Collectors.toList());
    }

}
