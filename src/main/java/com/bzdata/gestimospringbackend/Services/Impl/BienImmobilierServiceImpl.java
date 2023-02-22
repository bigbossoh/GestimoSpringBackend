package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.BienImmobilierAffiheDto;
import com.bzdata.gestimospringbackend.Models.BailLocation;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Models.Chapitre;
import com.bzdata.gestimospringbackend.Models.Operation;
import com.bzdata.gestimospringbackend.Services.BienImmobilierService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.BailLocationRepository;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;
import com.bzdata.gestimospringbackend.repository.ChapitreRepository;

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
public class BienImmobilierServiceImpl implements BienImmobilierService {
    final BienImmobilierRepository bienImmobilierRepository;
    final GestimoWebMapperImpl gestimoWebMapperImpl;
    final BailLocationRepository bailLocationRepository;
   final ChapitreRepository chapitreRepository;

    @Override
    public List<BienImmobilierAffiheDto> findAll(Long idAgence, Long chapitre) {

        if (chapitre == 0) {

            return bienImmobilierRepository.findAll().stream()
            .map(gestimoWebMapperImpl::fromBienImmobilier)
            .filter(agence -> agence.getIdAgence() == idAgence)
            .collect(Collectors.toList());
        } else {

            return bienImmobilierRepository.findAll().stream()
            .filter(agence -> agence.getIdAgence() == idAgence && agence.getChapitre().getId() == chapitre)
            .map(gestimoWebMapperImpl::fromBienImmobilier)
            .collect(Collectors.toList());
        }

    }

    @Override
    public List<BienImmobilierAffiheDto> findAllBienOccuper(Long idAgence, Long chapitre) {
        if (chapitre == 0 || chapitre == null) {
            return bienImmobilierRepository.findAll().stream()
                    .filter(agence -> agence.getIdAgence() == idAgence)
                    .filter(Bienimmobilier::isOccupied)
                    .map(gestimoWebMapperImpl::fromBienImmobilier)
                    .collect(Collectors.toList());
        } else {
            return bienImmobilierRepository.findAll().stream()
                    .filter(agence -> agence.getIdAgence() == idAgence && agence.getChapitre().getId() == chapitre
                            && agence.isOccupied())
                    .map(gestimoWebMapperImpl::fromBienImmobilier)
                    .collect(Collectors.toList());
        }

    }

    @Override
    public Bienimmobilier findBienByBailEnCours(Long idBail) {
        BailLocation bailLocation = bailLocationRepository.findById(idBail)
                .filter(bail -> bail.isEnCoursBail() == true)
                .orElse(null);
        if (bailLocation != null) {
            Bienimmobilier bienimmobilier = bienImmobilierRepository
                    .findById(bailLocation.getBienImmobilierOperation().getId())
                    .orElse(null);
            return bienimmobilier;
        }
        return null;
    }

    @Override
    public BienImmobilierAffiheDto rattacherUnBienAUnChapitre(Long idBail, Long chapitre) {
        Bienimmobilier bienImmobiler = bienImmobilierRepository.findById(idBail)

                .orElse(null);
        Chapitre chapitreTrouver=chapitreRepository.findById(chapitre)
                .orElse(null);
                if (bienImmobiler != null) {

                    bienImmobiler.setChapitre(chapitreTrouver);
                    bienImmobilierRepository.save(bienImmobiler);
                    return gestimoWebMapperImpl.fromBienImmobilier(bienImmobiler);
                }
                return null;
    }

}
