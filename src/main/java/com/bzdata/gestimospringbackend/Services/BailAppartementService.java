package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BailAppartementDto;

public interface BailAppartementService {
    BailAppartementDto save(BailAppartementDto dto);

    boolean delete(Long id);

    List<BailAppartementDto> findAll();

    BailAppartementDto findById(Long id);

    BailAppartementDto findByName(String nom);

    List<BailAppartementDto> findAllByIdBienImmobilier(Long id);

    List<BailAppartementDto> findAllByIdLocataire(Long id);
}
