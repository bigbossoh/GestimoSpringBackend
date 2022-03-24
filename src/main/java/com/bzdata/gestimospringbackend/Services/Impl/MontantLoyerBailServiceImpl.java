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
    public MontantLoyerBailDto saveNewMontantLoyerBail(MontantLoyerBailDto dto) {
        log.info("We are going to create  a new Montant loyer bail {}", dto);
        List<String> errors = MontantLoyerBailDtoValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("le montant loyer bail n'est pas valide {}", errors);
            throw new InvalidEntityException("Certain attributs de l'object Montant loyer bail sont null.",
                    ErrorCodes.MONTANTLOYERBAIL_NOT_VALID, errors);
        }
        boolean shoulddoUpdate = false;
        BailLocation bailLocation = bailLocationRepository.findById(dto.getBailLocation().getId())
                .orElseThrow(() -> new InvalidEntityException("Aucun BailMagasin has been found with Code " +
                        dto.getBailLocation().getId(),
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
        List<MontantLoyerBail> byBailLocation = montantLoyerBailRepository.findByBailLocation(bailLocation);
        dto.setBailLocation(bailLocation);
        if (byBailLocation.size() == 0) {
            shoulddoUpdate = false;
            dto.setAncienMontantLoyer(0);
            dto.setTauxLoyer(0);
            dto.setMontantAugmentation((dto.getNouveauMontantLoyer() - dto.getAncienMontantLoyer()));
            dto.setDebutLoyer(bailLocation.getDateDebut());
        } else {
            Optional<MontantLoyerBail> firstMontantLoyerBail = byBailLocation.stream().findFirst();
            shoulddoUpdate = true;
            if (firstMontantLoyerBail.get().getAncienMontantLoyer() != 0) {
                dto.setTauxLoyer((dto.getNouveauMontantLoyer() - firstMontantLoyerBail.get().getAncienMontantLoyer() ) * 100
                        / firstMontantLoyerBail.get().getAncienMontantLoyer() );
            }
            dto.setMontantAugmentation((dto.getNouveauMontantLoyer() - dto.getAncienMontantLoyer()));
            dto.setDebutLoyer(bailLocation.getDateDebut());
        }
        MontantLoyerBail montantLoyerBail = montantLoyerBailRepository.save(MontantLoyerBailDto.toEntity(dto));

        if (shoulddoUpdate) {

            // UPDATE THE PREVIOUS PRICE SET IT THE FALSE IN STATUS FIELD AND PROVIDE THE
            // END DATE
            log.info("cool");
        }
        return MontantLoyerBailDto.fromEntity(montantLoyerBail);

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
