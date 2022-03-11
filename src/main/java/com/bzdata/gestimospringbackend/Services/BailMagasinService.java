package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BailMagasinDto;

public interface BailMagasinService {
    BailMagasinDto save(BailMagasinDto dto);

    boolean delete(Long id);

    List<BailMagasinDto> findAll();

    BailMagasinDto findById(Long id);

    BailMagasinDto findByName(String nom);

    List<BailMagasinDto> findAllByIdBienImmobilier(Long id);

    List<BailMagasinDto> findAllByIdLocataire(Long id);
}