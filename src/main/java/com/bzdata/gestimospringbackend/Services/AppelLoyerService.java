package com.bzdata.gestimospringbackend.Services;

import java.time.LocalDate;
import java.util.List;

import com.bzdata.gestimospringbackend.DTOs.AnneeAppelLoyersDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyerRequestDto;
import com.bzdata.gestimospringbackend.DTOs.AppelLoyersFactureDto;

public interface AppelLoyerService {

    List<String> save(AppelLoyerRequestDto dto);

    boolean deleteAppelDto(Long id);

    List<AppelLoyersFactureDto> findAll();

    List<AppelLoyersFactureDto> findAllAppelLoyerByPeriode(String periodeAppelLoyer);

    //List<AppelLoyersFactureDto> findAllAppelLoyerSuperieurPeriodePourCloture(LocalDate dateDebutMois);

    public double soldeArrierer(Long idBailLocation);

    AppelLoyersFactureDto findById(Long id);

    List<AppelLoyerDto> findAllAppelLoyerByBailId(Long idBailLocation);

    List<Integer> listOfDistinctAnnee();

    List<String> listOfPerodesByAnnee(Integer annee);

    List<AnneeAppelLoyersDto> listOfAppelLoyerByAnnee(Integer annee);

    List<AppelLoyersFactureDto> findAllAppelLoyerImpayerByBailId(Long idBailLocation);
}
