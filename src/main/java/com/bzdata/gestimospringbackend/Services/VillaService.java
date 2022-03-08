package com.bzdata.gestimospringbackend.Services;
import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;

import java.math.BigDecimal;
import java.util.List;

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
