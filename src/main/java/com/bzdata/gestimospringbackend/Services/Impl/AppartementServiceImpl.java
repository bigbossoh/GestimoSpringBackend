package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;
import com.bzdata.gestimospringbackend.Models.Appartement;
import com.bzdata.gestimospringbackend.Models.Etage;
import com.bzdata.gestimospringbackend.Models.Site;
import com.bzdata.gestimospringbackend.Services.AppartementService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.AppartementRepository;
import com.bzdata.gestimospringbackend.repository.EtageRepository;
import com.bzdata.gestimospringbackend.validator.AppartementDtoValidator;

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
public class AppartementServiceImpl implements AppartementService {
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    final AppartementRepository appartementRepository;

    final EtageRepository etageRepository;

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Appartement with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Appartement");
            return false;
        }
        boolean exist = appartementRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucune Appartement avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.APPARTEMENT_NOT_FOUND);
        }

        appartementRepository.deleteById(id);
        return true;
    }

    @Override
    public List<AppartementDto> findAll(Long idAgence) {
        return appartementRepository.findAll().stream()
        .filter(agence->agence.getIdAgence()==idAgence)
                .map(gestimoWebMapperImpl::fromAppartement)
                .collect(Collectors.toList());
    }

    @Override
    public AppartementDto findById(Long id) {
        log.info("We are going to get back the Appartement By {}", id);
        if (id == null) {
            log.error("you are not provided a Appartement.");
            return null;
        }
        return appartementRepository.findById(id).map(gestimoWebMapperImpl::fromAppartement).orElseThrow(
                () -> new InvalidEntityException("Aucun Appartement has been found with Code " + id,
                        ErrorCodes.APPARTEMENT_NOT_FOUND));
    }

    @Override
    public AppartementDto findByName(String nom) {
        log.info("We are going to get back the Appartement By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Appartement.");
            return null;
        }
        return appartementRepository.findByNomCompletBienImmobilier(nom).map(gestimoWebMapperImpl::fromAppartement)
                .orElseThrow(
                        () -> new InvalidEntityException("Aucun Appartement has been found with name " + nom,
                                ErrorCodes.APPARTEMENT_NOT_FOUND));
    }

    @Override
    public List<AppartementDto> findAllByIdEtage(Long id) {
        log.info("We are going to get back the Appartement By {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        Etage etage = etageRepository.findById(id).orElseThrow(() -> new InvalidEntityException(
                "Aucun Appartement has been found with id " + id,
                ErrorCodes.APPARTEMENT_NOT_FOUND));
        return appartementRepository.findByEtageAppartement(etage).stream()
                .map(gestimoWebMapperImpl::fromAppartement)
                .collect(Collectors.toList());
    }

    @Override
    public AppartementDto  save(AppartementDto dto) {

        log.info("We are going to create  a new Appartement {}", dto);
        List<String> errors = AppartementDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("l'Appartement n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Appartement sont null.",
                    ErrorCodes.APPARTEMENT_NOT_VALID, errors);
        }
        Optional<Appartement> unAppartementTrouve = appartementRepository.findById(dto.getId());
        if (unAppartementTrouve.isPresent()) {

            unAppartementTrouve.get().setNbrPieceApp(dto.getNbrPieceApp());
            unAppartementTrouve.get().setNbreChambreApp(dto.getNbreChambreApp());
            unAppartementTrouve.get().setNbreSalleEauApp(dto.getNbreSalleEauApp());
            unAppartementTrouve.get().setNbreSalonApp(dto.getNbreSalonApp());
            unAppartementTrouve.get().setIdAgence(dto.getIdAgence());
            unAppartementTrouve.get().setIdCreateur(dto.getIdCreateur());

            unAppartementTrouve.get().setNomBaptiserBienImmobilier(dto.getNomBaptiserBienImmobilier());

            unAppartementTrouve.get().setBienMeublerResidence(dto.isBienMeublerResidence());
            unAppartementTrouve.get().setDescription(dto.getDescription());
            unAppartementTrouve.get().setSuperficieBien(dto.getSuperficieBien());
            log.info("dto.isBienMeublerResidence() {}",dto.isBienMeublerResidence());
            // unAppartementTrouve.setUtilisateurProprietaire(etage.getImmeuble().getUtilisateurProprietaire());
            Appartement appartementSave = appartementRepository.save(unAppartementTrouve.get());
            return gestimoWebMapperImpl.fromAppartement(appartementSave);
        } else {
            Etage etage = getEtage(dto);
            Long numApp = nombreVillaByIdSite(etage.getImmeuble().getSite());
            Appartement appartement = new Appartement();
            appartement.setEtageAppartement(etage);
            appartement.setNbrPieceApp(dto.getNbrPieceApp());
            appartement.setNbreChambreApp(dto.getNbreChambreApp());
            appartement.setNbreSalleEauApp(dto.getNbreSalleEauApp());
            appartement.setNbreSalonApp(dto.getNbreSalonApp());
            appartement.setIdAgence(dto.getIdAgence());
            appartement.setIdCreateur(dto.getIdCreateur());
            appartement.setNumApp(numApp);
            appartement.setCodeAbrvBienImmobilier((etage.getCodeAbrvEtage() + "-APPT-" + numApp).toUpperCase());
            appartement.setNomBaptiserBienImmobilier(dto.getNomBaptiserBienImmobilier());
            appartement.setNomCompletBienImmobilier(etage.getNomCompletEtage() + "-APPARTEMENT-" + numApp);
            appartement.setBienMeublerResidence(dto.isBienMeublerResidence());
            appartement.setDescription(dto.getDescription());
            appartement.setSuperficieBien(dto.getSuperficieBien());
            appartement.setUtilisateurProprietaire(etage.getImmeuble().getUtilisateurProprietaire());
            Appartement appartementSave = appartementRepository.save(appartement);
            return gestimoWebMapperImpl.fromAppartement(appartementSave);
        }

    }

    // FIN SAVE APPARTMENT
    private Etage getEtage(AppartementDto dto) {
        return etageRepository.findById(dto.getIdEtageAppartement()).orElseThrow(() -> new InvalidEntityException(
                "Aucun Etage has been found with id " + dto.getIdEtageAppartement(),
                ErrorCodes.APPARTEMENT_NOT_FOUND));
    }

    @Override
    public List<AppartementDto> findAllLibre(Long idAgence) {
        return appartementRepository.findAll(Sort.by(Direction.ASC, "codeAbrvBienImmobilier")).stream()
                .map(gestimoWebMapperImpl::fromAppartement)
                .filter((app) -> !app.isOccupied())
                .filter(agence->agence.getIdAgence()==idAgence)
                .collect(Collectors.toList());
    }

    private Long nombreVillaByIdSite(Site site) {
        Map<Site, Long> numbreVillabySite = appartementRepository.findAll()
                .stream()
                .filter(e -> e.getEtageAppartement().getImmeuble().getSite().equals(site))
                .collect(Collectors.groupingBy(e -> e.getEtageAppartement().getImmeuble().getSite(),
                        Collectors.counting()));

        for (Map.Entry m : numbreVillabySite.entrySet()) {
            if (m.getKey().equals(site)) {
                return (Long) m.getValue() + 1L;
            }
        }
        return 1L;
    }

    @Override
    public List<AppartementDto> findAllMeuble(Long idAgence) {
        return appartementRepository.findAll().stream()
        .filter(appa->appa.getIdAgence()==idAgence && appa.isBienMeublerResidence()==true)
                .map(gestimoWebMapperImpl::fromAppartement)
                .collect(Collectors.toList());
    }

}
