package com.bzdata.gestimospringbackend.Services;

import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;

public interface VillaService {

    VillaDto save(VillaDto dto);

    boolean delete(Long id);

    Long maxOfNumBien();

    List<VillaDto> findAll();

    VillaDto findById(Long id);

    VillaDto findByName(String nom);

    List<VillaDto> findAllBySite(SiteRequestDto siteRequestDto);

    List<VillaDto> findAllByIdSite(Long id);
}
