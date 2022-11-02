package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.MontantLoyerBailDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.MontantLoyerBail;
import com.bzdata.gestimospringbackend.Services.MontantLoyerBailService;
import com.bzdata.gestimospringbackend.exceptions.ErrorCodes;
import com.bzdata.gestimospringbackend.exceptions.InvalidEntityException;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.MontantLoyerBailRepository;

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
    public boolean saveNewMontantLoyerBail(Long currentIdMontantLoyerBail, double nouveauMontantLoyer,
            double ancienMontantLoyer, Long idBailLocation, Long idAgence) {
        log.info("We are going to create  a new  {} {} {}", nouveauMontantLoyer, idBailLocation, idAgence);


        try {

            MontantLoyerBail newMontantLoyerBail = new MontantLoyerBail();
            BailLocation bailLocation = bailLocationRepository.findById(idBailLocation)
                    .orElseThrow(() -> new InvalidEntityException("Aucun BailMagasin has been found with Code " +
                            idBailLocation,
                            ErrorCodes.BAILLOCATION_NOT_FOUND));
            List<MontantLoyerBail> ListBauxMontantLoyerBail = montantLoyerBailRepository
                    .findByBailLocation(bailLocation);
            Optional<MontantLoyerBail> oldMontantBail = montantLoyerBailRepository.findById(currentIdMontantLoyerBail);
            if (oldMontantBail.isPresent()) {
                newMontantLoyerBail.setId(oldMontantBail.get().getId());
            }
            if (ListBauxMontantLoyerBail.size() == 0) {
                newMontantLoyerBail.setId(currentIdMontantLoyerBail);
                newMontantLoyerBail.setAncienMontantLoyer(0);
                newMontantLoyerBail.setTauxLoyer(0);
                newMontantLoyerBail.setMontantAugmentation((nouveauMontantLoyer - ancienMontantLoyer));
                newMontantLoyerBail.setDebutLoyer(bailLocation.getDateDebut());
                newMontantLoyerBail.setFinLoyer(bailLocation.getDateFin());
                newMontantLoyerBail.setIdAgence(bailLocation.getIdAgence());
                newMontantLoyerBail.setBailLocation(bailLocation);
                newMontantLoyerBail.setNouveauMontantLoyer(nouveauMontantLoyer);
                newMontantLoyerBail.setStatusLoyer(true);
            } else {
                Optional<MontantLoyerBail> firstMontantLoyerBail = ListBauxMontantLoyerBail.stream()
                        .filter(montantLoyerBail -> montantLoyerBail.isStatusLoyer() == true)
                        .findFirst();
                if (firstMontantLoyerBail.get().getAncienMontantLoyer() != 0) {
                    newMontantLoyerBail.setTauxLoyer(
                            (nouveauMontantLoyer - firstMontantLoyerBail.get().getAncienMontantLoyer()) * 100
                                    / firstMontantLoyerBail.get().getAncienMontantLoyer());
                }

                newMontantLoyerBail.setId(firstMontantLoyerBail.get().getId());
                if (firstMontantLoyerBail.get().getNouveauMontantLoyer() != nouveauMontantLoyer) {
                    firstMontantLoyerBail.get()
                            .setAncienMontantLoyer(firstMontantLoyerBail.get().getNouveauMontantLoyer());
                    firstMontantLoyerBail.get().setFinLoyer(bailLocation.getDateFin());
                    firstMontantLoyerBail.get().setStatusLoyer(false);
                    montantLoyerBailRepository.save(firstMontantLoyerBail.get());
                }
                newMontantLoyerBail.setAncienMontantLoyer(0);
                newMontantLoyerBail.setMontantAugmentation((nouveauMontantLoyer - ancienMontantLoyer));
                newMontantLoyerBail.setDebutLoyer(bailLocation.getDateDebut());
                newMontantLoyerBail.setFinLoyer(bailLocation.getDateFin());
                newMontantLoyerBail.setIdAgence(bailLocation.getIdAgence());
                newMontantLoyerBail.setBailLocation(bailLocation);
                newMontantLoyerBail.setNouveauMontantLoyer(nouveauMontantLoyer);
                newMontantLoyerBail.setStatusLoyer(true);
                newMontantLoyerBail.setMontantAugmentation((nouveauMontantLoyer - ancienMontantLoyer));
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
    public List<MontantLoyerBailDto> findAll(Long idAgence) {

        return null;
    }

    @Override
    public MontantLoyerBailDto findById(Long id) {
        return null;
    }

    @Override
    public List<MontantLoyerBailDto> findAllMontantLoyerBailByBailId(Long idBailLocation) {
        BailLocation bailLocation = bailLocationRepository.findById(idBailLocation)
                .orElseThrow(() -> new InvalidEntityException("Aucun BailMagasin has been found with Code " +
                        idBailLocation,
                        ErrorCodes.BAILLOCATION_NOT_FOUND));
        List<MontantLoyerBail> ListBauxMontantLoyerBail = montantLoyerBailRepository.findByBailLocation(bailLocation);

        return ListBauxMontantLoyerBail.stream()
                .filter(montantLoyerBail -> montantLoyerBail.isStatusLoyer() == true)
                // .findFirst()
                .map(MontantLoyerBailDto::fromEntity)
                .collect(Collectors.toList());

        // return null;
    }
}
