package com.bzdata.gestimospringbackend.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import com.bzdata.gestimospringbackend.DTOs.BienImmobilierAffiheDto;
import com.bzdata.gestimospringbackend.Models.Bienimmobilier;
import com.bzdata.gestimospringbackend.Services.BienImmobilierService;
import com.bzdata.gestimospringbackend.mappers.GestimoWebMapperImpl;
import com.bzdata.gestimospringbackend.repository.BienImmobilierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service

@Transactional
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BienImmobilierServiceImpl implements BienImmobilierService {
    final BienImmobilierRepository bienImmobilierRepository;
    final GestimoWebMapperImpl gestimoWebMapperImpl;

    @Override
    public List<BienImmobilierAffiheDto> findAll(Long idAgence) {
        return bienImmobilierRepository.findAll().stream()
                .map(gestimoWebMapperImpl::fromBienImmobilier)
                .filter(agence->agence.getIdAgence()==idAgence)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierAffiheDto> findAllBienOccuper(Long idAgence) {
        return bienImmobilierRepository.findAll().stream()
        .filter(agence->agence.getIdAgence()==idAgence)
                .filter(Bienimmobilier::isOccupied)
                .map(gestimoWebMapperImpl::fromBienImmobilier)
                .collect(Collectors.toList());

    }

}
