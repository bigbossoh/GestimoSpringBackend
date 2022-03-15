package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.AppelLoyerDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.Models.AppelLoyer;

import java.util.List;

public interface AppelLoyerService {

    List<String> save(AppelLoyerRequestDto dto);

    boolean deleteAppelDto(Long id);

    List<AppelLoyer> findAll();

    AppelLoyer findById(Long id);

    List<AppelLoyer> findAllAppelLoyerByBailId(Long idBailLocation);
}
