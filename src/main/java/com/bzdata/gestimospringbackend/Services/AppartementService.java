package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AppartementDto;

public interface AppartementService {
    AppartementDto save(AppartementDto dto);

    boolean delete(Long id);

    List<AppartementDto> findAll();

    AppartementDto findById(Long id);

    AppartementDto findByName(String nom);

    List<AppartementDto> findAllByIdEtage(Long id);
}
