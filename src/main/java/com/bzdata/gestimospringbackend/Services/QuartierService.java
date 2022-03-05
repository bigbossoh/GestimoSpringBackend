package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.QuartierDto;

public interface QuartierService {
    QuartierDto save(QuartierDto dto);

    boolean delete(Long id);

    List<QuartierDto> findAll();

    QuartierDto findById(Long id);

    QuartierDto findByName(String nom);

    List<QuartierDto> findAllByIdCommune(Long id);
}
