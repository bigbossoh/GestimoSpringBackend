package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;
import com.bzdata.gestimospringbackend.validator.MontantLoyerBailDtoValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MontantLoyerBailServiceImpl implements MontantLoyerBailService {

    final MontantLoyerBailRepository montantLoyerBailRepository;
    final BailLocationRepository bailLocationRepository;

    @Override
    public boolean saveNewMontantLoyerBail(MontantLoyerBailDto dto) {
        log.info("We are going to create  a new Montant loyer bail {}", dto);
        List<String> errors = MontantLoyerBailDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le montant loyer bail n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Montant loyer bail sont null.",
                    ErrorCodes.MONTANTLOYERBAIL_NOT_VALID, errors);
        }

        try {

            MontantLoyerBail newMontantLoyerBail = new MontantLoyerBail();
            BailLocation bailLocation = bailLocationRepository.findById(dto.getBailLocation())
                    .orElseThrow(() -> new InvalidEntityException("Aucun BailMagasin has been found with Code " +
                            dto.getBailLocation(),
                            ErrorCodes.BAILLOCATION_NOT_FOUND));
            List<MontantLoyerBail> byBailLocation = montantLoyerBailRepository.findByBailLocation(bailLocation);
            Optional<MontantLoyerBail> oldMontantBail = montantLoyerBailRepository.findById(dto.getId());
            if (oldMontantBail.isPresent()) {
                newMontantLoyerBail.setId(oldMontantBail.get().getId());
            }
            if (byBailLocation.size() == 0) {
                newMontantLoyerBail.setAncienMontantLoyer(0);
                newMontantLoyerBail.setTauxLoyer(0);
                newMontantLoyerBail
                        .setMontantAugmentation((dto.getNouveauMontantLoyer() - dto.getAncienMontantLoyer()));
                newMontantLoyerBail.setDebutLoyer(bailLocation.getDateDebut());
            } else {
                Optional<MontantLoyerBail> firstMontantLoyerBail = byBailLocation.stream().findFirst();
                if (firstMontantLoyerBail.get().getAncienMontantLoyer() != 0) {
                    newMontantLoyerBail.setTauxLoyer(
                            (dto.getNouveauMontantLoyer() - firstMontantLoyerBail.get().getAncienMontantLoyer()) * 100
                                    / firstMontantLoyerBail.get().getAncienMontantLoyer());
                }
                newMontantLoyerBail
                        .setMontantAugmentation((dto.getNouveauMontantLoyer() - dto.getAncienMontantLoyer()));
                newMontantLoyerBail.setDebutLoyer(bailLocation.getDateDebut());
            }
            montantLoyerBailRepository.save(newMontantLoyerBail);
            return true;
        } catch (Exception e) {
            throw new InvalidEntityException(
                    "Erreur : " + e.getMessage());
        }
    }

    @Override
    public MontantLoyerBailDto updateNewMontantLoyerBail(MontantLoyerBailDto dto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<MontantLoyerBailDto> findAll() {
        return null;
    }

    @Override
    public MontantLoyerBailDto findById(Long id) {
        return null;
    }

    @Override
    public List<MontantLoyerBailDto> findAllMontantLoyerBailByBailId(Long idBailLocation) {
        return null;
    }
}
