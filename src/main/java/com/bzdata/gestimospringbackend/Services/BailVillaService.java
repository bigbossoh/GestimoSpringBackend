package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.BailVillaDto;

public interface BailVillaService {
    BailVillaDto save(BailVillaDto dto);

    boolean delete(Long id);

    List<BailVillaDto> findAll();

    BailVillaDto findById(Long id);

    BailVillaDto findByName(String nom);

    List<BailVillaDto> findAllByIdBienImmobilier(Long id);

    List<BailVillaDto> findAllByIdLocataire(Long id);
}
