package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.*;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.Magasin;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Models.Utilisateur;
import com.bzdata.gestimospringbackend.Services.AppelLoyerService;
import com.bzdata.gestimospringbackend.Services.BailMagasinService;
import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;
import com.bzdata.gestimospringbackend.repository.MagasinRepository;
import com.bzdata.gestimospringbackend.repository.UtilisateurRepository;
import com.bzdata.gestimospringbackend.validator.BailMagasinDtoValidator;

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
public class BailMagasinServiceImpl implements BailMagasinService {

    final BailLocationRepository bailLocationRepository;
    final UtilisateurRepository utilisateurRepository;
    final MagasinRepository magasinRepository;
    final MontantLoyerBailService montantLoyerBailService;
    final AppelLoyerService appelLoyerService;
    final BienImmobilierRepository bienImmobilierRepository;

    @Override
    public BailMagasinDto save(BailMagasinDto dto) {
        BailLocation bailLocationMagasin = new BailLocation();
        log.info("We are going to create  a new Bail Magasin in the service layer {}", dto);
        List<String> errors = BailMagasinDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le bail magasin n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Bail sont null.",
                    ErrorCodes.BAILLOCATION_NOT_VALID, errors);
        }

        Utilisateur utilisateur = utilisateurRepository
                .findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getIdUtilisateur(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if (utilisateur.getUrole().getRoleName().equals("LOCATAIRE")) {
            Bienimmobilier bienImmobilierOperation = bienImmobilierRepository.findById(dto.getIdMagasin())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun Bien has been found with code " + dto.getIdMagasin(),
                            ErrorCodes.MAGASIN_NOT_FOUND));
            Magasin magasinBail = magasinRepository.findById(dto.getIdMagasin())
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun Magasin has been found with code " + dto.getIdMagasin(),
                            ErrorCodes.MAGASIN_NOT_FOUND));
            bailLocationMagasin.setBienImmobilierOperation(bienImmobilierOperation);
            bailLocationMagasin.setMagasinBail(magasinBail);
            bailLocationMagasin.setUtilisateurOperation(utilisateur);
            BailLocation magasinBailSave = bailLocationRepository.save(bailLocationMagasin);
            /**
             * Mise a jour du status de l'object Magasin
             */
            magasinBail.setOccupied(true);
            magasinBail.setStatutBien("Occupied");
            magasinRepository.save(magasinBail);
            /**
             * Creation d'un montant de loyer juste apres que le contrat de bail a été crée
             */
            MontantLoyerBail montantLoyerBail = new MontantLoyerBail();
            montantLoyerBail.setNouveauMontantLoyer(dto.getNouveauMontantLoyer());
            montantLoyerBail.setBailLocation(magasinBailSave);
            montantLoyerBail.setIdAgence(magasinBail.getIdAgence());
            montantLoyerBailService.saveNewMontantLoyerBail(0L,
                    dto.getNouveauMontantLoyer(), 0.0, magasinBailSave.getId(), dto.getIdAgence());
            /**
             * Creation de l'appel loyer
             */
            AppelLoyerRequestDto appelLoyerRequestDto = new AppelLoyerRequestDto();

            appelLoyerRequestDto.setIdBailLocation(magasinBail.getId());
            appelLoyerRequestDto.setMontantLoyerEnCours(dto.getNouveauMontantLoyer());
            appelLoyerRequestDto.setIdAgence(magasinBail.getIdAgence());

            appelLoyerService.save(appelLoyerRequestDto);
            return BailMagasinDto.fromEntity(magasinBailSave);
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
            throw new EntityNotFoundException("Aucune Studio avec l'ID = " + id + " "
                    + "n' ete trouve dans la BDD", ErrorCodes.BAILLOCATION_NOT_FOUND);
        }

        bailLocationRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BailMagasinDto> findAll() {
        return bailLocationRepository.findAll(Sort.by(Direction.ASC, "designationBail")).stream()
                .map(BailMagasinDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BailMagasinDto findById(Long id) {
        log.info("We are going to get back the Bail By {}", id);
        if (id == null) {
            log.error("you are not provided a Studio.");
            return null;
        }
        return bailLocationRepository.findById(id).map(BailMagasinDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with Code " + id,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
    }

    @Override
    public BailMagasinDto findByName(String nom) {
        log.info("We are going to get back the Studio By {}", nom);
        if (!StringUtils.hasLength(nom)) {
            log.error("you are not provided a Studio.");
            return null;
        }
        return bailLocationRepository.findByDesignationBail(nom).map(BailMagasinDto::fromEntity).orElseThrow(
                () -> new InvalidEntityException("Aucun Bail has been found with name " + nom,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
    }

    @Override
    public List<BailMagasinDto> findAllByIdBienImmobilier(Long id) {

        return null;
    }

    @Override
    public List<BailMagasinDto> findAllByIdLocataire(Long id) {

        return null;
    }

}
