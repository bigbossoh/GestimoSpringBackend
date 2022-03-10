package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.MagasinDto;
import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;

import java.util.List;

public interface MagasinService {

    MagasinDto save(MagasinDto dto);
    boolean delete(Long id);
    Long maxOfNumBienMagasin();
    List<MagasinDto> findAll();
    MagasinDto findById(Long id);
    MagasinDto findByName(String nom);
    List<MagasinDto> findAllBySite(SiteRequestDto siteRequestDto);
    List<MagasinDto> findAllByIdSite(Long id);
    List<MagasinDto> findAllByIdEtage(Long id);
}
