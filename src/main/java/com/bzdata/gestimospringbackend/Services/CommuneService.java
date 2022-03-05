package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.CommuneDto;
import com.bzdata.gestimospringbackend.DTOs.VilleDto;

public interface CommuneService {
    CommuneDto save(CommuneDto dto);

    boolean delete(Long id);

    List<CommuneDto> findAll();

    CommuneDto findById(Long id);

    CommuneDto findByName(String nom);

    List<CommuneDto> findAllByVille(VilleDto villeDto);

    List<CommuneDto> findAllByIdVille(Long id);
}
