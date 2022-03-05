package com.bzdata.gestimospringbackend.Services;
import com.bzdata.gestimospringbackend.DTOs.SiteDto;

import java.util.List;

public interface SiteService {
    SiteDto save(SiteDto dto);
    boolean delete(Long id);
    List<SiteDto> findAll();
    SiteDto findById(Long id);
    SiteDto  findByName(String nom);
}
