package com.bzdata.gestimospringbackend.Services;

import java.util.List;
import java.util.Map;

import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.VillaDto;
import com.bzdata.gestimospringbackend.Models.Site;

public interface VillaService {

    //boolean save(VillaDto dto);
    VillaDto saveUneVilla(VillaDto dto);
    boolean delete(Long id);

    Long maxOfNumBien();

    List<VillaDto> findAll();
    List<VillaDto> findAllLibre();
    VillaDto findById(Long id);

    VillaDto findByName(String nom);

    List<VillaDto> findAllBySite(SiteRequestDto siteRequestDto);

    List<VillaDto> findAllByIdSite(Long id);

  //  Map<Site, Long> getNumberVillaBySite();
}
