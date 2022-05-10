package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.StudioDto;

public interface StudioService {
    boolean save(StudioDto dto);

    boolean delete(Long id);

    List<StudioDto> findAll();

    StudioDto findById(Long id);

    StudioDto findByName(String nom);

    List<StudioDto> findAllByIdEtage(Long id);
}
