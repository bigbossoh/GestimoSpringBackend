package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.BailMagasinDto;
import com.bzdata.gestimospringbackend.DTOs.MagasinDto;
import com.bzdata.gestimospringbackend.DTOs.UtilisateurRequestDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Services.BailMagasinService;
import com.bzdata.gestimospringbackend.exceptions.EntityNotFoundException;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
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

    @Override
    public BailMagasinDto save(BailMagasinDto dto) {
        log.info("We are going to create  a new Bail Magasin {}", dto);
        List<String> errors = BailMagasinDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le Studio n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Bail sont null.",
                    ErrorCodes.BAILLOCATION_NOT_VALID, errors);
        }

        UtilisateurRequestDto utilisateurRequestDto = utilisateurRepository
                .findById(dto.getUtilisateurRequestDto().getId()).map(UtilisateurRequestDto::fromEntity)
                .orElseThrow(() -> new InvalidEntityException(
                        "Aucun Utilisateur has been found with code " + dto.getUtilisateurRequestDto().getId(),
                        ErrorCodes.UTILISATEUR_NOT_FOUND));
        if (utilisateurRequestDto.getRoleRequestDto().getRoleName().equals("LOCATAIRE")) {

            MagasinDto magasinDto = magasinRepository.findById(dto.getMagasinDto().getId()).map(MagasinDto::fromEntity)
                    .orElseThrow(() -> new InvalidEntityException(
                            "Aucun Magasin has been found with code " + dto.getMagasinDto().getId(),
                            ErrorCodes.MAGASIN_NOT_FOUND));
            dto.setMagasinDto(magasinDto);
            dto.setUtilisateurRequestDto(utilisateurRequestDto);
            BailLocation magasinBail = bailLocationRepository.save(BailMagasinDto.toEntity(dto));
            return BailMagasinDto.fromEntity(magasinBail);
        } else {
            throw new InvalidEntityException("L'utilisateur choisi n'a pas un rôle propriétaire, mais pluôt "
                    + utilisateurRequestDto.getRoleRequestDto().getRoleName(),
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
