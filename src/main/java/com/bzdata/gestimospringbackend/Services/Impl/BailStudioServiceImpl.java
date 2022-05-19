package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.DTOs.BailStudioDto;
import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.Studio;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.BailStudioService;
import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.StudioRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.BailStudioDtoValidator;

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
public class BailStudioServiceImpl implements BailStudioService {
    // final BailLocation bailLocation;
    final UtilisateurRepository utilisateurRepository;
    final StudioRepository studioRepository;
    final BailLocationRepository bailLocationRepository;
    final MontantLoyerBailService montantLoyerBailService;
    final AppelLoyerService appelLoyerService;

    @Override
    public BailStudioDto save(BailStudioDto dto) {
        BailLocation bailLocationStudio = new BailLocation();
        log.info("We are going to create  a new Bail Studio {}", dto);
        List<String> errors = BailStudioDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le Villa n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Bail sont null.",
                    ErrorCodes.BAILLOCATION_NOT_VALID, errors);
        }

        Utilisateur utilisateur = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if (utilisateur.getUrole().getRoleName().equals("LOCATAIRE")) {

            Studio studio = studioRepository.findById(dto.getIdStudio())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucune Villa has been found with code " + dto.getIdStudio(),
                            ErrorCodes.MAGASIN_NOT_FOUND));
            if(studio.isOccupied()==false){
                bailLocationStudio.setStudioBail(studio);
                bailLocationStudio.setUtilisateurOperation(utilisateur);
                bailLocationStudio.setArchiveBail(false);
                bailLocationStudio.setDateDebut(dto.getDateDebut());
                bailLocationStudio.setDateFin(dto.getDateFin());
                bailLocationStudio.setDesignationBail(dto.getDesignationBail());
                bailLocationStudio.setEnCoursBail(true);
                bailLocationStudio.setMontantCautionBail(dto.getMontantCautionBail());
                bailLocationStudio.setAbrvCodeBail(dto.getAbrvCodeBail());
                bailLocationStudio.setNbreMoisCautionBail(dto.getNbreMoisCautionBail());
                bailLocationStudio.setIdAgence(dto.getIdAgence());
                BailLocation studioBailSave = bailLocationRepository.save(bailLocationStudio);

                studio.setOccupied(true);
                studio.setStatutStudio("Occupied");
                studioRepository.save(studio);
                /**
                 * Creation d'un montant de loyer juste apres que le contrat de bail a été crée
                 */

                montantLoyerBailService.saveNewMontantLoyerBail(0L,
                        dto.getNouveauMontantLoyer(),0.0,studioBailSave.getId(),dto.getIdAgence());
                /**
                 * Creation de l'appel loyer
                 */
                AppelLoyerRequestDto appelLoyerRequestDto = new AppelLoyerRequestDto();

                appelLoyerRequestDto.setIdBailLocation(studioBailSave.getId());
                appelLoyerRequestDto.setMontantLoyerEnCours(dto.getNouveauMontantLoyer());
                appelLoyerRequestDto.setIdAgence(dto.getIdAgence());

                appelLoyerService.save(appelLoyerRequestDto);

                return BailStudioDto.fromEntity(studioBailSave);
            }else {
                throw new InvalidEntityException("le studio est déjà occupé "
                        + studio.isOccupied(),
                        ErrorCodes.STUDIO_ALREADY_IN_USE);
            }

        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle Locataire, mais pluôt "
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
            throw new EntityNotFoundException("Aucune Bail avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.BAILLOCATION_NOT_FOUND);
        }

        bailLocationRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BailStudioDto> findAll() {
        return bailLocationRepository.findAll(Sort.by(Direction.ASC, "designationBail")).stream()
                .map(BailStudioDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BailStudioDto findById(Long id) {
        log.info("We are going to get back the Bail By {}", id);
        if (id == null) {
            log.error("you are not provided a Bail.");
            return null;
        }
        return bailLocationRepository.findById(id).map(BailStudioDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with Code " + id,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
    }

    @Override
    public BailStudioDto findByName(String nom) {
        log.info("We are going to get back the Bail By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Bail.");
            return null;
        }
        return bailLocationRepository.findByDesignationBail(nom).map(BailStudioDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with name " + nom,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
    }

    @Override
    public List<BailStudioDto> findAllByIdBienImmobilier(Long id) {

        return null;
    }

    @Override
    public List<BailStudioDto> findAllByIdLocataire(Long id) {

        return null;
    }

}
