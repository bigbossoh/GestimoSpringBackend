package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.BailAppartementDto;
import com.bzdata.gestimospringbackend.Models.Appartement;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.BailAppartementService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.AppartementRepository;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.BailAppartementDtoValidator;

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
public class BailAppartmentServiceImpl implements BailAppartementService {
    final BailLocationRepository bailLocationRepository;
    final UtilisateurRepository utilisateurRepository;
    final AppartementRepository appartementRepository;

    @Override
    public BailAppartementDto save(BailAppartementDto dto) {
        BailLocation bailLocation = new BailLocation();
        log.info("We are going to create  a new Bail Appartement {}", dto);
        List<String> errors = BailAppartementDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le Bail n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Bail sont null.",
                    ErrorCodes.BAILLOCATION_NOT_VALID, errors);
        }

        Utilisateur utilisateur = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if (utilisateur.getUrole().getRoleName().equals("LOCATAIRE")) {

            Appartement appartementBail = appartementRepository.findById(dto.getIdAppartement())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun Appartement has been found with code " + dto.getIdAppartement(),
                            ErrorCodes.MAGASIN_NOT_FOUND));

            bailLocation.setAppartementBail(appartementBail);
            bailLocation.setUtilisateurOperation(utilisateur);
            bailLocation.setArchiveBail(false);
            bailLocation.setDateDebut(dto.getDateDebut());
            bailLocation.setDateFin(dto.getDateFin());
            bailLocation.setDesignationBail(dto.getDesignationBail());
            bailLocation.setEnCoursBail(true);
            bailLocation.setMontantCautionBail(dto.getMontantCautionBail());
            bailLocation.setNbreMoisCautionBail(dto.getNbreMoisCautionBail());
            bailLocation.setUtilisateurOperation(utilisateur);

            BailLocation appartementBailSave = bailLocationRepository.save(bailLocation);
            return BailAppartementDto.fromEntity(appartementBailSave);
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateur.getUrole().getRoleName(),
                    ErrorCodes.UTILISATEUR_NOT_GOOD_ROLE);
        }
    }

    @Override
    public boolean delete(Long id) {
        log.info("We are going to delete a Bail with the ID {}", id);
        if (id == null) {
            log.error("you are provided a null ID for the Bail");
            return false;
        }
        boolean exist = bailLocationRepository.existsById(id);
        if (!exist) {
            throw new EntityNotFoundException("Aucun Bail avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.BAILLOCATION_NOT_FOUND);
        }

        bailLocationRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BailAppartementDto> findAll() {
        return bailLocationRepository.findAll(Sort.by(Direction.ASC, "designationBail")).stream()
                .map(BailAppartementDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BailAppartementDto findById(Long id) {
        log.info("We are going to get back the Bail By {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        return bailLocationRepository.findById(id).map(BailAppartementDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with Code " + id,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
    }

    @Override
    public BailAppartementDto findByName(String nom) {
        log.info("We are going to get back the Bail By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Bail.");
            return null;
        }
        return bailLocationRepository.findByDesignationBail(nom).map(BailAppartementDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with name " + nom,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
    }

    @Override
    public List<BailAppartementDto> findAllByIdBienImmobilier(Long id) {

        return null;
    }

    @Override
    public List<BailAppartementDto> findAllByIdLocataire(Long id) {

        return null;
    }

}
