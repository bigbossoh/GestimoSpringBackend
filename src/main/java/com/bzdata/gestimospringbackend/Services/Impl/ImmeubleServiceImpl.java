package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.ImmeubleDto;
import com.bzdata.gestimospringbackend.DTOs.ImmeubleEtageDto;
import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Immeuble;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.ImmeubleService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.ImmeubleMapperImpl;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.repository.ImmeubleRepository;
import com.bzdata.gestimospringbackend.repository.SiteRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
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

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImmeubleServiceImpl implements ImmeubleService {

    final SiteRepository siteRepository;
    final ImmeubleRepository immeubleRepository;
    final UtilisateurRepository utilisateurRepository;
    final ImmeubleMapperImpl immeubleMapper;
    final EtageRepository etageRepository;


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
        immeuble.setUtilisateurProprietaire(getUtilisateur(3L));
        int size = new ArrayList<>(immeubleRepository.findAll()).size();
        if (size == 0) {
            numBien = 1L;
        } else {
            numBien=nombreVillaByIdSite(getSite(dto.getIdSite()));
        }
        immeuble.setCodeNomAbrvImmeuble(
                (immeuble.getSite().getAbrSite() + "-IMME-" + numBien).toUpperCase());
        immeuble.setNomCompletImmeuble(
                ( immeuble.getSite().getNomSite() + "-IMMEUBLE-" + numBien).toUpperCase());
        immeuble.setGarrage(dto.isGarrage());
        immeuble.setNumImmeuble(Math.toIntExact(numBien));
        immeuble.setIdAgence(dto.getIdAgence());
        immeuble.setIdCreateur(dto.getIdCreateur());
        immeuble.setDescriptionImmeuble(dto.getDescriptionImmeuble());
        immeuble.setNbrEtage(dto.getNbrEtage());
        immeuble.setNbrePiecesDansImmeuble(dto.getNbrePiecesDansImmeuble());
        immeuble.setNomBaptiserImmeuble(dto.getNomBaptiserImmeuble());
        immeuble.setDescriptionImmeuble(dto.getDescriptionImmeuble());
        Immeuble immeubleSave = immeubleRepository.save(immeuble);
        Etage etage;
        for (int i = 0; i <= immeubleSave.getNbrEtage(); i++) {
            etage = new Etage();
            etage.setIdAgence(immeubleSave.getIdAgence());
            etage.setIdCreateur(immeubleSave.getIdCreateur());
            if (i == 0) {
                etage.setNomCompletEtage((immeubleSave.getNomCompletImmeuble()+"-"+"rez-de-chaussée").toUpperCase());
            } else {
                etage.setNomCompletEtage((immeubleSave.getNomCompletImmeuble()+"-"+"Etage N°" + i).toUpperCase());
            }
            etage.setCodeAbrvEtage((immeubleSave.getCodeNomAbrvImmeuble()+"-Etage-" + i).toUpperCase());
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
    public List<ImmeubleDto> findAll(Long idAgence) {
        return immeubleRepository.findAll(Sort.by(Direction.ASC, "descriptionImmeuble")).stream()
                .map(ImmeubleDto::fromEntity)
                .filter(agence->agence.getIdAgence()==idAgence)
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
    public List<ImmeubleEtageDto> findAllPourAffichageImmeuble(Long idAgence) {

        return immeubleRepository.findAll(Sort.by(Direction.ASC, "descriptionImmeuble")).stream()
                .map(immeubleMapper::fromImmeubleEtage)
                .filter(agence->agence.getIdAgence()==idAgence)
                .collect(Collectors.toList());

    }

    private Long nombreVillaByIdSite(Site site){
        Map<Site, Long> numbreVillabySite = immeubleRepository.findAll()
                .stream()
                .filter(e->e.getSite().equals(site))
                .collect(Collectors.groupingBy(Immeuble::getSite, Collectors.counting()));

        for (Map.Entry m : numbreVillabySite.entrySet()) {
            if(m.getKey().equals(site)){

                return (Long) m.getValue()+1L;

            }

        }
        return 1L;
    }

}
