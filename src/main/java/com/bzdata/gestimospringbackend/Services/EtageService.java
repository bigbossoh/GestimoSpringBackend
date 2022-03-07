package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.EtageDto;

public interface EtageService {

    EtageDto save(EtageDto dto);

    boolean delete(Long id);

    List<EtageDto> findAll();

    EtageDto findById(Long id);

    EtageDto findByName(String nom);

    List<EtageDto> findAllByIdImmeuble(Long id);
}
