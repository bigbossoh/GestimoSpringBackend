package com.bzdata.gestimospringbackend.Services;
import com.bzdata.gestimospringbackend.DTOs.SiteRequestDto;
import com.bzdata.gestimospringbackend.DTOs.SiteResponseDto;

import java.util.List;

public interface SiteService {
    SiteResponseDto save(SiteRequestDto dto);
    boolean delete(Long id);
    List<SiteResponseDto> findAll();
    SiteResponseDto findById(Long id);
    SiteResponseDto findByName(String nom);
}
